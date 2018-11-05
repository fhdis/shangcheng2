package ruilelin.com.shifenlife;

import android.support.multidex.MultiDexApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.squareup.picasso.Picasso;
import com.tsy.sdk.myokhttp.MyOkHttp;
import java.util.concurrent.TimeUnit;
import cn.alien95.util.Utils;
import okhttp3.OkHttpClient;

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;
    private MyOkHttp mMyOkHttp;
    private boolean isWXLogin;
    private boolean isWXBangDing;

    public boolean isWXLogin() {
        return isWXLogin;
    }

    public void setWXLogin(boolean WXLogin) {
        isWXLogin = WXLogin;
    }

    public boolean isWXBangDing() {
        return isWXBangDing;
    }

    public void setWXBangDing(boolean WXBangDing) {
        isWXBangDing = WXBangDing;
    }

    @Override
    public void onCreate() {
        isWXLogin = false;
        isWXBangDing = false;
        super.onCreate();
        mInstance = this;
        Fresco.initialize(this);
        Utils.initialize(this);
        final Picasso picasso = new Picasso.Builder(this).build();
        Picasso.setSingletonInstance(picasso);

        // 从 Persistor 获取 cookie
        SharedPrefsCookiePersistor cookiePersistor = new SharedPrefsCookiePersistor(getApplicationContext());
        SetCookieCache cookieCache = new SetCookieCache();
        cookieCache.addAll(cookiePersistor.loadAll());


        ClearableCookieJar cookieJar = new PersistentCookieJar(cookieCache, cookiePersistor);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)       //设置开启cookie
//                .addInterceptor(new ReceivedCookiesInterceptor(this))
                .build();
        mMyOkHttp = new MyOkHttp(okHttpClient);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MyOkHttp getMyOkHttp() {
        return mMyOkHttp;
    }
}
