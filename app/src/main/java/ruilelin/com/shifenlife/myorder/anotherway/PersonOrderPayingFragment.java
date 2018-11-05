package ruilelin.com.shifenlife.myorder.anotherway;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseFragment4;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.PayOrder;
import ruilelin.com.shifenlife.json.RequestOrder;
import ruilelin.com.shifenlife.utils.Constant;

public class PersonOrderPayingFragment extends BaseFragment4 {
    RecyclerView mRvOrder;
    ProgressBar mPbLoad;
    private OrderAdapter mAllOrderAdapter;
    private static int current=0;
    private String status;
    private List<Object> mAllOrderList ;
    //所有订单
    private List<PayOrder> allList;
    //代付款
    private List<PayOrder> payingList;
    //已经完成
    private List<PayOrder> finishList;
    //已取消
    private List<PayOrder> cancelList;
    private int page = 1;
    private int size = 100;

    private int resCode = -1;
    private  String username;
    private RelativeLayout rl_no_contant;
    private Button button_add;


    //统一的Fragment构建方法
    public static PersonOrderPayingFragment newInstance(int flag) {
        Bundle args = new Bundle();
        //type代表页签，0：全部订单 1：待发货 2：待收货 3：已完成
        args.putString("type", String.valueOf(flag));
        Log.d("order1","flag=="+flag);
        current = flag;
        PersonOrderPayingFragment fragment = new PersonOrderPayingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_person_order, null);
        mRvOrder = (RecyclerView)view.findViewById(R.id.rv_order);
        mPbLoad = (ProgressBar)view.findViewById(R.id.pb_load);
        rl_no_contant = (RelativeLayout) view.findViewById(R.id.rl_no_contant);
        button_add = (Button) view.findViewById(R.id.button_add);
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        return view;
    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences =mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        mAllOrderList = new ArrayList<>();
        mAllOrderAdapter = new OrderAdapter(mContext,mAllOrderList);
        allList = new ArrayList<>();
        payingList = new ArrayList<>();
        finishList = new ArrayList<>();
        cancelList = new ArrayList<>();
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.custom_divider_address));
        mRvOrder.addItemDecoration(divider);
        mRvOrder.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mRvOrder.setAdapter(mAllOrderAdapter);
        if(current == 0 ){
            status = "";
        }else if(current ==1){
            status = "NO_PAY";
        }else if(current ==2){
            status = "PAID";
        }else if(current ==3){
            status = "CANCEL";
        }
        RequestOrder requestOrder = new RequestOrder("NO_PAY");
        String jsonstyle = JSON.toJSONString(requestOrder);
        Log.d("order1","current=="+current);
        Log.d("order","status=="+status);
        mMyOkhttp.post()
                .url(Constant.SFSHURL+Constant.AllOrderList+page+"/"+size)
                .addHeader("cookie",username)
                .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                        Data<List<PayOrder>> obj = (Data<List<PayOrder>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<PayOrder>>>(){});
                        resCode = obj.getCode();
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (resCode == Constant.SUCCESSCODE) {
                                        Log.d("order","order=="+obj.getData());
                                        if(!(obj.getData()==null) && obj.getData().size()>0) {
                                            mAllOrderList.addAll(OrderDataHelper.getDataAfterHandle(obj.getData()));
                                            mAllOrderAdapter.notifyDataSetChanged();
                                        }else if(obj.getData().size()==0){
                                            mRvOrder.setVisibility(View.GONE);
                                            rl_no_contant.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONArray response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONArray:" + response);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doPostJSON onFailure:" + error_msg);
                    }
                });
    }
}
