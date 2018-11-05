package ruilelin.com.shifenlife.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.tsy.sdk.myokhttp.MyOkHttp;

import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.activity.Recharge;

public abstract class BaseFragment3 extends Fragment {
    //protected AppCompatActivity mBaseActivity;  //贴附的activity,Fragment中可能用到
    // protected View mRootView;           //根view

    protected Context mContext;
    protected Recharge activity;
    public MyOkHttp mMyOkhttp;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        activity = (Recharge) getActivity();
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }


    protected abstract int setLayoutResourceId();


    /**
     * 初始化数据
     *
     * @param bundle 接收到的从其他地方传递过来的数据
     */
    protected abstract void initData(Bundle bundle);

    //视图初始化，子类必须实现
    protected abstract View initView();

    //设置监听事件
    protected abstract void setListener();

    protected void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mUnbinder.unbind();
    }


    protected void startActivity(Class clazz) {
        Intent intent = new Intent(activity, clazz);
        startActivity(intent);
    }


}
