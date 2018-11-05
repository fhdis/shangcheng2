package ruilelin.com.shifenlife.typetwoway;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseRecyclerViewAdapter;
import ruilelin.com.shifenlife.jsonmodel.ChannelModel;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;


public class TypeAdapter extends BaseRecyclerViewAdapter<ChannelModel> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected OnClickRecyclerViewListener mOnRecyclerViewListener;
    private int pressPosition;


    public TypeAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public int getPressPosition() {
        return pressPosition;
    }

    public void setPressPosition(int pressPosition) {
        this.pressPosition = pressPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_type, null);
        return new TypeAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == getPressPosition()){
            ((TypeAdapterHolder) holder).linear_layout.setBackground(mContext.getResources().getDrawable(R.mipmap.background_press));
        }else{
            ((TypeAdapterHolder) holder).linear_layout.setBackgroundColor(mContext.getResources().getColor(R.color.type));
        }
        ((TypeAdapterHolder) holder).bindView(mDataList.get(position));
    }

    /**
     * RecyclerView不提供点击事件，自定义点击事件
     */
    public void setOnRecyclerViewListener(OnClickRecyclerViewListener onRecyclerViewListener) {
        mOnRecyclerViewListener = onRecyclerViewListener;
    }



    class TypeAdapterHolder extends BaseRvHolder implements View.OnClickListener {
        TextView tv_tyepe_name;
        LinearLayout linear_layout;
        TypeAdapterHolder(View itemView) {
            super(itemView);
            linear_layout = (LinearLayout)itemView.findViewById(R.id.linear_layout);
            tv_tyepe_name = (TextView)itemView.findViewById(R.id.tv_tyepe_name);
        }

        @Override
        protected void bindView(ChannelModel channelModel) {
            tv_tyepe_name.setText(channelModel.getName());
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemClick(getLayoutPosition(), v);
            }
        }

    }
}


