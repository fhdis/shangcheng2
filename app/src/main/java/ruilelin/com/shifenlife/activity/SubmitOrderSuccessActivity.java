package ruilelin.com.shifenlife.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;

public class SubmitOrderSuccessActivity extends BaseActivity {

    private Button viewOrders;
    private Button continueShopping;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_submit_order_success;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        viewOrders = (Button)findViewById(R.id.viewOrders);
        continueShopping = (Button)findViewById(R.id.continueShopping);
    }

    @Override
    protected void initListener() {
        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubmitOrderSuccessActivity.this, PersonOrderActivity.class);
                intent.putExtra("ID",0);
                startActivity(intent);
            }
        });

        continueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubmitOrderSuccessActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
