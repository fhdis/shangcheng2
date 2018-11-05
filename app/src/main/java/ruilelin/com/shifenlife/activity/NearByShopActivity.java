package ruilelin.com.shifenlife.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.home.adapter.NearByShopRvAdapter;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.NearbyShop;
import ruilelin.com.shifenlife.json.RequestNearby;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.service.LocationService;
import ruilelin.com.shifenlife.utils.Constant;

public class NearByShopActivity extends BaseActivity {
    private TextView mTbTitle;
    private Button back;
    private RecyclerView recyc_nearby;
    private List<NearbyShop> mNearByList = new ArrayList<NearbyShop>();
    private NearByShopRvAdapter nearByShopRvAdapter;
    private double lat, lng;
    RequestNearby requestNearby;
    private String username;

   // private MyReceiver receiver=null;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_nearby_shop;
    }

    @Override
    protected void initData() {
        mTbTitle = (TextView) findViewById(R.id.title);
        back = (Button) findViewById(R.id.back);
        mTbTitle.setText("选择附近店铺");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        nearByShopRvAdapter = new NearByShopRvAdapter(this);
        nearByShopRvAdapter.setOnRecyclerViewListener(new OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View view) {
                // Toast.makeText(NearByShopActivity.this,"选择了该门店"+mNearByList.get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("nearbyshop", mNearByList.get(position));
                setResult(12, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
        recyc_nearby.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyc_nearby.setAdapter(nearByShopRvAdapter);

        Intent startIntent = new Intent(this, LocationService.class);
        startService(startIntent); // 启动服务
        //注册广播接收器
       /* receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("ruilelin.com.location");
        NearByShopActivity.this.registerReceiver(receiver,filter);*/

    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        recyc_nearby = (RecyclerView) findViewById(R.id.recyc_nearby);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        lat = getIntent().getDoubleExtra("lat",0);
        lng = getIntent().getDoubleExtra("lng",0);
        requestNearby = new RequestNearby(lat, lng);
        String jsonstyle = JSON.toJSONString(requestNearby);
        mMyOkhttp.post()
                .url(Constant.SFSHURL + Constant.Nearby)
                .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Data<List<NearbyShop>> obj = (Data<List<NearbyShop>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<NearbyShop>>>() {
                        });
                        int resCode = obj.getCode();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    if (obj.getData().size() > 0) {
                                        mNearByList = obj.getData();
                                        nearByShopRvAdapter.updataData(obj.getData());
                                    } else if (obj.getData().size() == 0) {
                                        Toast.makeText(NearByShopActivity.this, "没有发现附近的门店哦", Toast.LENGTH_SHORT).show();
                                    }
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
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(this, LocationService.class);
        stopService(stopIntent); // 停止服务
    }


    //获取广播数据
   /* public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            lat=bundle.getDouble("latitude");
            lng=bundle.getDouble("longitude");
            Log.d("nearby","MyReceiver=="+lat);
            Log.d("nearby","lat http=="+lat);
            Log.d("nearby","lng http=="+lng);
            requestNearby = new RequestNearby(lat, lng);
            String jsonstyle = JSON.toJSONString(requestNearby);
            mMyOkhttp.post()
                    .url(Constant.SFSHURL + Constant.Nearby)
                    .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            Data<List<NearbyShop>> obj = (Data<List<NearbyShop>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<NearbyShop>>>() {
                            });
                            int resCode = obj.getCode();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (resCode == Constant.SUCCESSCODE) {
                                        if (obj.getData().size() > 0) {
                                            mNearByList = obj.getData();
                                            nearByShopRvAdapter.updataData(obj.getData());
                                        } else if (obj.getData().size() == 0) {
                                            Toast.makeText(NearByShopActivity.this, "没有发现附近的门店哦", Toast.LENGTH_SHORT).show();
                                        }
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
    }*/
}
