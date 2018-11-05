package ruilelin.com.shifenlife.home.loadmore;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.List;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.json.Infomation;

import android.util.Log;
import android.widget.TextView;
public class ListAdapter extends CommonAdapter<Infomation> {


    private Context mContext;

    public ListAdapter(@NonNull List<Infomation> dataList) {
        // super(dataList, R.layout.item_list);
        super(dataList, R.layout.item_info);
    }

    public ListAdapter(Context context, List<Infomation> dataList) {
        super(dataList, R.layout.item_info);
        mContext = context;
    }

    @Override
    public void convert(BaseViewHolder holder, Infomation infomation, int position) {

        boolean readout = infomation.getReadout();//消息是否已读
        TextView tv_info = holder.convertView.findViewById(R.id.tv_info);
        TextView tv_time = holder.convertView.findViewById(R.id.tv_time);
        TextView tv_detail = holder.convertView.findViewById(R.id.tv_info_detail);
        Resources resources = mContext.getResources();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = sdf.format(infomation.getCreateTime());
        //设置数据
        tv_time.setText(format);
        tv_detail.setText(infomation.getMessage());

        if (readout) {
            //将图标修改为 "已读"
            Drawable drawableRead = resources.getDrawable(R.mipmap.icon_read);
            tv_info.setCompoundDrawablesWithIntrinsicBounds(drawableRead, null, null, null);

            //将文字修改为灰色
            tv_time.setTextColor(resources.getColor(android.R.color.darker_gray));
            tv_detail.setTextColor(resources.getColor(R.color.hint_color));
            tv_info.setTextColor(resources.getColor(R.color.hint_color));
        }else{
            //将图标修改为 "未读"
            Drawable drawableUnRead = resources.getDrawable(R.mipmap.icon_un_read);
            tv_info.setCompoundDrawablesWithIntrinsicBounds(drawableUnRead, null, null, null);

            //将文字修改为灰色
            tv_time.setTextColor(resources.getColor(android.R.color.darker_gray));
            tv_detail.setTextColor(resources.getColor(R.color.black));
            tv_info.setTextColor(resources.getColor(R.color.black));
        }
    }

}
