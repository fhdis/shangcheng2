package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.LoginWithTelAndVer;
import ruilelin.com.shifenlife.json.VerCodeJson;
import ruilelin.com.shifenlife.utils.Constant;

public class LoginWithTelCode extends BaseActivity {
    private EditText user;
    private EditText et_password;
    private Button btn_get_code;
    private CheckBox chbAgree;
    private Button login;
    private Button icon_login_back;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_login_with_tel_code;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        user = (EditText)findViewById(R.id.user);
        et_password = (EditText)findViewById(R.id.password);
        btn_get_code = (Button)findViewById(R.id.btn_get_code);
        chbAgree = (CheckBox)findViewById(R.id.chbAgree);
        login = (Button)findViewById(R.id.login);
        icon_login_back = (Button)findViewById(R.id.icon_login_back);
    }

    @Override
    protected void initListener() {
        icon_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取验证码
        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type="LOGIN";
                String mobile = user.getText().toString();
                VerCodeJson verCodeJson = new VerCodeJson(type,mobile);
                String jsonstyle = JSON.toJSONString(verCodeJson);
                mMyOkhttp.post()
                        .url(Constant.SFSHURL+Constant.GetVerCode)
                        .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>(){});
                                int  resCode = obj.getCode();
                                if(resCode ==Constant.SUCCESSCODE){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginWithTelCode.this,"成功获取验证码",Toast.LENGTH_LONG).show();
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
        });

        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chbAgree.isChecked()) {
                    String mobile = user.getText().toString();
                    String code = et_password.getText().toString();
                    LoginWithTelAndVer loginWithTelAndVer = new LoginWithTelAndVer(mobile, code);
                    String jsonstyle = JSON.toJSONString(loginWithTelAndVer);

                    mMyOkhttp.post()
                            .url(Constant.SFSHURL + Constant.LoginWithSecurityCode)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>(){});
                                    resCode = obj.getCode();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (resCode == Constant.SUCCESSCODE) {
                                                SharedPreferences sharedPreferences = getSharedPreferences("UserTel", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("tel",mobile);
                                                editor.commit();
                                                Toast.makeText(LoginWithTelCode.this, "登录成功", Toast.LENGTH_LONG).show();
                                                startActivity(MainActivity.class);
                                                finish();
                                            } else if (resCode == Constant.FAIL) {
                                                Toast.makeText(LoginWithTelCode.this, "手机号或验证码错误", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(LoginWithTelCode.this,"请勾选并同意相关协议",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
