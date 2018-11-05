package ruilelin.com.shifenlife.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gxz.PagerSlidingTabStrip;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import me.curzbin.library.BottomDialog;
import me.curzbin.library.Item;
import me.curzbin.library.OnItemClickListener;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.cart.bean.ShoppingCarDataBean;
import ruilelin.com.shifenlife.goodsinfopage.adapter.ItemTitlePagerAdapter;
import ruilelin.com.shifenlife.goodsinfopage.fragment.GoodsInfoFragment;
import ruilelin.com.shifenlife.goodsinfopage.widget.NoScrollViewPager;
import ruilelin.com.shifenlife.json.AddToCart;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.ProductDetail;
import ruilelin.com.shifenlife.json.ProductDetailJson;
import ruilelin.com.shifenlife.jsonmodel.RecommendModel;
import ruilelin.com.shifenlife.listener.FragmentToActivity;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.utils.Util;

public class GoodsInfoPage extends BaseActivity implements FragmentToActivity {

    public PagerSlidingTabStrip psts_tabs;
    public NoScrollViewPager vp_content;
    public TextView tv_title;
    public Button bt_share;
    public Button bt_add_cart;
    public Button bt_buy_now;

    private List<Fragment> fragmentList = new ArrayList<>();
    private GoodsInfoFragment goodsInfoFragment;
    private RecommendModel recommendModel;
    private ProductDetailJson productDetailJson;
    private TextView tv_guige;
    private TextView tv_price;
    private TextView tv_origin_price;

    private String username;
    private static final int THUMB_SIZE = 150;
    private IWXAPI api;

    private ImageView iv_back;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_goods_info_page;
    }

    @Override
    protected void initData() {
        api = WXAPIFactory.createWXAPI(this, "wxb090b32121969d14");
        recommendModel = (RecommendModel)getIntent().getSerializableExtra("goodsid");
        productDetailJson = (ProductDetailJson)getIntent().getSerializableExtra("productdetail");
        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomDialog(GoodsInfoPage.this)
                        .title(R.string.share_title)
                        .orientation(BottomDialog.HORIZONTAL)
                        .inflateMenu(R.menu.menu_share, new OnItemClickListener() {
                            @Override
                            public void click(Item item) {
                                if("微信好友".equals(item.getTitle())){
                                    WXWebpageObject webpage = new WXWebpageObject();
                                    webpage.webpageUrl = "http://www.qq.com";
                                    WXMediaMessage msg = new WXMediaMessage(webpage);
                                    msg.title = "WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                                    msg.description = "WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.menu_cyc);
                                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                    bmp.recycle();
                                    msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                                    req.transaction = buildTransaction("webpage");
                                    req.message = msg;
                                    req.scene = 0;
                                    api.sendReq(req);
                                    finish();
                                }

                                if("朋友圈".equals(item.getTitle())){
                                    WXWebpageObject webpage = new WXWebpageObject();
                                    webpage.webpageUrl = "http://www.qq.com";
                                    WXMediaMessage msg = new WXMediaMessage(webpage);
                                    msg.title = "WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                                    msg.description = "WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.menu_cyc);
                                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                    bmp.recycle();
                                    msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                                    req.transaction = buildTransaction("webpage");
                                    req.message = msg;
                                    req.scene = 1;
                                    api.sendReq(req);
                                    finish();
                                }

                                Toast.makeText(GoodsInfoPage.this, getString(R.string.share_title) + item.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences =getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        recommendModel = (RecommendModel)getIntent().getSerializableExtra("goodsid");
        productDetailJson = (ProductDetailJson)getIntent().getSerializableExtra("productdetail");
        psts_tabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
        vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
        bt_share = (Button) findViewById(R.id.bt_share);
        bt_add_cart = (Button) findViewById(R.id.bt_add_cart);
        bt_buy_now = (Button) findViewById(R.id.bt_buy_now);

        tv_guige = (TextView) findViewById(R.id.tv_guige);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_origin_price = (TextView) findViewById(R.id.tv_origin_price);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        if(productDetailJson!=null) {
            goodsInfoFragment = new GoodsInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("detail",productDetailJson);
            goodsInfoFragment.setArguments(bundle);
            fragmentList.add(goodsInfoFragment);
            vp_content.setAdapter(new ItemTitlePagerAdapter(getSupportFragmentManager(), fragmentList, new String[]{"商品"}));
            vp_content.setOffscreenPageLimit(3);
            psts_tabs.setViewPager(vp_content);
        }

    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onProductInfo(ProductDetail productDetail) {

        if(productDetail!=null){
            tv_guige.setText(productDetail.getName()  +   productDetail.getSpec());
            tv_price.setText("¥"+productDetail.getPrice()+"元" );
            tv_origin_price.setText("¥"+productDetail.getMarketPrice()+"元");
            tv_origin_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

            bt_buy_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<ShoppingCarDataBean.DatasBean.Commodity> gotopay = new ArrayList<>();
                    ShoppingCarDataBean.DatasBean.Commodity commodity = new ShoppingCarDataBean.DatasBean.Commodity(productDetail.getId(),productDetail.getName(),"1",productDetail.isPiece(),productDetail.getSpec(),productDetail.getMarketPrice(),productDetail.getPrice(),productDetail.getThumbnail(),productDetail.getCreateTime(),productDetail.getSupplierId(),true,productDetail.getSupplierName());
                    gotopay.add(commodity);
                    Intent intent = new Intent(GoodsInfoPage.this, CreateOrderActivity.class);
                    intent.putExtra("goods", (Serializable) gotopay);
                    startActivity(intent);
                }
            });

            bt_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddToCart addToCart = new AddToCart(Integer.parseInt(productDetail.getId()),1);
                    String jsonstyle = JSON.toJSONString(addToCart);
                    mMyOkhttp.post()
                            .addHeader("cookie",username)
                            .url(Constant.SFSHURL+Constant.AddToCart)
                            .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                            .tag(this)
                            .enqueue(new JsonResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>(){});
                                    int  resCode = obj.getCode();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(resCode ==Constant.SUCCESSCODE){
                                                Toast.makeText(GoodsInfoPage.this,"成功添加到购物车",Toast.LENGTH_LONG).show();
                                            }else if(resCode == Constant.NOTlOG || resCode ==Constant.FAIL ){
                                                Toast.makeText(GoodsInfoPage.this,"添加购物车失败",Toast.LENGTH_LONG).show();
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
                                        runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GoodsInfoPage.this,"添加购物车失败",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });

                }
            });
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
