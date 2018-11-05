package ruilelin.com.shifenlife.myorder.anotherway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;


public class ItemOrderAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<OrderGoodsItem> temp ;
    public List<String> namelist =  new ArrayList<>();

    public ItemOrderAdapter(Context context,List<OrderGoodsItem> temp1) {
        this.mContext = context;
        temp = temp1;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        namelist.add("新鲜水果");
        namelist.add("田园时蔬");
        namelist.add("禽蛋肉类");
        return namelist.size();
    }

    @Override
    public Object getItem(int i) {
        return temp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            //没缓存
            convertView = mLayoutInflater.inflate(R.layout.ordergoodsitem, null);
            holder = new ViewHolder();
            holder.tv_good_name = (TextView)convertView.findViewById(R.id.tv_good_name);
            holder.tv_good_size_color = (TextView)convertView.findViewById(R.id.tv_good_size_color);
            holder.tv_good_price = (TextView)convertView.findViewById(R.id.tv_good_price);
            holder.tv_good_count = (TextView)convertView.findViewById(R.id.tv_good_count);
            convertView.setTag(holder);
        }else {
            //有缓存
            holder = (ViewHolder) convertView.getTag();
        }
        if(temp.size()>0 && temp.size()>position) {
           holder.tv_good_name.setText(temp.get(position).getGoodsName());
           holder.tv_good_count.setText(temp.get(position).getGoodsNumber());
           holder.tv_good_price.setText(temp.get(position).getGoodsPrice());
           holder.tv_good_size_color.setText(temp.get(position).getSpec());
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_good_name;
        TextView tv_good_size_color;
        TextView tv_good_price;
        TextView tv_good_count;
    }
}
