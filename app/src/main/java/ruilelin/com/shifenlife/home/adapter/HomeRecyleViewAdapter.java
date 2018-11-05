package ruilelin.com.shifenlife.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.GoodsInfoPage;
import ruilelin.com.shifenlife.home.moremenu.SFSHCategory;
import ruilelin.com.shifenlife.home.recyclerview.SpacesItemDecoration;
import ruilelin.com.shifenlife.json.BannerJson;
import ruilelin.com.shifenlife.json.HotProduct;
import ruilelin.com.shifenlife.json.ProductDetailJson;
import ruilelin.com.shifenlife.jsonmodel.ChannelModel;
import ruilelin.com.shifenlife.jsonmodel.RecommendModel;
import ruilelin.com.shifenlife.utils.Constant;


public class HomeRecyleViewAdapter extends RecyclerView.Adapter {
    private int currentType = BANNER; //当前类型
    /**
     * 横幅广告
     */
    public static final int BANNER = 0;
    private List<BannerJson> banners;
    public static final int CHANNEL = 1;
    private List<ChannelModel> channels;
    public static final int RECOMMEND=2;
    private List<RecommendModel> recommends;
    public static final int HOT=3;
    private List<HotProduct> nearbyShops;
    private final Context mviewContext;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    LinearLayout moreMenuLinearLayout;
    WindowManager windowManager;
    public List<SFSHCategory> menuLists = new ArrayList<>();



    public HomeRecyleViewAdapter(Context context) {
        this.mviewContext = context;
        inflater = LayoutInflater.from(mviewContext);
        banners = new ArrayList<>();
        channels = new ArrayList<>();
        recommends = new ArrayList<>();
        nearbyShops = new ArrayList<>();
        windowManager=(WindowManager)mviewContext.getSystemService(Context.WINDOW_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == BANNER ){
            View viewtop = inflater.inflate(R.layout.home_item2, parent, false);
            return new TypeBannerHolder(viewtop);
        }else if(viewType ==CHANNEL){
            View viewtop1 = inflater.inflate(R.layout.channel_layout, parent, false);
            return new TypeChannelHolder(viewtop1);
        }else if(viewType ==RECOMMEND){
            View viewtop2 = inflater.inflate(R.layout.home_item4, parent, false);
            return new TypeRecommendHolder(viewtop2);
        }else if(viewType == HOT){
            View viewtop3 = inflater.inflate(R.layout.home_item5, parent, false);
            return new TypeHotHolder(viewtop3);
        }

       return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         if(getItemViewType(position) == BANNER){
            initBanner((TypeBannerHolder)holder,banners);
         }else if(getItemViewType(position) ==CHANNEL ){
             initChannel((TypeChannelHolder)holder,channels);
         }else if(getItemViewType(position) ==RECOMMEND){
             Log.d("111","position="+recommends.size());
             initRecommend((TypeRecommendHolder)holder,recommends);
         }else if(getItemViewType(position) ==HOT){
             inithot((TypeHotHolder)holder,nearbyShops);
         }
    }

    @Override
    public int getItemCount() {
        return 4;
    }



    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case BANNER:
                currentType = BANNER;
                break;
            case CHANNEL:
                currentType = CHANNEL;
                break;
            case RECOMMEND:
                currentType = RECOMMEND;
                break;
            case HOT:
                currentType = HOT;
                break;
        }
        return currentType;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public class TypeHotHolder extends RecyclerView.ViewHolder {
        GridView gv_hot;
        public TypeHotHolder(View itemView) {
            super(itemView);
            gv_hot = (GridView)itemView.findViewById(R.id.gv_hot);
            gv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("position","position=="+nearbyShops.get(i));
                    Intent intent = new Intent(mviewContext, GoodsInfoPage.class);
                    ProductDetailJson productDetailJson = new ProductDetailJson(String.valueOf(nearbyShops.get(i).getSupplierId()),String.valueOf(nearbyShops.get(i).getId()));
                    intent.putExtra("productdetail", productDetailJson);
                    mviewContext.startActivity(intent);
                }
            });
        }
    }

    private void inithot(TypeHotHolder holder,List<HotProduct> datalist){
        HotAdapter hotAdapter = new HotAdapter(mviewContext,datalist);
        holder.gv_hot.setAdapter(hotAdapter);
        setGridViewHeight(holder.gv_hot,datalist);
        hotAdapter.notifyDataSetChanged();
    }



    public void sethotbean(List<HotProduct> datalist){
        nearbyShops = datalist;
        notifyDataSetChanged();
    }
    public class TypeRecommendHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_recommend;
        public TypeRecommendHolder(View itemView) {
            super(itemView);
            rv_recommend = (RecyclerView)itemView.findViewById(R.id.rv_recommend);
        }
    }

    private void initRecommend(TypeRecommendHolder holder,List<RecommendModel> datalist){
        SeckillRecyclerViewAdapter adapter = new SeckillRecyclerViewAdapter(mviewContext, datalist);
        int spacingInPixels = mviewContext.getResources().getDimensionPixelSize(R.dimen.spacing);
        holder.rv_recommend.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        holder.rv_recommend.setLayoutManager(new LinearLayoutManager(mviewContext, LinearLayoutManager.HORIZONTAL ,false));
        holder.rv_recommend.setAdapter(adapter);
        adapter.setOnItemClickListener(new SeckillRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                /*Log.d("position","position=="+datalist.get(position));
                Intent intent = new Intent(mviewContext, GoodsInfoPage.class);
                ProductDetailJson productDetailJson = new ProductDetailJson(String.valueOf(datalist.get(position).getSupplierId()),String.valueOf(datalist.get(position).getId()));
                intent.putExtra("productdetail", productDetailJson);
                mviewContext.startActivity(intent);*/
                 Log.d("position","position=="+datalist);
                Intent intent = new Intent(mviewContext, GoodsInfoPage.class);
                ProductDetailJson productDetailJson = new ProductDetailJson(String.valueOf(datalist.get(position).getSupplierId()),String.valueOf(datalist.get(position).getId()));
                intent.putExtra("productdetail", productDetailJson);
                mviewContext.startActivity(intent);
            }
        });
    }
    public void setrecommendbean(List<RecommendModel> datalist) {
        Log.d("heightAA","setrecommendbean  datalist.size=="+datalist.size());
        recommends = datalist;
        notifyDataSetChanged();
    }

    public class TypeChannelHolder extends RecyclerView.ViewHolder {
        GridView gv_channel;
        public TypeChannelHolder(View itemView) {
            super(itemView);
            gv_channel = (GridView)itemView.findViewById(R.id.gv_channel);
        }
    }


    private void initChannel(TypeChannelHolder holder,List<ChannelModel> datalist){
        ChannelAdapter channelAdapter = new ChannelAdapter(mviewContext,datalist);
        holder.gv_channel.setAdapter(channelAdapter);
        holder.gv_channel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("channel","position=="+i);
                if(channels.size()>=8) {
                    if(i==7){
                        showMoreMenu(menuLists);
                        return;
                    }
                }else{
                    if (i == (channels.size() - 1)) {
                        showMoreMenu(menuLists);
                        return;
                    }
                }
                mItemOnClickListener.itemTypeOnClickListener(view);
            }
        });
    }




    public void setchannelbean(List<ChannelModel> datalist) {
        if(datalist!=null && datalist.size()>0){
            menuLists.clear();
            for(int i=0;i<datalist.size();i++){
                SFSHCategory sfshCategory = new SFSHCategory(datalist.get(i).getName(),datalist.get(i).getImage(),1);
                menuLists.add(sfshCategory);
                sfshCategory = null;
            }
            SFSHCategory sfshCategoryMore = new SFSHCategory("敬请期待","",1);
            menuLists.add(sfshCategoryMore);
        }
        channels = datalist;
        if(channels.size()>=8){
            ChannelModel channelModel = new ChannelModel("更多");
            channels.add(7,channelModel);
        }else{
            ChannelModel channelModel = new ChannelModel("更多");
            channels.add(channelModel);
        }
        notifyDataSetChanged();
    }

    public class TypeBannerHolder extends RecyclerView.ViewHolder {
        MZBannerView banner;

        public TypeBannerHolder(View view) {
            super(view);
            banner = (view).findViewById(R.id.banner);
        }
    }

    public void setbannerbean(List<BannerJson> datalist) {
        banners = datalist;
        notifyDataSetChanged();
    }
    private void initBanner(TypeBannerHolder holder,List<BannerJson> datalist){
        holder.banner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
             Log.d("banner","position=="+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        holder.banner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                Intent intent = new Intent(mviewContext, GoodsInfoPage.class);
                ProductDetailJson productDetailJson = new ProductDetailJson(String.valueOf(banners.get(i).getSupplierId()),String.valueOf(banners.get(i).getId()));
                intent.putExtra("productdetail", productDetailJson);
                mviewContext.startActivity(intent);
            }
        });

        holder.banner.setPages(banners, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        holder.banner.start();
    }

    //在线获取图片
    public static class BannerViewHolder implements MZViewHolder<BannerJson>{
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.remote_banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.remote_item_image);
            return view;
        }

        @Override
        public void onBind(Context context, int i, BannerJson movie) {
            //Picasso.with(context).load("http://new.ruilelin.com/images/201807/source_img/243_P_1531204461990.jpg").into(mImageView);
            Glide.with(context).load(Constant.SFSHURL + movie.getImg()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    //Toast.makeText(context,"广告栏图片加载失败",Toast.LENGTH_SHORT).show();
                    mImageView.setBackground(context.getResources().getDrawable(R.mipmap.temp_home));
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(mImageView);
        }
    }


    void showMoreMenu(final List<SFSHCategory> menuEntities ) {

        WindowManager.LayoutParams params=new WindowManager.LayoutParams();
        LayoutInflater inflater=LayoutInflater.from(mviewContext);
        moreMenuLinearLayout= (LinearLayout) inflater.inflate(R.layout.more_menu,null);

        ImageButton closeMoreMenuButton = (ImageButton) moreMenuLinearLayout.findViewById(R.id.closeMoreMenuButton);
        closeMoreMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMoreMenu(moreMenuLinearLayout);
            }
        });

        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(menuEntities);
        GridView moreMenuGridView = (GridView) moreMenuLinearLayout.findViewById(R.id.gridview_menu);
        moreMenuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String name = menuLists.get(position).getCategoryName();
                //TODO
            }
        });
        moreMenuGridView.setAdapter(menuItemAdapter);
        params.width= WindowManager.LayoutParams.MATCH_PARENT;
        params.height=WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity= Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        params.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format= PixelFormat.RGBA_8888; //设置图片格式，效果为背景透明
        windowManager.addView(moreMenuLinearLayout,params);
    }

    void hideMoreMenu(View view) {
        if (windowManager!=null) {
            windowManager.removeView(view);
        }
    }

    final class MenuItemAdapter extends BaseAdapter {
        List<SFSHCategory> menuInfo = new ArrayList<SFSHCategory>();

        public MenuItemAdapter(List<SFSHCategory> menuEntities) {
            this.menuInfo = menuEntities;
        }

        @Override
        public int getCount() {
            return menuInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return menuInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView =inflater.inflate(R.layout.grid_item_category, null);
            }
            TextView itemName = (TextView) convertView.findViewById(R.id.item_name);

            if (menuInfo != null) {
                itemName.setText(menuInfo.get(position).getCategoryName());
            }
            return convertView;
        }

    }


    public void setRecyclerViewHeight(RecyclerView recyclerView) {
        // 获取gridview的布局参数
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        //params.height = orderGoods.size()*100;
        params.height =((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1*180, mviewContext.getResources().getDisplayMetrics()));
        recyclerView.setLayoutParams(params);
    }

    public void setGridViewHeight(GridView gridview,List<HotProduct> temp) {
        // 获取gridview的布局参数
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        //params.height = orderGoods.size()*100;
        params.height =((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, temp.size()*110, mviewContext.getResources().getDisplayMetrics()));
        gridview.setLayoutParams(params);
    }


    private ItemTypeOnClickListener mItemOnClickListener;

    public void setItemTypeOnClickListener(ItemTypeOnClickListener listener){
        this.mItemOnClickListener = listener;
    }

    public interface ItemTypeOnClickListener{
        /**
         * 传递点击的view
         * @param
         */
        public void itemTypeOnClickListener(View view);
    }

}
