package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseFragment3;
import ruilelin.com.shifenlife.person.fragment.OnlineRechargeFragment;
import ruilelin.com.shifenlife.person.fragment.WelfareCardRechargeFragment;

public class Recharge extends FragmentActivity {
    private TextView tv_detail;
    private TextView mTbTitle;
    private Button back;
    private RadioGroup rgmain;
    private ArrayList<BaseFragment3> mFragments;
    private BaseFragment3 mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        rgmain = (RadioGroup) findViewById(R.id.rg_main);
        mTbTitle = (TextView) findViewById(R.id.title);
        back = (Button) findViewById(R.id.back);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_detail.setVisibility(View.VISIBLE);

        tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Recharge.this, RechargeRecord.class);
                startActivity(intent);
            }
        });
        mTbTitle.setText("充值");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        //创建5个fragment并添加到容器
        mFragments = new ArrayList<>();
        mFragments.add(new OnlineRechargeFragment());
        mFragments.add(new WelfareCardRechargeFragment());
        Log.d("chongzhi", "initView2");
    }

    private void initEvent() {
        //radioGroup选中事件
        rgmain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                int curPosition = 0; //viewGroup当前选中索引
                switch (checkId) {
                    case R.id.rb_online:
                        curPosition = 0;
                        break;
                    case R.id.rb_welfare_card:
                        curPosition = 1;
                        break;
                }
                //switchFragment(mFragments.get(curPosition));
                switchFragment(mContext, mFragments.get(curPosition));
            }
        });
        rgmain.check(R.id.rb_online);//默认选中首页
    }

    //切换fragment 每次都会初始化frgament数据
    public void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_home, fragment).commit();
    }

    //切换fragment fragment只初始化一次
    private void switchFragment(Fragment fromFragment, BaseFragment3 nextFragment) {
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
}
