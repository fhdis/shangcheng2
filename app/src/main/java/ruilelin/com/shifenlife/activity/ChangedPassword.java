package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.InfoUnRead;
import ruilelin.com.shifenlife.json.LoginWithPasswordJson;
import ruilelin.com.shifenlife.json.RegisterJson;
import ruilelin.com.shifenlife.json.VerCodeJson;
import ruilelin.com.shifenlife.json.PasswordRequest;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.utils.CountDownTimerUtils;

public class ChangedPassword extends BaseActivity {
    private boolean hadBalancePassword;
    private boolean hadPassword;
    private String type;
    private TextView tv_mobile;
    private EditText et_one, et_two, et_thress, et_four, edit_password, edit_password_ver, edit_before_password;
    private TextView tv_vercode;
    private TextView tv_last_password;
    private TextView tv_forgot_password;
    private Button btn_tijiao;
    private Button bt_same;
    private String username;
    private String usertel;
    private Button back;
    private TextView title;
    private LinearLayout layout_vercode;
    private LinearLayout layout_before_password;
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private Map<String, Boolean> hadForgotPassword = new HashMap<>();

    /**
     * 将密码输入框默认 "." 替换为 "*"
     */
    private class SubCharSequence implements CharSequence {
        private CharSequence mSource;

        public SubCharSequence(CharSequence source) {
            mSource = source;
        }

        public char charAt(int index) {
            return '*';
        }

        public int length() {
            return mSource.length();
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }

    @Override
    protected void initData() {
        type = getIntent().getStringExtra("style");
        hadBalancePassword = getIntent().getBooleanExtra("hadBalancePassword", false);
        hadPassword = getIntent().getBooleanExtra("hadPassword", false);
        tv_mobile.setText(usertel);
        //title.setText("密码修改");
       /* if ("CHANGE_PAY_PASSWORD".equals(type)) {
            edit_password.setHint("请设置支付密码");
            edit_password.setTextColor(this.getResources().getColor(R.color.hint_color));
        } else if ("CHANGE_PASSWORD".equals(type)) {
            edit_password.setHint("请设置登录密码");
            edit_password.setTextColor(this.getResources().getColor(R.color.hint_color));
        }*/

        //将密码密文改为雪花状"*"
        edit_before_password.setTransformationMethod(transformMethod());
        edit_password.setTransformationMethod(transformMethod());
        edit_password_ver.setTransformationMethod(transformMethod());

        //TODO 待优化 2018-10-25
        // 优化完成 2018-10-26
        //登录密码设置
        if ("CHANGE_PAY_PASSWORD".equals(type)) {
            if (hadBalancePassword) {
                title.setText("支付密码修改");//修改title
                modifyUserPassword();
            } else {
                title.setText("支付密码设置");//修改title
                setUserPassword();
            }
        }
        if ("CHANGE_PASSWORD".equals(type)) {
            if (hadPassword) {
                title.setText("登录密码修改");//修改title
                modifyUserPassword();
            } else {
                title.setText("登录密码设置");//修改title
                setUserPassword();
            }
        }
        //默认按钮不可点击
        unClickable();
        //是否忘记密码，默认未忘记
        hadForgotPassword.put("isForgotPassword", false);
    }

    /**
     * 密码设置
     */
    private void setUserPassword() {
        layout_vercode.setVisibility(View.VISIBLE);//显示 输入验证码layout
        layout_before_password.setVisibility(View.GONE);//隐藏 输入原密码layout
        tv_vercode.setVisibility(View.VISIBLE);//显示 "发送验证码"
        tv_last_password.setVisibility(View.GONE);//显示 "输入原密码"
        tv_forgot_password.setVisibility(View.GONE);//显示 "忘记密码?"
        btn_tijiao.setText("确认");//修改按钮文字
        btn_tijiao.setClickable(false);
    }

    /**
     * 密码修改
     */
    private void modifyUserPassword() {
        layout_vercode.setVisibility(View.GONE);//隐藏 输入验证码layout
        layout_before_password.setVisibility(View.VISIBLE);//显示 输入原密码layout
        tv_vercode.setVisibility(View.GONE);//隐藏 "发送验证码"
        tv_last_password.setVisibility(View.VISIBLE);//显示 "输入原密码"
        tv_forgot_password.setVisibility(View.VISIBLE);//显示 "忘记密码?"
        btn_tijiao.setText("确认修改");//修改按钮文字
        btn_tijiao.setClickable(false);//不可点击
    }

    @NonNull
    private TransformationMethod transformMethod() {
        return new TransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                return new SubCharSequence(source);
            }

            @Override
            public void onFocusChanged(View view, CharSequence charSequence, boolean b, int i, Rect rect) {
            }
        };
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");

        SharedPreferences sharedPreferencestel = getSharedPreferences("UserTel", Context.MODE_PRIVATE);
        usertel = sharedPreferencestel.getString("tel", "");

        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        btn_tijiao = (Button) findViewById(R.id.btn_tijiao);
        bt_same = (Button) findViewById(R.id.bt_same);
        tv_vercode = findViewById(R.id.tv_vercode);
        tv_last_password = findViewById(R.id.tv_last_password);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        et_one = (EditText) findViewById(R.id.et_one);
        et_two = (EditText) findViewById(R.id.et_two);
        et_thress = (EditText) findViewById(R.id.et_thress);
        et_four = (EditText) findViewById(R.id.et_four);
        edit_before_password = findViewById(R.id.et_before_password);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password_ver = (EditText) findViewById(R.id.edit_password_ver);
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        layout_vercode = findViewById(R.id.layout_vercode);
        layout_before_password = findViewById(R.id.layout_before_password);
    }

    @Override
    protected void initListener() {
        //绑定获取焦点事件
        bindOnFocusChangeListener();
        //绑定TextChangedListener事件
        bindTextChangedListener();
        //绑定OnClickListener事件
        bindOnClickListener();
        //绑定OnKeyListener监听
        bindOnKeyListener();
    }

    private void bindOnKeyListener() {
        //给每一个editText设置View.OnKeyListener监听，用于检测删除键。
        et_one.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backFocus();
                }
                return false;
            }
        });
        et_two.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backFocus();
                }
                return false;
            }
        });
        et_thress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backFocus();
                }
                return false;
            }
        });
        et_four.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    backFocus();
                }
                return false;
            }
        });
    }

    private void bindOnClickListener() {
        //为提交按钮绑定setOnClickListener事件
        btn_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals("FIND_PASSWORD")) {
                    String code = et_one.getText().toString() + et_two.getText().toString() + et_thress.getText().toString() + et_four.getText().toString();
                    String balancePassword = edit_password.getText().toString();
                    String jiaoyan = edit_password_ver.getText().toString();
                    if (!balancePassword.equals(jiaoyan)) {
                        bt_same.setVisibility(View.VISIBLE);
                        return;
                    } else if (balancePassword.equals(jiaoyan)) {
                        bt_same.setVisibility(View.INVISIBLE);
                        //提交请求
                        RegisterJson registerJson = new RegisterJson(tv_mobile.getText().toString(), code, balancePassword);
                        String jsonstyle = JSON.toJSONString(registerJson);
                        Log.d("TAG30", "balancePassword==" + balancePassword + "===" + code);
                        mMyOkhttp.post()
                                .addHeader("cookie", username)
                                .url(Constant.SFSHURL + Constant.FindPassword)
                                .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                .tag(this)
                                .enqueue(new JsonResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, JSONObject response) {
                                        Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                        });
                                        resCode = obj.getCode();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (resCode == Constant.SUCCESSCODE) {
                                                    Toast.makeText(ChangedPassword.this, "密码找回成功", Toast.LENGTH_LONG).show();
                                                    ChangedPassword.this.finish();
                                                } else if (resCode == Constant.FAIL) {
                                                    Toast.makeText(ChangedPassword.this, "验证码有误", Toast.LENGTH_LONG).show();
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
                } else if (type.equals("CHANGE_PAY_PASSWORD")) {
                    //String code = et_one.getText().toString() + et_two.getText().toString() + et_thress.getText().toString() + et_four.getText().toString();
                    String code = getResult();
                    String jiaoyan = edit_password_ver.getText().toString();
                    if (!password.equals(jiaoyan)) {
                        bt_same.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        bt_same.setVisibility(View.INVISIBLE);
                        //提交请求
                        Boolean isForgotPassword = hadForgotPassword.get("isForgotPassword");
                        if (isForgotPassword || !hadBalancePassword) {
                            //-----------------------------------------------忘记密码---------------------------
                            PasswordRequest passwordRequest = new PasswordRequest(password, code);
                            String jsonstyle = JSON.toJSONString(passwordRequest);
                            Log.d("TAG30", "balancePassword==" + password + "===" + code);
                            mMyOkhttp.post()
                                    .addHeader("cookie", username)
                                    .url(Constant.SFSHURL + Constant.ResetBalancePassword)
                                    .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                    .tag(this)
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                            });
                                            resCode = obj.getCode();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (resCode == Constant.SUCCESSCODE) {
                                                        Toast.makeText(ChangedPassword.this, "成功设置支付密码", Toast.LENGTH_LONG).show();
                                                        ChangedPassword.this.finish();
                                                    } else if (resCode == Constant.FAIL) {
                                                        Toast.makeText(ChangedPassword.this, "验证码有误", Toast.LENGTH_LONG).show();
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
                            //-----------------------------------------------没有忘记密码---------------------------
                            //PasswordRequest passwordRequest = new PasswordRequest(oldPassword, password);
                            PasswordRequest passwordRequest = new PasswordRequest();
                            passwordRequest.setOldPassword(oldPassword);
                            passwordRequest.setPassword(password);
                            String jsonstyle = JSON.toJSONString(passwordRequest);
                            Log.d("TAG30", "balancePassword==" + password + "===" + code);

                            mMyOkhttp.post()
                                    .addHeader("cookie", username)
                                    .url(Constant.SFSHURL + Constant.ModifyBalancePassword)
                                    .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                    .tag(this)
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            Data<Boolean> obj = (Data<Boolean>) JSON.parseObject(response.toString(), new TypeReference<Data<Boolean>>() {
                                            });
                                            resCode = obj.getCode();
                                            Boolean result = obj.getData();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (resCode == Constant.SUCCESSCODE) {
                                                        if (result) {
                                                            Toast.makeText(ChangedPassword.this, "支付密码修改成功", Toast.LENGTH_LONG).show();
                                                            ChangedPassword.this.finish();
                                                        } else {
                                                            Toast.makeText(ChangedPassword.this, "原密码输入有误", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else if (resCode == Constant.FAIL) {
                                                        Toast.makeText(ChangedPassword.this, "支付密码修改失败", Toast.LENGTH_LONG).show();
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
                    }
                } else if (type.equals("CHANGE_PASSWORD")) {
                    //Toast.makeText(ChangedPassword.this, password + "--" + confirmPassword, Toast.LENGTH_LONG).show();
                    //String code = et_one.getText().toString() + et_two.getText().toString() + et_thress.getText().toString() + et_four.getText().toString();
                    //oldPassword = edit_before_password.getText().toString();
                    showToast("oldPassword:" + oldPassword + ",password:" + password);
                    String code = getResult();
                    if (!password.equals(confirmPassword)) {
                        bt_same.setVisibility(View.VISIBLE);
                    } else {
                        bt_same.setVisibility(View.INVISIBLE);
                        //提交请求
                        Boolean isForgotPassword = hadForgotPassword.get("isForgotPassword");
                        if (isForgotPassword || !hadPassword) {
                            //-----------------------------------------------忘记密码---------------------------
                            PasswordRequest passwordRequest = new PasswordRequest(password, code);
                            String jsonstyle = JSON.toJSONString(passwordRequest);
                            mMyOkhttp.post()
                                    .addHeader("cookie", username)
                                    .url(Constant.SFSHURL + Constant.ResetPassword)
                                    .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                    .tag(this)
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                            });
                                            resCode = obj.getCode();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (resCode == Constant.SUCCESSCODE) {
                                                        Toast.makeText(ChangedPassword.this, "登录密码设置成功", Toast.LENGTH_LONG).show();
                                                        ChangedPassword.this.finish();
                                                    } else if (resCode == Constant.FAIL) {
                                                        Toast.makeText(ChangedPassword.this, "验证码有误", Toast.LENGTH_LONG).show();
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
                            //-----------------------------------------------没有忘记密码---------------------------
                            //PasswordRequest passwordRequest = new PasswordRequest(oldPassword, password);
                            PasswordRequest passwordRequest = new PasswordRequest();
                            passwordRequest.setOldPassword(oldPassword);
                            passwordRequest.setPassword(password);
                            String jsonstyle = JSON.toJSONString(passwordRequest);
                            // Toast.makeText(ChangedPassword.this, "-" + jsonstyle, Toast.LENGTH_LONG).show();
                            Log.d("TAG30", "password==" + password + "===" + null);
                            mMyOkhttp.post()
                                    .addHeader("cookie", username)
                                    .url(Constant.SFSHURL + Constant.ModifyPassword)
                                    .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                    .tag(this)
                                    .enqueue(new JsonResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, JSONObject response) {
                                            Data<Boolean> obj = (Data<Boolean>) JSON.parseObject(response.toString(), new TypeReference<Data<Boolean>>() {
                                            });
                                            resCode = obj.getCode();
                                            Boolean result = obj.getData();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (resCode == Constant.SUCCESSCODE) {
                                                        if (result) {
                                                            Toast.makeText(ChangedPassword.this, "登录密码修改成功", Toast.LENGTH_LONG).show();
                                                            ChangedPassword.this.finish();
                                                        } else {
                                                            Toast.makeText(ChangedPassword.this, "原密码输入有误", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(ChangedPassword.this, "登录密码修改失败", Toast.LENGTH_LONG).show();
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
                    }
                }
            }
        });
        //为忘记密码绑定click事件
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //清空密码输入框
                edit_password.setText("");
                edit_password_ver.setText("");

                //隐藏 “密码不一致提示”
                bt_same.setVisibility(View.INVISIBLE);

                //title.setText("密码设置");//修改title
                if ("CHANGE_PAY_PASSWORD".equals(type)) {
                    title.setText("支付密码设置");//修改title
                    setUserPassword();
                } else if ("CHANGE_PASSWORD".equals(type)) {
                    title.setText("登录密码设置");//修改title
                    setUserPassword();
                }


                layout_vercode.setVisibility(View.VISIBLE);//显示 输入验证码layout
                layout_before_password.setVisibility(View.GONE);//隐藏 输入原密码layout
                tv_vercode.setVisibility(View.VISIBLE);//显示 "发送验证码"
                tv_last_password.setVisibility(View.GONE);//显示 "输入原密码"
                tv_forgot_password.setVisibility(View.GONE);//显示 "忘记密码?"
                btn_tijiao.setText("确认");//修改按钮文字
                btn_tijiao.setClickable(false);

                //忘记密码
                hadForgotPassword.put("isForgotPassword", true);

                //获取焦点
                et_one.requestFocus();
            }
        });
        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //获取验证码
        tv_vercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //倒计时
                CountDownTimerUtils mCountTimerUtils = new CountDownTimerUtils(ChangedPassword.this, tv_vercode, 1000 * 60, 1000);
                mCountTimerUtils.start();
                //获取验证码
                if (type.equals("FIND_PASSWORD")) {
                    VerCodeJson verCodeJson = new VerCodeJson("FIND_PASSWORD", tv_mobile.getText().toString());
                    String jsonstyle = JSON.toJSONString(verCodeJson);
                    mMyOkhttp.post()
                            .addHeader("cookie", username)
                            .url(Constant.SFSHURL + Constant.GetVerCode)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                    });
                                    int resCode = obj.getCode();
                                    if (resCode == Constant.SUCCESSCODE) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ChangedPassword.this, "成功获取验证码", Toast.LENGTH_LONG).show();
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
                } else if (type.equals("CHANGE_PAY_PASSWORD")) {
                    Log.d("type", "type==" + type);
                    Log.d("type", "mobile==" + tv_mobile.getText().toString());
                    VerCodeJson verCodeJson = new VerCodeJson("CHANGE_PAY_PASSWORD", tv_mobile.getText().toString());
                    String jsonstyle = JSON.toJSONString(verCodeJson);
                    mMyOkhttp.post()
                            .addHeader("cookie", username)
                            .url(Constant.SFSHURL + Constant.GetVerCode)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                    });
                                    int resCode = obj.getCode();
                                    if (resCode == Constant.SUCCESSCODE) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ChangedPassword.this, "成功获取验证码", Toast.LENGTH_LONG).show();
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
                } else if ("CHANGE_PASSWORD".equals(type)) {
                    Log.d("type", "type==" + type);
                    Log.d("type", "mobile==" + tv_mobile.getText().toString());
                    VerCodeJson verCodeJson = new VerCodeJson("CHANGE_PASSWORD", tv_mobile.getText().toString());
                    String jsonstyle = JSON.toJSONString(verCodeJson);
                    mMyOkhttp.post()
                            .addHeader("cookie", username)
                            .url(Constant.SFSHURL + Constant.GetVerCode)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                    Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                    });
                                    int resCode = obj.getCode();
                                    if (resCode == Constant.SUCCESSCODE) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ChangedPassword.this, "成功获取验证码", Toast.LENGTH_LONG).show();
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
        });
    }

    private void bindTextChangedListener() {
        //为输入原密码框绑定addTextChangedListener事件
        edit_before_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                oldPassword = edit_before_password.getText().toString();
                if (TextUtils.isEmpty(oldPassword)) {
                    tv_last_password.setText("输入有误");
                    unClickable();
                } else {
                    tv_last_password.setText("输入原密码");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = edit_password.getText().toString();
                oldPassword = edit_before_password.getText().toString();
                confirmPassword = edit_password_ver.getText().toString();
                changeButtonBackground(oldPassword, password, confirmPassword);
            }
        });
        //为输入密码框绑定addTextChangedListener事件
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //password = edit_password.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(ChangedPassword.this, password + "-" + confirmPassword, Toast.LENGTH_LONG).show();
                password = edit_password.getText().toString();
                oldPassword = edit_before_password.getText().toString();
                confirmPassword = edit_password_ver.getText().toString();
                Boolean isForgotPassword = hadForgotPassword.get("isForgotPassword");
                //showToast("忘记密码：" + isForgotPassword);
                //showToast("type:"+type+",hadPassword:"+hadPassword);
                //是否忘记密码
                if (isForgotPassword) {
                    changeButtonBackground(getResult(), password, confirmPassword);
                } else {
                    if ("CHANGE_PAY_PASSWORD".equals(type)) {
                        if (hadBalancePassword) {//有余额支付密码
                            //修改
                            changeButtonBackground(oldPassword, password, confirmPassword);
                        } else {//没有
                            //设置
                            changeButtonBackground(getResult(), password, confirmPassword);
                        }
                    } else if ("CHANGE_PASSWORD".equals(type)) {
                        if (hadPassword) {//有登录密码
                            //修改
                            changeButtonBackground(oldPassword, password, confirmPassword);
                        } else {//没有登录密码
                            //设置
                            changeButtonBackground(getResult(), password, confirmPassword);
                        }
                    }
                }
            }
        });
        //为再次输入密码框绑定addTextChangedListener事件
        edit_password_ver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //confirmPassword = edit_password_ver.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(ChangedPassword.this, password + "-" + confirmPassword, Toast.LENGTH_LONG).show();
                password = edit_password.getText().toString();
                oldPassword = edit_before_password.getText().toString();
                confirmPassword = edit_password_ver.getText().toString();
                Boolean isForgotPassword = hadForgotPassword.get("isForgotPassword");
                //showToast("忘记密码：" + isForgotPassword);
                //showToast("type:"+type+",hadPassword:"+hadPassword);
                //showToast(""+isForgotPassword+hadBalancePassword+hadPassword);

                //是否忘记密码
                if (isForgotPassword) {
                    changeButtonBackground(getResult(), password, confirmPassword);
                } else {
                    //changeButtonBackground(oldPassword, password, confirmPassword);
                    if ("CHANGE_PAY_PASSWORD".equals(type)) {
                        if (hadBalancePassword) {//有余额支付密码
                            //修改
                            changeButtonBackground(oldPassword, password, confirmPassword);
                        } else {//没有
                            //设置
                            changeButtonBackground(getResult(), password, confirmPassword);
                        }
                    } else if ("CHANGE_PASSWORD".equals(type)) {
                        if (hadPassword) {//有登录密码
                            //修改
                            changeButtonBackground(oldPassword, password, confirmPassword);
                        } else {//没有登录密码
                            //设置
                            changeButtonBackground(getResult(), password, confirmPassword);
                        }
                    }
                }
            }
        });
        //为验证密码输入框设置事件
        et_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    focus();
                }
            }
        });
        et_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    focus();
                }
            }
        });
        et_thress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    focus();
                }
            }
        });
        et_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    focus();
                }

                password = edit_password.getText().toString();
                oldPassword = edit_before_password.getText().toString();
                confirmPassword = edit_password_ver.getText().toString();
                //showToast("忘记密码：" + isForgotPassword);
                //showToast("验证码：" + getResult());
                changeButtonBackground(getResult(), password, confirmPassword);
            }
        });
    }

    private void bindOnFocusChangeListener() {
        //为输入原密码框绑定离焦事件
        edit_before_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {//聚焦事件

                } else {//离焦事件
                    String oldPasswordUrl = Constant.SFSHURL + Constant.OldPassword;
                    oldPassword = edit_before_password.getText().toString();
                    LoginWithPasswordJson loginWithPasswordJson = new LoginWithPasswordJson();
                    loginWithPasswordJson.setPassword(oldPassword);
                    String jsonstyle = JSON.toJSONString(loginWithPasswordJson);
                    mMyOkhttp.post()
                            .addHeader("cookie", username)
                            .url(oldPasswordUrl)
                            .jsonParams(jsonstyle)
                            .tag(this)
                            .enqueue(new RawResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, String response) {
                                    Data<Boolean> obj = (Data<Boolean>) JSON.parseObject(response.toString(), new TypeReference<Data<Boolean>>() {
                                    });
                                    resCode = obj.getCode();
                                    Boolean legalPassword = obj.getData();
                                    int resCode = JSON.parseObject(response.toString(), InfoUnRead.class).getCode();
                                    if (resCode == Constant.SUCCESSCODE) {
                                        if (!legalPassword) {
                                            tv_last_password.setText("输入有误");
                                            unClickable();
                                        } else {
                                            tv_last_password.setText("输入原密码");
                                            oldPassword = edit_before_password.getText().toString();
                                            password = edit_password.getText().toString();
                                            confirmPassword = edit_password_ver.getText().toString();
                                            changeButtonBackground(oldPassword, password, confirmPassword);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, String error_msg) {
                                    Log.d("APITest", "doGet onFailure:" + error_msg);
                                }
                            });
                }
            }
        });
        //让editText依次获取焦点，防止出现第一个editText还没输入就能输入第二个editText的情况。
        et_one.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focus();
                }
            }
        });
        et_two.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focus();
                }
            }
        });
        et_thress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focus();
                }
            }
        });
        et_four.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focus();
                }
            }
        });
    }

    private void backFocus() {
        EditText editText;
        //循环检测有字符的`editText`，把其置空，并获取焦点。
        for (int i = getChildCount() - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() >= 1) {
                editText.setText("");
                editText.setCursorVisible(true);
                editText.requestFocus();
                return;
            }
        }
    }

    private int getChildCount() {
        int childCount = layout_vercode.getChildCount();
        return childCount - 1;
    }

    private EditText getChildAt(int i) {
        return (EditText) layout_vercode.getChildAt(i);
    }

    private String getResult() {
        StringBuffer sb = new StringBuffer();
        String oneCode = et_one.getText().toString();
        String twoCode = et_two.getText().toString();
        String threeCode = et_thress.getText().toString();
        String fourCode = et_four.getText().toString();

        sb.append(oneCode).append(twoCode).append(threeCode).append(fourCode);
        return sb.toString();
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        //利用for循环找出还最前面那个还没被输入字符的EditText，并把焦点移交给它。
        for (int i = 0; i < count; i++) {
            editText = getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.setCursorVisible(true);
                editText.requestFocus();
                return;
            } else {
                editText.setCursorVisible(false);
            }
        }
        //如果最后一个输入框有字符，则返回结果
        EditText lastEditText = (EditText) getChildAt(getChildCount() - 1);
        if (lastEditText.getText().length() > 0) {
            getResult();
        }
    }


    private void changeButtonBackground(String message, String password, String confirmPassword) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            bt_same.setVisibility(View.INVISIBLE);
            unClickable();
        } else {
            if (!password.equals(confirmPassword)) {
                bt_same.setText("密码不一致");
                bt_same.setVisibility(View.VISIBLE);
                unClickable();
            } else {
                if (confirmPassword.length() > 5 && confirmPassword.length() <= 16) {
                    Boolean isForgotPassword = hadForgotPassword.get("isForgotPassword");
                    //showToast(isForgotPassword + "-" + hadPassword + "-" + hadBalancePassword);
                    if (isForgotPassword) {//忘记密码
                        //showToast(" isForgotPassword " + isForgotPassword);
                        isCorrectVerCode(message);
                    } else {//没有忘记密码
                        //canNotEqual(message, confirmPassword);
                        if ("CHANGE_PAY_PASSWORD".equals(type)) {
                            if (hadBalancePassword) {//有余额支付密码
                                //修改
                                canNotEqual(message, confirmPassword);
                            } else {//没有
                                //设置
                                isCorrectVerCode(message);
                            }
                        } else if ("CHANGE_PASSWORD".equals(type)) {
                            if (hadPassword) {//有登录密码
                                //修改
                                canNotEqual(message, confirmPassword);
                            } else {//没有登录密码
                                //设置
                                isCorrectVerCode(message);
                            }
                        }
                    }
                    bt_same.setVisibility(View.INVISIBLE);//隐藏
                } else {
                    bt_same.setText("6-16位");
                    bt_same.setVisibility(View.VISIBLE);//显示
                    unClickable();
                }
            }
        }
    }

    private void canNotEqual(String message, String confirmPassword) {
        if (confirmPassword.equals(message)) {//原密码跟新密码相等
            showToast("原密码跟新密码不能相同");
            unClickable();
        } else {//原密码跟新密码不等
            //原密码不能为空
            if (TextUtils.isEmpty(message)) {
                tv_last_password.setText("输入有误");
                unClickable();
            } else {
                //原密码长度不够6位
                if (message.length() < 6) {
                    tv_last_password.setText("6-16位");
                    unClickable();
                } else {
                    tv_last_password.setText("输入原密码");
                    clickable();
                }
            }
        }
    }

    private void isCorrectVerCode(String message) {
        if (message.length() < 4) {
            unClickable();
        } else {
            clickable();
        }
    }

    /**
     * 按钮置为可点击状态
     */
    private void clickable() {
        btn_tijiao.setClickable(true);//可点击
        btn_tijiao.setEnabled(true);//不可用
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.round_corner_yellow_fill);
        btn_tijiao.setBackground(drawable);//修改背景
        int color = resources.getColor(R.color.white);
        btn_tijiao.setTextColor(color);
    }

    /**
     * 按钮置为不可点击状态
     */
    private void unClickable() {
        btn_tijiao.setClickable(false);//不可点击
        btn_tijiao.setEnabled(false);//不可用
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.round_corner_gray_fill);
        btn_tijiao.setBackground(drawable);//修改背景
        int color = resources.getColor(R.color.btn_not_confirm);
        btn_tijiao.setTextColor(color);
    }

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_change_password;
    }
}
