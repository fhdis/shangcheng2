package ruilelin.com.shifenlife.person.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import q.rorbin.badgeview.QBadgeView;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.AboutUs;
import ruilelin.com.shifenlife.activity.BalanceDetails;
import ruilelin.com.shifenlife.activity.ChangedPassword;
import ruilelin.com.shifenlife.activity.FeedbackActivity;
import ruilelin.com.shifenlife.activity.InfoActivity;
import ruilelin.com.shifenlife.activity.LoginActivity;
import ruilelin.com.shifenlife.activity.LoginWithTelCode;
import ruilelin.com.shifenlife.activity.PersonOrderActivity;
import ruilelin.com.shifenlife.activity.PersonPlaceActivity;
import ruilelin.com.shifenlife.activity.Recharge;
import ruilelin.com.shifenlife.activity.UserProfile;
import ruilelin.com.shifenlife.base.BaseFragment;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.InfoUnRead;
import ruilelin.com.shifenlife.model.UserInfo;
import ruilelin.com.shifenlife.person.view.ClickableListItem;
import ruilelin.com.shifenlife.utils.Constant;

public class UserFragment extends BaseFragment {

    private ClickableListItem about_us;
    private ClickableListItem customer_feedback;
    private TextView tv_user_name;
    private TextView tv_user_yue;
    private de.hdodenhof.circleimageview.CircleImageView civ_user_icon;
    private ClickableListItem tv_recharge_manage;
    private ClickableListItem tv_address_manage;
    private ClickableListItem zhifusetting;
    private ClickableListItem loginpasswordsetting;
    private ClickableListItem bind_wx;
    private ImageView iv_info;
    private int resCode = -1;
    private UserInfo userInfo;
    private ClickableListItem tv_login_out;

    private TextView tv_all_order;
    private TextView tv_nopay_order;
    private TextView tv_complet_order;

    private String username;
    private IWXAPI api;
    private MyApplication app = MyApplication.getInstance();
    private Boolean hadBalancePassword;
    private Boolean hadPassword;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_person, null);
        about_us = (ClickableListItem) view.findViewById(R.id.about_us);
        customer_feedback = (ClickableListItem) view.findViewById(R.id.customer_feedback);
        tv_recharge_manage = (ClickableListItem) view.findViewById(R.id.tv_recharge_manage);
        tv_address_manage = (ClickableListItem) view.findViewById(R.id.tv_address_manage);
        tv_login_out = (ClickableListItem) view.findViewById(R.id.tv_login_out);
        zhifusetting = (ClickableListItem) view.findViewById(R.id.zhifusetting);
        loginpasswordsetting = view.findViewById(R.id.loginpasswordsetting);
        bind_wx = (ClickableListItem) view.findViewById(R.id.bind_wx);

        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_user_yue = (TextView) view.findViewById(R.id.tv_user_yue);
        civ_user_icon = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.civ_user_icon);
        iv_info = (ImageView) view.findViewById(R.id.iv_info);

        tv_all_order = (TextView) view.findViewById(R.id.tv_all_order);
        tv_nopay_order = (TextView) view.findViewById(R.id.tv_nopay_order);
        tv_complet_order = (TextView) view.findViewById(R.id.tv_complet_order);
        return view;
    }

    @Override
    protected void initData() {
        api = WXAPIFactory.createWXAPI(mContext, "wxb090b32121969d14");
        getInfodata();

        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InfoActivity.class);
                startActivity(intent);
            }
        });

        bind_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyOkhttp.get()
                        .url(Constant.SFSHURL + Constant.IsBindWx)
                        .tag(this)
                        .enqueue(new RawResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, String response) {
                                Log.d("APITest", "doGet onSuccess:" + response);
                                Data<Boolean> obj = (Data<Boolean>) JSON.parseObject(response.toString(), new TypeReference<Data<Boolean>>() {
                                });
                                resCode = obj.getCode();
                                Boolean isbindwx = obj.getData();
                                Log.d("wx", "isbindwx==" + isbindwx);
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (resCode == Constant.SUCCESSCODE) {
                                                if (isbindwx) {
                                                    Toast.makeText(mContext, "您已经绑定微信", Toast.LENGTH_SHORT).show();
                                                } else if (!isbindwx) {
                                                    Toast.makeText(mContext, "进入微信授权登录界面", Toast.LENGTH_SHORT).show();
                                                    IWXAPI mApi = WXAPIFactory.createWXAPI(mContext, Constant.APP_ID, true);
                                                    mApi.registerApp(Constant.APP_ID);
                                                    if (mApi != null && mApi.isWXAppInstalled()) {
                                                        app.setWXBangDing(true);
                                                        SendAuth.Req req = new SendAuth.Req();
                                                        req.scope = "snsapi_userinfo";
                                                        req.state = "wechat_sdk_微信登录";
                                                        api.sendReq(req);
                                                    } else {
                                                        Toast.makeText(mContext, "用户未安装微信", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
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
            }
        });

        tv_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserProfile.class);
                startActivity(intent);
            }
        });

        civ_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserProfile.class);
                startActivity(intent);
            }
        });

        tv_user_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BalanceDetails.class);
                startActivity(intent);
            }
        });

        tv_address_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PersonPlaceActivity.class);
                startActivity(intent);
            }
        });

        tv_recharge_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Recharge.class);
                startActivity(intent);
            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AboutUs.class);
                startActivity(intent);
            }
        });

        customer_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        tv_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Constant.SFSHURL + Constant.LoginOut;
                mMyOkhttp.delete()
                        .url(url)
                        .tag(this)
                        .enqueue(new RawResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, String response) {
                                int resCode = JSON.parseObject(response.toString(), InfoUnRead.class).getCode();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCode == Constant.SUCCESSCODE) {
                                            Toast.makeText(mContext, "成功退出登录", Toast.LENGTH_SHORT).show();
                                            SharedPreferences userSettings = mContext.getSharedPreferences("COOKIE", 0);
                                            SharedPreferences.Editor editor = userSettings.edit();
                                            editor.clear();
                                            editor.commit();
                                            Intent intent = new Intent(mContext, LoginWithTelCode.class);
                                            startActivity(intent);
                                            getActivity().finish();
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
        });

        tv_all_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PersonOrderActivity.class);
                intent.putExtra("ID", 0);
                startActivity(intent);
            }
        });

        tv_nopay_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PersonOrderActivity.class);
                intent.putExtra("ID", 1);
                startActivity(intent);
            }
        });

        tv_complet_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PersonOrderActivity.class);
                intent.putExtra("ID", 2);
                startActivity(intent);
            }
        });


        String balancePasswordUrl = Constant.SFSHURL + Constant.HasBalancePassword;
        mMyOkhttp.get()
                .url(balancePasswordUrl)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Data<Boolean> obj = (Data<Boolean>) JSON.parseObject(response.toString(), new TypeReference<Data<Boolean>>() {
                        });
                        resCode = obj.getCode();
                        Boolean hasBalancePassword = obj.getData();
                        int resCode = JSON.parseObject(response.toString(), InfoUnRead.class).getCode();
                        if (resCode == Constant.SUCCESSCODE) {
                            hadBalancePassword = hasBalancePassword;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });

        zhifusetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChangedPassword.class);
                intent.putExtra("style", "CHANGE_PAY_PASSWORD");
                intent.putExtra("hadBalancePassword", hadBalancePassword);
                startActivity(intent);
            }
        });


        String hasPasswordUrl = Constant.SFSHURL + Constant.HasPassword;
        mMyOkhttp.get()
                .url(hasPasswordUrl)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Data<Boolean> obj = (Data<Boolean>) JSON.parseObject(response.toString(), new TypeReference<Data<Boolean>>() {
                        });
                        resCode = obj.getCode();
                        Boolean hasPassword = obj.getData();
                        int resCode = JSON.parseObject(response.toString(), InfoUnRead.class).getCode();
                        if (resCode == Constant.SUCCESSCODE) {
                            hadPassword = hasPassword;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });

        loginpasswordsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChangedPassword.class);
                intent.putExtra("style", "CHANGE_PASSWORD");
                intent.putExtra("hadPassword", hadPassword);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getInfodata();


        //设置未读消息数量
        String url = Constant.SFSHURL + Constant.UnReadInfo;
        mMyOkhttp.get()
                .url(url)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        int resCode = JSON.parseObject(response.toString(), InfoUnRead.class).getCode();
                        final int unreadnum = JSON.parseObject(response.toString(), InfoUnRead.class).getData();
                        if (resCode == Constant.SUCCESSCODE) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new QBadgeView(mContext).bindTarget(iv_info).setBadgeNumber(unreadnum).setBadgeTextSize(8, true).setBadgePadding(1, true).setBadgeGravity(Gravity.END | Gravity.TOP);
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });
    }

    //获取个人信息
    public void getInfodata() {
        mMyOkhttp.get()
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
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (userInfo != null) {
                                            Glide.with(mContext).load(Constant.SFSHURL + userInfo.getHeadImg()).listener(new RequestListener<String, GlideDrawable>() {
                                                @Override
                                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                    Toast.makeText(mContext, "头像加载失败", Toast.LENGTH_SHORT).show();
                                                    civ_user_icon.setBackground(getResources().getDrawable(R.mipmap.default_icon));
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                    return false;
                                                }
                                            }).into(civ_user_icon);
                                            Log.d("path", "path==" + userInfo.getHeadImg());
                                            if ("".equals(userInfo.getNickname())) {
                                                tv_user_name.setText("设置昵称");
                                            } else {
                                                tv_user_name.setText(userInfo.getNickname());
                                            }
                                            tv_user_yue.setText(userInfo.getBalance() + "元");
                                        }
                                    }
                                });
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
