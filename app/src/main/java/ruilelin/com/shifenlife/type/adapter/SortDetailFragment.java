package ruilelin.com.shifenlife.type.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.GoodsInfoPage;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.HotParcelable;
import ruilelin.com.shifenlife.json.HotProduct;
import ruilelin.com.shifenlife.json.Primary;
import ruilelin.com.shifenlife.json.PrimarySeri;
import ruilelin.com.shifenlife.json.ProductDetailJson;
import ruilelin.com.shifenlife.type.listener.CheckListener;
import ruilelin.com.shifenlife.type.listener.RvListener;
import ruilelin.com.shifenlife.utils.Constant;

public class SortDetailFragment extends BaseFragment<SortDetailPresenter, String> implements CheckListener {
    private RecyclerView mRv;
    private ClassifyDetailAdapter mAdapter;
    private GridLayoutManager mManager;
    private List<RightBean> mDatas = new ArrayList<>();
    private ItemHeaderDecoration mDecoration;
    private boolean move = false;
    private int mIndex = 0;
    private CheckListener checkListener;
    public MyOkHttp mMyOkhttp;
    private  String username;
    private int secPosition = -1;
    private int i = 0;
    ArrayList<Primary> rightList;
    private List<HotProduct> hotslist = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sort_detail;
    }

    @Override
    protected void initCustomView(View view) {
        mRv = (RecyclerView) view.findViewById(R.id.rv);

    }

    @Override
    protected void initListener() {
        mRv.addOnScrollListener(new RecyclerViewListener());
    }

    @Override
    protected SortDetailPresenter initPresenter() {
        showRightPage(1);
        mManager = new GridLayoutManager(mContext, 3);
        //通过isTitle的标志来判断是否是title
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDatas.get(position).isTitle() ? 3 : 1;
            }
        });
        mRv.setLayoutManager(mManager);
        mAdapter = new ClassifyDetailAdapter(mContext, mDatas, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {
                String content = "";
                switch (id) {
                    case R.id.root:
                        content = "title";
                        break;
                    case R.id.content:
                        content = "content";
                        break;

                }
                secPosition = position;
                Snackbar snackbar = Snackbar.make(mRv, "当前点击的是" + content + ":" + mDatas.get(position).getName(), Snackbar.LENGTH_SHORT);
                View mView = snackbar.getView();
                mView.setBackgroundColor(Color.BLUE);
                TextView text = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                text.setTextColor(Color.WHITE);
                text.setTextSize(25);
                snackbar.show();

                if(content.equals("content") && !mDatas.get(position).getName().equals("暂无数据")){
                    Intent intent = new Intent(getContext(), GoodsInfoPage.class);
                    ProductDetailJson productDetailJson = new ProductDetailJson(String.valueOf(mDatas.get(position).getSupplierId()),String.valueOf(mDatas.get(position).getId()));
                    intent.putExtra("productdetail", productDetailJson);
                    getContext().startActivity(intent);
                }
            }
        });

        mRv.setAdapter(mAdapter);
        mDecoration = new ItemHeaderDecoration(mContext, mDatas);
        mRv.addItemDecoration(mDecoration);
        mDecoration.setCheckListener(checkListener);
        initData();
        return new SortDetailPresenter();
    }


    private void initData() {
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        SharedPreferences sharedPreferences =mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        //getSecondaryClassification();
        rightList = getArguments().getParcelableArrayList("right");
        /*if(rightList!=null && rightList.size()>0){
            for(int i=0;i<rightList.size();i++){
                Log.d("hot","isMainThread11111=="+isMainThread());
                new ClassificationTask(i).execute();
            }
        }*/
        secPosition = getArguments().getInt("sec");
        Log.d("1234","secPosition=="+secPosition);

       //ArrayList<SortBean.CategoryOneArrayBean> rightList = getArguments().getParcelableArrayList("right");*/
        if(rightList!=null) {
            for (int i = 0; i < rightList.size(); i++) {
                    RightBean head = new RightBean(rightList.get(i).getName());
                    //头部设置为true
                    head.setTitle(true);
                    head.setTitleName(rightList.get(i).getName());
                    head.setTag(String.valueOf(i));
                    mDatas.add(head);

                    List<HotParcelable> categoryTwoArray = rightList.get(i).getGoods();
                    Log.d("hot", "categoryTwoArray==" + categoryTwoArray.size());
                    if (categoryTwoArray != null) {
                        if(categoryTwoArray.size() ==0){
                            RightBean body = new RightBean("暂无数据");
                            body.setTag(String.valueOf(i));
                            String name = rightList.get(i).getName();
                            body.setTitleName(name);
                            mDatas.add(body);
                        }
                        for (int j = 0; j < categoryTwoArray.size(); j++) {
                            RightBean body = new RightBean(categoryTwoArray.get(j).getName());
                            body.setTag(String.valueOf(i));
                            String name = rightList.get(i).getName();
                            body.setTitleName(name);
                            body.setId(categoryTwoArray.get(j).getId());
                            body.setSupplierId(categoryTwoArray.get(j).getSupplierId());
                            mDatas.add(body);
                        }
                    }
                }

                mAdapter.notifyDataSetChanged();
                mDecoration.setData(mDatas);
            }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void refreshView(int code, String data) {

    }

    public void setData(int n) {
        mIndex = n;
        mRv.stopScroll();
        smoothMoveToPosition(n);
    }

    @Override
    protected void getData() {

    }

    public void setListener(CheckListener listener) {
        this.checkListener = listener;
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        Log.d("first--->", String.valueOf(firstItem));
        Log.d("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            mRv.scrollToPosition(n);
        } else if (n <= lastItem) {
            Log.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = mRv.getChildAt(n - firstItem).getTop();
            Log.d("top---->", String.valueOf(top));
            mRv.scrollBy(0, top);
        } else {
            mRv.scrollToPosition(n);
            move = true;
        }
    }


    @Override
    public void check(int position, boolean isScroll) {
        checkListener.check(position, isScroll);

    }


    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                Log.d("n---->", String.valueOf(n));
                if (0 <= n && n < mRv.getChildCount()) {
                    int top = mRv.getChildAt(n).getTop();
                    Log.d("top--->", String.valueOf(top));
                    mRv.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRv.getChildCount()) {
                    int top = mRv.getChildAt(n).getTop();
                    mRv.scrollBy(0, top);
                }
            }
        }
    }


    public class ClassificationTask extends AsyncTask<Void,Void,List<HotProduct>> {
        List<HotProduct> hots = new ArrayList<>();
        int position;

        public ClassificationTask(int position) {
            this.position = position;
        }
        @Override
        protected List<HotProduct> doInBackground(Void... voids) {
            Log.d("hot","hots444=="+position);
            String url = Constant.SFSHURL + Constant.FirsClassification+rightList.get(position).getId();
            mMyOkhttp.get()
                    .url(url)
                    .addHeader("cookie", username)
                    .tag(this)
                    .enqueue(new RawResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, String response) {
                            Log.d("APITest", "doGet onFailure:");
                            Data<List<HotProduct>> obj = (Data<List<HotProduct>>) JSON.parseObject(response.toString(), new TypeReference<Data<List<HotProduct>>>() {});
                            int resCode = obj.getCode();
                            if(resCode==Constant.SUCCESSCODE){
                                hots = obj.getData();
                                if(hots!=null ) {
                                    hotslist.addAll(hots);
                                }
                                Log.d("hot","hots3333=="+hots.size());
                                Log.d("hot","hots5555=="+hotslist.size());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            Log.d("APITest", "doGet onFailure:" + error_msg);
                        }
                    });
            return hots;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<HotProduct> hotProducts) {
            Log.d("hot","hots2222=="+hotProducts.size());
            super.onPostExecute(hotProducts);
            if(hotProducts!=null ) {
                hotslist.addAll(hotProducts);
                Log.d("hot","hots1111=="+hotslist.size());
                Log.d("hot","isMainThread=="+isMainThread());
            }
        }
    }

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
