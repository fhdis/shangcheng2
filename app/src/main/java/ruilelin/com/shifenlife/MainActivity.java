package ruilelin.com.shifenlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.utils.L;
import com.tsy.sdk.myokhttp.MyOkHttp;
import java.util.ArrayList;
import ruilelin.com.shifenlife.base.BaseFragment;
import ruilelin.com.shifenlife.cart.fragment.CartFragment;
import ruilelin.com.shifenlife.home.fragment.RecyclerHomeFragment;
import ruilelin.com.shifenlife.person.fragment.UserFragment;
import ruilelin.com.shifenlife.typetwoway.NewTypeFragment;


public class MainActivity extends FragmentActivity implements  RecyclerHomeFragment.OnEditListener,CartFragment.OnEditListener{
    RadioGroup rgmain;
    private ArrayList<BaseFragment> mFragments;
    private BaseFragment mContext;
    private MyOkHttp mMyOkhttp;
    private String searchtext;
    private String channeltext;
    private OnGetTextListener onGetTextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        // Login();
    }

    private void initData() {

        //创建5个fragment并添加到容器
        mFragments = new ArrayList<>();
        mFragments.add(new RecyclerHomeFragment());
        mFragments.add(new NewTypeFragment());
        mFragments.add(new CartFragment());
        mFragments.add(new UserFragment());
    }

    private void initEvent() {
        //radioGroup选中事件
        rgmain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                int curPosition = 0; //viewGroup当前选中索引
                switch (checkId) {
                    case R.id.rb_home:
                        curPosition = 0;
                        break;
                    case R.id.rb_type:
                        curPosition = 1;
                        break;
                    case R.id.rb_cart:
                        curPosition = 2;
                        break;
                    case R.id.rb_user:
                        curPosition = 3;
                        break;
                }
                //switchFragment(mFragments.get(curPosition));
                switchFragment(mContext, mFragments.get(curPosition));
            }
        });
        rgmain.check(R.id.rb_home);//默认选中首页
    }

    //切换fragment 每次都会初始化frgament数据
    public void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_home, fragment).commit();
    }

    //切换fragment fragment只初始化一次
    private void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
        //判断当前fragment和目标fragment是否是同一个
        if (mContext != nextFragment) {
            //不是同一个 当前要显示的fragment就是nextfragment
            mContext = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加过
                if (!nextFragment.isAdded()) {
                    //nextFragment没被添加过

                    //隐藏当前Fragment 添加nextFragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.add(R.id.fl_home, nextFragment).commit();
                } else {
                    //nextFragment被添加过

                    //隐藏当前Fragment  显示nextFragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
    }

    private void initView() {
        rgmain = (RadioGroup) findViewById(R.id.rg_main);
    }

    @Override
    public void onEditSelected(String text,int from) {
        rgmain.check(R.id.rb_type);//默认选中首页
        if(from==1) {
            searchtext = text;
            Log.d("pipei", "text=" + searchtext);
           // getTitles();
            if(onGetTextListener!=null) {
                onGetTextListener.ongetSearchText(searchtext, 1);
            }
        }

        if(from==2){
            channeltext = text;
            Log.d("pipei","channeltext=="+channeltext);
           //getChannelText();
            if(onGetTextListener!=null) {
                onGetTextListener.ongetTypeText(channeltext, 2);
            }
        }
    }

    public String getTitles(){
        return searchtext;
    }

    public String getChannelText(){
        return channeltext;
    }


    @Override
    public void onEditSelected() {
        rgmain.check(R.id.rb_home);//默认选中首页
    }

    @Override
    protected void onResume() {
        int checkStatus = getIntent().getIntExtra("check", 0);
        if (checkStatus == 3) {
            rgmain.check(R.id.rb_user);
        }
        super.onResume();
    }

    public void setOnGetTextListener(OnGetTextListener listener) {
        onGetTextListener = listener;
    }
    public interface OnGetTextListener {
        void ongetSearchText(String search,int from);
        void ongetTypeText(String type,int from);
    }

}
