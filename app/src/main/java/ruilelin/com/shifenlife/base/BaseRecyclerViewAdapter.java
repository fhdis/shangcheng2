package ruilelin.com.shifenlife.base;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ruilelin.com.shifenlife.listener.OnClickRecyclerViewListener;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mDataList = new ArrayList<>();
    protected OnClickRecyclerViewListener mOnRecyclerViewListener;


    public void setDataList(List<T>dataList){
        mDataList = dataList;
    }

    public void clearData() {
        Log.d("adapter", "6");
        mDataList.clear();
    }

    //更新数据
    public void updataData(List dataList) {
        Log.d("adapter","6");
        mDataList.clear();
        appendData(dataList);
    }

    //分页加载，追加数据
    public void appendData(List dataList) {
        if (null != dataList && !dataList.isEmpty()) {
            Log.d("adapter","7");
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        } else if (dataList != null && dataList.isEmpty()) {
            notifyDataSetChanged();
            //空数据更新
        }
    }


    @Override
    public int getItemCount() {
        Log.d("adapter","8");
        return mDataList.size();
    }

    /**
     * RecyclerView不提供点击事件，自定义点击事件
     */
    public void setOnRecyclerViewListener(OnClickRecyclerViewListener onRecyclerViewListener) {
        Log.d("adapter","9");
        mOnRecyclerViewListener = onRecyclerViewListener;
    }


    public abstract class BaseRvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public BaseRvHolder(View itemView) {
            super(itemView);
            // ButterKnife.bind(this, itemView);
            //ButterKnife.inject(itemView);
            Log.d("adapter","10");
            itemView.setOnClickListener(this);
        }

        protected abstract void bindView(T t);

        @Override
        public void onClick(View v) {
            if (mOnRecyclerViewListener != null) {
                mOnRecyclerViewListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

}

