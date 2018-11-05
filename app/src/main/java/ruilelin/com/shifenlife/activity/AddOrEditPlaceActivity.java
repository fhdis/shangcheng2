package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Answer;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.EditAddress;
import ruilelin.com.shifenlife.json.PlusAddressJson;
import ruilelin.com.shifenlife.model.AddressModel;

//import com.amap.api.services.core.PoiItem;

import ruilelin.com.shifenlife.utils.Constant;

public class AddOrEditPlaceActivity extends BaseActivity implements SensorEventListener {

    private boolean isAdd;
    private AddressModel mAddress;
    //private Toolbar mTbTitle;
    private TextView title;
    private Button back;
    //private TextView mTvTitle;
    private EditText mEtName;
    private TextView mTvMale;
    private TextView mTvFemale;
    private EditText mEtPhone;
    private TextView mEtPlace;
    private Button mBtnConfirm;
    //private Button mBtnDelete;
    // private MyOkHttp mMyOkhttp;
    private int resCodeAdd = -1;
    private int resCodeEdit = -1;
    private int resCodeDelete = -1;
    private double mCurrentLat = 31.873496;
    private double mCurrentLon = 120.5205;
    //private PoiItem poiItem;
    private CheckBox chbAgree;
    private EditText et_place_number;
    private String username;
    private String sex;

    private String consignee;
    private String mobile;
    private String address;
    private String looseAddress;

    private boolean defaultAddress;

    @Override
    protected int setLayoutResID() {
        Log.d("APITest", "1==");
        return R.layout.activity_add_or_edit_place;
    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);

        Log.d("APITest", "2==");
        isAdd = getIntent().getBooleanExtra("isAdd", false);
        //mTbTitle.setTitle("");
        //mTbTitle.setNavigationIcon(R.mipmap.arrow_left);
        //setSupportActionBar(mTbTitle);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (isAdd) {
            //默认不可提交，不可点击
            unClickable();

            //mTvTitle.setText("新增地址");
            title.setText("新增地址");
            //mBtnDelete.setVisibility(View.GONE);
            mTvMale.setSelected(true);
            sex = "BOY";
        } else {
            //可点击，可提交
            clickable();

            Log.d("APITest", "3==");
            //mTvTitle.setText("编辑地址");
            title.setText("编辑地址");
            // mBtnDelete.setVisibility(View.VISIBLE);
            mAddress = (AddressModel) getIntent().getSerializableExtra("address");
            mEtName.setText(mAddress.getConsignee());
            mEtPhone.setText(mAddress.getMobile());
            mEtPlace.setText(mAddress.getLooseAddress());
            et_place_number.setText(mAddress.getAddress());

            defaultAddress = mAddress.getDefaultAddress();
            if (defaultAddress) {
                //chbAgree.setChecked(true);
                chbAgree.setVisibility(View.GONE);
            } else {
                chbAgree.setVisibility(View.VISIBLE);
                chbAgree.setChecked(false);
            }
            sex = mAddress.getSex();
            if (sex.equals("BOY")) {
                mTvMale.setSelected(true);
                mTvFemale.setSelected(false);
                setSexLeftDrawable(true);
            } else if (sex.equals("GIRL")) {
                mTvMale.setSelected(false);
                mTvFemale.setSelected(true);
                setSexLeftDrawable(false);
            }
        }
    }

    /**
     * 设置性别左侧图标
     *
     * @param flag 选择"女士"还是"先生"
     *             true:先生
     *             false: 女士
     */
    private void setSexLeftDrawable(boolean flag) {
        Drawable drawableLeftChecked = getResources().getDrawable(R.drawable.sex_checked);
        Drawable drawableLeftUnChecked = getResources().getDrawable(R.drawable.sex_unchecked);

        if (flag) {
            mTvMale.setCompoundDrawablesWithIntrinsicBounds(drawableLeftChecked, null, null, null);
            mTvFemale.setCompoundDrawablesWithIntrinsicBounds(drawableLeftUnChecked, null, null, null);
        } else {
            mTvMale.setCompoundDrawablesWithIntrinsicBounds(drawableLeftUnChecked, null, null, null);
            mTvFemale.setCompoundDrawablesWithIntrinsicBounds(drawableLeftChecked, null, null, null);
        }
    }

    @Override
    protected void initView() {
        Log.d("APITest", "4==");
        //mTbTitle = (Toolbar) findViewById(R.id.tb_title);

        mEtName = (EditText) findViewById(R.id.et_name);
        mTvMale = (TextView) findViewById(R.id.tv_male);
        mTvFemale = (TextView) findViewById(R.id.tv_female);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPlace = (TextView) findViewById(R.id.et_place);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        //mBtnDelete = (Button) findViewById(R.id.btn_delete);
        chbAgree = (CheckBox) findViewById(R.id.chbAgree);
        et_place_number = (EditText) findViewById(R.id.et_place_number);
        //mTvTitle = findViewById(R.id.mTvTitle);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        //  mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
    }

    @Override
    protected void initListener() {

        //     setSupportActionBar(mTbTitle);
        //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mTbTitle.setNavigationOnClickListener(new View.OnClickListener() {
        //   @Override
        //  public void onClick(View v) {
        //     finish();
        //}
        //});
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex = "BOY";
                mTvMale.setSelected(true);
                mTvFemale.setSelected(false);
                setSexLeftDrawable(true);
            }
        });

        mTvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex = "GIRL";
                mTvFemale.setSelected(true);
                mTvMale.setSelected(false);
                setSexLeftDrawable(false);
            }
        });

        //为输入密码框绑定addTextChangedListener事件
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //loginPassword = edit_password.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                consignee = mEtName.getText().toString();
                mobile = mEtPhone.getText().toString();
                looseAddress = et_place_number.getText().toString();

                //是否忘记密码
                changeButtonBackground(consignee, mobile, looseAddress);
            }
        });

        //为电话号码绑定addTextChangedListener事件
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //confirmPassword = edit_password_ver.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(ChangedPassword.this, loginPassword + "-" + confirmPassword, Toast.LENGTH_LONG).show();
                consignee = mEtName.getText().toString();
                mobile = mEtPhone.getText().toString();
                looseAddress = et_place_number.getText().toString();

                //是否忘记密码
                changeButtonBackground(consignee, mobile, looseAddress);
            }
        });

        mEtPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //confirmPassword = edit_password_ver.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                consignee = mEtName.getText().toString();
                mobile = mEtPhone.getText().toString();
                looseAddress = et_place_number.getText().toString();

                changeButtonBackground(consignee, mobile, looseAddress);
            }
        });

        et_place_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                consignee = mEtName.getText().toString();
                mobile = mEtPhone.getText().toString();
                looseAddress = et_place_number.getText().toString();

                //是否忘记密码
                changeButtonBackground(consignee, mobile, looseAddress);
            }
        });

        mEtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddOrEditPlaceActivity.this, GaodeActivity.class);
                // startActivity(intent);
                intent.putExtra("task2", "Choose_poi");
                startActivityForResult(intent, 8);
            }
        });
        Log.d("APITest", "5==");
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdd) {
                    // POST请求
                    String consignee = mEtName.getText().toString();
                    //String sex = "BOY";
                    /*if (mTvMale.isPressed()) {
                        mTvMale.setSelected(true);
                        mTvFemale.setSelected(false);
                        sex = "GIRL";
                    } else if (mTvFemale.isPressed()) {
                        mTvMale.setSelected(false);
                        mTvFemale.setSelected(true);
                        sex = "BOY";
                    }*/
                    String mobile = mEtPhone.getText().toString();
                    //Boolean defaultAddress = false;
                    //Boolean defaultAddress = chbAgree.isChecked();
                    String address = et_place_number.getText().toString();
                    Double lat = mCurrentLat;
                    Double lng = mCurrentLon;
                    String looseAddress = mEtPlace.getText().toString();

                    PlusAddressJson plusAddressJson = new PlusAddressJson(consignee, sex, mobile, defaultAddress, address, lat, lng, looseAddress);
                    String jsonstyle = JSON.toJSONString(plusAddressJson);
                    Log.d("APITest", "djsonstyle=" + jsonstyle);
                    mMyOkhttp.post()
                           // .addHeader("cookie", username)
                            .url(Constant.SFSHURL + Constant.AddAddress)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Data<List<AddressModel>> obj = (Data<List<AddressModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<AddressModel>>>() {
                                    });
                                    resCodeAdd = obj.getCode();
                                    Log.d("APITest", "doPostJSON add_address=" + response);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (resCodeAdd == Constant.SUCCESSCODE) {
                                                Toast.makeText(AddOrEditPlaceActivity.this, "添加地址成功", Toast.LENGTH_LONG).show();
                                                //AddOrEditPlaceActivity.this.finish();
                                                Intent intent = new Intent(AddOrEditPlaceActivity.this, PersonPlaceActivity.class);
                                                startActivity(intent);
                                                AddOrEditPlaceActivity.this.finish();
                                            } else if (resCodeAdd == Constant.NOTlOG) {
                                                Toast.makeText(AddOrEditPlaceActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(AddOrEditPlaceActivity.this, "新增收货地址失败", Toast.LENGTH_LONG).show();
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
                } else {
                    //编辑收货地址
                    String consignee = mEtName.getText().toString();
                    String mobile = mEtPhone.getText().toString();
                    //Boolean defaultAddress = false;
                    Boolean defaultAddress = chbAgree.isChecked();
                    String address = et_place_number.getText().toString();
                    Double lat = mCurrentLat;
                    Double lng = mCurrentLon;
                    String looseAddress = mEtPlace.getText().toString();
                    EditAddress editAddress = new EditAddress(consignee, sex, mobile, defaultAddress, address, lat, lng, looseAddress, mAddress.getId());
                    Log.d("APITest", "mAddress.getId()=" + mAddress.getId());
                    String jsonstyle = JSON.toJSONString(editAddress);


                    Log.d("APITest", "editaddress=" + jsonstyle);
                    final MediaType JSONStyle = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSONStyle, jsonstyle);
                    //创建一个请求对象
                    Request request = new Request.Builder()
                          //  .addHeader("cookie", username)
                            .url(Constant.SFSHURL + Constant.EditAddress)
                            .put(requestBody)
                            .build();

                    mMyOkhttp.getOkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("APITest", "fail:");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("APITest1", "xiugai onSuccess:" + response.body().toString());
                            Answer obj = (Answer) JSON.parseObject(response.body().string(), new TypeReference<Answer>() {
                            });
                            resCodeEdit = obj.getCode();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (resCodeEdit == Constant.SUCCESSCODE) {
                                        Toast.makeText(AddOrEditPlaceActivity.this, "修改地址提交成功", Toast.LENGTH_LONG).show();
                                        //AddOrEditPlaceActivity.this.finish();
                                        Intent intent = new Intent(AddOrEditPlaceActivity.this, PersonPlaceActivity.class);
                                        startActivity(intent);
                                        AddOrEditPlaceActivity.this.finish();
                                    } else if (resCodeEdit == Constant.NOTlOG) {
                                        Toast.makeText(AddOrEditPlaceActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(AddOrEditPlaceActivity.this, "修改地址提交失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        /*mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除地址提交
                String url = Constant.SFSHURL + Constant.deletsAddress + mAddress.getId();
                mMyOkhttp.delete()
                        .addHeader("cookie", username)
                        .url(url)
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
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
                                            Toast.makeText(AddOrEditPlaceActivity.this, "删除地址成功", Toast.LENGTH_LONG).show();
                                            //AddOrEditPlaceActivity.this.finish();
                                            Intent intent = new Intent(AddOrEditPlaceActivity.this, PersonPlaceActivity.class);
                                            startActivity(intent);
                                        } else if (resCodeDelete == Constant.NOTlOG) {
                                            Toast.makeText(AddOrEditPlaceActivity.this, "请先登录", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(AddOrEditPlaceActivity.this, "删除地址失败", Toast.LENGTH_LONG).show();
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
        });*/
    }

    /**
     * 修改按钮状态
     *
     * @param consignee
     * @param mobile
     * @param looseAddress
     */
    private void changeButtonBackground(String consignee, String mobile, String looseAddress) {
        if (TextUtils.isEmpty(consignee) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(looseAddress)) {
            unClickable();
        } else {
            clickable();
        }
    }

    /**
     * 按钮置为可点击状态
     */
    private void clickable() {
        mBtnConfirm.setClickable(true);//可点击
        mBtnConfirm.setEnabled(true);//不可用
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.round_corner_yellow_fill);
        mBtnConfirm.setBackground(drawable);//修改背景
        int color = resources.getColor(R.color.white);
        mBtnConfirm.setTextColor(color);
    }

    /**
     * 按钮置为不可点击状态
     */
    private void unClickable() {
        mBtnConfirm.setClickable(false);//不可点击
        mBtnConfirm.setEnabled(false);//不可用
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.round_corner_gray_fill);
        mBtnConfirm.setBackground(drawable);//修改背景
        int color = resources.getColor(R.color.btn_not_confirm);
        mBtnConfirm.setTextColor(color);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
           // mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onPause() {
        //mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // mMapView.onResume();
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 关闭定位图层
        super.onDestroy();
        //关闭当前Activity
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 10) {
            if (requestCode == 8) {
               /* poiItem = (PoiItem) intent.getParcelableExtra("poiItem");
                if (poiItem != null) {
                    mCurrentLat = poiItem.getLatLonPoint().getLatitude();
                    mCurrentLon = poiItem.getLatLonPoint().getLongitude();
                    Log.d("ceshi", "mCurrentLat1111==" + mCurrentLat);
                    Log.d("ceshi", "mCurrentLon111==" + mCurrentLon);
                    mEtPlace.setText(poiItem.getAdName() + poiItem.getSnippet() + poiItem.getTitle());
                }*/
            }
        }
    }


}
