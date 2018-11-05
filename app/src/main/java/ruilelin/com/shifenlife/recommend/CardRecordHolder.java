package ruilelin.com.shifenlife.recommend;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.lemon.view.adapter.BaseViewHolder;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.recommend.bean.Consumption;

class CardRecordHolder extends BaseViewHolder<Consumption> {

    private TextView name;
    private TextView type;
    private TextView consumeNum;
    private TextView remainNum;
    private TextView consumeAddress;
    private TextView time;

    private ImageView imageView;
    private TextView nameTextView;
    private TextView tv_guige;
    private TextView tv_product_detail;
    private TextView priceTextView;
    private TextView tv_origin_price;
    private Button bt_addto_cart;
    public CardRecordHolder(ViewGroup parent) {
        super(parent, R.layout.item_product_recommend);
    }

    @Override
    public void setData(final Consumption object) {
        super.setData(object);
       /* name.setText("Demo");
        type.setText(object.getLx());
        consumeNum.setText("消费金额：" + object.getJe());
        remainNum.setText("卡里余额：" + object.getYe());
        consumeAddress.setText(object.getSh());
        time.setText(object.getSj());*/

        nameTextView.setText("当季超甜橙子");
        tv_guige.setText("规格:500克");
        tv_product_detail.setText("富含多种有机酸，维生素，可调节人体新陈代谢");
        priceTextView.setText("￥5.77元");
        tv_origin_price.setText("￥37.00元");
        bt_addto_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("button","button click");
            }
        });

    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        consumeNum = findViewById(R.id.consume_num);
        remainNum = findViewById(R.id.remain_num);
        consumeAddress = findViewById(R.id.consume_address);
        time = findViewById(R.id.time);

        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameTextView);
        tv_guige = findViewById(R.id.tv_guige);
        tv_product_detail = findViewById(R.id.tv_product_detail);
        priceTextView = findViewById(R.id.priceTextView);
        tv_origin_price = findViewById(R.id.tv_origin_price);
        bt_addto_cart = findViewById(R.id.bt_addto_cart);
    }

    @Override
    public void onItemViewClick(Consumption object) {
        super.onItemViewClick(object);
        //点击事件
        Log.i("CardRecordHolder","onItemViewClick");
    }
}