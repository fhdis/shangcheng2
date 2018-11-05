package ruilelin.com.shifenlife;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        List<String> cookieList =  originalResponse.headers("Set-Cookie");
        if(cookieList != null && cookieList.size()>0) {
           // for(String s:cookieList) {//Cookie的格式为:cookieName=cookieValue;path=xxx
                //保存你需要的cookie数据
               // Log.d("cookie","s=="+s)
                SharedPreferences sharedPreferences = context.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cookie",cookieList.get(0).substring(11));
                android.util.Log.d("cookie","cookieList.get(0).substring(11)=="+cookieList.get(0).substring(11));
                editor.commit();

           // }

        }

        return originalResponse;
    }
}