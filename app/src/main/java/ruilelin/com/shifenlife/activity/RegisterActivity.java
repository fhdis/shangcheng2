package ruilelin.com.shifenlife.activity;

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
import ruilelin.com.shifenlife.json.RegisterJson;
import ruilelin.com.shifenlife.json.VerCodeJson;
import ruilelin.com.shifenlife.utils.Constant;

public class RegisterActivity extends BaseActivity {

    private EditText edit_phone_number;
    private EditText edit_code_number;
    private Button btn_get_code;
    private EditText edit_password;
    private EditText edit_confirm_password;
    private CheckBox text_agreement;
    private Button btn_register;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_regitser;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        edit_phone_number = (EditText)findViewById(R.id.edit_phone_number);
        edit_code_number = (EditText)findViewById(R.id.edit_code_number);
        btn_get_code = (Button)findViewById(R.id.btn_get_code);
        edit_password = (EditText)findViewById(R.id.edit_password);
        edit_confirm_password = (EditText)findViewById(R.id.edit_confirm_password);
        text_agreement = (CheckBox) findViewById(R.id.text_agreement);
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initListener() {
        //获取验证码
        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type="REGISTRY";
                String mobile = edit_phone_number.getText().toString();
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
                                if(resCode == Constant.SUCCESSCODE){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this,"成功获取验证码",Toast.LENGTH_LONG).show();
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

        //注册
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text_agreement.isChecked()) {
                    if(!edit_password.getText().toString().equals(edit_confirm_password.getText().toString())){
                        Toast.makeText(RegisterActivity.this, "两次输入密码不一致哦", Toast.LENGTH_LONG).show();
                        return ;
                    }
                    String mobile = edit_phone_number.getText().toString();
                    String code = edit_code_number.getText().toString();
                    String password = edit_password.getText().toString();
                    RegisterJson registerJson = new RegisterJson(mobile, code,password);
                    String jsonstyle = JSON.toJSONString(registerJson);

                    mMyOkhttp.post()
                            .url(Constant.SFSHURL + Constant.REGISTER)
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
                                                Toast.makeText(RegisterActivity.this, "恭喜您，注册成功", Toast.LENGTH_LONG).show();
                                                startActivity(LoginActivity.class);
                                                finish();
                                            } else if (resCode == Constant.FAIL) {
                                                Toast.makeText(RegisterActivity.this, "手机号或验证码错误", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(RegisterActivity.this,"请勾选并同意相关协议",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
