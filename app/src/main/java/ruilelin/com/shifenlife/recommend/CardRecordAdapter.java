package ruilelin.com.shifenlife.recommend;

import android.content.Context;
import android.view.ViewGroup;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import ruilelin.com.shifenlife.recommend.bean.Consumption;

public class CardRecordAdapter extends RecyclerAdapter<Consumption> {

    public CardRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<Consumption> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new CardRecordHolder(parent);
    }
}