package ruilelin.com.shifenlife.home.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.ConfirmAddress;
import ruilelin.com.shifenlife.activity.InfoActivity;
import ruilelin.com.shifenlife.activity.NearByShopActivity;
import ruilelin.com.shifenlife.base.BaseFragment;
import ruilelin.com.shifenlife.cart.customview.RoundCornerDialog;
import ruilelin.com.shifenlife.home.adapter.HomeRecyleViewAdapter;
import ruilelin.com.shifenlife.home.view.DrawableTextView;
import ruilelin.com.shifenlife.json.BannerJson;
import ruilelin.com.shifenlife.json.BannerRequest;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.HotProduct;
import ruilelin.com.shifenlife.json.NearbyShop;
import ruilelin.com.shifenlife.jsonmodel.ChannelModel;
import ruilelin.com.shifenlife.jsonmodel.RecommendModel;
import ruilelin.com.shifenlife.model.AddressModel;
import ruilelin.com.shifenlife.service.LocationService;
import ruilelin.com.shifenlife.utils.Constant;

public class RecyclerHomeFragment extends BaseFragment {
    private RecyclerView recycle_home;
    private HomeRecyleViewAdapter homeRecyleViewAdapter;
    private TextView tv_message_home;
    //附近店铺
    private DrawableTextView tv_nearby_shop;
    private DrawableTextView tv_address;
    private EditText tv_search_home;

    private NearbyShop nearByShopId;
    private String username;
    OnEditListener editListener;
    public MainActivity activity;

    private int shopid;
    private String shopaddress;
    private String shopmobile;
    private String shopname;

    private String currentAddress;
    private AddressModel getAddressmodel;

    private MyReceiver receiver=null;
    private double lat, lng;
    private boolean firstLocation = true;


    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home_one, null);
        recycle_home = (RecyclerView)view.findViewById(R.id.recycle_home);
        tv_message_home =  (TextView)view.findViewById(R.id.tv_message_home);
        tv_nearby_shop =  (DrawableTextView)view.findViewById(R.id.tv_nearby_shop);
        tv_address =  (DrawableTextView)view.findViewById(R.id.tv_address);
        tv_search_home = (EditText)view.findViewById(R.id.tv_search_home);
        return view;
    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences =mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        tv_search_home.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEND ||(keyEvent!=null&&keyEvent.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    Log.d("edit","(editListener!=null)=="+(editListener!=null));
                   if(editListener!=null){
                       Log.d("edit","i=="+tv_search_home.getText().toString());
                       editListener.onEditSelected(tv_search_home.getText().toString(),1);
                       return true;
                   }
                    Log.d("edit","i=="+i);
                    return false;
                }
                    return false;
            }
        });
         //跳转到消息界面
        tv_message_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InfoActivity.class);
                startActivity(intent);
            }
        });

        //附近店铺
        tv_nearby_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NearByShopActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                startActivityForResult(intent, 10);
            }
        });

        ConfirmAddress confirmAddress = new ConfirmAddress();
        confirmAddress.setOnActivityToFragment(new ConfirmAddress.ActivityToFragment() {
            @Override
            public void ongetAddress(AddressModel addressModel) {
                getAddressmodel = addressModel;
                Log.d("addressmodel","getAddressmodel==null?"+(getAddressmodel==null));
            }
        });


        //选择收货地址
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ConfirmAddress.class);
                 //startActivity(intent);
                intent.putExtra("location", "location");
                startActivityForResult(intent, 4);
            }
        });

        homeRecyleViewAdapter = new HomeRecyleViewAdapter(mContext);
        recycle_home.setAdapter(homeRecyleViewAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,1);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        recycle_home.setLayoutManager(layoutManager);

        Intent startIntent = new Intent(mContext, LocationService.class);
        mContext.startService(startIntent); // 启动服务
        //注册广播接收器
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("ruilelin.com.location");
        mContext.registerReceiver(receiver,filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(mContext, LocationService.class);
        mContext.stopService(stopIntent); // 停止服务
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            return;
        }else{
            ConfirmAddress confirmAddress = new ConfirmAddress();
            confirmAddress.setOnActivityToFragment(new ConfirmAddress.ActivityToFragment() {
                @Override
                public void ongetAddress(AddressModel addressModel) {
                    getAddressmodel = addressModel;
                    Log.d("addressmodel","getAddressmodel==null?"+(getAddressmodel==null));
                }
            });

        }
    }



    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences_shop =mContext.getSharedPreferences("SHOP", Context.MODE_PRIVATE);
        shopid = sharedPreferences_shop.getInt("shopid",0);
        shopaddress = sharedPreferences_shop.getString("shopaddress","");
        shopmobile = sharedPreferences_shop.getString("shoptel","");
        shopname = sharedPreferences_shop.getString("shopname","");


        homeRecyleViewAdapter.setItemTypeOnClickListener(new HomeRecyleViewAdapter.ItemTypeOnClickListener() {
            @Override
            public void itemTypeOnClickListener(View view) {
                TextView tv = (TextView) view.findViewById(R.id.tv_channel);
                Log.d("edit","tv.getText().toString()="+tv.getText().toString());
                editListener.onEditSelected(tv.getText().toString(),2);
            }
        });

        if(shopid!=0){
            tv_nearby_shop.setText(shopname);
            BannerRequest bannerRequest = new BannerRequest(shopid,"INDEX");
            String jsonstyle = JSON.toJSONString(bannerRequest);
            //广告图展示
            mMyOkhttp.post()
                    //.addHeader("cookie",username)
                    .url(Constant.SFSHURL+Constant.Advertisings)
                    .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            Data<List<BannerJson>> obj = (Data<List<BannerJson>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<BannerJson>>>(){});
                            int  resCodes = obj.getCode();
                            if(getActivity()!=null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCodes == Constant.SUCCESSCODE) {
                                            Log.d("APITest", " banner obj.getData().size" + obj.getData().size());
                                            if(obj.getData().size()==0){
                                                List<BannerJson> temps = new ArrayList<>();
                                                BannerJson bannerJson3 = new BannerJson("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540442844211&di=288f573047cf8f2c109c4d3afb6d9222&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F88%2F38%2F29258PICIrW_1024.jpg");
                                                BannerJson bannerJson2 = new BannerJson("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540442844211&di=288f573047cf8f2c109c4d3afb6d9222&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F88%2F38%2F29258PICIrW_1024.jpg");
                                                BannerJson bannerJson1 = new BannerJson("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540442844211&di=288f573047cf8f2c109c4d3afb6d9222&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F88%2F38%2F29258PICIrW_1024.jpg");
                                                temps.add(bannerJson3);
                                                temps.add(bannerJson2);
                                                temps.add(bannerJson1);
                                                homeRecyleViewAdapter.setbannerbean(temps);
                                            }else if(obj.getData().size()>0){
                                                homeRecyleViewAdapter.setbannerbean(obj.getData());
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


            //获取所有分类
            mMyOkhttp.get()
                    .url(Constant.SFSHURL+Constant.AllCategories+shopid)
                  //  .addHeader("cookie",username)
                    // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);

                            Data<List<ChannelModel>> obj = (Data<List<ChannelModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<ChannelModel>>>(){});
                            int  resCodes = obj.getCode();
                            Log.d("cookies", "resCodes=:" + resCodes);
                            if(getActivity()!=null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCodes == Constant.SUCCESSCODE) {
                                            // recommend_list = obj.getData();
                                            homeRecyleViewAdapter.setchannelbean(obj.getData());
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


            //请求推荐商品
            mMyOkhttp.get()
                    .url(Constant.SFSHURL+Constant.RecommendGoods+shopid)
                 //   .addHeader("cookie",username)
                    // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                            Data<List<RecommendModel>> obj = (Data<List<RecommendModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<RecommendModel>>>(){});
                            int  resCodes = obj.getCode();
                            if(getActivity()!=null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCodes == Constant.SUCCESSCODE) {
                                            // recommend_list = obj.getData();
                                            Log.d("heightAA","setrecommendbean obj.getData().size()=="+obj.getData().size());
                                            homeRecyleViewAdapter.setrecommendbean(obj.getData());
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

            //店铺热卖
            mMyOkhttp.get()
                    .url(Constant.SFSHURL+Constant.HOT+shopid)
                //    .addHeader("cookie",username)
                    // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference  <Data<List<HotProduct>>>(){});
                            int resCode = obj.getCode();
                            if(getActivity()!=null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(resCode ==Constant.SUCCESSCODE){
                                            homeRecyleViewAdapter.sethotbean(obj.getData());
                                        }
                                    }
                                });}

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
        }else{
            showDeleteDialog();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==12){
            if(requestCode==10){
                NearbyShop nearbyShop = (NearbyShop)intent.getSerializableExtra("nearbyshop");
            if(nearbyShop!=null) {
                SharedPreferences sharedPreferences_shop = mContext.getSharedPreferences("SHOP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences_shop.edit();
                editor.putInt("shopid",nearbyShop.getId());
                editor.putString("shopaddress",nearbyShop.getAddress());
                editor.putString("shoptel",nearbyShop.getMobile());
                editor.putString("shopname",nearbyShop.getName());
                editor.commit();

                tv_nearby_shop.setText(nearbyShop.getName());
                nearByShopId = nearbyShop;
                if(nearByShopId!=null){
                    BannerRequest bannerRequest = new BannerRequest(nearByShopId.getId(),"INDEX");
                    String jsonstyle = JSON.toJSONString(bannerRequest);
                    //广告图展示
                    mMyOkhttp.post()
                         //   .addHeader("cookie",username)
                            .url(Constant.SFSHURL+Constant.Advertisings)
                             .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Data<List<BannerJson>> obj = (Data<List<BannerJson>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<BannerJson>>>(){});
                                    int  resCodes = obj.getCode();
                                    if(getActivity()!=null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (resCodes == Constant.SUCCESSCODE) {
                                                    if(obj.getData().size()==0){
                                                        List<BannerJson> temps = new ArrayList<>();
                                                        BannerJson bannerJson3 = new BannerJson("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540442844211&di=288f573047cf8f2c109c4d3afb6d9222&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F88%2F38%2F29258PICIrW_1024.jpg");
                                                        BannerJson bannerJson2 = new BannerJson("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540442844211&di=288f573047cf8f2c109c4d3afb6d9222&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F88%2F38%2F29258PICIrW_1024.jpg");
                                                        BannerJson bannerJson1 = new BannerJson("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540442844211&di=288f573047cf8f2c109c4d3afb6d9222&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F88%2F38%2F29258PICIrW_1024.jpg");
                                                        temps.add(bannerJson3);
                                                        temps.add(bannerJson2);
                                                        temps.add(bannerJson1);
                                                        homeRecyleViewAdapter.setbannerbean(temps);
                                                    }else if(obj.getData().size()>0){
                                                        homeRecyleViewAdapter.setbannerbean(obj.getData());
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


                    //获取所有分类
                    mMyOkhttp.get()
                            .url(Constant.SFSHURL+Constant.AllCategories+nearByShopId.getId())
                       //     .addHeader("cookie",username)
                            // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<List<ChannelModel>> obj = (Data<List<ChannelModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<ChannelModel>>>(){});
                                    int  resCodes = obj.getCode();
                                    if(getActivity()!=null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (resCodes == Constant.SUCCESSCODE) {
                                                    // recommend_list = obj.getData();
                                                    homeRecyleViewAdapter.setchannelbean(obj.getData());
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


                    //请求推荐商品
                    mMyOkhttp.get()
                            .url(Constant.SFSHURL+Constant.RecommendGoods+nearByShopId.getId())
                          //  .addHeader("cookie",username)
                            // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<List<RecommendModel>> obj = (Data<List<RecommendModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<RecommendModel>>>(){});
                                    int  resCodes = obj.getCode();
                                    if(getActivity()!=null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (resCodes == Constant.SUCCESSCODE) {
                                                    // recommend_list = obj.getData();
                                                    Log.d("heightAA","2222 setrecommendbean obj.getData().size()=="+obj.getData().size());
                                                    homeRecyleViewAdapter.setrecommendbean(obj.getData());
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

                    //店铺热卖
                    mMyOkhttp.get()
                            .url(Constant.SFSHURL+Constant.HOT+nearByShopId.getId())
                       //     .addHeader("cookie",username)
                            // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference  <Data<List<HotProduct>>>(){});
                                    int resCode = obj.getCode();
                                    if(getActivity()!=null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(resCode ==Constant.SUCCESSCODE){
                                                    homeRecyleViewAdapter.sethotbean(obj.getData());
                                                }
                                            }
                                        });}

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

        if(resultCode==6){
            if(requestCode==4){
                AddressModel addressModel = (AddressModel)intent.getSerializableExtra("address_info");
                if(addressModel!=null){
                    tv_address.setText(addressModel.getAddress());
                    lat=addressModel.getLat();
                    lng=addressModel.getLng();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            editListener = (OnEditListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    public interface OnEditListener {
         void onEditSelected(String text,int from);
    }


    private void showDeleteDialog() {
        View view = View.inflate(mContext, R.layout.dialog_one_button, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        //roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("请先点击界面右上角 附件店铺 选择您附近的店铺，这样才能显示店铺商品信息呦.");

        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.cancel();
            }
        });
    }

    //获取广播数据
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            lat=bundle.getDouble("latitude");
            lng=bundle.getDouble("longitude");
            if(firstLocation) {
                if (bundle.getString("cityname") != null) {
                    firstLocation = false;
                    currentAddress = bundle.getString("cityname");
                    tv_address.setText(currentAddress);
                }
            }
        }
    }
}
