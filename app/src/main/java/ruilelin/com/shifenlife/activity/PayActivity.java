package ruilelin.com.shifenlife.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.cart.customview.RoundCornerDialog;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.Order;
import ruilelin.com.shifenlife.json.PayWayJson;
import ruilelin.com.shifenlife.json.PayWayReply;
import ruilelin.com.shifenlife.model.UserInfo;
import ruilelin.com.shifenlife.pay.ali.AuthResult;
import ruilelin.com.shifenlife.pay.ali.PayResult;
import ruilelin.com.shifenlife.pay.ali.PayWay_ALI;
import ruilelin.com.shifenlife.pay.wx.PayWayWXReply;
import ruilelin.com.shifenlife.utils.Constant;

public class PayActivity extends BaseActivity {

    private Button bt_pay;
    private Button bt_cancel;
    private Order payorder;
    private TextView v_pay_price;
    private TextView tv_yue;
    private String username;

    private CheckBox iv_yue_check;
    private CheckBox iv_wechat_check;
    private  CheckBox iv_ali_check;
    private  String payWay = "BALANCE";
    PayWayJson payWayJson;
    String jsonstyle;
    private PayWay_ALI payWay_ali;
    private Map<String, String>  payWay_wx;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private IWXAPI api;
    private UserInfo userInfo;
    private String yue;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PayActivity.this, SubmitOrderSuccessActivity.class);
                        startActivity(intent);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_zhifu_way;
    }

    @Override
    protected void initData() {
        payorder = (Order)getIntent().getSerializableExtra("oderinfo");
        getInfodata();
        if(payorder!=null){
            v_pay_price.setText("您需支付\n"+payorder.getPrice()+"元");
        }

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.registerApp(Constant.APP_ID);
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences =getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        iv_yue_check = (CheckBox)findViewById(R.id.iv_yue_check);
        iv_wechat_check = (CheckBox)findViewById(R.id.iv_wechat_check);
        iv_ali_check = (CheckBox)findViewById(R.id.iv_ali_check);
        bt_pay = (Button)findViewById(R.id.bt_pay);
        bt_cancel = (Button)findViewById(R.id.bt_cancel);
        v_pay_price = (TextView)findViewById(R.id.tv_pay_price);
        tv_yue= (TextView)findViewById(R.id.tv_yue);
    }

    @Override
    protected void initListener() {
        iv_wechat_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    iv_yue_check.setChecked(!b);
                    iv_ali_check.setChecked(!b);
                }
            }
        });

        iv_ali_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    iv_yue_check.setChecked(!b);
                    iv_wechat_check.setChecked(!b);
                }
            }
        });

        bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(iv_yue_check.isChecked()){
                    payWay = "BALANCE";
                    int result = yue.compareTo(payorder.getPrice());
                   if(result < 0){
                       showDeleteDialog();
                       return;
                   }
                }
                if(iv_ali_check.isChecked()){
                    payWay = "ALI";
                }
                if( iv_wechat_check.isChecked()){
                    payWay = "WX";
                }
                if(payorder!=null){
                    payWayJson = new PayWayJson(Integer.parseInt(payorder.getId()),payWay);
                    Log.d("test", "datas.getId()==" + payWayJson.getId());
                    jsonstyle = JSON.toJSONString(payWayJson);
                    mMyOkhttp.post()
                            .addHeader("cookie",username)
                            .url(Constant.SFSHURL+Constant.OrderPay)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    if("ALI".equals(payWay)) {
                                        resCode = JSON.parseObject(response.toString(), PayWayReply.class).getCode();
                                        if(resCode ==Constant.FAIL){
                                            Toast.makeText(PayActivity.this, "出错了 o(╥﹏╥)o", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        payWay_ali = JSON.parseObject(response.toString(), PayWayReply.class).getData();
                                        if (resCode == Constant.SUCCESSCODE) {
                                            Runnable payRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    Map<String, String> result = alipay.payV2(payWay_ali.getAli(), true);
                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = result;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        }
                                    }else if("WX".equals(payWay)) {
                                        PayWayWXReply res = JSON.parseObject(response.toString(), PayWayWXReply.class);
                                        resCode = res.getCode();
                                        if(resCode ==Constant.FAIL){
                                            Toast.makeText(PayActivity.this, "出错了 o(╥﹏╥)o", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        payWay_wx = res.getData().get("wx");
                                        try {
                                            if (payWay_wx != null ) {
                                                PayReq req = new PayReq();
                                                req.appId			= "wxb090b32121969d14";
                                                req.partnerId		= "1512642551";
                                                req.prepayId = payWay_wx.get("prepayid");
                                                req.packageValue = payWay_wx.get("package");
                                                req.nonceStr = payWay_wx.get("noncestr");
                                                req.timeStamp = payWay_wx.get("timestamp");
                                                req.sign = payWay_wx.get("sign");
                                                req.extData = payWay_wx.get("sn");
                                                Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                                api.sendReq(req);
                                            } else {
                                                Log.d("PAY_GET", "服务器请求错误");
                                                Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            Log.e("PAY_GET", "异常：" + e.getMessage());
                                            Toast.makeText(PayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }else if("BALANCE".equals(payWay)){
                                        Intent intent = new Intent(PayActivity.this, SubmitOrderSuccessActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(PayActivity.this,response.toString(),Toast.LENGTH_LONG).show();
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

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消订单
                mMyOkhttp.get()
                        .addHeader("cookie",username)
                        .url(Constant.SFSHURL+Constant.CancelOrder+payorder.getId())
                        // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>(){});
                                int  resCodes = obj.getCode();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(resCodes== Constant.SUCCESSCODE) {
                                                Toast.makeText(PayActivity.this,"成功取消订单",Toast.LENGTH_SHORT).show();
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
        });
    }

    private void showDeleteDialog() {
        View view = View.inflate(PayActivity.this, R.layout.dialog_two_btn, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(PayActivity.this, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("抱歉，余额不足您是否需要充值？");

        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, Recharge.class);
                startActivity(intent);
                finish();
            }
        });
        //取消
        tv_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };


    //获取个人信息
    public void getInfodata() {
        mMyOkhttp.get()
                .url(Constant.SFSHURL + Constant.UserInfo)
                .addHeader("cookie",username)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d("APITest", "doGet onSuccess:" + response);
                        Data<UserInfo> obj = (Data<UserInfo>) JSON.parseObject(response.toString(), new TypeReference<Data<UserInfo>>() {
                        });
                        resCode = obj.getCode();
                        userInfo = obj.getData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    if (userInfo != null) {
                                        yue = userInfo.getBalance();
                                        tv_yue.setText("余额:("+userInfo.getBalance() + ")");
                                    }
                                }
                            }
                        });
                    }


                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });
    }
}
