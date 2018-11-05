package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.model.TimeLineModel;
import ruilelin.com.shifenlife.person.adapter.TimeLineAdapter;
import ruilelin.com.shifenlife.utils.Constant;

public class BalanceDetails extends BaseActivity {

    //private Toolbar mTbTitle;
    private RecyclerView mRecycler;

    private RecyclerViewHelper recyclerViewHelper;
    private TimeLineAdapter adapter;
    private int loadCount = 2;
    private int page = 1;
    private int size = 5;
    private List<TimeLineModel> dataList = new ArrayList<>();
    private boolean isOver = false;
    private String username;
    private TextView title;
    private Button back;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_balance_detail;
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
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        //mTbTitle = (Toolbar)findViewById(R.id.tb_title);
        //mTbTitle.setTitle("余额明细");
        //mTbTitle.setNavigationOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View v) {
        //      finish();
        //  }
        //});
        title.setText("余额明细");

        mRecycler = (RecyclerView) findViewById(R.id.time_line_recycler);
        initRecycler();
    }

    @Override
    protected void initListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new TimeLineAdapter(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(adapter);
        recyclerViewHelper = new RecyclerViewHelper(mRecycler, adapter);
    }


    private void initInfoData() {
        adapter.clearData();
        Log.d("page", "page==" + page);
        mMyOkhttp.get()
               // .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.BalanceRecord + page + "/" + size)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d("APITest", "doGet onSuccess:" + response);
                        Data<List<TimeLineModel>> obj = (Data<List<TimeLineModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<TimeLineModel>>>() {});
                        int resCode = obj.getCode();
                        List<TimeLineModel> tempList = obj.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    if (resCode == Constant.SUCCESSCODE) {
                                        if (tempList != null && tempList.size() == 0) {
                                            //首次加载数据成功
                                            recyclerViewHelper.loadComplete(true);
                                        } else if (tempList != null && tempList.size() < size) {
                                            adapter.updataData(tempList);
                                            recyclerViewHelper.loadComplete(false);
                                        } else if (tempList != null && tempList.size() == size) {
                                            adapter.updataData(tempList);
                                            isOver = true;
                                            page++;
                                            recyclerViewHelper.loadComplete(true);
                                        }
                                    }
                                } else if (resCode == Constant.NOTlOG) {
                                    Toast.makeText(BalanceDetails.this, "尚未登录", Toast.LENGTH_LONG).show();
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
               // .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.BalanceRecord + page + "/" + size)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Data<List<TimeLineModel>> obj = (Data<List<TimeLineModel>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<TimeLineModel>>>() {
                        });
                        int resCode = obj.getCode();
                        List<TimeLineModel> temList = obj.getData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (temList != null && temList.size() < size) {
                                                adapter.updataData(temList);
                                                isOver = false;
                                                //分页数据加载成功，还有下一页
                                                recyclerViewHelper.loadComplete(false);
                                            } else if (temList != null && temList.size() == 5) {
                                                adapter.updataData(temList);
                                                page++;
                                                isOver = true;
                                                //分页数据加载成功，没有更多。即全部加载完成
                                                recyclerViewHelper.loadComplete(true);
                                            }

                                        }
                                    });
                                } else if (resCode == Constant.NOTlOG) {
                                    Toast.makeText(BalanceDetails.this, "尚未登录", Toast.LENGTH_LONG).show();
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
