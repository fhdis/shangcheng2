package ruilelin.com.shifenlife.goodsinfopage.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.gxz.PagerSlidingTabStrip;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.GoodsInfoPage;
import ruilelin.com.shifenlife.goodsinfopage.adapter.NetworkImageHolderView;
import ruilelin.com.shifenlife.goodsinfopage.bean.RecommendGoodsBean;
import ruilelin.com.shifenlife.goodsinfopage.widget.SlideDetailsLayout;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.ProductDetail;
import ruilelin.com.shifenlife.json.ProductDetailJson;
import ruilelin.com.shifenlife.jsonmodel.RecommendModel;
import ruilelin.com.shifenlife.listener.FragmentToActivity;
import ruilelin.com.shifenlife.utils.Constant;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * item页ViewPager里的商品Fragment
 */
public class GoodsInfoFragment extends Fragment implements View.OnClickListener, SlideDetailsLayout.OnSlideDetailsListener {
    private PagerSlidingTabStrip psts_tabs;
    private SlideDetailsLayout sv_switch;
    private ScrollView sv_goods_info;
    private FloatingActionButton fab_up_slide;
    public ConvenientBanner vp_item_goods_img, vp_recommend;
    private LinearLayout ll_goods_detail, ll_goods_config;
    private TextView tv_goods_detail, tv_goods_config;
    private View v_tab_cursor;
    public FrameLayout fl_content;
    public LinearLayout ll_current_goods, ll_activity, ll_comment, ll_recommend, ll_pull_up;
    public TextView tv_goods_title, tv_new_price, tv_old_price, tv_current_goods, tv_comment_count, tv_good_comment;

    /** 当前商品详情数据页的索引分别是图文详情、规格参数 */
    private int nowIndex;
    private float fromX;
    public GoodsConfigFragment goodsConfigFragment;
    public GoodsInfoWebFragment goodsInfoWebFragment;
    private Fragment nowFragment;
    private List<TextView> tabTextList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    public GoodsInfoPage activity;
    private LayoutInflater inflater;

    public MyOkHttp mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
    private ProductDetail productDetail;
    private  String mess;
    private TextView tv_goods_guige;
    private TextView tv_shop_name;
    private TextView tv_goods_sales;
    private CheckBox check_you;
    private CheckBox check_time;

    public FragmentToActivity fragmentToActivity;
    private ProductDetailJson productDetailJson;
    private String username;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GoodsInfoPage) context;
        if (context instanceof FragmentToActivity) {//如果宿主Activity实现了该接口
            fragmentToActivity = (FragmentToActivity) context; //infoCallback 指向该Activity
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences =activity.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        //开始自动翻页
        vp_item_goods_img.startTurning(4000);
        Bundle bundle =this.getArguments();//得到从Activity传来的数据
        mess = null;
        if(bundle!=null){
            productDetailJson = (ProductDetailJson) bundle.getSerializable("detail");
            mess = bundle.getString("data");
            //请求商品详情数据
            //请求推荐商品
            String jsonstyle = JSON.toJSONString(productDetailJson);
            Log.d("detail","jsonstyle=="+jsonstyle);
            mMyOkhttp.post()
                    .addHeader("cookie",username)
                    .url(Constant.SFSHURL+Constant.GoodsDetail)
                     .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                    .tag(this)
                    .enqueue(new JsonResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                            Data<ProductDetail> obj = (Data<ProductDetail>) JSON.parseObject(response.toString(), new TypeReference<Data<ProductDetail>>(){});
                            int  resCodes = obj.getCode();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(resCodes== Constant.SUCCESSCODE) {
                                        productDetail = obj.getData();
                                        if(productDetail!=null) {
                                            tv_goods_title.setText(productDetail.getName());
                                            tv_goods_guige.setText("规格:"+productDetail.getSpec());
                                            tv_goods_detail.setText(productDetail.getDescription());
                                            tv_new_price.setText(productDetail.getPrice()+"元");
                                            tv_old_price.setText(productDetail.getMarketPrice());
                                            tv_shop_name.setText(productDetail.getSupplierName());
                                            tv_goods_sales.setText("已售:" + productDetail.getSales());
                                            check_you.setText(productDetail.getMailingDescription());
                                            check_time.setText(productDetail.getDistributionDescription());

                                            fragmentToActivity.onProductInfo(productDetail);
                                        }
                                    }
                                }
                            });
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
    }



    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        vp_item_goods_img.stopTurning();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_goods_info, null);
        initView(rootView);
        initListener();
        initData();
        return rootView;
    }

    private void initListener() {
        fab_up_slide.setOnClickListener(this);
        ll_activity.setOnClickListener(this);
        ll_comment.setOnClickListener(this);
        ll_pull_up.setOnClickListener(this);
        ll_goods_detail.setOnClickListener(this);
        sv_switch.setOnSlideDetailsListener(this);
    }

    private void initView(View rootView) {
        psts_tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.psts_tabs);
        fab_up_slide = (FloatingActionButton) rootView.findViewById(R.id.fab_up_slide);
        sv_switch = (SlideDetailsLayout) rootView.findViewById(R.id.sv_switch);
        sv_goods_info = (ScrollView) rootView.findViewById(R.id.sv_goods_info);
        vp_item_goods_img = (ConvenientBanner) rootView.findViewById(R.id.vp_item_goods_img);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);
        ll_activity = (LinearLayout) rootView.findViewById(R.id.ll_activity);
        ll_comment = (LinearLayout) rootView.findViewById(R.id.ll_comment);
        ll_pull_up = (LinearLayout) rootView.findViewById(R.id.ll_pull_up);
        ll_goods_detail = (LinearLayout) rootView.findViewById(R.id.ll_goods_detail);
        tv_goods_detail = (TextView) rootView.findViewById(R.id.tv_goods_detail);
        tv_goods_title = (TextView) rootView.findViewById(R.id.tv_goods_title);
        tv_new_price = (TextView) rootView.findViewById(R.id.tv_new_price);
        tv_old_price = (TextView) rootView.findViewById(R.id.tv_old_price);
        tv_goods_guige = (TextView) rootView.findViewById(R.id.tv_goods_guige);
        tv_shop_name = (TextView) rootView.findViewById(R.id.tv_shop_name);
        tv_goods_sales = (TextView) rootView.findViewById(R.id.tv_goods_sales);
        check_you = (CheckBox) rootView.findViewById(R.id.check_you);
        check_time = (CheckBox) rootView.findViewById(R.id.check_time);

        setDetailData();
        setLoopView();
        setRecommendGoods();

        //设置文字中间一条横线
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        fab_up_slide.hide();

        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        vp_item_goods_img.setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red});
        vp_item_goods_img.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        tabTextList = new ArrayList<>();
        tabTextList.add(tv_goods_detail);
    }

    /**
     * 加载完商品详情执行
     */
    public void setDetailData() {
        goodsConfigFragment = new GoodsConfigFragment();
        goodsInfoWebFragment = new GoodsInfoWebFragment();
        fragmentList.add(goodsConfigFragment);
        fragmentList.add(goodsInfoWebFragment);

        nowFragment = goodsInfoWebFragment;
        fragmentManager = getChildFragmentManager();
        //默认显示商品详情tab
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();
    }

    /**
     * 设置推荐商品
     */
    public void setRecommendGoods() {
        List<RecommendGoodsBean> data = new ArrayList<>();
        data.add(new RecommendGoodsBean("Letv/乐视 LETV体感-超级枪王 乐视TV超级电视产品玩具 体感游戏枪 电玩道具 黑色",
                "http://img4.hqbcdn.com/product/79/f3/79f3ef1b0b2283def1f01e12f21606d4.jpg", new BigDecimal(599), "799"));
        data.add(new RecommendGoodsBean("IPEGA/艾派格 幽灵之子 无线蓝牙游戏枪 游戏体感枪 苹果安卓智能游戏手柄 标配",
                "http://img2.hqbcdn.com/product/00/76/0076cedb0a7d728ec1c8ec149cff0d16.jpg", new BigDecimal(299), "399"));
        data.add(new RecommendGoodsBean("Letv/乐视 LETV体感-超级枪王 乐视TV超级电视产品玩具 体感游戏枪 电玩道具 黑色",
                "http://img4.hqbcdn.com/product/79/f3/79f3ef1b0b2283def1f01e12f21606d4.jpg", new BigDecimal(599), "799"));
        data.add(new RecommendGoodsBean("IPEGA/艾派格 幽灵之子 无线蓝牙游戏枪 游戏体感枪 苹果安卓智能游戏手柄 标配",
                "http://img2.hqbcdn.com/product/00/76/0076cedb0a7d728ec1c8ec149cff0d16.jpg", new BigDecimal(299), "399"));
        List<List<RecommendGoodsBean>> handledData = handleRecommendGoods(data);
        //设置如果只有一组数据时不能滑动
        //  vp_recommend.setManualPageable(handledData.size() == 1 ? false : true);
        //  vp_recommend.setCanLoop(handledData.size() == 1 ? false : true);
      /*  vp_recommend.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ItemRecommendAdapter();
            }
        }, handledData);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pull_up:
                //上拉查看图文详情
                sv_switch.smoothOpen(true);
                break;

            case R.id.fab_up_slide:
                //点击滑动到顶部
                sv_goods_info.smoothScrollTo(0, 0);
                sv_switch.smoothClose(true);
                break;

            case R.id.ll_goods_detail:
                //商品详情tab
                nowIndex = 0;
                scrollCursor();
                switchFragment(nowFragment, goodsInfoWebFragment);
                nowFragment = goodsInfoWebFragment;
                break;

           /* case R.id.ll_goods_config:
                //规格参数tab
                nowIndex = 1;
                scrollCursor();
                switchFragment(nowFragment, goodsConfigFragment);
                nowFragment = goodsConfigFragment;
                break;*/

            default:
                break;
        }
    }

    /**
     * 给商品轮播图设置图片路径
     */
    public void setLoopView() {
        List<String> imgUrls = new ArrayList<>();
        imgUrls.add("https://img14.360buyimg.com/n4/jfs/t21793/211/610710429/182079/477ff354/5b431a5cNd434d35f.jpg");
        imgUrls.add("https://img13.360buyimg.com/n9/s40x40_jfs/t23098/144/2026213315/556885/a886ba38/5b70f2faN779f93fb.jpg");
        imgUrls.add("https://img13.360buyimg.com/n9/s40x40_jfs/t5719/115/7116346600/113182/33686e76/596ea898N45e022d0.jpg");
        imgUrls.add("https://img13.360buyimg.com/n9/s40x40_jfs/t23098/144/2026213315/556885/a886ba38/5b70f2faN779f93fb.jpg");
        imgUrls.add("https://img10.360buyimg.com/n9/s40x40_jfs/t1/158/11/3873/488360/5b99f390Ec549eb1b/ec37a1bf42b8c360.jpg");
        //初始化商品图片轮播
        vp_item_goods_img.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgUrls);
    }

    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
        if (status == SlideDetailsLayout.Status.OPEN) {
            //当前为图文详情页
            fab_up_slide.show();
            activity.vp_content.setNoScroll(true);
            activity.tv_title.setVisibility(View.VISIBLE);
            activity.psts_tabs.setVisibility(View.GONE);
        } else {
            //当前为商品详情页
            fab_up_slide.hide();
            activity.vp_content.setNoScroll(false);
            activity.tv_title.setVisibility(View.GONE);
            activity.psts_tabs.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 滑动游标
     */
    private void scrollCursor() {

    }

    /**
     * 切换Fragment
     * <p>(hide、show、add)
     * @param fromFragment
     * @param toFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if (nowFragment != toFragment) {
            fragmentTransaction = fragmentManager.beginTransaction();
            if (!toFragment.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fromFragment).add(R.id.fl_content, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到activity中
            } else {
                fragmentTransaction.hide(fromFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * 处理推荐商品数据(每两个分为一组)
     *
     * @param data
     * @return
     */
    public static List<List<RecommendGoodsBean>> handleRecommendGoods(List<RecommendGoodsBean> data) {
        List<List<RecommendGoodsBean>> handleData = new ArrayList<>();
        int length = data.size() / 2;
        if (data.size() % 2 != 0) {
            length = data.size() / 2 + 1;
        }
        for (int i = 0; i < length; i++) {
            List<RecommendGoodsBean> recommendGoods = new ArrayList<>();
            for (int j = 0; j < (i * 2 + j == data.size() ? 1 : 2); j++) {
                recommendGoods.add(data.get(i * 2 + j));
            }
            handleData.add(recommendGoods);
        }
        return handleData;
    }
}
