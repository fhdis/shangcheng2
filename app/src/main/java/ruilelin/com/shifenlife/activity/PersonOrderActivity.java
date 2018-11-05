package ruilelin.com.shifenlife.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.base.BaseFragment4;
import ruilelin.com.shifenlife.myorder.anotherway.MyPagerAdapter;
import ruilelin.com.shifenlife.myorder.anotherway.PersonOrderCancelFragment;
import ruilelin.com.shifenlife.myorder.anotherway.PersonOrderFragment;
import ruilelin.com.shifenlife.myorder.anotherway.PersonOrderOnGoingFragment;
import ruilelin.com.shifenlife.myorder.anotherway.PersonOrderPayedFragment;
import ruilelin.com.shifenlife.myorder.anotherway.PersonOrderPayingFragment;

public class PersonOrderActivity extends BaseActivity {
    private Toolbar mTbTitle;
    private TabLayout mTlCategory;
    private ViewPager mVpOrderRecord;
    private MyPagerAdapter mMyPagerAdapter;
    private List<BaseFragment4> mPersonOrderFragmentList;
    int current=0;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_person_order;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        current=intent.getIntExtra("ID",0);
        mTbTitle=(Toolbar)findViewById(R.id.tb_title);
        mTlCategory=(TabLayout)findViewById(R.id.tl_category);
        mVpOrderRecord=(ViewPager)findViewById(R.id.vp_order_record);

        mTbTitle.setTitle("我的订单");
        setSupportActionBar(mTbTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPersonOrderFragmentList = new ArrayList<>();
        PersonOrderFragment personOrderFragmentAll = PersonOrderFragment.newInstance(0);
        PersonOrderPayingFragment personOrderFragmentPayIng = PersonOrderPayingFragment.newInstance(1);
        PersonOrderOnGoingFragment personOrderOnGoingFragment = PersonOrderOnGoingFragment.newInstance(2);
        PersonOrderPayedFragment personOrderFragmentPayFinish = PersonOrderPayedFragment.newInstance(3);
        PersonOrderCancelFragment personOrderFragmentPayCancel = PersonOrderCancelFragment.newInstance(4);

        mPersonOrderFragmentList.add(personOrderFragmentAll);
        mPersonOrderFragmentList.add(personOrderFragmentPayIng);
        mPersonOrderFragmentList.add(personOrderOnGoingFragment);
        mPersonOrderFragmentList.add(personOrderFragmentPayFinish);
        mPersonOrderFragmentList.add(personOrderFragmentPayCancel);


        String[] titles = getResources().getStringArray(R.array.order_status);
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), titles, mPersonOrderFragmentList);
        mVpOrderRecord.setAdapter(mMyPagerAdapter);
        mVpOrderRecord.setOffscreenPageLimit(5);
        mVpOrderRecord.setCurrentItem(current);
        mTlCategory.setupWithViewPager(mVpOrderRecord);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }
}
