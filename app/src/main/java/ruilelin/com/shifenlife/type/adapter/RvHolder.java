package ruilelin.com.shifenlife.type.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import ruilelin.com.shifenlife.type.listener.RvListener;


public abstract class RvHolder<T> extends RecyclerView.ViewHolder {
    protected RvListener mListener;

    public RvHolder(View itemView, int type, RvListener listener) {
        super(itemView);
        this.mListener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v.getId(), getAdapterPosition());
            }
        });
    }

    public abstract void bindHolder(T t, int position);

}

