package ruilelin.com.shifenlife.person.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseFragment3;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.OnlineRechargeJson;
import ruilelin.com.shifenlife.model.UserInfo;
import ruilelin.com.shifenlife.pay.ali.AuthResult;
import ruilelin.com.shifenlife.pay.ali.PayResult;
import ruilelin.com.shifenlife.pay.ali.PayWay_ALI;
import ruilelin.com.shifenlife.pay.wx.PayWayWXReply;
import ruilelin.com.shifenlife.person.adapter.DemoAdapter;
import ruilelin.com.shifenlife.person.adapter.ItemModel;
import ruilelin.com.shifenlife.utils.Constant;


public class OnlineRechargeFragment extends BaseFragment3 {
    private RecyclerView recyclerView;
    private DemoAdapter adapter;
    private RelativeLayout rlAliPay;
    private RelativeLayout rlWechatPay;
    private ImageView ivAliCheck;
    private ImageView ivWechatCheck;
    private int resCode = -1;
    private UserInfo userInfo;
    private TextView tv_balance;

    private Map<String, String> payWay_wx;
    private IWXAPI api;
    private Button btn_confirm_pay;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private int moneynum;
    private BigDecimal[] arr2 = {
            new BigDecimal(100).setScale(2), new BigDecimal(200).setScale(2),
            new BigDecimal(300).setScale(2), new BigDecimal(500).setScale(2),
            new BigDecimal(800).setScale(2), new BigDecimal(1000).setScale(2)};
    private String zhifuStyle;
    private PayWay_ALI payWay_ali;


    private String username;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    Log.d("test", "test7");
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(mContext,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(mContext,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected int setLayoutResourceId() {
        return R.layout.online_recharge;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected View initView() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        View view = View.inflate(mContext, R.layout.online_recharge, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        rlAliPay = (RelativeLayout) view.findViewById(R.id.rl_ali_pay);
        rlWechatPay = (RelativeLayout) view.findViewById(R.id.rl_wechat_pay);
        btn_confirm_pay = (Button) view.findViewById(R.id.btn_confirm_pay);
        ivAliCheck = (ImageView) view.findViewById(R.id.iv_ali_check);
        ivWechatCheck = (ImageView) view.findViewById(R.id.iv_wechat_check);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new DemoAdapter();
        recyclerView.setAdapter(adapter);
        adapter.replaceAll(getData());


        Log.d("APITest", "initView");
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        api = WXAPIFactory.createWXAPI(mContext, Constant.APP_ID);
        api.registerApp(Constant.APP_ID);
        //获取个人信息
        mMyOkhttp.get()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.UserInfo)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d("APITest", "doGet onSuccess:" + response);
                        Data<UserInfo> obj = (Data<UserInfo>) JSON.parseObject(response.toString(), new TypeReference<Data<UserInfo>>() {
                        });
                        resCode = obj.getCode();
                        userInfo = obj.getData();
                        if (resCode == Constant.SUCCESSCODE) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (userInfo != null) {
                                        tv_balance.setText(userInfo.getBalance());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });

        adapter.setClickListener(new DemoAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                moneynum = position;
            }
        });

        zhifuStyle = "WX";
        rlAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable btn_uncheck = mContext.getResources().getDrawable(R.drawable.btn_uncheck);
                Drawable btn_check = mContext.getResources().getDrawable(R.drawable.btn_check);

                //ivAliCheck.setVisibility(View.VISIBLE);
                //ivWechatCheck.setVisibility(View.GONE);
                //选中ali
                ivAliCheck.setImageDrawable(btn_check);
                //未选中WX
                ivWechatCheck.setImageDrawable(btn_uncheck);
                zhifuStyle = "ALI";
            }
        });

        rlWechatPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ivAliCheck.setVisibility(View.GONE);
                //ivWechatCheck.setVisibility(View.VISIBLE);
                Drawable btn_uncheck = mContext.getResources().getDrawable(R.drawable.btn_uncheck);
                Drawable btn_check = mContext.getResources().getDrawable(R.drawable.btn_check);
                //选中ali
                ivAliCheck.setImageDrawable(btn_uncheck);
                //未选中WX
                ivWechatCheck.setImageDrawable(btn_check);
                zhifuStyle = "WX";
            }
        });

        //发起支付请求
        btn_confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnlineRechargeJson onlineRechargeJson = new OnlineRechargeJson(arr2[moneynum], zhifuStyle);
                String jsonstyle = JSON.toJSONString(onlineRechargeJson);
                mMyOkhttp.post()
                        .addHeader("cookie", username)
                        .url(Constant.SFSHURL + Constant.Online)
                        .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("monry", "doPostJSON onSuccess JSONObject:" + response);
                                if ("ALI".equals(zhifuStyle)) {
                                    Data<PayWay_ALI> obj = (Data<PayWay_ALI>) JSON.parseObject(response.toString(), new TypeReference<Data<PayWay_ALI>>() {
                                    });
                                    resCode = obj.getCode();
                                    payWay_ali = obj.getData();
                                    Log.d("test", "resCode==" + resCode);
                                    if (resCode == Constant.SUCCESSCODE) {
                                        Runnable payRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                PayTask alipay = new PayTask(getActivity());
                                                Map<String, String> result = alipay.payV2(payWay_ali.getAli(), true);
                                                Log.d("test", "result==" + result);
                                                Log.i("msp", result.toString());

                                                Message msg = new Message();
                                                msg.what = SDK_PAY_FLAG;
                                                msg.obj = result;
                                                mHandler.sendMessage(msg);
                                            }
                                        };

                                        Thread payThread = new Thread(payRunnable);
                                        payThread.start();
                                    }
                                } else if ("WX".equals(zhifuStyle)) {
                                    Log.d("wxpay_res_data", response.toString());
                                    PayWayWXReply res = JSON.parseObject(response.toString(), PayWayWXReply.class);
                                    if (res == null) {
                                        return;
                                    }
                                    resCode = res.getCode();
                                    payWay_wx = res.getData().get("wx");
                                    try {
                                        if (payWay_wx != null) {
                                            PayReq req = new PayReq();
                                            req.appId = "wxb090b32121969d14";
                                            req.partnerId = "1512642551";
                                            req.prepayId = payWay_wx.get("prepayid");
                                            req.packageValue = payWay_wx.get("package");
                                            req.nonceStr = payWay_wx.get("noncestr");
                                            req.timeStamp = payWay_wx.get("timestamp");
                                            req.sign = payWay_wx.get("sign");
                                            req.extData = payWay_wx.get("sn");
                                            Toast.makeText(mContext, "正常调起支付", Toast.LENGTH_SHORT).show();
                                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                            api.sendReq(req);
                                        } else {
                                            Log.d("PAY_GET", "服务器请求错误");
                                            Toast.makeText(mContext, "服务器请求错误", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Log.e("PAY_GET", "异常：" + e.getMessage());
                                        Toast.makeText(mContext, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
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
    }

    public ArrayList<ItemModel> getData() {
        ArrayList<ItemModel> list = new ArrayList<>();
        ItemModel itemModel1 = new ItemModel(ItemModel.ONE, 100.00+ "元");
        list.add(itemModel1);
        ItemModel itemModel2 = new ItemModel(ItemModel.ONE, 200.00+ "元");
        list.add(itemModel2);
        ItemModel itemModel3 = new ItemModel(ItemModel.ONE, 300.00+ "元");
        list.add(itemModel3);
        ItemModel itemModel4 = new ItemModel(ItemModel.ONE, 500.00+ "元");
        list.add(itemModel4);
        ItemModel itemModel5 = new ItemModel(ItemModel.ONE, 800.00+ "元");
        list.add(itemModel5);
        ItemModel itemModel6 = new ItemModel(ItemModel.ONE, 1000.00 + "元");
        list.add(itemModel6);
        return list;
    }

}
