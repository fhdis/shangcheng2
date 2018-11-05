package ruilelin.com.shifenlife.myorder.anotherway;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ruilelin.com.shifenlife.base.BaseFragment4;

public class MyPagerAdapter<T extends BaseFragment4> extends FragmentPagerAdapter {

    private String[] mTitles;
    private List<T> mBaseFragmentList;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyPagerAdapter(FragmentManager fm, String[] titles, List<T> baseFragmentList) {
        super(fm);
        mTitles = titles;
        mBaseFragmentList = baseFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mBaseFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mBaseFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}

