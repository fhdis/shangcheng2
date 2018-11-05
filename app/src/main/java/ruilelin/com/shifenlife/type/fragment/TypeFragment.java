package ruilelin.com.shifenlife.type.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseFragment;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.HotParcelable;
import ruilelin.com.shifenlife.json.Primary;
import ruilelin.com.shifenlife.type.adapter.ItemHeaderDecoration;
import ruilelin.com.shifenlife.type.adapter.SortAdapter;
import ruilelin.com.shifenlife.type.adapter.SortBean;
import ruilelin.com.shifenlife.type.adapter.SortDetailFragment;
import ruilelin.com.shifenlife.type.listener.CheckListener;
import ruilelin.com.shifenlife.type.listener.RvListener;
import ruilelin.com.shifenlife.utils.Constant;

public class TypeFragment extends BaseFragment implements CheckListener{

    private RecyclerView rvSort;
    private SortAdapter mSortAdapter;
    private SortDetailFragment mSortDetailFragment;
    private LinearLayoutManager mLinearLayoutManager;
    private int targetPosition;//点击左边某一个具体的item的位置
    private boolean isMoved;
    private SortBean mSortBean;
    private  String username;
    private List<Primary> primarylist = new ArrayList<>();
    private List<HotParcelable> allgoods = new ArrayList<>();
    private List<HotParcelable> searchgoods = new ArrayList<>();
    private int secPosition = -1;
    private int shopid;
    private String shopaddress;
    private String shopmobile;
    private String shopname;
    private TextView tv_shopinfo;

    private Primary searchPrimary;
    private Primary allPrimary;
    private String selectName;
    private EditText tv_search_home;


    @Override
    protected View initView() {
        SharedPreferences sharedPreferences =mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        View view = View.inflate(mContext, R.layout.fragment_type, null);
        tv_shopinfo = (TextView)view.findViewById(R.id.tv_shopinfo);
        tv_search_home = (EditText) view.findViewById(R.id.tv_search_home);
        Log.d("pipei","tv_search_home=");
        initTypeView(view);
        return view;
    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences_shop =mContext.getSharedPreferences("SHOP", Context.MODE_PRIVATE);
        shopid = sharedPreferences_shop.getInt("shopid",0);

        //tv_search_home.setText(selectName);
        Log.d("type","shopid=="+shopid);
        shopaddress = sharedPreferences_shop.getString("shopaddress","");
        shopmobile = sharedPreferences_shop.getString("shoptel","");
        shopname = sharedPreferences_shop.getString("shopname","");
        tv_shopinfo.setText(shopname+'\n'+"地址:"+shopaddress+'\n'+"电话:"+shopmobile);
        if(shopid==0){
            Toast.makeText(mContext,"没有获取店铺信息呢，请您先选择附近的店铺哦。",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    public void check(int position, boolean isScroll) {
        Log.d("position","position="+position);
        setChecked(position, isScroll);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences_shop =mContext.getSharedPreferences("SHOP", Context.MODE_PRIVATE);
        shopid = sharedPreferences_shop.getInt("shopid",0);
        Log.d("type","shopid=="+shopid);
        shopaddress = sharedPreferences_shop.getString("shopaddress","");
        shopmobile = sharedPreferences_shop.getString("shoptel","");
        shopname = sharedPreferences_shop.getString("shopname","");
        tv_shopinfo.setText(shopname+'\n'+"地址:"+shopaddress+'\n'+"电话:"+shopmobile);
        initTypeData();
        tv_search_home.setText(selectName);
    }

    private void initTypeView(View view) {
        rvSort = (RecyclerView) view.findViewById(R.id.rv_sort);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        rvSort.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        rvSort.addItemDecoration(decoration);

    }

    private void initTypeData() {
        getPrimaryClassification();
    }





    //将当前选中的item居中
    private void moveToCenter(int position) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        View childAt = rvSort.getChildAt(position - mLinearLayoutManager.findFirstVisibleItemPosition());
        if (childAt != null) {
            int y = (childAt.getTop() - rvSort.getHeight() / 2);
            rvSort.smoothScrollBy(0, y);
        }

    }

    public void createSecFragment(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mSortDetailFragment = new SortDetailFragment();
        Bundle bundle = new Bundle();
        ArrayList<Primary> mList = new ArrayList<>();
        Log.d("all","primarylist=="+primarylist);
        mList.addAll(primarylist);
        bundle.putParcelableArrayList("right", mList);
        mSortDetailFragment.setArguments(bundle);
        mSortDetailFragment.setListener(this);
        fragmentTransaction.add(R.id.lin_fragment, mSortDetailFragment);
        fragmentTransaction.commit();
    }



    private void setChecked(int position, boolean isLeft) {
        Log.d("p-------->", String.valueOf(position));
        if (isLeft) {
            mSortAdapter.setCheckedPosition(position);
            //此处的位置需要根据每个分类的集合来进行计算
            int count = 0;
            for (int i = 0; i < position; i++) {
                //count += mSortBean.getCategoryOneArray().get(i).getCategoryTwoArray().size();
                count += primarylist.get(i).getGoods().size();
            }
            count += position;
            mSortDetailFragment.setData(count);
            ItemHeaderDecoration.setCurrentTag(String.valueOf(targetPosition));//凡是点击左边，将左边点击的位置作为当前的tag
        } else {
            if (isMoved) {
                isMoved = false;
            } else
                mSortAdapter.setCheckedPosition(position);
            ItemHeaderDecoration.setCurrentTag(String.valueOf(position));//如果是滑动右边联动左边，则按照右边传过来的位置作为tag

        }
        moveToCenter(position);
    }

    public void getPrimaryClassification(){
        Log.d("type","shopid="+shopid);
        //获取一级分类
        String url = Constant.SFSHURL + Constant.AllCategoriesAndGoods+shopid;
        mMyOkhttp.get()
                .url(url)
                .addHeader("cookie",username)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Data<List<Primary>> obj = (Data<List<Primary>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<Primary>>>(){});
                        int  resCode =  obj.getCode();
                         if(getActivity()!=null) {
                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     if (resCode == Constant.SUCCESSCODE) {
                                         if(obj.getData()!=null && obj.getData().size()>0){
                                             List<String> list = new ArrayList<>();
                                             list.add("搜索");
                                             list.add("全部");
                                             List<HotParcelable> goodtemp = new ArrayList<>();
                                             allPrimary = new Primary(goodtemp);
                                             allPrimary.setName("全部");

                                             List<HotParcelable> goodtemp1 = new ArrayList<>();
                                             searchPrimary = new Primary(goodtemp1);
                                             searchPrimary.setName("搜索");
                                             //初始化左侧列表数据
                                             allgoods.clear();
                                             searchgoods.clear();
                                             for (int i = 0; i < obj.getData().size(); i++) {
                                                 if(obj.getData().get(i).getGoods()!=null && obj.getData().get(i).getGoods().size()>0){
                                                     for(int j=0;j<obj.getData().get(i).getGoods().size();j++) {
                                                         allgoods.add(obj.getData().get(i).getGoods().get(j));

                                                         if(selectName!=null && obj.getData().get(i).getGoods().get(j).getName().contains(selectName)){
                                                             Log.d("pipei","yes");
                                                             searchgoods.add(obj.getData().get(i).getGoods().get(j));
                                                         }

                                                     }
                                                 }
                                                 list.add(obj.getData().get(i).getName());
                                             }

                                             allPrimary.getGoods().addAll(allgoods);
                                             searchPrimary.getGoods().addAll(searchgoods);
                                             primarylist.clear();
                                             primarylist.add(searchPrimary);
                                             primarylist.add(allPrimary);
                                             primarylist.addAll(obj.getData());

                                             mSortAdapter = new SortAdapter(mContext, list, new RvListener() {
                                                 @Override
                                                 public void onItemClick(int id, int position) {
                                                     if (mSortDetailFragment != null) {
                                                         isMoved = true;
                                                         Log.d("position1","position1="+position);
                                                         targetPosition = position;
                                                         setChecked(position, true);
                                                     }
                                                 }
                                             });
                                             rvSort.setAdapter(mSortAdapter);
                                             createSecFragment();
                                         }
                                     }
                                 }
                             });
                         }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        selectName = ((MainActivity) context).getTitles();
        Log.d("pipei","selectName=" +selectName);

    }


    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("^((?!(\\*|//)).)+[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


}
