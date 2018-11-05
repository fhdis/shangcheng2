package ruilelin.com.shifenlife.typetwoway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.GoodsInfoPage;
import ruilelin.com.shifenlife.activity.LoginActivity;
import ruilelin.com.shifenlife.base.BaseFragment;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.HotProduct;
import ruilelin.com.shifenlife.json.LoginWithPasswordJson;
import ruilelin.com.shifenlife.json.ProductDetailJson;
import ruilelin.com.shifenlife.json.Search;
import ruilelin.com.shifenlife.jsonmodel.ChannelModel;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.view.PaddingDecoration;

public class NewTypeFragment extends BaseFragment {

    private RecyclerView recycler_left;
    private TypeAdapter typeAdapter;
    private SecondTypeAdapter secondTypeAdapter;
    private List<ChannelModel> OneList = new ArrayList<>();
    private RecyclerView recycler_right;
    private LinearLayout llcontent;
    private GridLayoutManager mManager;
    private List<HotProduct> TwoList = new ArrayList<>();
    private  String username;
    private int shopid;
    private String shopaddress;
    private String shopmobile;
    private String shopname;
    private TextView tv_shopinfo;
    private String selectName;
    private EditText tv_search_home;
    private String channeltext;
    private boolean isType = false;
    private boolean isSearch = false;
    private int page = 1;
    private int size = 100;


    @Override
    protected View initView() {
        Log.d("typehaha", "initView" );
        View view = View.inflate(mContext, R.layout.fragment_new_type, null);
        SharedPreferences sharedPreferences =mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");

        tv_shopinfo = (TextView)view.findViewById(R.id.tv_shopinfo);
        recycler_left = (RecyclerView)view.findViewById(R.id.recycler_left);
        llcontent = (LinearLayout)view.findViewById(R.id.llcontent);
        recycler_right = (RecyclerView)view.findViewById(R.id.recycler_right);
        tv_search_home = (EditText) view.findViewById(R.id.tv_search_home);
        return view;
    }

    @Override
    protected void initData() {
        Log.d("typehaha", "initData" );
        mManager = new GridLayoutManager(mContext, 3);
        recycler_right.setLayoutManager(mManager);
        typeAdapter = new TypeAdapter(mContext);
        secondTypeAdapter = new SecondTypeAdapter(mContext);
        //二级分类点击跳转到商品详情页
        secondTypeAdapter.setOnRecyclerViewListener(new OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(getContext(), GoodsInfoPage.class);
                ProductDetailJson productDetailJson = new ProductDetailJson(String.valueOf(TwoList.get(position).getSupplierId()),String.valueOf(TwoList.get(position).getId()));
                intent.putExtra("productdetail", productDetailJson);
                getContext().startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        recycler_right.setAdapter(secondTypeAdapter);

        //添加自定义分割线
        PaddingDecoration divider = new PaddingDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.custom_divider_address));
        recycler_left.addItemDecoration(new DashlineItemDivider());
        recycler_left.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recycler_left.setItemAnimator(new DefaultItemAnimator());

        recycler_left.setAdapter(typeAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("typehaha", "initData" );
        if (hidden) {
            return;
        }else{
            ((MainActivity) mContext).setOnGetTextListener(new MainActivity.OnGetTextListener() {
                @Override
                public void ongetSearchText(String search, int from) {
                    isSearch = true;
                    selectName = search;
                    tv_search_home.setText(selectName);
                    Log.d("interface", "selectName=:" + selectName);
                }

                @Override
                public void ongetTypeText(String type, int from) {
                    isType = true;
                    channeltext = type;
                    Log.d("interface", "channeltext=:" + channeltext);
                }
            });

            mMyOkhttp.get()
                    .url(Constant.SFSHURL+Constant.AllCategories+shopid)
                    .addHeader("cookie",username)
                    // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            Log.d("typehaha", "doPostJSON onSuccess JSONObject:" + response);
                            Data<List<ChannelModel>> obj = (Data<List<ChannelModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<ChannelModel>>>(){});
                            int  resCodes = obj.getCode();
                            if(getActivity()!=null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCodes == Constant.SUCCESSCODE) {
                                            // recommend_list = obj.getData();
                                            OneList.clear();
                                            if(isSearch){
                                                isSearch = false;
                                                ChannelModel channelModel = new ChannelModel("  搜   索  ","search");
                                                OneList.add(channelModel);
                                            }
                                            if(isType){
                                                isType = false;
                                                ChannelModel channelModelall = new ChannelModel("全  部  ","all");
                                                OneList.add(channelModelall);
                                            }
                                            //OneList = obj.getData();
                                            OneList.addAll(obj.getData());
                                            //typeAdapter.updataData(obj.getData());
                                            typeAdapter.updataData(OneList);
                                            getChannelClick();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, JSONArray response) {
                            Log.d("typehaha", "doPostJSON onSuccess JSONArray:" + response);
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            Log.d("typehaha", "doPostJSON onFailure:" + error_msg);
                        }
                    });

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("typehaha", "onResume" );
        SharedPreferences sharedPreferences_shop =mContext.getSharedPreferences("SHOP", Context.MODE_PRIVATE);
        shopid = sharedPreferences_shop.getInt("shopid",0);
        shopaddress = sharedPreferences_shop.getString("shopaddress","");
        shopmobile = sharedPreferences_shop.getString("shoptel","");
        shopname = sharedPreferences_shop.getString("shopname","");
        tv_shopinfo.setText(shopname+'\n'+"地址:"+shopaddress+'\n'+"电话:"+shopmobile);
        if(shopid==0){
            Toast.makeText(mContext,"没有获取店铺信息呢，请您先选择附近的店铺哦。",Toast.LENGTH_SHORT).show();
            return;
        }

        ((MainActivity) mContext).setOnGetTextListener(new MainActivity.OnGetTextListener() {
            @Override
            public void ongetSearchText(String search, int from) {
                selectName = search;
                tv_search_home.setText(selectName);
                Log.d("interface", "selectName=:" + selectName);
            }

            @Override
            public void ongetTypeText(String type, int from) {
                channeltext = type;
                Log.d("interface", "channeltext=:" + channeltext);
            }
        });


        mMyOkhttp.get()
                .url(Constant.SFSHURL+Constant.AllCategories+shopid)
                .addHeader("cookie",username)
                // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d("typehaha", "doPostJSON onSuccess JSONObject:" + response);
                        Data<List<ChannelModel>> obj = (Data<List<ChannelModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<ChannelModel>>>(){});
                        int  resCodes = obj.getCode();
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (resCodes == Constant.SUCCESSCODE) {
                                        // recommend_list = obj.getData();
                                        OneList.clear();

                                        ChannelModel channelModelall = new ChannelModel("全  部  ","all");
                                        OneList.add(channelModelall);

                                        OneList.addAll(obj.getData());
                                        typeAdapter.updataData(OneList);
                                        getChannelClick();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONArray response) {
                        Log.d("typehaha", "doPostJSON onSuccess JSONArray:" + response);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("typehaha", "doPostJSON onFailure:" + error_msg);
                    }
                });


        typeAdapter.setOnRecyclerViewListener(new OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.d("name","OneList.get(position).getId()=="+OneList.get(position).getId());
                typeAdapter.setPressPosition(position);
                typeAdapter.notifyDataSetChanged();
                if(OneList.get(position).getName().equals("全  部  ")){
                    mMyOkhttp.get()
                            .url(Constant.SFSHURL+Constant.AllProduct+shopid)
                            .addHeader("cookie",username)
                            // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<HotProduct>>>(){});
                                    int  resCodes = obj.getCode();
                                    if(getActivity()!=null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (resCodes == Constant.SUCCESSCODE) {
                                                    // recommend_list = obj.getData();
                                                    TwoList.clear();
                                                    TwoList = obj.getData();
                                                    if(obj.getData().size() ==0){
                                                        llcontent.setVisibility(View.VISIBLE);
                                                        recycler_right.setVisibility(View.GONE);
                                                    }else {
                                                        llcontent.setVisibility(View.GONE);
                                                        recycler_right.setVisibility(View.VISIBLE);
                                                        secondTypeAdapter.updataData(obj.getData());
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

                    return;
                }

                if(OneList.get(position).getName().equals("  搜   索  ")){
                    Search search = new Search(shopid, selectName);
                    String jsonstyle = JSON.toJSONString(search);
                    mMyOkhttp.post()
                            .addHeader("cookie",username)
                            .url(Constant.SFSHURL + Constant.Search+page+"/"+size)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<HotProduct>>>(){});
                                    int  resCodes = obj.getCode();
                                    if(getActivity()!=null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (resCodes == Constant.SUCCESSCODE) {
                                                    // recommend_list = obj.getData();
                                                    TwoList.clear();
                                                    TwoList = obj.getData();
                                                    if(obj.getData().size() ==0){
                                                        llcontent.setVisibility(View.VISIBLE);
                                                        recycler_right.setVisibility(View.GONE);
                                                    }else {
                                                        llcontent.setVisibility(View.GONE);
                                                        recycler_right.setVisibility(View.VISIBLE);
                                                        secondTypeAdapter.updataData(obj.getData());
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

                    return;
                }

                mMyOkhttp.get()
                        .url(Constant.SFSHURL+Constant.FirsClassification+OneList.get(position).getId()+"/"+shopid)
                        .addHeader("cookie",username)
                        // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<HotProduct>>>(){});
                                int  resCodes = obj.getCode();
                                if(getActivity()!=null){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (resCodes == Constant.SUCCESSCODE) {
                                                // recommend_list = obj.getData();
                                                TwoList.clear();
                                                TwoList = obj.getData();
                                                if(obj.getData().size() ==0){
                                                    llcontent.setVisibility(View.VISIBLE);
                                                    recycler_right.setVisibility(View.GONE);
                                                }else {
                                                    llcontent.setVisibility(View.GONE);
                                                    recycler_right.setVisibility(View.VISIBLE);
                                                    secondTypeAdapter.updataData(obj.getData());
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

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("typehaha", "onAttach" );
       if(context instanceof MainActivity){
           ((MainActivity) context).setOnGetTextListener(new MainActivity.OnGetTextListener() {
               @Override
               public void ongetSearchText(String search, int from) {
                   selectName = search;
               }

               @Override
               public void ongetTypeText(String type, int from) {
                   channeltext = type;
               }
           });
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
        /*selectName = ((MainActivity) context).getTitles();
        Log.d("pipei","newtypefragment=" +channeltext);
        channeltext= ((MainActivity) context).getChannelText();*/
    }

    public void getChannelClick(){
        if(OneList!=null && OneList.size()>0){
            for(int i=0;i<OneList.size();i++){
                Log.d("typehaha","OneList.get(i).getName().equals(channeltext)=="+(OneList.get(i).getName().equals(channeltext)));
                if(OneList.get(i).getName().equals(channeltext)){
                    typeAdapter.setPressPosition(i);
                    typeAdapter.notifyDataSetChanged();
                    mMyOkhttp.get()
                            .url(Constant.SFSHURL+Constant.FirsClassification+OneList.get(i).getId()+"/"+shopid)
                            .addHeader("cookie",username)
                            // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<HotProduct>>>(){});
                                    int  resCodes = obj.getCode();
                                    if(getActivity()!=null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (resCodes == Constant.SUCCESSCODE) {
                                                    // recommend_list = obj.getData();
                                                    TwoList.clear();
                                                    TwoList = obj.getData();
                                                    if(obj.getData().size() ==0){
                                                        llcontent.setVisibility(View.VISIBLE);
                                                        recycler_right.setVisibility(View.GONE);
                                                    }else {
                                                        llcontent.setVisibility(View.GONE);
                                                        recycler_right.setVisibility(View.VISIBLE);
                                                        secondTypeAdapter.updataData(obj.getData());
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
        }
    }




}
