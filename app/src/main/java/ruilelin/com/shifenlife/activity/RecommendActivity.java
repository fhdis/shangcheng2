package ruilelin.com.shifenlife.activity;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.alien95.util.Utils;
import cn.lemon.view.RefreshRecyclerView;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.recommend.CardRecordAdapter;
import ruilelin.com.shifenlife.recommend.bean.Consumption;

public class RecommendActivity extends BaseActivity {
    private RefreshRecyclerView mRecyclerView;
    private CardRecordAdapter mAdapter;
    private Handler mHandler;
    private int page = 1;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void initData() {
        mHandler = new Handler();
        mAdapter = new CardRecordAdapter(this);
        //添加Header
       /* final TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(48)));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        //textView.setText("加载更多");
        mAdapter.setHeader(textView);*/
        //添加footer
        final TextView footer = new TextView(this);
        footer.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(48)));
        footer.setTextSize(16);
        footer.setGravity(Gravity.CENTER);
        footer.setText("加载更多");
        mAdapter.setFooter(footer);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addRefreshAction(() -> getData(true));

        mRecyclerView.addLoadMoreAction(() -> {
            getData(false);
            page++;
        });

        mRecyclerView.addLoadMoreErrorAction(() -> {
            getData(false);
            page++;
        });

        mRecyclerView.post(() -> {
            mRecyclerView.showSwipeRefresh();
            getData(true);
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(() -> {
            if (isRefresh) {
                page = 1;
                mAdapter.clear();
                mAdapter.addAll(getVirtualData());
                mRecyclerView.dismissSwipeRefresh();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
            } else if (page == 3) {
                mAdapter.showLoadMoreError();
            } else {
                mAdapter.addAll(getVirtualData());
                if (page >= 5) {
                    mRecyclerView.showNoMore();
                }
            }
        }, 1500);
    }

    public Consumption[] getVirtualData() {
        return new Consumption[]{
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼"),
                new Consumption("Demo", "2015-12-18 12:09", "消费", 9.7f, 24.19f, "兴业源三楼")
        };
    }

}
