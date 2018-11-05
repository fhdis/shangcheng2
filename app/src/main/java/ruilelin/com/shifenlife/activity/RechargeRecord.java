package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jeanboy.recyclerviewhelper.RecyclerViewHelper;
import com.jeanboy.recyclerviewhelper.adapter.ViewType;
import com.jeanboy.recyclerviewhelper.footer.FooterState;
import com.jeanboy.recyclerviewhelper.listener.LoadMoreListener;
import com.jeanboy.recyclerviewhelper.listener.OnFooterChangeListener;
import com.jeanboy.recyclerviewhelper.listener.OnViewBindListener;
import com.jeanboy.recyclerviewhelper.listener.TipsListener;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.List;

import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.model.RecharRecordModel;
import ruilelin.com.shifenlife.person.adapter.RechargeRecordAdapter;
import ruilelin.com.shifenlife.utils.Constant;

public class RechargeRecord extends BaseActivity {

    //Toolbar mTbTitle;
    private Button back;
    private TextView title;
    RecyclerView mRvPersonPlace;
    private RechargeRecordAdapter mRechargeRecordAdapter;
    private String username;
    private RecyclerViewHelper recyclerViewHelper;
    private boolean isOver = false;
    private int page = 1;
    private int size = 5;


    @Override
    protected int setLayoutResID() {
        return R.layout.recharge_record;
    }

    @Override
    protected void initData() {
        //设置没有数据的Tips
        recyclerViewHelper.setTipsEmptyView(R.layout.view_data_empty);
        //设置加载中的Tips
        recyclerViewHelper.setTipsLoadingView(R.layout.view_data_loading);
        //设置加载失败的Tips
        recyclerViewHelper.setTipsErrorView(R.layout.view_data_error);
        //设置header
        //recyclerViewHelper.setHeaderView(R.layout.view_header);

        //默认加载更多 footer 也可自定义
        recyclerViewHelper.useDefaultFooter();

        //加载失败，没有数据时Tips的接口
        recyclerViewHelper.setTipsListener(new TipsListener() {
            @Override
            public void retry() {
                initInfoData();
            }
        });

        //加载更多的接口
        recyclerViewHelper.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                if (isOver) {
                    loadNext();
                }
            }
        });

        recyclerViewHelper.setOnViewBindListener(new OnViewBindListener() {
            @Override
            public void onBind(RecyclerView.ViewHolder holder, int viewType) {
                Log.d(MainActivity.class.getName(), "==============onBind============");
                if (ViewType.TYPE_HEADER == viewType) {
                    // TODO: 2017/7/13 header view bind
                } else if (ViewType.TYPE_FOOTER == viewType) {
                    // TODO: 2017/7/13 footer view bind
                }
            }
        });

        recyclerViewHelper.setFooterChangeListener(new OnFooterChangeListener() {
            @Override
            public void onChange(RecyclerView.ViewHolder holder, int state) {
                Log.d(MainActivity.class.getName(), "==============onChange============");
                if (FooterState.LOADING == state) {
                    // TODO: 2017/7/13 加载中
                } else if (FooterState.ERROR == state) {
                    // TODO: 2017/7/13 加载失败
                } else if (FooterState.NO_MORE == state) {
                    // TODO: 2017/7/13 加载完成
                }
            }
        });

        initInfoData();
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mRvPersonPlace = (RecyclerView) findViewById(R.id.rv_person_info);
       /* mTbTitle = (Toolbar)findViewById(R.id.tb_title);
        mTbTitle.setNavigationIcon(R.mipmap.arrow_left);
        mTbTitle.setTitle("我的充值明细");
        mTbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        title.setText("我的充值明细");
        title.setTypeface(Typeface.DEFAULT_BOLD);
        mRechargeRecordAdapter = new RechargeRecordAdapter(this);


        //添加自定义分割线
        //DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        // mRvPersonPlace.addItemDecoration(divider);
        //mRvPersonPlace.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //mRvPersonPlace.setItemAnimator(new DefaultItemAnimator());
        //mRvPersonPlace.setAdapter(mRechargeRecordAdapter);

        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRechargeRecordAdapter = new RechargeRecordAdapter(this);
        mRvPersonPlace.setLayoutManager(layoutManager);
        mRvPersonPlace.setAdapter(mRechargeRecordAdapter);
        recyclerViewHelper = new RecyclerViewHelper(mRvPersonPlace, mRechargeRecordAdapter);
    }

    @Override
    protected void initListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRechargeRecordAdapter.setOnRecyclerViewListener(new OnClickRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View view) {

            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

    }

    private void initInfoData() {
        mRechargeRecordAdapter.clearData();

        mMyOkhttp.get()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.RechargeRecord + "/" + page + "/" + size)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d("APITest", "doGet onSuccess:" + response);
                        Data<List<RecharRecordModel>> obj = (Data<List<RecharRecordModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<RecharRecordModel>>>() {
                        });
                        int resCode = obj.getCode();
                        List<RecharRecordModel> tempList = obj.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    if (resCode == Constant.SUCCESSCODE) {
                                        if (tempList != null && tempList.size() == 0) {
                                            //首次加载数据成功
                                            recyclerViewHelper.loadComplete(true);
                                        } else if (tempList != null && tempList.size() < size) {
                                            mRechargeRecordAdapter.updataData(tempList);
                                            recyclerViewHelper.loadComplete(false);
                                        } else if (tempList != null && tempList.size() == size) {
                                            mRechargeRecordAdapter.updataData(tempList);
                                            isOver = true;
                                            page++;
                                            recyclerViewHelper.loadComplete(true);
                                        }
                                    }
                                } else if (resCode == Constant.NOTlOG) {
                                    Toast.makeText(RechargeRecord.this, "尚未登录", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });
    }


    private void loadNext() {
        Log.d("page", "page11==" + page);
        mMyOkhttp.get()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.RechargeRecord + "/" + page + "/" + size)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Data<List<RecharRecordModel>> obj = (Data<List<RecharRecordModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<RecharRecordModel>>>() {
                        });
                        int resCode = obj.getCode();
                        List<RecharRecordModel> temList = obj.getData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (temList != null && temList.size() < size) {
                                                mRechargeRecordAdapter.appendData(temList);
                                                isOver = false;
                                                //分页数据加载成功，还有下一页
                                                recyclerViewHelper.loadComplete(false);
                                            } else if (temList != null && temList.size() == 5) {
                                                mRechargeRecordAdapter.appendData(temList);
                                                isOver = true;
                                                page++;
                                                //分页数据加载成功，没有更多。即全部加载完成
                                                recyclerViewHelper.loadComplete(true);
                                            }

                                        }
                                    });
                                } else if (resCode == Constant.NOTlOG) {
                                    Toast.makeText(RechargeRecord.this, "尚未登录", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });
    }
}
