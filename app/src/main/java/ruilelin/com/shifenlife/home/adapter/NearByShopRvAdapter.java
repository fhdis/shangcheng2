package ruilelin.com.shifenlife.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseRecyclerViewAdapter;
import ruilelin.com.shifenlife.json.NearbyShop;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewAddressListener;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;

public class NearByShopRvAdapter extends BaseRecyclerViewAdapter<NearbyShop> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected OnClickRecyclerViewListener mOnRecyclerViewListener;
    protected OnClickRecyclerViewAddressListener mOnClickRecyclerViewAddressListener;

    public NearByShopRvAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_info, null);
        return new NearByShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("adapter","mDataList.size=="+mDataList.size());
        ((NearByShopViewHolder) holder).bindView(mDataList.get(position));
    }

    /**
     * RecyclerView不提供点击事件，自定义点击事件
     */
    public void setOnRecyclerViewListener(OnClickRecyclerViewListener onRecyclerViewListener) {
        Log.d("adapter","9");
        mOnRecyclerViewListener = onRecyclerViewListener;
    }
    public void setOnClickRecyclerViewAddressListener(OnClickRecyclerViewAddressListener onClickRecyclerViewAddressListener) {
        Log.d("adapter","9");
        mOnClickRecyclerViewAddressListener =onClickRecyclerViewAddressListener;
    }

    class NearByShopViewHolder extends BaseRvHolder implements View.OnClickListener{
        TextView tv_info;
        TextView tv_time;
        TextView tv_info_detail;
        NearByShopViewHolder(View itemView) {
            super(itemView);
            tv_info = (TextView)itemView.findViewById(R.id.tv_info);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            tv_info_detail = (TextView)itemView.findViewById(R.id.tv_info_detail);
        }

        @Override
        protected void bindView(NearbyShop nearbyShop) {
            tv_info.setCompoundDrawables(null, null, null, null);
            tv_info.setText(nearbyShop.getName());
            tv_time.setText(nearbyShop.getDistance()+"米");
            tv_info_detail.setText(nearbyShop.getAddress());
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}
