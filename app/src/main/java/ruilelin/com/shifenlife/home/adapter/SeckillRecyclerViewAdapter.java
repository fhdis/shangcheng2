package ruilelin.com.shifenlife.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.jsonmodel.RecommendModel;
import ruilelin.com.shifenlife.utils.Constant;
import android.util.Log;

public class SeckillRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final List<RecommendModel> seckill_info;
    public static final int []RES = new int[]{R.mipmap.temp_prodct,R.mipmap.temp_prodct,R.mipmap.temp_prodct,R.mipmap.temp_prodct,R.mipmap.temp_prodct,R.mipmap.temp_prodct};
    public SeckillRecyclerViewAdapter(Context context, List<RecommendModel> seckill_info) {
        this.mContext = context;
        this.seckill_info = seckill_info;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //绑定数据
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tvOriginPrice.setText("¥"+seckill_info.get(position).getMarketPrice());//+"/"+seckill_info.get(position).getSpec()//);
        ViewGroup.LayoutParams params = myViewHolder.tvOriginPrice.getLayoutParams();
        Log.d("height","myViewHolder.tvOriginPrice height=="+params.height);
        myViewHolder.tvCoverPrice.setText(seckill_info.get(position).getName());
        myViewHolder.tv_origin_price_new.setText(seckill_info.get(position).getSpec());
        ViewGroup.LayoutParams params1 = myViewHolder.tvCoverPrice.getLayoutParams();
        Log.d("height","myViewHolder.tvCoverPrice height=="+params1.height);
        Glide.with(mContext).load(Constant.SFSHURL+seckill_info.get(position).getThumbnail()).into(myViewHolder.ivFigure);
        ViewGroup.LayoutParams params2 = myViewHolder.ivFigure.getLayoutParams();
        Log.d("height","myViewHolder.ivFigure height=="+params2.height);

        //图片点击事件
        myViewHolder.ivFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //回调position给首页RecycleView
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemCLick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return seckill_info.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFigure;
        TextView tvCoverPrice;
        TextView tvOriginPrice;
        TextView tv_origin_price_new;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivFigure = (ImageView) itemView.findViewById(R.id.iv_figure);
            tvCoverPrice = (TextView) itemView.findViewById(R.id.tv_cover_price);
            tvOriginPrice = (TextView) itemView.findViewById(R.id.tv_origin_price);
            tv_origin_price_new = (TextView) itemView.findViewById(R.id.tv_origin_price_new);
        }
    }

    public interface OnItemClickListener {
        void onItemCLick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    //条目点击回调给首页SeckillViewHolder
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}


