package ruilelin.com.shifenlife.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.json.AddToCart;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.HotProduct;
import ruilelin.com.shifenlife.utils.Constant;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;


public class HotAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<HotProduct> temp ;
    public List<String> namelist =  new ArrayList<>();
    public MyOkHttp mMyOkhttp;
    private String username;
    private ViewHolder holder = null;;

    public HotAdapter(Context context,List<HotProduct> temp1) {
        this.mContext = context;
        temp = temp1;
        mLayoutInflater = LayoutInflater.from(mContext);
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        SharedPreferences sharedPreferences =mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
    }

    @Override
    public int getCount() {
        namelist.add("新鲜水果");
        namelist.add("田园时蔬");
        namelist.add("禽蛋肉类");
        return temp.size();
    }

    @Override
    public Object getItem(int i) {
        return temp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            //没缓存
            convertView = mLayoutInflater.inflate(R.layout.item_shop_hot, null);
            holder = new ViewHolder();
            holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.tv_product_guige = (TextView) convertView.findViewById(R.id.tv_product_guige);
            holder.tv_product_dec = (TextView) convertView.findViewById(R.id.tv_product_dec);
            holder.tv_product_price = (TextView) convertView.findViewById(R.id.tv_product_price);
            holder.tv_product_origin_price = (TextView) convertView.findViewById(R.id.tv_product_origin_price);
            holder.tv_product_sale_num = (TextView) convertView.findViewById(R.id.tv_product_sale_num);
            holder.tv_shopname = (TextView) convertView.findViewById(R.id.tv_shopname);
            holder.image_product_pic = (ImageView) convertView.findViewById(R.id.image_product_pic);
            holder.imgae_cart = (ImageView) convertView.findViewById(R.id.imgae_cart);
            convertView.setTag(holder);
        }else {
            //有缓存
            holder = (ViewHolder) convertView.getTag();
        }


        //绑定viewholder数据
        if(temp.size()>0 && temp.size()>position) {
            holder.tv_product_name.setText(temp.get(position).getName());
            holder.tv_product_guige.setText("规格:"+temp.get(position).getSpec());
            holder.tv_product_dec.setText(temp.get(position).getDescription());
            holder.tv_product_price.setText("¥"+temp.get(position).getPrice()+"元");

            holder.tv_product_origin_price.setText("¥"+temp.get(position).getMarketPrice()+"元");
            holder.tv_product_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_product_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);


            holder.tv_product_sale_num.setText("销量:"+temp.get(position).getSales());
            holder.tv_shopname.setText(temp.get(position).getSupplierName());
            Glide.with(mContext).load(Constant.SFSHURL + temp.get(position).getImg()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    holder.image_product_pic.setBackground(mContext.getResources().getDrawable(R.mipmap.default_icon));
                    Toast.makeText(mContext," 商品图片加载失败",Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.image_product_pic);

            holder.imgae_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"加入购物车？",Toast.LENGTH_SHORT).show();
                    AddToCart addToCart = new AddToCart(temp.get(position).getId(),1);
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

                                        ((Activity)mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(resCode ==Constant.SUCCESSCODE){
                                                Toast.makeText(mContext,"成功添加到购物车",Toast.LENGTH_LONG).show();
                                                }else if(resCode ==Constant.NOTlOG || resCode ==Constant.FAIL ){
                                                    Toast.makeText(mContext,"添加购物车失败",Toast.LENGTH_LONG).show();
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
                                    ((Activity)mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                Toast.makeText(mContext,"添加购物车失败",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });

                }
            });

        }

        return convertView;
    }

    class ViewHolder {
      //  ImageView iv;
        TextView tv_product_name;
        ImageView image_product_pic;
        TextView tv_product_guige;
        TextView tv_product_dec;
        TextView tv_product_price;
        TextView tv_product_origin_price;
        TextView tv_product_sale_num;
        TextView tv_shopname;
        ImageView imgae_cart;
    }
}
