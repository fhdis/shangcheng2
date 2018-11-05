package ruilelin.com.shifenlife.person.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseFragment3;
import ruilelin.com.shifenlife.json.Answer;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.WelfareCardJson;
import ruilelin.com.shifenlife.model.UserInfo;
import ruilelin.com.shifenlife.utils.Constant;


public class WelfareCardRechargeFragment extends BaseFragment3 {
    private EditText edit_cardnumber;
    private EditText edit_password;
    private Button btn_confirm_pay;
    private int resCode = -1;
    private UserInfo userInfo;
    private TextView tv_balance;
    private String username;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.welfare_card_recharge;
    }

    @Override
    protected void initData(Bundle bundle) {

    }

    @Override
    protected View initView() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        View view = View.inflate(mContext, R.layout.welfare_card_recharge, null);
        edit_cardnumber = (EditText) view.findViewById(R.id.edit_cardnumber);
        edit_password = (EditText) view.findViewById(R.id.edit_password);
        btn_confirm_pay = (Button) view.findViewById(R.id.btn_confirm_pay);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        return view;
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        btn_confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //福利卡充值
                String cardNumber = edit_cardnumber.getText().toString();
                String password = edit_password.getText().toString();
                WelfareCardJson welfareCardJson = new WelfareCardJson(cardNumber, password);
                String jsonstyle = JSON.toJSONString(welfareCardJson);

                mMyOkhttp.post()
                        .addHeader("cookie", username)
                        .url(Constant.SFSHURL + Constant.WelfareCard)
                        .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                Answer obj = (Answer) JSON.parseObject(response.toString(), new TypeReference<Answer>() {
                                });
                                resCode = obj.getCode();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCode == Constant.SUCCESSCODE) {
                                            Toast.makeText(mContext, "充值成功", Toast.LENGTH_SHORT).show();
                                        } else if (resCode == Constant.FAIL) {
                                            Toast.makeText(mContext, "账号或密码输入有误o(╯□╰)o ", Toast.LENGTH_SHORT).show();
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
}
