package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.LoginWithPasswordJson;
import ruilelin.com.shifenlife.utils.Constant;

public class LoginActivity extends BaseActivity {
    private EditText user;
    private EditText et_password;
    private CheckBox chbAgree;
    private Button login;
    private TextView tv_yanzhengma;
    private TextView register;
    private TextView forget;
    private TextView tv_weixin;
    private IWXAPI api;
    private MyApplication app = MyApplication.getInstance();
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_login_with_account;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        user = (EditText)findViewById(R.id.user);
        et_password = (EditText)findViewById(R.id.password);
        chbAgree = (CheckBox)findViewById(R.id.chbAgree);
        login = (Button)findViewById(R.id.login);
        tv_yanzhengma = (TextView) findViewById(R.id.tv_yanzhengma);
        register = (TextView)findViewById(R.id.register);
        forget = (TextView)findViewById(R.id.forget);
        tv_weixin = (TextView)findViewById(R.id.tv_weixin);
        api = WXAPIFactory.createWXAPI(this, "wxb090b32121969d14");
        //SharedPreferences sharedPreferences = getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE);
       // String username = sharedPreferences.getString("cookie", "");
        SharedPrefsCookiePersistor cookiePersistor = new SharedPrefsCookiePersistor(getApplicationContext());
       // if(!username.equals("")){
        if(cookiePersistor.loadAll().size()>0){
            Toast.makeText(LoginActivity.this, "获取cookie登录", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    protected void initListener() {
        tv_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send oauth request
                IWXAPI mApi = WXAPIFactory.createWXAPI(LoginActivity.this, Constant.APP_ID, true);
                mApi.registerApp(Constant.APP_ID);

                if (mApi != null && mApi.isWXAppInstalled()) {
                    app.setWXLogin(true);
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                    req.state = "wechat_sdk_微信登录";
                    api.sendReq(req);
                } else{
                    Toast.makeText(LoginActivity.this, "用户未安装微信", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //验证码登录界面
        tv_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginWithTelCode.class);
            }
        });


        //注册账号
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RegisterActivity.class);
            }
        });


        //忘记密码
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ChangedPassword.class);
                intent.putExtra("style", "FIND_PASSWORD");
                startActivity(intent);
            }
        });

        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chbAgree.isChecked()) {
                    String mobile = user.getText().toString();
                    String password = et_password.getText().toString();
                    if (mobile.equals("")) {
                        Toast.makeText(LoginActivity.this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    if (password.equals("")) {
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    LoginWithPasswordJson loginWithPasswordJson = new LoginWithPasswordJson(mobile, password);
                    String jsonstyle = JSON.toJSONString(loginWithPasswordJson);
                    mMyOkhttp.post()
                            .url(Constant.SFSHURL + Constant.LoginWithPassword)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>(){});
                                    int  resCodes = obj.getCode();
                                    if(resCodes==Constant.SUCCESSCODE){
                                        SharedPreferences sharedPreferences = getSharedPreferences("UserTel", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("tel",mobile);
                                        editor.commit();
                                        Toast.makeText(LoginActivity.this, "成功登陆", Toast.LENGTH_SHORT).show();
                                        startActivity(MainActivity.class);
                                        finish();
                                    }else if(resCodes==Constant.FAIL){
                                        Toast.makeText(LoginActivity.this, "手机号或密码错误,请重试", Toast.LENGTH_SHORT).show();
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
                }else{
                    Toast.makeText(LoginActivity.this,"请勾选并同意相关协议",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
