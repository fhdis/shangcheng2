package ruilelin.com.shifenlife.person.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseRecyclerViewAdapter;
import ruilelin.com.shifenlife.model.RecharRecordModel;
import ruilelin.com.shifenlife.utils.TimeUtils;

public class RechargeRecordAdapter extends BaseRecyclerViewAdapter<RecharRecordModel> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("M");
    private Map<String, String> cacheMonth = new HashMap<>();

    public RechargeRecordAdapter(Context context) {
        Log.d("adapter", "1");
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("adapter", "2");
        View view = mLayoutInflater.inflate(R.layout.item_recharge_detail, null);
        return new RechargeRecordAdapter.RechargeRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("adapter", "3");
        ((RechargeRecordAdapter.RechargeRecordHolder) holder).bindView(mDataList.get(position));
    }

    class RechargeRecordHolder extends BaseRvHolder {

        TextView tv_month;
        TextView tv_time;
        TextView tv_payway;
        TextView tv_date;
        TextView tv_money;

        RechargeRecordHolder(View itemView) {
            super(itemView);
            Log.d("adapter", "4");
            tv_month = (TextView) itemView.findViewById(R.id.tv_month);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_payway = (TextView) itemView.findViewById(R.id.tv_payway);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
        }

        @Override
        protected void bindView(RecharRecordModel info) {

            /* Log.d("adapter","5");
            tv_time.setText(info.getCreateTime());
            Log.d("adapter","5=="+info.getCreateTime());
            tv_what.setText(info.getPayWay());
            Log.d("adapter","51=="+info.getCreateTime());
            tv_detail.setText("+"+info.getMoney());
            Log.d("adapter","52=="+info.getCreateTime());*/
            String today = sdf.format(new Date());//当前日期
            String createDay = sdf.format(info.getCreateTime());//充值明细创建时间
            String weekDay;
            if (today.equals(createDay)) {
                weekDay = "今天";
            } else {
                weekDay = TimeUtils.dayOfWeeks(info.getCreateTime());
            }

            String titleMonth = sdf2.format(info.getCreateTime());
            String payWay = info.getNote();
            int money = info.getMoney();
            if (cacheMonth.size() == 0) {
                Log.d("adapter", "5");
                //calendar.setTime(info.getCreateTime());
                tv_month.setText(titleMonth + "月");
                setData(createDay, weekDay, payWay, money);
                //缓存
                cacheMonth.put("month", titleMonth);
            } else {
                String month = cacheMonth.get("month");//从Map中获取数据
                if (month.equals(titleMonth)) {
                    setData(createDay, weekDay, payWay, money);
                    tv_month.setVisibility(View.GONE);
                } else {
                    setData(createDay, weekDay, payWay, money);
                    tv_month.setText(titleMonth + "月");
                    tv_month.setVisibility(View.VISIBLE);

                    //更新
                    cacheMonth.put("month", titleMonth);
                }
            }
        }

        private void setData(String createDay, String weekDay, String payWay, int money) {
            tv_time.setText(weekDay);
            tv_payway.setText(payWay);
            tv_date.setText(createDay);
            tv_money.setText("+" + money);
        }
    }
}

