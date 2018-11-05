package ruilelin.com.shifenlife.activity;

import android.view.View;
import android.widget.Button;

import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;

public class AboutUs extends BaseActivity {
    private Button back;
    @Override
    protected int setLayoutResID() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        back = (Button)findViewById(R.id.back);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
