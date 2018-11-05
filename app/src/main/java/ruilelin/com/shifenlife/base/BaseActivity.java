package ruilelin.com.shifenlife.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.tsy.sdk.myokhttp.MyOkHttp;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.activity.NetWorkErrorActivity;
import ruilelin.com.shifenlife.networkmanager.NetworkManager;
import ruilelin.com.shifenlife.networkmanager.NetworkObserver;


public abstract class BaseActivity extends AppCompatActivity {
    public int  resCode = -1;
    public MyOkHttp mMyOkhttp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResID());
        Log.d("test0924","setLayoutResID");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //ButterKnife.bind(this);
        //initState();
       NetworkManager.getInstance().initialized(this);
        if(!NetworkManager.getInstance().isNetworkConnected()) {
            Toast.makeText(BaseActivity.this,"无网络连接,请检查网络",Toast.LENGTH_LONG).show();
        }
        NetworkManager.getInstance().registerNetworkObserver(mNetworkObserver);
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);

        initView();
        initData();
        initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().unregisterNetworkObserver(mNetworkObserver);
    }

    protected abstract int setLayoutResID();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    public void hideInputSoft(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    protected void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    protected void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected static String getString(EditText et) {
        return et.getText().toString();
    }

    private NetworkObserver mNetworkObserver = new NetworkObserver() {
        @Override
        public void onNetworkStateChanged(boolean networkConnected, NetworkInfo currentNetwork, NetworkInfo lastNetwork) {
            if(networkConnected) {
            } else {
                Toast.makeText(BaseActivity.this,"无网络连接,请检查网络",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BaseActivity.this, NetWorkErrorActivity.class);
                startActivity(intent);
            }
            Log.e("AAAA", null == currentNetwork ? "无网络连接" : currentNetwork.toString());
        }
    };

   /* public void Login() {
        // OptionHandle(account,password);// 处理自动登录及记住密码
        LoginTelJson loginTelJson = new LoginTelJson("15262327502","aaaaaa");
        String jsonstyle = JSON.toJSONString(loginTelJson);
        Log.d("APITest","url=="+ Constant.SFSHURL+Constant.LOGINWITHTEL);
        Log.d("APITest","json=="+jsonstyle);

    }*/


}
