package ruilelin.com.shifenlife.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONObject;
import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.json.Answer;
import ruilelin.com.shifenlife.utils.Constant;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    /**
     * 微信登录相关
     */
    private IWXAPI api;
    private MyOkHttp mMyOkhttp;
    private MyApplication app = MyApplication.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID,true);
        //将应用的appid注册到微信
        api.registerApp(Constant.APP_ID);
        // ViseLog.d("------------------------------------");
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result =  api.handleIntent(getIntent(), this);
            if(!result){
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data,this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d("wx","resp.getType()=="+baseResp.getType());
        String result = "";
        switch(baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result ="发送成功";
//                showDialog("正在获取个人资料..");
                //现在请求获取数据 access_token https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
//                showMsg(1,result);
                /*Call call = RetrofitUtils.getApiService("https://api.weixin.qq.com/").getWeiXinAccessToken(Config.APP_ID_WX,Config.APP_SECRET_WX,entity.getCode(),"authorization_code");
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        ViseLog.d("response:"+JSON.toJSONString(response));
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        closeDialog();
                    }
                });*/
                if(baseResp.getType()==1) {
                    WXBaseRespEntity entity = JSON.parseObject(JSON.toJSONString(baseResp), WXBaseRespEntity.class);
                    OkHttpUtils.get().url("https://api.weixin.qq.com/sns/oauth2/access_token")
                            .addParams("appid", Constant.APP_ID)
                            .addParams("secret", Constant.APP_SECRET_WX)
                            .addParams("code", entity.getCode())
                            .addParams("grant_type", "authorization_code")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(okhttp3.Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    WXAccessTokenEntity accessTokenEntity = JSON.parseObject(response, WXAccessTokenEntity.class);
                                    if (accessTokenEntity != null) {
                                        if(app.isWXBangDing()){
                                            app.setWXBangDing(false);
                                            Toast.makeText(WXEntryActivity.this, "微信成功绑定", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else if(app.isWXLogin()){
                                            app.setWXLogin(false);
                                            getUserInfo(accessTokenEntity);
                                        }
                                    } else {

                                    }
                                }
                            });
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                finish();
                break;
            case BaseResp.ErrCode.ERR_BAN:
                result = "签名错误";
                break;
            default:
                result = "发送返回";
                //showMsg(0,result);
                finish();
                break;
        }
        Toast.makeText(WXEntryActivity.this,result,Toast.LENGTH_LONG).show();

    }

    /**
     * 获取个人信息
     * @param accessTokenEntity
     */
    private void getUserInfo(WXAccessTokenEntity accessTokenEntity) {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        OkHttpUtils.get()
                .url("https://api.weixin.qq.com/sns/userinfo")
                .addParams("access_token",accessTokenEntity.getAccess_token())
                .addParams("openid",accessTokenEntity.getOpenid())//openid:授权用户唯一标识
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        WXUserInfo wxResponse = JSON.parseObject(response,WXUserInfo.class);
                        String headUrl = wxResponse.getHeadimgurl();
                        int sexInt = wxResponse.getSex();
                        String sex;
                        if(sexInt == 1){
                            sex = "BOY";
                        }else if(sexInt ==2){
                            sex = "GIRL";
                        }else{
                            sex = "SECRET";
                        }
                        String nickname = wxResponse.getNickname();
                        String wxUnionId = wxResponse.getUnionid();
                        LoginApply loginApply = new LoginApply(wxUnionId,nickname,sex,headUrl);
                        String jsonstyle = JSON.toJSONString(loginApply);
                        mMyOkhttp.post()
                                .url(Constant.SFSHURL+Constant.LoginWithWX)
                                .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                .tag(this)
                                .enqueue(new JsonResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, JSONObject response) {
                                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                        int  resCode =  JSON.parseObject(response.toString(), Answer.class).getCode();

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(resCode ==Constant.SUCCESSCODE) {
                                                        Toast.makeText(WXEntryActivity.this, "微信成功登陆", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
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
                        //App.getShared().putString("headUrl",headUrl);
                        /*Intent intent = getIntent();
                        intent.putExtra("headUrl",headUrl);
                        WXEntryActivity.this.setResult(0,intent);*/
                        finish();
                    }
                });
    }
}
