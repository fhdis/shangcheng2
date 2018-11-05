package ruilelin.com.shifenlife.myorder.anotherway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.List;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.activity.PayActivity;
import ruilelin.com.shifenlife.json.Answer;
import ruilelin.com.shifenlife.json.ConfirmReceipt;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.Order;
import ruilelin.com.shifenlife.utils.Constant;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Object> data ;
    public static final int TYPE_EMPTY =0, ITEM_HEADER = 1, ITEM_CONTENT = 2, ITEM_FOOTER = 3;
    private int currentType = ITEM_HEADER;
    private Context mContxxt;
    private View headerView;
    private int REQUEST_CODE_PAY_METHOD=3;
    public MyOkHttp mMyOkhttp;
    private String username;

    public OrderAdapter(Context context, List<Object> datas){
        this.mContxxt = context;
        this.data = datas;
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        SharedPreferences sharedPreferences =mContxxt.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_EMPTY) {
            view = LayoutInflater.from(mContxxt).inflate(R.layout.activity_order_empty, parent, false);
            return new OrderAdapter.EmptyViewHolder(view);
        }else if(viewType == ITEM_HEADER) {
            view = LayoutInflater.from(mContxxt).inflate(R.layout.goodsorderinfo, parent, false);
            return new OrderAdapter.ViewHolderHeader(view);
        }else if(viewType == ITEM_CONTENT){
            view = LayoutInflater.from(mContxxt).inflate(R.layout.item_order_gv, parent, false);
            return new OrderAdapter.ViewHolderItem(view);
        }else if(viewType == ITEM_FOOTER){
            view = LayoutInflater.from(mContxxt).inflate(R.layout.ordergoodsfooter, parent, false);
            return new OrderAdapter.ViewHolderFooter(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHeader) {
            GoodsOrderInfo  goodsOrderInfo = (GoodsOrderInfo) data.get(position);
            ((ViewHolderHeader) holder).txt_order.setText("订单号:"+goodsOrderInfo.getSn()+" "+goodsOrderInfo.getSupplierName());
           // ((ViewHolderHeader) holder).txt_order_status.setText(goodsOrderInfo.getStatus());
            if(goodsOrderInfo.getStatus().equals("NO_PAY")){
                Drawable top = mContxxt.getResources().getDrawable(R.mipmap.icon_tel);
                ((ViewHolderHeader) holder).txt_order_status.setCompoundDrawablesWithIntrinsicBounds(top, null , null, null);
                ((ViewHolderHeader) holder).txt_order_status.setText("待付款");
            }else if(goodsOrderInfo.getStatus().equals("PAID")){
                Drawable top = mContxxt.getResources().getDrawable(R.mipmap.icon_tel);
                ((ViewHolderHeader) holder).txt_order_status.setCompoundDrawablesWithIntrinsicBounds(top, null , null, null);
                ((ViewHolderHeader) holder).txt_order_status.setText("已完成");
            }else if(goodsOrderInfo.getStatus().equals("CANCEL")){
                ((ViewHolderHeader) holder).txt_order_status.setCompoundDrawablesWithIntrinsicBounds(null, null , null, null);
                ((ViewHolderHeader) holder).txt_order_status.setText("已取消");
            }else if(goodsOrderInfo.getStatus().equals("DISTRIBUTING") || goodsOrderInfo.getStatus().equals("DISTRIBUTECOMPLETE") || goodsOrderInfo.getStatus().equals("ALLOCATEMAN") || goodsOrderInfo.getStatus().equals("PICKING") || goodsOrderInfo.getStatus().equals("SENDING") || goodsOrderInfo.getStatus().equals("CONFIGURATIONCOMPLETE")|| goodsOrderInfo.getStatus().equals("FINISHED")){
                ((ViewHolderHeader) holder).txt_order_status.setCompoundDrawablesWithIntrinsicBounds(null, null , null, null);
                ((ViewHolderHeader) holder).txt_order_status.setText("进行中");
            }
        } else if (holder instanceof ViewHolderItem) {
            List<OrderGoodsItem> orderGoodsItem = (List<OrderGoodsItem>) data.get(position);
            ItemOrderAdapter itemOrderAdapter = new ItemOrderAdapter(mContxxt,orderGoodsItem);
           ((ViewHolderItem) holder).gv_hot.setAdapter(itemOrderAdapter);
            setGridViewHeight(((ViewHolderItem) holder).gv_hot,orderGoodsItem);
            Log.d("order2","orderGoodsItem.size="+orderGoodsItem.size());
            itemOrderAdapter.notifyDataSetChanged();
        } else if (holder instanceof ViewHolderFooter) {
            OrderGoodsFooter orderGoodsFooter = (OrderGoodsFooter) data.get(position);
            ((ViewHolderFooter) holder).tv_order_time.setText(orderGoodsFooter.getCreateTime());
            ((ViewHolderFooter) holder).txt_order_heji.setText("共"+orderGoodsFooter.getNum()+"件"+" "+"合计:¥"+orderGoodsFooter.getPrice());
            if(orderGoodsFooter.getStatus().equals("NO_PAY")){
                ((ViewHolderFooter) holder).button1.setText("取消订单");
                ((ViewHolderFooter) holder).button2.setText("去支付");
                ((ViewHolderFooter) holder).button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMyOkhttp.get()
                                .addHeader("cookie",username)
                                .url(Constant.SFSHURL+Constant.CancelOrder+orderGoodsFooter.getId())
                                // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                .tag(this)
                                .enqueue(new JsonResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, JSONObject response) {
                                        Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response);
                                        Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>(){});
                                        int  resCodes = obj.getCode();
                                        ((Activity)mContxxt).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(resCodes== Constant.SUCCESSCODE) {
                                                    Toast.makeText(mContxxt,"成功取消订单",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, JSONArray response) {
                                        Log.d("APITest", "doPostJSON onSuccess JSONArray:" + response);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, String error_msg) {
                                        Log.d("APITest", "doPostJSON onFailure:" + error_msg);
                                    }
                                });

                    }
                });
                ((ViewHolderFooter) holder).button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContxxt, PayActivity.class);
                        Order payorder = new Order(orderGoodsFooter.getId());
                        intent.putExtra("oderinfo", (Serializable) payorder);
                        ((Activity)mContxxt).startActivityForResult(intent, REQUEST_CODE_PAY_METHOD);
                    }
                });
            }else if(orderGoodsFooter.getStatus().equals("PAID")){
                ((ViewHolderFooter) holder).button1.setVisibility(View.INVISIBLE);
                ((ViewHolderFooter) holder).button2.setVisibility(View.INVISIBLE);
            }else if(orderGoodsFooter.getStatus().equals("CANCEL")){
                ((ViewHolderFooter) holder).button1.setVisibility(View.INVISIBLE);
                ((ViewHolderFooter) holder).button2.setVisibility(View.INVISIBLE);
            }else if(orderGoodsFooter.getStatus().equals("DISTRIBUTING") || orderGoodsFooter.getStatus().equals("DISTRIBUTECOMPLETE") || orderGoodsFooter.getStatus().equals("ALLOCATEMAN") || orderGoodsFooter.getStatus().equals("PICKING") || orderGoodsFooter.getStatus().equals("SENDING") || orderGoodsFooter.getStatus().equals("CONFIGURATIONCOMPLETE")){
                ((ViewHolderFooter) holder).button1.setVisibility(View.INVISIBLE);
                ((ViewHolderFooter) holder).button2.setText("确认收货");
                ((ViewHolderFooter) holder).button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConfirmReceipt confirmReceipt = new ConfirmReceipt(orderGoodsFooter.getSn());
                        String jsonstyle = JSON.toJSONString(confirmReceipt);
                        mMyOkhttp.post()
                                .url(Constant.SFSHURL + Constant.ConfirmOrder)
                                .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                                .tag(this)
                                .enqueue(new JsonResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, JSONObject response) {
                                        Data<Answer> obj = (Data<Answer>) JSON.parseObject(response.toString(), new TypeReference<Data<Answer>>(){});
                                        int  resCodes = obj.getCode();
                                        if(resCodes==Constant.SUCCESSCODE){
                                            Toast.makeText(mContxxt, "确认收货成功", Toast.LENGTH_SHORT).show();
                                        }else if(resCodes==Constant.FAIL){
                                            Toast.makeText(mContxxt, "确认出货出错 ", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onSuccess(int statusCode, JSONArray response) {
                                        Log.d("APITest", "doPostJSON onSuccess JSONArray:" + response);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, String error_msg) {
                                        Log.d("APITest", "doPostJSON onFailure:" + error_msg);
                                    }
                                });
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
       /* if (data.size() == 0) {
            return TYPE_EMPTY;
        }else*/ if(data.get(position) instanceof GoodsOrderInfo) {
            Log.d("more","ITEM_HEADER=");
            return ITEM_HEADER;
        }else if(data.get(position) instanceof OrderGoodsItem){
            Log.d("more","ITEM_CONTENT=");
            return ITEM_CONTENT;
        }else if(data.get(position) instanceof OrderGoodsFooter){
            Log.d("more","ITEM_FOOTER=");
            return ITEM_FOOTER;
        }
        return ITEM_CONTENT;
    }


    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView txt_order;
        TextView txt_order_status;
        public ViewHolderHeader(View itemView) {
            super(itemView);
            txt_order = (TextView)itemView.findViewById(R.id.txt_order);
            txt_order_status = (TextView)itemView.findViewById(R.id.txt_order_status);
        }
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {
        GridView gv_hot;
        public ViewHolderItem(View itemView) {
            super(itemView);
            gv_hot = (GridView)itemView.findViewById(R.id.gv_hot);
        }
    }

    static class ViewHolderFooter extends RecyclerView.ViewHolder {
        TextView  tv_order_time;
        TextView txt_order_heji;
        TextView tv_order_beizhu;
        Button button1;
        Button button2;
        public ViewHolderFooter(View itemView) {
            super(itemView);
            tv_order_time = (TextView)itemView.findViewById(R.id.tv_order_time);
            txt_order_heji = (TextView)itemView.findViewById(R.id.txt_order_heji);
            tv_order_beizhu = (TextView)itemView.findViewById(R.id.tv_order_beizhu);
            button1 = (Button)itemView.findViewById(R.id.button1);
            button2 = (Button)itemView.findViewById(R.id.button2);
        }
    }

    public void setGridViewHeight(GridView gridview,List<OrderGoodsItem> orderGoods) {
        // 获取gridview的布局参数
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height =((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, orderGoods.size()*95, mContxxt.getResources().getDisplayMetrics()));
        gridview.setLayoutParams(params);
    }

}
