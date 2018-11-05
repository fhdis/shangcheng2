package ruilelin.com.shifenlife.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import java.io.Serializable;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.GoodsInfoPage;
import ruilelin.com.shifenlife.home.recyclerview.SpacesItemDecoration;
import ruilelin.com.shifenlife.jsonmodel.RecommendModel;

public class RecommendAdapter extends RecyclerView.Adapter{

    private  List<RecommendModel> datas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public RecommendAdapter( Context mContext,List<RecommendModel> data) {
        this.datas = data;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SeckillViewHolder(mLayoutInflater.inflate(R.layout.recommend_layout, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SeckillViewHolder seckillViewHolder = (SeckillViewHolder) holder;
        seckillViewHolder.setData(datas);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class SeckillViewHolder extends RecyclerView.ViewHolder {
        List<RecommendModel>  seckill_infos;
        //private final TextView mTvMore;
        //private final TextView mTime;
        private final RecyclerView mRv;

        public SeckillViewHolder(View itemView) {
            super(itemView);
            //mTvMore = (TextView) itemView.findViewById(R.id.tv_more_seckill);
           // mTime = (TextView) itemView.findViewById(R.id.tv_time_seckill);
            mRv = (RecyclerView) itemView.findViewById(R.id.rv_recommend);
        }

        public void setData( List<RecommendModel> seckill_info) {
            seckill_infos = seckill_info;

            //recycleView布局管理器 参数三：是否倒序
            int spacingInPixels = mContext.getResources().getDimensionPixelSize(R.dimen.spacing);
            mRv.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
            mRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            //recycleVIew适配器
            SeckillRecyclerViewAdapter adapter = new SeckillRecyclerViewAdapter(mContext, seckill_info);
            mRv.setAdapter(adapter);

            adapter.setOnItemClickListener(new SeckillRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemCLick(int position) {
                    Log.d("re","position=="+position);
                    Intent intent = new Intent(mContext, GoodsInfoPage.class);
                    intent.putExtra("goodsid", (Serializable) seckill_info.get(position));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
