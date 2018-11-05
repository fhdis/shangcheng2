package ruilelin.com.shifenlife.cart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseRecyclerViewAdapter;
import ruilelin.com.shifenlife.cart.bean.ShoppingCarDataBean;

public class CreateOrderRvAdapter extends BaseRecyclerViewAdapter<ShoppingCarDataBean.DatasBean.Commodity> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public CreateOrderRvAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("test", "onCreateViewHolder");
        View view = mLayoutInflater.inflate(R.layout.item_order_product, null);
        return new CreateOrderRvAdapter.GoodInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("test", "onBindViewHolder");
        ((CreateOrderRvAdapter.GoodInfoViewHolder) holder).bindView(mDataList.get(position));
    }

    class GoodInfoViewHolder extends BaseRvHolder {
        ImageView iv_item_allorder_pic;
        TextView tv_item_allorder_title;
        TextView tv_item_allorder_item_price;
        TextView tv_item_allorder_item_num;
        TextView tv_good_size_color;

        GoodInfoViewHolder(View itemView) {
            super(itemView);
            Log.i("test", "GoodInfoViewHolder");
            tv_item_allorder_title = (TextView)itemView.findViewById(R.id.tv_good_name);
            tv_item_allorder_item_price = (TextView)itemView.findViewById(R.id.tv_good_price);
            tv_item_allorder_item_num = (TextView)itemView.findViewById(R.id.tv_good_count);
            iv_item_allorder_pic = (ImageView)itemView.findViewById(R.id.iv_good_image);
            tv_good_size_color = (TextView)itemView.findViewById(R.id.tv_good_size_color);
        }

        @Override
        protected void bindView(ShoppingCarDataBean.DatasBean.Commodity info) {
            tv_item_allorder_title.setText(info.getName());
            tv_item_allorder_item_price.setText("¥"+mul(info.getPrice(),info.getNumber(),1));
            Log.d("pay","price=="+info.getPrice());
            tv_item_allorder_item_num.setText("共"+info.getNumber()+"件 ");
            Log.d("pay","number=="+info.getNumber());
            Glide.with(mContext).load(info.getThumbnail()).into(iv_item_allorder_pic);
            tv_good_size_color.setText(info.getSpec());
        }
    }

    //提供精确的乘法运算
    public static String mul(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留的小数位数必须大于零");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
}
