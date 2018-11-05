package ruilelin.com.shifenlife.activity;

import android.content.Intent;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    sleep(3000);//使程序休眠五秒
                    Intent it=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(it);
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }

    @Override
    protected void initListener() {

    }
}
