package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.home.adapter.GetAddressAdapter;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.LoginWithPasswordJson;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.map.nearbyaddress.LocationInfo;
import ruilelin.com.shifenlife.map.poi.InputTipsAdapter;
import ruilelin.com.shifenlife.model.AddressModel;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.view.PaddingDecoration;

public class ConfirmAddress extends BaseActivity implements AMapLocationListener,PoiSearch.OnPoiSearchListener,SearchView.OnQueryTextListener,Inputtips.InputtipsListener, AdapterView.OnItemClickListener{
    private RecyclerView recyc_address;
    private String username;
    private GetAddressAdapter getAddressAdapter;
    private List<AddressModel> openList = new ArrayList<>();
    private List<AddressModel> closeList = new ArrayList<>();
    private List<AddressModel> alladdressList = new ArrayList<>();
    private int size =3;
    private TextView tv_address;
    private TextView tv_refresh;
    private TextView tv_address_one;
    private TextView tv_address_two;
    public AMapLocationClient mlocationClient = null;
    private SearchView searchView;
    private ActivityToFragment activityToFragment;
    private AddressModel addressModel_one,addressModel_two;

    private ListView mInputListView;
    private List<Tip> mCurrentTipList;
    private InputTipsAdapter mIntipAdapter;

    public static String DEFAULT_CITY = "张家港";
    public static final int RESULT_CODE_INPUTTIPS = 101;
    public static final int REQUEST_SUC = 1000;
    private AddressModel addressModel;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_show_address;
    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");

        initLocate();
        getAddressAdapter = new GetAddressAdapter(this);

        //添加自定义分割线
        //DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        PaddingDecoration divider = new PaddingDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider_address));
        recyc_address.addItemDecoration(divider);
        recyc_address.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyc_address.setItemAnimator(new DefaultItemAnimator());

        recyc_address.setAdapter(getAddressAdapter);
        getAddressAdapter.setHideOrShowCallBack(new GetAddressAdapter.HideOrShowCallBack() {
            @Override
            public void hide() {
                getAddressAdapter.setHideList(closeList);
            }

            @Override
            public void open() {
                getAddressAdapter.setOpenList(openList);
            }
        });

        getAddressAdapter.setOnRecyclerViewListener(new OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.d("addressmodel","activityToFragment!=null?"+(activityToFragment!=null));
               /*if(activityToFragment!=null){
                    activityToFragment.ongetAddress(alladdressList.get(position));
                    finish();
                }*/
                Intent intent = new Intent();
                intent.putExtra("address_info", alladdressList.get(position));
                setResult(6, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
    }

    @Override
    protected void initView() {
        recyc_address = (RecyclerView)findViewById(R.id.recyc_address);
        tv_address = (TextView)findViewById(R.id.tv_address);
        tv_refresh = (TextView)findViewById(R.id.tv_refresh);
        tv_address_one = (TextView)findViewById(R.id.tv_address_one);
        tv_address_two = (TextView)findViewById(R.id.tv_address_two);
        initSearchView();
        mInputListView = findViewById(R.id.inputtip_list);
        mInputListView.setOnItemClickListener(this);
    }



    private void initSearchView() {
        searchView = findViewById(R.id.keyWord);
        searchView.setOnQueryTextListener(this);
        //设置SearchView默认为展开显示
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);
    }

    /**
     * 输入提示回调
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        // 正确返回
        if (rCode == REQUEST_SUC) {
            mCurrentTipList = tipList;
            mIntipAdapter = new InputTipsAdapter(ConfirmAddress.this, mCurrentTipList);
            mInputListView.setAdapter(mIntipAdapter);
            mIntipAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "错误码 :" + rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mCurrentTipList != null) {
            Tip tip = (Tip) adapterView.getItemAtPosition(i);
            addressModel = new AddressModel(tip.getAddress(),tip.getPoint().getLatitude(),tip.getPoint().getLongitude());
            Intent intent = new Intent();
            intent.putExtra("address_info", addressModel);
            setResult(6, intent);
            finish();
            }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * 输入字符变化时触发
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            mInputListView.setVisibility(View.VISIBLE);
            InputtipsQuery inputquery = new InputtipsQuery(newText, DEFAULT_CITY);
            Inputtips inputTips = new Inputtips(ConfirmAddress.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            // 如果输入为空  则清除 listView 数据
            if (mIntipAdapter != null && mCurrentTipList != null) {
                mCurrentTipList.clear();
                mIntipAdapter.notifyDataSetChanged();
                mInputListView.setVisibility(View.GONE);
            }
        }
        return true;
    }


    @Override
    protected void initListener() {
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLocate();
                Toast.makeText(ConfirmAddress.this, "正在重定位", Toast.LENGTH_SHORT).show();
            }
        });

        tv_address_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("address_info", addressModel_one);
                setResult(6, intent);
                finish();
            }
        });

        tv_address_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("address_info", addressModel_one);
                setResult(6, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setFocusable(false);
        searchView.setFocusableInTouchMode(false);
        mMyOkhttp.get()
                //.addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.AllAddress)
                // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                        Data<List<AddressModel>> obj = (Data<List<AddressModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<AddressModel>>>() {
                        });
                        int resCodes = obj.getCode();
                        if (resCodes == Constant.SUCCESSCODE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (obj.getData() != null & obj.getData().size() > 0) {
                                        Log.d("1031","obj.getData().size()=="+obj.getData().size());
                                        alladdressList.clear();
                                        alladdressList = obj.getData();
                                        if (obj.getData().size() > size) {
                                            openList.clear();
                                            closeList.clear();
                                            for (int i = 0; i < obj.getData().size(); i++) {
                                                openList.add(obj.getData().get(i));
                                            }
                                            AddressModel open = new AddressModel("收起");
                                            openList.add(open);

                                            for (int i = 0; i < 3; i++) {
                                                closeList.add(obj.getData().get(i));
                                            }
                                            AddressModel addressModel = new AddressModel("查看更多 V");
                                            closeList.add(addressModel);
                                            getAddressAdapter.setHideList(closeList);
                                        } else {
                                            getAddressAdapter.setRealList(obj.getData());
                                        }
                                        //getAddressAdapter.updataData(obj.getData());
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

    //定位
    //获取定位信息
    private void initLocate() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setAddress(amapLocation.getAddress());
                locationInfo.setLatitude(latitude);
                locationInfo.setLonTitude(longitude);
                tv_address.setText(amapLocation.getAddress());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                PoiSearch.Query query = new PoiSearch.Query("", "生活服务", "");
                query.setPageSize(20);
                PoiSearch search = new PoiSearch(this, query);
                search.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 100000));
                search.setOnPoiSearchListener(this);
                search.searchPOIAsyn();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        PoiSearch.Query query = poiResult.getQuery();
        ArrayList<PoiItem> pois = poiResult.getPois();
        if(pois != null && pois.size()>0) {
            if (pois.get(0)!=null) {
                addressModel_one = new AddressModel(pois.get(0).getSnippet(),pois.get(0).getLatLonPoint().getLatitude(),pois.get(0).getLatLonPoint().getLongitude());
                tv_address_one.setText(pois.get(0).getSnippet());
            }
            if (pois.get(1)!=null) {
                addressModel_two = new AddressModel(pois.get(1).getSnippet(),pois.get(1).getLatLonPoint().getLatitude(),pois.get(1).getLatLonPoint().getLongitude());
                tv_address_two.setText(pois.get(1).getSnippet());
            }
        }
       /* for (PoiItem poi : pois) {
            String name = poi.getCityName();
            String snippet = poi.getSnippet();
            LocationInfo info = new LocationInfo();
            info.setAddress(snippet);
            LatLonPoint point = poi.getLatLonPoint();

            info.setLatitude(point.getLatitude());
            info.setLonTitude(point.getLongitude());
        }*/
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public void setOnActivityToFragment(ActivityToFragment listener) {
        activityToFragment = listener;
    }
    public interface ActivityToFragment {
        void ongetAddress(AddressModel addressModel);
    }
}
