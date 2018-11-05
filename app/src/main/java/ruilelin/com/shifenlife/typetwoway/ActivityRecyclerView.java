package ruilelin.com.shifenlife.typetwoway;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.HotProduct;
import ruilelin.com.shifenlife.jsonmodel.ChannelModel;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.view.PaddingDecoration;

public class ActivityRecyclerView extends BaseActivity {

    private RecyclerView recycler_left;
    private TypeAdapter typeAdapter;
    private SecondTypeAdapter secondTypeAdapter;
    private List<ChannelModel> OneList = new ArrayList<>();
    private RecyclerView recycler_right;
    private GridLayoutManager mManager;
    private List<HotProduct> TwoList = new ArrayList<>();
    private  String username;
    private int shopid;
    private String shopaddress;
    private String shopmobile;
    private String shopname;
    private TextView tv_shopinfo;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initData() {
        typeAdapter = new TypeAdapter(this);
        secondTypeAdapter = new SecondTypeAdapter(this);
        recycler_right.setAdapter(secondTypeAdapter);
        typeAdapter.setOnRecyclerViewListener(new OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.d("name","OneList.get(position).getId()=="+OneList.get(position).getId());
                Log.d("name","OneList.get(position).getId()=="+OneList.get(position).getId());
                typeAdapter.setPressPosition(position);
                typeAdapter.notifyDataSetChanged();
                mMyOkhttp.get()
                        .url(Constant.SFSHURL+Constant.FirsClassification+OneList.get(position).getId()+"/"+1)
                        .addHeader("cookie",username)
                        // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<HotProduct>>>(){});
                                int  resCodes = obj.getCode();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCodes == Constant.SUCCESSCODE) {
                                            // recommend_list = obj.getData();
                                            TwoList = obj.getData();
                                            secondTypeAdapter.updataData(obj.getData());
                                        }
                                    }
                                });
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

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
        //添加自定义分割线
        PaddingDecoration divider = new PaddingDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider_address));
        recycler_left.addItemDecoration(new DashlineItemDivider());
        recycler_left.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_left.setItemAnimator(new DefaultItemAnimator());

        recycler_left.setAdapter(typeAdapter);
        mMyOkhttp.get()
                .url(Constant.SFSHURL+Constant.AllCategories+1)
                .addHeader("cookie",username)
                // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                        Data<List<ChannelModel>> obj = (Data<List<ChannelModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<ChannelModel>>>(){});
                        int  resCodes = obj.getCode();
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (resCodes == Constant.SUCCESSCODE) {
                                        // recommend_list = obj.getData();
                                        OneList = obj.getData();
                                        typeAdapter.updataData(obj.getData());
                                    }
                                }
                            });
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

    @Override
    protected void initView() {
        SharedPreferences sharedPreferencesshop =getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferencesshop.getString("cookie", "");
        View view = View.inflate(this, R.layout.fragment_type, null);
        tv_shopinfo = (TextView)view.findViewById(R.id.tv_shopinfo);

        SharedPreferences sharedPreferences =getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        recycler_left = (RecyclerView)findViewById(R.id.recycler_left);
        recycler_right = (RecyclerView)findViewById(R.id.recycler_right);
        mManager = new GridLayoutManager(ActivityRecyclerView.this, 3);
        recycler_right.setLayoutManager(mManager);
    }

    @Override
    protected void initListener() {

    }
}
