package ruilelin.com.shifenlife.typetwoway;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseRecyclerViewAdapter;
import ruilelin.com.shifenlife.json.HotProduct;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.utils.Constant;
import android.util.Log;

public class SecondTypeAdapter extends BaseRecyclerViewAdapter <HotProduct>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected OnClickRecyclerViewListener mOnRecyclerViewListener;


    public SecondTypeAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("size","mDataList.size()=="+mDataList.size());
        View view = mLayoutInflater.inflate(R.layout.item_classify_detail, null);
        return new TypeAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TypeAdapterHolder) holder).bindView(mDataList.get(position));

    }

    /**
     * RecyclerView不提供点击事件，自定义点击事件
     */
    public void setOnRecyclerViewListener(OnClickRecyclerViewListener onRecyclerViewListener) {
        mOnRecyclerViewListener = onRecyclerViewListener;
    }


    class EmptyDataHolder extends BaseRvHolder implements View.OnClickListener {
        public EmptyDataHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bindView(HotProduct hotProduct) {

        }
    }
    class TypeAdapterHolder extends BaseRvHolder implements View.OnClickListener {
        TextView tvCity;
        ImageView avatar;
        TypeAdapterHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
        }

        @Override
        protected void bindView(HotProduct hotProduct) {
            android.util.Log.d("name","hotProduct=="+Constant.SFSHURL + hotProduct.getImg());
            tvCity.setText(hotProduct.getName());
            Glide.with(mContext).load(Constant.SFSHURL + hotProduct.getThumbnail()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    avatar.setBackground(mContext.getResources().getDrawable(R.mipmap.cate_fruit));
                    Toast.makeText(mContext,"商品图片加载失败",Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(avatar);
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemClick(getLayoutPosition(), v);
            }
        }

    }
}

