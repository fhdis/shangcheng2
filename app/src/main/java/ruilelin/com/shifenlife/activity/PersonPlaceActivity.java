package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.InfoUnRead;
import ruilelin.com.shifenlife.json.LoginWithPasswordJson;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewAddressListener;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.listener.OnItemLongPressListener;
import ruilelin.com.shifenlife.model.AddressModel;
import ruilelin.com.shifenlife.person.adapter.PersonPlaceRvAdapter;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.view.MyDialog;
import ruilelin.com.shifenlife.view.PaddingDecoration;


public class PersonPlaceActivity extends BaseActivity implements OnClickRecyclerViewListener, OnClickRecyclerViewAddressListener, OnItemLongPressListener {

    // private MyOkHttp mMyOkhttp;
    private TextView title;
    RecyclerView mRvPersonPlace;
    LinearLayout mLlAddPlace;
    ImageButton mIbAddPlace;
    private String task;
    private String task1;
    private String username;
    private Button back;

    private PersonPlaceRvAdapter mPersonPlaceRvAdapter;
    private List<AddressModel> mInfoList = new ArrayList<AddressModel>();
    private int resCodeDelete = -1;


    @Override
    protected int setLayoutResID() {
        return R.layout.activity_person_place;
    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");

        task = getIntent().getStringExtra("task");
        task1 = getIntent().getStringExtra("task1");
        mPersonPlaceRvAdapter = new PersonPlaceRvAdapter(this);
        mPersonPlaceRvAdapter.setOnRecyclerViewListener(this);
        mPersonPlaceRvAdapter.setOnClickRecyclerViewAddressListener(this);
        mPersonPlaceRvAdapter.setOnItemLongPressListener(this);
        //添加自定义分割线
        //DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        PaddingDecoration divider = new PaddingDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider_address));
        mRvPersonPlace.addItemDecoration(divider);
        mRvPersonPlace.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvPersonPlace.setItemAnimator(new DefaultItemAnimator());

        mRvPersonPlace.setAdapter(mPersonPlaceRvAdapter);
        mIbAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonPlaceActivity.this, AddOrEditPlaceActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initView() {
        mRvPersonPlace = findViewById(R.id.rv_person_place);
        mLlAddPlace = findViewById(R.id.ll_add_place);
        mIbAddPlace = findViewById(R.id.ib_add_place);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        title.setText("地址管理");
        // mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(PersonPlaceActivity.this, MainActivity.class);
                intent.putExtra("check",3);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position, View view) {
        Log.d("task", "edit==");
        Intent intent = new Intent(this, AddOrEditPlaceActivity.class);
        intent.putExtra("isAdd", false);
        intent.putExtra("address", mInfoList.get(position));
        startActivity(intent);
        //startActivityForResult(intent, 1);
        finish();
    }

    @Override
    public void onItemAddressClick(int position, View view) {
        Log.d("task", "task==" + task);
        if (task != null) {
            if (task.equals("Choose_Address")) {
                Intent intent = new Intent();
                intent.putExtra("address_info", mInfoList.get(position));
                setResult(3, intent);
                finish();
            }
        }
        if (task1 != null) {
            if (task1.equals("Choose_Address_too")) {
                Intent intent = new Intent();
                intent.putExtra("address_info", mInfoList.get(position));
                setResult(6, intent);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }

    //调用地址请求
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("APITest", "onResume");
        mMyOkhttp.get()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.AllAddress)
                // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                        Data<List<AddressModel>> obj = (Data<List<AddressModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<AddressModel>>>() {});
                        int resCodes = obj.getCode();
                        if (resCodes == Constant.SUCCESSCODE) {
                            mInfoList = obj.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mInfoList != null & mInfoList.size() > 0) {
                                        mPersonPlaceRvAdapter.updataData(mInfoList);
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


    public void Login() {
        // OptionHandle(account,password);// 处理自动登录及记住密码
        LoginWithPasswordJson loginTelJson = new LoginWithPasswordJson("15262327502", "aaaaaa");
        String jsonstyle = JSON.toJSONString(loginTelJson);
        Log.d("APITest", "url==" + Constant.SFSHURL + Constant.LoginWithPassword);
        Log.d("APITest", "json==" + jsonstyle);


        mMyOkhttp.post()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.LoginWithPassword)
                .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                        sp.edit().putString("username", "15262327502").apply();
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
    public void onItemLongPress(int position, View view) {
        MyDialog.show(PersonPlaceActivity.this, "确认删除吗?", new MyDialog.OnConfirmListener() {
            @Override
            public void onConfirmClick() {

                AddressModel addressModel = mInfoList.get(position);
                if (addressModel.getDefaultAddress()) {
                    showToast("默认地址不可删除");
                    return;
                }

                String url = Constant.SFSHURL + Constant.deletsAddress + addressModel.getId();
                mMyOkhttp.delete()
                        .addHeader("cookie", username)
                        .url(url)
                        .tag(this).enqueue(
                        new JsonResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doDelete onSuccess:" + response);
                                Data<List<AddressModel>> obj = (Data<List<AddressModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<AddressModel>>>() {
                                });
                                resCodeDelete = obj.getCode();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCodeDelete == Constant.SUCCESSCODE) {
                                            //AddOrEditPlaceActivity.this.finish();
                                            /*Intent intent = new Intent(PersonPlaceActivity.this, PersonPlaceActivity.class);
                                            startActivity(intent);*/
                                            showToast("删除地址成功");
                                            if (mInfoList != null & mInfoList.size() > 0) {
                                                mInfoList.remove(position);
                                                mPersonPlaceRvAdapter.updataData(mInfoList);
                                            }
                                        } else if (resCodeDelete == Constant.NOTlOG) {
                                            showToast("请先登录");
                                        } else {
                                            showToast("删除地址失败");
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                Log.d("APITest", "doDelete onFailure:" + error_msg);
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}