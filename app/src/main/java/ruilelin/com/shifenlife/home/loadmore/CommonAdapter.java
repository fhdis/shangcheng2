package ruilelin.com.shifenlife.home.loadmore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {


    private List<T> dataList;
    private int itemLayoutId = 0;

    public CommonAdapter(@NonNull List<T> dataList, int itemLayoutId) {
        this.dataList = dataList;
        this.itemLayoutId = itemLayoutId;
    }
    //更新数据
    public void updateData(List dataList) {
        Log.d("adapter","6");
        dataList.clear();
        appendData(dataList);
    }

    //分页加载，追加数据
    public void appendData(List dataList) {
        if (null != dataList && !dataList.isEmpty()) {
            Log.d("adapter","7");
            dataList.addAll(dataList);
            notifyDataSetChanged();
        } else if (dataList != null && dataList.isEmpty()) {
            notifyDataSetChanged();
            //空数据更新
        }
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("loadmore", "onCreateViewHolder");
        BaseViewHolder holder = new BaseViewHolder(getLayoutView(parent, itemLayoutId));
        setListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Log.d("loadmore", "holder" + holder);
        Log.d("loadmore", "dataList.get(position)" + dataList.get(position));
        Log.d("loadmore", "holder.getAdapterPosition()" + holder.getAdapterPosition());
        convert(holder, dataList.get(position), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        Log.d("loadmore", "dataList.size()==" + dataList.size());
        return dataList.size();
    }

    public View getLayoutView(ViewGroup parent, int layoutId) {
        Log.d("loadmore", "getLayoutView==");
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    /**
     * 设置
     *
     * @param viewHolder
     */
    private void setListener(final BaseViewHolder viewHolder) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    onItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = viewHolder.getAdapterPosition();
                onItemLongPressListener.onItemLongPress(position, v);
                return true;
            }
        });
    }


    public abstract void convert(BaseViewHolder holder, T t, int position);


    private OnItemClickListener onItemClickListener;
    private OnItemLongPressListener onItemLongPressListener;

    public interface OnItemClickListener {
        void onItemClick(View view, BaseViewHolder holder, int position);
    }

    public interface OnItemLongPressListener {
        void onItemLongPress(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongPressListener(OnItemLongPressListener onItemLongPressListener) {
        this.onItemLongPressListener = onItemLongPressListener;
    }
}
