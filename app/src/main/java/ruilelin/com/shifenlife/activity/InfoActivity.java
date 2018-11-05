package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jeanboy.recyclerviewhelper.RecyclerViewHelper;
import com.jeanboy.recyclerviewhelper.adapter.ViewType;
import com.jeanboy.recyclerviewhelper.footer.FooterState;
import com.jeanboy.recyclerviewhelper.listener.LoadMoreListener;
import com.jeanboy.recyclerviewhelper.listener.OnFooterChangeListener;
import com.jeanboy.recyclerviewhelper.listener.OnViewBindListener;
import com.jeanboy.recyclerviewhelper.listener.TipsListener;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.easing.Linear;
import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.home.loadmore.BaseViewHolder;
import ruilelin.com.shifenlife.home.loadmore.CommonAdapter;
import ruilelin.com.shifenlife.home.loadmore.ListAdapter;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.InfoUnRead;
import ruilelin.com.shifenlife.json.Infomation;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.view.MyDialog;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class InfoActivity extends BaseActivity {

    //Toolbar mTbTitle;
    private RecyclerView infoResyclerView;
    private List<Infomation> dataList;
    private ListAdapter listAdapter;
    private RecyclerViewHelper recyclerViewHelper;
    private int loadCount = 2;
    private int page = 1;
    private int size = 5;
    private boolean isOver = false;
    private String username;
    //private TextView mTvTitle;
    private Button back;
    private TextView title;


    @Override
    protected int setLayoutResID() {
        return R.layout.activity_info;
    }

    @Override
    protected void initData() {

        //mTbTitle.setNavigationIcon(R.mipmap.arrow_left);
        //mTbTitle.setTitle("");
        //setSupportActionBar(mTbTitle);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mTvTitle.setText("消息中心");
        //mTbTitle.setNavigationOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View view) {
        //      finish();
        // }
        //});
        title.setText("消息中心");

        dataList = new ArrayList<Infomation>();
        listAdapter = new ListAdapter(InfoActivity.this, dataList);
        recyclerViewHelper = new RecyclerViewHelper(infoResyclerView, listAdapter);

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

        initInfoData();
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        infoResyclerView = (RecyclerView) findViewById(R.id.rv_person_info);
        //mTbTitle = (Toolbar) findViewById(R.id.tb_title);
        //mTvTitle = findViewById(R.id.mTvTitle);
        //mRelativeLayout = findViewById(R.id.rl_information);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
    }

    @Override
    protected void initListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        listAdapter.setOnItemLongPressListener(new CommonAdapter.OnItemLongPressListener() {
            @Override
            public void onItemLongPress(int position, View v) {

                MyDialog.show(InfoActivity.this, "确认删除吗?", new MyDialog.OnConfirmListener() {
                    @Override
                    public void onConfirmClick() {
                        mMyOkhttp.delete()
                                .addHeader("cookie", username)
                                .url(Constant.SFSHURL + Constant.DelateInfomation + dataList.get(position).getId())
                                .tag(this)
                                .enqueue(new JsonResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, JSONObject response) {
                                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                        Data<Boolean> obj = (Data<Boolean>) JSON.parseObject(response.toString(), new TypeReference<Data<Boolean>>() {
                                        });
                                        resCode = obj.getCode();
                                        Boolean deleteResult = obj.getData();
                                        int resCode = JSON.parseObject(response.toString(), InfoUnRead.class).getCode();
                                        if (resCode == Constant.SUCCESSCODE) {
                                            if (deleteResult) {
                                                showToast("删除成功");
                                                //刷新界面
                                                //dataList.remove(position);//删除数据源,移除集合中当前下标的数据
                                                //listAdapter.updataData(dataList);

                                                if (dataList != null & dataList.size() > 0) {
                                                    dataList.remove(position);
                                                    listAdapter.updateData(dataList);
                                                }
                                            } else {
                                                showToast("删除失败");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, JSONArray response) {
                                        Log.d("APITest", "doPostJSON onSuccess JSONArray:" + response);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, String error_msg) {
                                        Log.d("APITest", "doPostJSON onFailure:" + error_msg);
                                    }
                                });
                    }
                });
            }
        });
        listAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                //showToast("触发点击事件，id：" + dataList.get(position).getId());

                //获取点击的条目
                TextView tvInfo = holder.itemView.findViewById(R.id.tv_info);
                TextView tvTime = holder.itemView.findViewById(R.id.tv_time);
                TextView tvInfoDetail = holder.itemView.findViewById(R.id.tv_info_detail);

                //修改样式
                //将图标修改为 "已读"
                Drawable drawableRead = getResources().getDrawable(R.mipmap.icon_read);
                tvInfo.setCompoundDrawablesWithIntrinsicBounds(drawableRead, null, null, null);

                //将文字修改为灰色
                tvTime.setTextColor(getResources().getColor(android.R.color.darker_gray));
                tvInfoDetail.setTextColor(getResources().getColor(R.color.hint_color));
                tvInfo.setTextColor(getResources().getColor(R.color.hint_color));

                //修改消息状态为已读
                int id = dataList.get(position).getId();
                Infomation infomation = new Infomation();
                infomation.setId(id);
                String jsonStyle = JSON.toJSONString(infomation);
                mMyOkhttp.post()
                        .addHeader("cookie", username)
                        .url(Constant.SFSHURL + Constant.InfomationReadout)
                        .jsonParams(jsonStyle)
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                });
                                resCode = obj.getCode();
                                int resCode = JSON.parseObject(response.toString(), InfoUnRead.class).getCode();
                                if (resCode == Constant.SUCCESSCODE) {
                                    //showToast("YES");
                                } else {
                                    //showToast("FALSE");
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode, JSONArray response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONArray:" + response);
                            }

                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                Log.d("APITest", "doPostJSON onFailure:" + error_msg);
                            }
                        });
            }
        });
    }

    private void initInfoData() {
        dataList.clear();
        Log.d("page", "page==" + page);
        mMyOkhttp.get()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.InfoList + page + "/" + size)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d("APITest", "doGet onSuccess:" + response);
                        Data<List<Infomation>> obj = (Data<List<Infomation>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<Infomation>>>() {
                        });
                        int resCode = obj.getCode();
                        List<Infomation> tempList = obj.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    if (tempList != null && tempList.size() == 0) {
                                        //首次加载数据成功
                                        recyclerViewHelper.loadComplete(true);
                                    } else if (tempList != null && tempList.size() < size) {
                                        for (int i = 0; i < tempList.size(); i++) {
                                            dataList.add(tempList.get(i));
                                        }
                                        recyclerViewHelper.loadComplete(true);
                                    } else if (tempList != null && tempList.size() == size) {
                                        for (int i = 0; i < tempList.size(); i++) {
                                            dataList.add(tempList.get(i));
                                        }
                                        isOver = true;
                                        page++;
                                        recyclerViewHelper.loadComplete(false);
                                    }
                                } else if (resCode == Constant.NOTlOG) {
                                    Toast.makeText(InfoActivity.this, "尚未登录", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });


        Log.e("dataList.size()---", dataList.size() + "");
    }


    private void loadNext() {
        Log.d("page", "page11==" + page);
        mMyOkhttp.get()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.InfoList + page + "/" + size)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Data<List<Infomation>> obj = (Data<List<Infomation>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<Infomation>>>() {
                        });
                        int resCode = obj.getCode();
                        List<Infomation> temList = obj.getData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (temList != null && temList.size() < size) {
                                                for (int i = 0; i < temList.size(); i++) {
                                                    dataList.add(temList.get(i));
                                                }
                                                isOver = false;
                                                //分页数据加载成功，还有下一页
                                                recyclerViewHelper.loadComplete(false);
                                            } else if (temList != null && temList.size() == 5) {
                                                for (int i = 0; i < temList.size(); i++) {
                                                    dataList.add(temList.get(i));
                                                }
                                                page++;
                                                isOver = true;
                                                //分页数据加载成功，没有更多。即全部加载完成
                                                recyclerViewHelper.loadComplete(true);
                                            }

                                        }
                                    });
                                } else if (resCode == Constant.NOTlOG) {
                                    Toast.makeText(InfoActivity.this, "尚未登录", Toast.LENGTH_LONG).show();
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
