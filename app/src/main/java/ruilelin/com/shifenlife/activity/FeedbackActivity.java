package ruilelin.com.shifenlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.Feedback;
import ruilelin.com.shifenlife.person.fragment.UserFragment;
import ruilelin.com.shifenlife.utils.Constant;

public class FeedbackActivity extends BaseActivity {
    private Button btn_commit;
    private EditText contentEditText;
    private String username;
    private Button back;
    private TextView title;

    protected int setLayoutResID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initData() {
        title.setText("用户反馈");
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        btn_commit = (Button) findViewById(R.id.btn_commit);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
    }

    @Override
    protected void initListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback feedback = new Feedback(contentEditText.getText().toString());
                String jsonstyle = JSON.toJSONString(feedback);

                mMyOkhttp.post()
                        .addHeader("cookie", username)
                        .url(Constant.SFSHURL + Constant.Feedback)
                        .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                        .tag(this)
                        .enqueue(new JsonResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, JSONObject response) {
                                Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                                });
                                resCode = obj.getCode();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCode == Constant.SUCCESSCODE) {
                                            Toast.makeText(FeedbackActivity.this, "成功提交反馈信息", Toast.LENGTH_LONG).show();
                                            FeedbackActivity.this.finish();
                                        } else if (resCode == Constant.FAIL) {
                                            Toast.makeText(FeedbackActivity.this, "抱歉，提交失败", Toast.LENGTH_LONG).show();
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
    }
}
