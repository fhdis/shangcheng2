package ruilelin.com.shifenlife.person.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.BaseViewHolder> {
    private ArrayList<ItemModel> dataList = new ArrayList<>();
    private int lastPressIndex = -1;

    public void replaceAll(ArrayList<ItemModel> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }


    @Override
    public DemoAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.ONE:
                return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_one, parent, false));
          /*  case ItemModel.TWO:
                return new TWoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.two, parent, false));*/
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DemoAdapter.BaseViewHolder holder, int position) {

        holder.setData(dataList.get(position).data);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        void setData(Object data) {
        }

        @Override
        public void onClick(View view) {
            Log.e("TAG", "OneViewHolder111: ");
            if (clickListener != null) {
                Log.e("TAG", "OneViewHolder222: ");
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }

    }

    private class OneViewHolder extends BaseViewHolder {
        private TextView tv;

        public OneViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG", "OneViewHolder: ");
                    int position = getAdapterPosition();
                    if (lastPressIndex == position) {
                        lastPressIndex = -1;
                    } else {
                        lastPressIndex = position;
                    }
                    Log.e("TAG", "clickListener != null" + (clickListener != null));
                    if (clickListener != null) {
                        Log.e("TAG", "OneViewHolder222: ");
                        clickListener.onClick(itemView, getAdapterPosition());
                    }
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onClick(View view) {
            Log.e("TAG", "OneViewHolder11111111: ");
            super.onClick(view);
        }

        @Override
        void setData(Object data) {
            if (data != null) {
                String text = (String) data;
                tv.setText(text);
                MyApplication instance = MyApplication.getInstance();
                if (getAdapterPosition() == lastPressIndex) {
                    tv.setSelected(true);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                    Drawable drawable = instance.getResources().getDrawable(R.drawable.round_corner_yellow_fill_charge);
                    tv.setBackground(drawable);

                } else {
                    tv.setSelected(false);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                    Drawable drawable = instance.getResources().getDrawable(R.drawable.round_corner_gray_fill_charge);
                    tv.setBackground(drawable);
                }
            }
        }
    }

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static interface OnItemClickListener {
        void onClick(View view, int position);
    }


}
