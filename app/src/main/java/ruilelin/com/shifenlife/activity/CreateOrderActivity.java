package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.cart.adapter.CreateOrderRvAdapter;
import ruilelin.com.shifenlife.cart.bean.ShoppingCarDataBean;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import ruilelin.com.shifenlife.json.CreateOrder;
import ruilelin.com.shifenlife.json.CreateOrderNote;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.Order;
import ruilelin.com.shifenlife.model.AddressModel;
import ruilelin.com.shifenlife.utils.Constant;

public class CreateOrderActivity extends BaseActivity {
    private TextView title;
    private Button back;
    private Button btn_createOrder;
    List<ShoppingCarDataBean.DatasBean.Commodity> gotopay;
    private ImageView bt_choose_address;
    //收货地址信息
    private TextView tv_name;
    private TextView tv_address;
    private AddressModel addressModel;
    //商品列表
    private RecyclerView recycler_view;
    private CreateOrderRvAdapter createOrderRvAdapter;
    //订单总价钱
    private TextView rb_webchat;
    private TextView txt_total;
    private double total_price;
    private String username;

    private TextView txt_order;
    private EditText contentEditText;

    //提交订单
    private int resCode = -1;
    private List<CreateOrder> orders = new ArrayList<>();
    private Order payorder;
    private int REQUEST_CODE_PAY_METHOD=3;

    @Override
    protected int setLayoutResID() {
        return R.layout.activity_create_order;
    }

    @Override
    protected void initData() {
        gotopay = (List<ShoppingCarDataBean.DatasBean.Commodity>)getIntent().getSerializableExtra("goods");
        Log.d("gotopay","gotopay=="+gotopay);
        total_price = 0.0;
        rb_webchat.setText("¥0.00");
        if(gotopay!=null && gotopay.size()>0){
            txt_order.setText(gotopay.get(0).getSuppliername());
            createOrderRvAdapter.appendData(gotopay);
            for(int i=0;i<gotopay.size();i++){
                String num = gotopay.get(i).getNumber();
                String price = gotopay.get(i).getPrice();
                double v = Double.parseDouble(num);
                double v1 = Double.parseDouble(price);
                total_price = total_price + v * v1;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                rb_webchat.setText("¥" + decimalFormat.format(total_price));
                txt_total.setText("¥" + decimalFormat.format(total_price));
                CreateOrder order = new CreateOrder(gotopay.get(i).getId(),String.valueOf(v * v1));
                orders.add(order);
            }
        }
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences =getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        btn_createOrder = (Button)findViewById(R.id.btn_createOrder);
        title = (TextView)findViewById(R.id.title);
        rb_webchat = (TextView)findViewById(R.id.rb_webchat);
        txt_total = (TextView)findViewById(R.id.txt_total);
        txt_order = (TextView)findViewById(R.id.txt_order);
        back = (Button)findViewById(R.id.back);
        bt_choose_address = (ImageView)findViewById(R.id.bt_choose_address);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_address = (TextView)findViewById(R.id.tv_address);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        contentEditText = (EditText) findViewById(R.id.contentEditText);

        title.setText("订单详情");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createOrderRvAdapter = new CreateOrderRvAdapter(this);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(createOrderRvAdapter);
    }

    @Override
    protected void initListener() {
        btn_createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateOrderNote createOrderNote = new CreateOrderNote(contentEditText.getText().toString(),orders);
                String jsonstyle = JSON.toJSONString(createOrderNote);
                mMyOkhttp.post()
                        .addHeader("cookie",username)
                        .url(Constant.SFSHURL+Constant.CreateOrder)
                        .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Log.d("APITest", "doPostJSON onSuccess JSONObject:" + response.toString());
                                Data<Order> obj = (Data<Order>) JSON.parseObject(response.toString(), new TypeReference<Data<Order>>(){});
                                resCode = obj.getCode();
                                payorder= obj.getData();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(resCode == Constant.SUCCESSCODE){
                                            Toast.makeText(CreateOrderActivity.this,"创建成功订单编号为"+payorder.getSn()+"的订单",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CreateOrderActivity.this, PayActivity.class);
                                            intent.putExtra("oderinfo", (Serializable) payorder);
                                            startActivityForResult(intent, REQUEST_CODE_PAY_METHOD);
                                        }else {
                                            Toast.makeText(CreateOrderActivity.this,"创建订单失败，请重试 ",Toast.LENGTH_SHORT).show();
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

        bt_choose_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateOrderActivity.this, PersonPlaceActivity.class);
                // startActivity(intent);
                intent.putExtra("task1", "Choose_Address_too");
                startActivityForResult(intent, 4);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==6){
            if(requestCode==4){
                addressModel = (AddressModel)intent.getSerializableExtra("address_info");
                if(addressModel!=null){
                    tv_name.setText(addressModel.getConsignee()+addressModel.getMobile());
                    tv_address.setText(addressModel.getAddress()+addressModel.getLooseAddress());
                }
            }
        }
    }
}
