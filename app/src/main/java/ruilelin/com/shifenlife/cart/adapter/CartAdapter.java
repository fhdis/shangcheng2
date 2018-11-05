package ruilelin.com.shifenlife.cart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.CreateOrderActivity;
import ruilelin.com.shifenlife.cart.bean.ShoppingCarDataBean;
import ruilelin.com.shifenlife.cart.util.ToastUtil;

public class CartAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final LinearLayout llSelectAll;
    private final ImageView ivSelectAll;
    private final Button btnOrder;
    private final Button btnDelete;
    private final RelativeLayout rlTotalPrice;
    private final TextView tvTotalPrice;
    private List<ShoppingCarDataBean.DatasBean> data;
    private boolean isSelectAll = false;
    private double total_price;
    private double total_coin_price;
    private String shownum;
    private static final int REQUEST_CODE_PAY = 2;


    public CartAdapter(Context context, LinearLayout llSelectAll,
                       ImageView ivSelectAll, Button btnOrder, Button btnDelete,
                       RelativeLayout rlTotalPrice, TextView tvTotalPrice) {
        this.context = context;
        this.llSelectAll = llSelectAll;
        this.ivSelectAll = ivSelectAll;
        this.btnOrder = btnOrder;
        this.btnDelete = btnDelete;
        this.rlTotalPrice = rlTotalPrice;
        this.tvTotalPrice = tvTotalPrice;
        Log.d("adapter","1");
    }

    /**
     * 自定义设置数据方法；
     * 通过notifyDataSetChanged()刷新数据，可保持当前位置
     *
     * @param data 需要刷新的数据
     */
    public void setData(List<ShoppingCarDataBean.DatasBean> data) {
        this.data = data;
        Log.d("adapter","2");
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (data != null && data.size() > 0) {
            Log.d("adapter","3");
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        Log.d("adapter","4");
        return data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        Log.d("adapter","5");
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        Log.d("adapter","getGroupView");
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_group, null);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final ShoppingCarDataBean.DatasBean datasBean = data.get(groupPosition);
        //店铺ID
        //   String store_id ;
        //店铺名称
        String store_name = datasBean.getSupplier().getName();

        if (store_name != null) {
            groupViewHolder.tvStoreName.setText(store_name);
        } else {
            groupViewHolder.tvStoreName.setText("");
        }

        //店铺内的商品都选中的时候，店铺的也要选中
        for (int i = 0; i < datasBean.getGoods().size(); i++) {
            ShoppingCarDataBean.DatasBean.Commodity goodsBean = datasBean.getGoods().get(i);
            boolean isSelect = goodsBean.isSelect();
            if (isSelect) {
                // datasBean.setIsSelect_shop(true);
                datasBean.getSupplier().setSelect_shop(true);
            } else {
                //datasBean.setIsSelect_shop(false);
                datasBean.getSupplier().setSelect_shop(false);
                break;
            }
        }

        //因为set之后要重新get，所以这一块代码要放到一起执行
        //店铺是否在购物车中被选中
        // final boolean isSelect_shop = datasBean.getIsSelect_shop();
        final boolean isSelect_shop = datasBean.getSupplier().isSelect_shop();
        if (isSelect_shop) {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.select);
        } else {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.unselect);
        }

        //店铺选择框的点击事件
        groupViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //datasBean.setIsSelect_shop(!isSelect_shop);
                datasBean.getSupplier().setSelect_shop(!isSelect_shop);

                List<ShoppingCarDataBean.DatasBean.Commodity> goods = datasBean.getGoods();
                for (int i = 0; i < goods.size(); i++) {
                    ShoppingCarDataBean.DatasBean.Commodity goodsBean = goods.get(i);
                    // goodsBean.setIsSelect(!isSelect_shop);
                    goodsBean.setSelect(!isSelect_shop);
                }
                notifyDataSetChanged();
            }
        });

        //当所有的选择框都是选中的时候，全选也要选中
        w:
        for (int i = 0; i < data.size(); i++) {
            List<ShoppingCarDataBean.DatasBean.Commodity> goods = data.get(i).getGoods();
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCarDataBean.DatasBean.Commodity goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();
                if (isSelect) {
                    isSelectAll = true;
                } else {
                    isSelectAll = false;
                    break w;//根据标记，跳出嵌套循环
                }
            }
        }
        if (isSelectAll) {
            ivSelectAll.setBackgroundResource(R.mipmap.select);
        } else {
            ivSelectAll.setBackgroundResource(R.mipmap.unselect);
        }

        //全选的点击事件
        llSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectAll = !isSelectAll;

                if (isSelectAll) {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShoppingCarDataBean.DatasBean.Commodity> goods = data.get(i).getGoods();
                        for (int y = 0; y < goods.size(); y++) {
                            ShoppingCarDataBean.DatasBean.Commodity goodsBean = goods.get(y);
                            // goodsBean.setIsSelect(true);
                            goodsBean.setSelect(true);
                        }
                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShoppingCarDataBean.DatasBean.Commodity> goods = data.get(i).getGoods();
                        for (int y = 0; y < goods.size(); y++) {
                            ShoppingCarDataBean.DatasBean.Commodity goodsBean = goods.get(y);
                            // goodsBean.setIsSelect(false);
                            goodsBean.setSelect(false);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        //合计的计算
        total_price = 0.0;
        total_coin_price = 0.0;
        tvTotalPrice.setText("¥0.00");
        for (int i = 0; i < data.size(); i++) {
            List<ShoppingCarDataBean.DatasBean.Commodity> goods = data.get(i).getGoods();
            Log.d("adapter","i=="+i);
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCarDataBean.DatasBean.Commodity goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();
                Log.d("adapter","isSelect=="+isSelect);
                if (isSelect) {
                    String num = goodsBean.getNumber();
                    String price = goodsBean.getPrice();
                    double v = Double.parseDouble(num);
                    double v1 = Double.parseDouble(price);
                    Log.d("coin","v=="+v);

                    total_price = total_price + v * v1;
                    //让Double类型完整显示，不用科学计数法显示大写字母E
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    tvTotalPrice.setText("¥" + decimalFormat.format(total_price));
                }
            }
        }

        //去结算的点击事件
        btnOrder.setOnClickListener(new View.OnClickListener() {
            List<ShoppingCarDataBean.DatasBean.Commodity> temp = new ArrayList<>();
            @Override
            public void onClick(View v) {
                //创建临时的List，用于存储被选中的商品

                for (int i = 0; i < data.size(); i++) {
                    List<ShoppingCarDataBean.DatasBean.Commodity> goods = data.get(i).getGoods();
                    for (int y = 0; y < goods.size(); y++) {
                        ShoppingCarDataBean.DatasBean.Commodity goodsBean = goods.get(y);
                        goodsBean.setSuppliername(data.get(i).getSupplier().getName());
                        boolean isSelect = goodsBean.isSelect();
                        if (isSelect) {
                            temp.add(goodsBean);
                        }
                    }
                }

                if (temp != null && temp.size() > 0) {//如果有被选中的
                    /**
                     * 实际开发中，如果有被选中的商品，
                     * 则跳转到确认订单页面，完成后续订单流程。
                     */

                  for(int m=0;m<temp.size();m++){
                      String onlyOneShop = temp.get(m).getSupplierId();
                     for(int n=(m+1);n<temp.size();n++){
                         String tempOneShop = temp.get(n).getSupplierId();
                         if(!onlyOneShop.equals(tempOneShop)){
                             ToastUtil.makeText(context, "亲，一次下单，只能选购一家店铺的商品哦。请检查购物车里面选购的商品是否是同一家店铺。");
                             return;
                         }
                     }
                  }
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("温馨提示")
                            .setMessage("总计:\n" + temp.size() + "个商品\n"+total_price + "RMB")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    List<ShoppingCarDataBean.DatasBean.Commodity> gotopay = temp;
                                    Intent intent = new Intent(context, CreateOrderActivity.class);
                                    intent.putExtra("goods", (Serializable) gotopay);
                                    ((Activity) context).startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                    ToastUtil.makeText(context, "跳转到确认订单页面，完成后续订单流程");
                } else {
                    ToastUtil.makeText(context, "请选择要购买的商品");
                }
            }
        });

        //删除的点击事件
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 实际开发中，通过回调请求后台接口实现删除操作
                 */
                if (mDeleteListener != null) {
                    mDeleteListener.onDelete();
                }
            }
        });

        return convertView;
    }

    static class GroupViewHolder {
        // @InjectView(R.id.iv_select)
        ImageView ivSelect;
        //  @InjectView(R.id.tv_store_name)
        TextView tvStoreName;
        //   @InjectView(R.id.ll)
        LinearLayout ll;

        GroupViewHolder(View view) {
            // ButterKnife.inject(this, view);
            ivSelect = (ImageView)view.findViewById(R.id.iv_select);
            tvStoreName = (TextView)view.findViewById(R.id.tv_store_name);
            ll = (LinearLayout)view.findViewById(R.id.ll);
        }
    }

    //------------------------------------------------------------------------------------------------
    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getGoods() != null && data.get(groupPosition).getGoods().size() > 0) {
            return data.get(groupPosition).getGoods().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getGoods().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_child, null);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ShoppingCarDataBean.DatasBean datasBean = data.get(groupPosition);
        //店铺ID
        // String store_id = datasBean.getStore_id();
        //店铺名称
        String store_name = datasBean.getSupplier().getName();
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getSupplier().isSelect_shop();
        final ShoppingCarDataBean.DatasBean.Commodity goodsBean = datasBean.getGoods().get(childPosition);
        //商品图片
        String goods_image = goodsBean.getThumbnail();
        //商品ID
        final String goods_id = goodsBean.getId();
        //商品名称
        String goods_name = goodsBean.getName();
        //商品价格
        String goods_price = goodsBean.getPrice();
        //String good_price_coin = goodsBean.getCoinPrice();
        //商品数量
        String goods_num = goodsBean.getNumber();
        //商品是否被选中
        final boolean isSelect = goodsBean.isSelect();

        Glide.with(context)
                .load(goods_image)
                .into(childViewHolder.ivPhoto);
        if (goods_name != null) {
            childViewHolder.tvName.setText(goods_name);
        } else {
            childViewHolder.tvName.setText("");
        }
        if (goods_price != null) {
            childViewHolder.tvPriceValue.setText(goods_price);
        } else {
            childViewHolder.tvPriceValue.setText("");
        }
        /*if (good_price_coin != null) {
            childViewHolder.tv_price_coin.setText(good_price_coin);
        } else {
            childViewHolder.tv_price_coin.setText("");
        }*/
        if (goods_num != null) {
            childViewHolder.tvEditBuyNumber.setText(goods_num);
        } else {
            childViewHolder.tvEditBuyNumber.setText("");
        }

        //商品是否被选中
        if (isSelect) {
            childViewHolder.ivSelect.setImageResource(R.mipmap.select);
        } else {
            childViewHolder.ivSelect.setImageResource(R.mipmap.unselect);
        }

        //商品选择框的点击事件
        childViewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsBean.setSelect(!isSelect);
                if (!isSelect == false) {
                    //  datasBean.setIsSelect_shop(false);
                    datasBean.getSupplier().setSelect_shop(false);
                }
                notifyDataSetChanged();
            }
        });

        //加号的点击事件
        childViewHolder.ivEditAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟加号操作
                if(goodsBean.isPiece()) {
                    String num = goodsBean.getNumber();
                    Integer integer = Integer.valueOf(num);
                    integer++;
                    goodsBean.setNumber(integer + "");
                    notifyDataSetChanged();
                }else if(!goodsBean.isPiece()){
                    shownum = String.valueOf(goodsBean.getNumber());
                    shownum = add(shownum,"0.1", 1);
                    // goodsBean.setGoods_num(shownum);
                    goodsBean.setNumber(shownum);

                    notifyDataSetChanged();
                }
                /**
                 * 实际开发中，通过回调请求后台接口实现数量的加减
                 */
                if (mChangeCountListener != null) {
                    mChangeCountListener.onChangeCount(goods_id);
                }
            }
        });
        //减号的点击事件
        childViewHolder.ivEditSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟减号操作
                if(goodsBean.isPiece()){
                    String num = goodsBean.getNumber();
                    Integer integer = Integer.valueOf(num);
                    if (integer > 1) {
                        integer--;
                        goodsBean.setNumber(integer + "");
                    }else if(!goodsBean.isPiece()){
                        shownum = goodsBean.getNumber();
                        BigDecimal bigDecimal = new BigDecimal(shownum);
                        int r = bigDecimal.compareTo(BigDecimal.ZERO);
                        Log.d("0830", "r==" + r);
                        if (r > 0){
                            String numnow = sub(shownum, "0.1", 1);
                            goodsBean.setNumber(numnow);
                        }else{
                            ToastUtil.makeText(context, "商品不能再减少了");
                        }
                    }

                    /**
                     * 实际开发中，通过回调请求后台接口实现数量的加减
                     */
                    if (mChangeCountListener != null) {
                        mChangeCountListener.onChangeCount(goods_id);
                    }
                } else {
                    ToastUtil.makeText(context, "商品不能再减少了");
                }
                notifyDataSetChanged();
            }
        });

        if (childPosition == data.get(groupPosition).getGoods().size() - 1) {
            childViewHolder.view.setVisibility(View.GONE);
            childViewHolder.viewLast.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.view.setVisibility(View.VISIBLE);
            childViewHolder.viewLast.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ChildViewHolder {
        //   @InjectView(R.id.iv_select)
        ImageView ivSelect;
        //  @InjectView(R.id.iv_photo)
        ImageView ivPhoto;
        // @InjectView(R.id.tv_name)
        TextView tvName;
        // @InjectView(R.id.tv_price_key)
        TextView tvPriceKey;
        //  @InjectView(R.id.tv_price_value)
        TextView tvPriceValue;
        // @InjectView(R.id.iv_edit_subtract)
        ImageView ivEditSubtract;
        // @InjectView(R.id.tv_edit_buy_number)
        TextView tvEditBuyNumber;
        // @InjectView(R.id.iv_edit_add)
        ImageView ivEditAdd;
        //  @InjectView(R.id.view)
        View view;
        // @InjectView(R.id.view_last)
        View viewLast;

        ChildViewHolder(View view1) {
            // ButterKnife.inject(this, view);
            ivSelect = (ImageView)view1.findViewById(R.id.iv_select);
            ivPhoto = (ImageView)view1.findViewById(R.id.iv_photo);
            tvName = (TextView)view1.findViewById(R.id.tv_name);
            tvPriceKey = (TextView)view1.findViewById(R.id.tv_price_key);
            tvPriceValue = (TextView)view1.findViewById(R.id.tv_price_value);
            ivEditSubtract = (ImageView)view1.findViewById(R.id.iv_edit_subtract);

            tvEditBuyNumber = (TextView)view1.findViewById(R.id.tv_edit_buy_number);
            ivEditAdd = (ImageView)view1.findViewById(R.id.iv_edit_add);
            view = (View)view1.findViewById(R.id.view);
            viewLast = (View)view1.findViewById(R.id.view_last);
        }
    }

    //-----------------------------------------------------------------------------------------------

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //删除的回调
    public interface OnDeleteListener {
        void onDelete();
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mDeleteListener = listener;
    }

    private OnDeleteListener mDeleteListener;

    //修改商品数量的回调
    public interface OnChangeCountListener {
        void onChangeCount(String goods_id);
    }

    public void setOnChangeCountListener(OnChangeCountListener listener) {
        mChangeCountListener = listener;
    }

    private OnChangeCountListener mChangeCountListener;

    //提供精确的减法运算
    public static String sub(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留的小数位数必须大于零");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    //提供精确的加法运算
    public static String add(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留的小数位数必须大于零");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    //提供精确的乘法运算
    public static String mul(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留的小数位数必须大于零");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    // 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
    public static String div(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留的小数位数必须大于零");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

}
