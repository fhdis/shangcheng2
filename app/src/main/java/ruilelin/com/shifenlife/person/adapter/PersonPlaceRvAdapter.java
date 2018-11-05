package ruilelin.com.shifenlife.person.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseRecyclerViewAdapter;
import ruilelin.com.shifenlife.home.loadmore.BaseViewHolder;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewAddressListener;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.listener.OnItemLongPressListener;
import ruilelin.com.shifenlife.model.AddressModel;


public class PersonPlaceRvAdapter extends BaseRecyclerViewAdapter<AddressModel> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected OnClickRecyclerViewListener mOnRecyclerViewListener;
    protected OnClickRecyclerViewAddressListener mOnClickRecyclerViewAddressListener;
    private int itemLayoutId;
    protected OnItemLongPressListener mOnItemLongPressListener;

    public PersonPlaceRvAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemLayoutId = R.layout.item_person_place;
        View view = mLayoutInflater.inflate(itemLayoutId, null);
        return new PersonPlaceViewHolder(view);
    }


    public View getLayoutView(ViewGroup parent, int layoutId) {
        Log.d("loadmore", "getLayoutView==");
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PersonPlaceViewHolder) holder).bindView(mDataList.get(position));
    }

    /**
     * RecyclerView不提供点击事件，自定义点击事件
     */
    public void setOnRecyclerViewListener(OnClickRecyclerViewListener onRecyclerViewListener) {
        Log.d("adapter", "9");
        mOnRecyclerViewListener = onRecyclerViewListener;
    }

    public void setOnClickRecyclerViewAddressListener(OnClickRecyclerViewAddressListener onClickRecyclerViewAddressListener) {
        Log.d("adapter", "9");
        mOnClickRecyclerViewAddressListener = onClickRecyclerViewAddressListener;
    }

    public void setOnItemLongPressListener(OnItemLongPressListener onItemLongPressListener) {
       mOnItemLongPressListener = onItemLongPressListener;
    }

    class PersonPlaceViewHolder extends BaseRvHolder implements View.OnClickListener {

        TextView mTvUserPlace;
        TextView mTvUserName;
        TextView mTvUserSex;
        TextView mTvUserContract;
        ImageView iv_edit;
        LinearLayout ll_address;
        TextView tv_user_place_default;

        PersonPlaceViewHolder(View itemView) {
            super(itemView);
            mTvUserPlace = (TextView) itemView.findViewById(R.id.tv_user_place);
            tv_user_place_default = (TextView) itemView.findViewById(R.id.tv_user_place_default);
            mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            mTvUserSex = (TextView) itemView.findViewById(R.id.tv_user_sex);
            mTvUserContract = (TextView) itemView.findViewById(R.id.tv_user_contract);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            ll_address = (LinearLayout) itemView.findViewById(R.id.ll_address);
            iv_edit.setOnClickListener(this);
            ll_address.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mOnClickRecyclerViewAddressListener != null) {
                        mOnClickRecyclerViewAddressListener.onItemAddressClick(getLayoutPosition(), view);
                    }
                }
            });

            ll_address.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mOnItemLongPressListener != null) {
                        mOnItemLongPressListener.onItemLongPress(getLayoutPosition(), view);
                    }
                    return true;
                }
            });
        }

        @Override
        protected void bindView(AddressModel address) {
            if (address.getDefaultAddress()) {
                mTvUserPlace.setText(address.getLooseAddress() + address.getAddress());
                //mTvUserPlace.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable( R.mipmap.checkbox_selected),null,null, null);
                tv_user_place_default.setVisibility(View.VISIBLE);
            } else {
                tv_user_place_default.setVisibility(View.GONE);
                mTvUserPlace.setText(address.getLooseAddress() + address.getAddress());
            }
            mTvUserName.setText(address.getConsignee());
            if ("BOY".equals(address.getSex())) {
                mTvUserSex.setText("先生");
            } else if ("GIRL".equals(address.getSex())) {
                mTvUserSex.setText("女士");
            }
            mTvUserContract.setText(address.getMobile());
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}

