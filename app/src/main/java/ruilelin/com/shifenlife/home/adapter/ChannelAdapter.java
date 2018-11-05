package ruilelin.com.shifenlife.home.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.jsonmodel.ChannelModel;


public class ChannelAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    List<ChannelModel> temp;
    public static final int []RES = new int[]{R.mipmap.home_cate_fruit,R.mipmap.home_cate_vegt,R.mipmap.home_cate_meat,R.mipmap.home_cate_fish,R.mipmap.home_cate_cooked,R.mipmap.home_cate_been,R.mipmap.home_cate_rice,R.mipmap.homecatemore};
    public static final int []RESName = new int[]{R.mipmap.name1,R.mipmap.name2,R.mipmap.name3,R.mipmap.name4,R.mipmap.name5,R.mipmap.name6,R.mipmap.name7,R.mipmap.name8};
    public List<String> namelist =  new ArrayList<>();

    public ChannelAdapter(Context mContext,List<ChannelModel> temp1) {
        this.mContext = mContext;
        temp = temp1;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return temp.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            convertView = mLayoutInflater.inflate(R.layout.item_channel, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_channel);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_channel);
            convertView.setTag(holder);
        } else {
            //有缓存
            holder = (ViewHolder) convertView.getTag();
        }

        //绑定viewholder数据
        //holder.tv.setText("");
        //Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),RESName[position]);
        //holder.tv.setBackgroundDrawable(new BitmapDrawable(bitmap));
        if(temp.size()>0 && temp.size()>position) {
            Glide.with(mContext).load(RES[position]).into(holder.iv);
            if(position==(temp.size()-1)){
                Glide.with(mContext).load(RES[7]).into(holder.iv);
            }
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/ziti.ttf");
            holder.tv.setTypeface(tf);
            holder.tv.setText(temp.get(position).getName());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }


}







