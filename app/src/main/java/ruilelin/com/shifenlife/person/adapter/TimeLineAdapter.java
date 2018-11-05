package ruilelin.com.shifenlife.person.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.cart.customview.TimeLineMarker;
import ruilelin.com.shifenlife.model.TimeLineModel;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    private List<TimeLineModel> mDataSet = new ArrayList<>();
    private Context mContext;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public TimeLineAdapter(Context context) {
        mContext = context;
    }

    //更新数据
    public void updataData(List dataList) {
        Log.d("adapter", "6");
        //mDataSet.clear();
        appendData(dataList);
    }

    //更新数据
    public void clearData() {
        Log.d("adapter", "6");
        mDataSet.clear();
    }

    //分页加载，追加数据
    public void appendData(List dataList) {
        if (null != dataList && !dataList.isEmpty()) {
            Log.d("adapter", "7");
            mDataSet.addAll(dataList);
            notifyDataSetChanged();
        } else if (dataList != null && dataList.isEmpty()) {
            notifyDataSetChanged();
            //空数据更新
        }
    }

    @Override
    public int getItemViewType(int position) {
        final int size = mDataSet.size() - 1;
        if (size == 0)
            return ItemType.ATOM;
        else if (position == 0)
            return ItemType.START;
        else if (position == size)
            return ItemType.END;
        else return ItemType.NORMAL;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new TimeLineViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_time_line, viewGroup, false), viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder timeLineViewHolder, int i) {

        if (i == 0) {
            TimeLineMarker mMarker = (TimeLineMarker) timeLineViewHolder.itemView.findViewById(R.id.item_time_line_mark);
            mMarker.setBeginLine(null);
        }
        //timeLineViewHolder.mName.setText(mDataSet.get(i).getEvent());
        TimeLineModel timeLineModel = mDataSet.get(i);
        timeLineViewHolder.item_time_line.setText(sdf.format(timeLineModel.getCreateTime()));
        //timeLineViewHolder.tv_shopname.setText("百果园（花园浜店）");
        timeLineViewHolder.tv_shopname.setText(timeLineModel.getNote());
        //timeLineViewHolder.tv_order_num.setText("订单号123456");

        //timeLineViewHolder.tv_xiaofei_detail.setText("消费"+mDataSet.get(i).getMoney()+"元");
        //timeLineViewHolder.tv_yue.setText("余额：30元");
        String type = timeLineModel.getType();
        if ("IN".equals(type)) {
            timeLineViewHolder.tv_xiaofei_detail.setText("充值" + mDataSet.get(i).getMoney() + "元");
            timeLineViewHolder.tv_order_num.setText("充值卡号:" + timeLineModel.getSn());
        } else if ("OUT".equals(type)) {
            timeLineViewHolder.tv_xiaofei_detail.setText("消费" + mDataSet.get(i).getMoney() + "元");
            timeLineViewHolder.tv_order_num.setText("订单号:" + timeLineModel.getSn());
        }
        if(timeLineModel.getUserBalance()!=null){
            timeLineViewHolder.tv_yue.setText("余" + timeLineModel.getUserBalance().setScale(0) + "元");
        }else{
            timeLineViewHolder.tv_yue.setText("余"  + "0元");
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ItemType {
        public final static int NORMAL = 0;

        public final static int HEADER = 1;
        public final static int FOOTER = 2;

        public final static int START = 4;
        public final static int END = 8;
        public final static int ATOM = 16;
    }


    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        private TextView item_time_line;
        private TextView tv_shopname;
        private TextView tv_order_num;
        private TextView tv_xiaofei_detail;
        private TextView tv_yue;

        public TimeLineViewHolder(View itemView, int type) {
            super(itemView);

            item_time_line = (TextView) itemView.findViewById(R.id.item_time_line);
            tv_shopname = (TextView) itemView.findViewById(R.id.tv_shopname);
            tv_order_num = (TextView) itemView.findViewById(R.id.tv_order_num);
            tv_xiaofei_detail = (TextView) itemView.findViewById(R.id.tv_xiaofei_detail);
            tv_yue = (TextView) itemView.findViewById(R.id.tv_yue);

            TimeLineMarker mMarker = (TimeLineMarker) itemView.findViewById(R.id.item_time_line_mark);
            if (type == ItemType.ATOM) {
                mMarker.setBeginLine(null);
                mMarker.setEndLine(null);
            } else if (type == ItemType.START) {
                mMarker.setBeginLine(null);
            } else if (type == ItemType.END) {
                mMarker.setEndLine(null);
            }
        }
    }
}
