package ruilelin.com.shifenlife.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseRecyclerViewAdapter;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewAddressListener;
import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;
import ruilelin.com.shifenlife.model.AddressModel;
import android.util.Log;

import java.util.List;

public class GetAddressAdapter extends BaseRecyclerViewAdapter<AddressModel> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected OnClickRecyclerViewListener mOnRecyclerViewListener;
    protected OnClickRecyclerViewAddressListener mOnClickRecyclerViewAddressListener;
    private boolean isHide;//隐藏
    private boolean isOpen;//展开

    public GetAddressAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_person_place_noimage, null);
        return new PersonPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PersonPlaceViewHolder) holder).bindView(mDataList.get(position));
        if(hideOrShowCallBack != null){
            ((PersonPlaceViewHolder) holder).ll_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == mDataList.size() - 1) {
                        if (isOpen) {
                            hideOrShowCallBack.hide();
                            return;
                        }
                        if (isHide) {
                            hideOrShowCallBack.open();
                            return;
                        }
                    }

                }
            });
        }
        if(mOnRecyclerViewListener!=null){
            ((PersonPlaceViewHolder) holder).ll_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnRecyclerViewListener.onItemClick(position, view);
                }
            });
        }
    }

    /**
     * RecyclerView不提供点击事件，自定义点击事件
     */
    public void setOnRecyclerViewListener(OnClickRecyclerViewListener onRecyclerViewListener) {
        Log.d("adapter1", "9");
        mOnRecyclerViewListener = onRecyclerViewListener;
    }

    public void setOnClickRecyclerViewAddressListener(OnClickRecyclerViewAddressListener onClickRecyclerViewAddressListener) {
        Log.d("adapter", "9");
        mOnClickRecyclerViewAddressListener = onClickRecyclerViewAddressListener;
    }

    class PersonPlaceViewHolder extends BaseRvHolder implements View.OnClickListener {

        TextView mTvUserPlace;
        TextView mTvUserName;
        TextView mTvUserSex;
        TextView mTvUserContract;
        //ImageView iv_edit;
        LinearLayout ll_address;
        TextView tv_user_place_default;

        PersonPlaceViewHolder(View itemView) {
            super(itemView);
            mTvUserPlace = (TextView) itemView.findViewById(R.id.tv_user_place);
            tv_user_place_default = (TextView) itemView.findViewById(R.id.tv_user_place_default);
            mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            mTvUserSex = (TextView) itemView.findViewById(R.id.tv_user_sex);
            mTvUserContract = (TextView) itemView.findViewById(R.id.tv_user_contract);
           // iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            ll_address = (LinearLayout) itemView.findViewById(R.id.ll_address);
            //iv_edit.setOnClickListener(this);
            ll_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("1102","ll_address "+(mOnRecyclerViewListener != null));
                    if (mOnRecyclerViewListener != null) {
                        mOnRecyclerViewListener.onItemClick(getLayoutPosition(), view);
                    }
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
            Log.d("1102","mOnRecyclerViewListener != null"+(mOnRecyclerViewListener != null));
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

    //展开
    public void setOpenList(List<AddressModel> openList) {
        this.mDataList = openList;
        this.isOpen = true;
        this.isHide = false;
        notifyDataSetChanged();
    }

    //不需要隐藏/展开
    public void setRealList(List<AddressModel> realList) {
        this.mDataList = realList;
        notifyDataSetChanged();
        this.isHide = false;
        this.isOpen = false;
    }
    //隐藏
    public void setHideList(List<AddressModel> newList) {
        this.mDataList = newList;
        notifyDataSetChanged();
        this.isHide = true;
        this.isOpen = false;
    }

    //清除数据
    public void clearData() {
        if (mDataList != null) {
            this.mDataList.clear();
            notifyDataSetChanged();
        }
    }


    private HideOrShowCallBack hideOrShowCallBack;

    public void setHideOrShowCallBack(HideOrShowCallBack hideOrShowCallBack) {
        this.hideOrShowCallBack = hideOrShowCallBack;
    }

    public interface HideOrShowCallBack {
        void hide();

        void open();
    }
}

