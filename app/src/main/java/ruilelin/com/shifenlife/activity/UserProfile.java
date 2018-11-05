package ruilelin.com.shifenlife.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseActivity;
import ruilelin.com.shifenlife.json.Answer;
import ruilelin.com.shifenlife.json.Data;
import ruilelin.com.shifenlife.json.ModifyHeadImg;
import ruilelin.com.shifenlife.model.UserInfo;
import ruilelin.com.shifenlife.utils.CapturePhotoUtil;
import ruilelin.com.shifenlife.utils.Constant;
import ruilelin.com.shifenlife.utils.FileUtils;
import ruilelin.com.shifenlife.utils.FolderManager;
import ruilelin.com.shifenlife.utils.PictureUtil;

public class UserProfile extends BaseActivity {

    private TextView mTbTitle;
    private Button back;
    private CircleImageView civ_user_icon;
    private EditText edit_nick_name;
    private EditText edit_phone_number;
    private Button btn_out;
    private TextView nick_name_label;
    private TextView phone_number_label;
    private CapturePhotoUtil mCapturePhotoUtil;
    private static final int CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_SELECT_REGION = 0;
    private Uri mUserIconUri;
    private LinearLayout ll_phone_number;
    private LinearLayout ll_nick_name;
    private int resCode = -1;
    private UserInfo userInfo;
    private boolean choosepic_isok = false;
    private String headImgpath;
    private String originName;
    private String username;

    String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,//读取外部存储
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入外部存储
            Manifest.permission.CAMERA//拍照
    };


    @Override
    protected int setLayoutResID() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected void initData() {
        mTbTitle = (TextView) findViewById(R.id.title);
        back = (Button) findViewById(R.id.back);
        mTbTitle.setText("个人资料");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //获取个人信息
        mMyOkhttp.get()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.UserInfo)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d("APITest", "doGet onSuccess:" + response);
                        Data<UserInfo> obj = (Data<UserInfo>) JSON.parseObject(response.toString(), new TypeReference<Data<UserInfo>>() {
                        });
                        resCode = obj.getCode();
                        userInfo = obj.getData();
                        if (resCode == Constant.SUCCESSCODE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (userInfo != null) {
                                        Glide.with(UserProfile.this)
                                                .load(Constant.SFSHURL + userInfo.getHeadImg()).listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                Toast.makeText(UserProfile.this, "头像加载失败", Toast.LENGTH_SHORT).show();
                                                civ_user_icon.setBackground(getResources().getDrawable(R.mipmap.icon_photo));
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                return false;
                                            }
                                        })
                                                .into(civ_user_icon);
                                        if ("".equals(userInfo.getNickname())) {
                                            edit_nick_name.setText("未设置");
                                        } else {
                                            edit_nick_name.setText(userInfo.getNickname());
                                        }

                                        if ("".equals(userInfo.getMobile())) {
                                            edit_phone_number.setText("未设置");
                                        } else {
                                            edit_phone_number.setText(userInfo.getMobile());
                                        }
                                        originName = userInfo.getNickname();
                                        headImgpath = userInfo.getHeadImg();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doGet onFailure:" + error_msg);
                    }
                });

        civ_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicture();
            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (choosepic_isok) {
                    //uploadUserIcon();
                    uploadGetPath();
                    choosepic_isok = false;
                } else {
                    //只修改昵称
                    uploadUserIcon();
                }
            }
        });
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        civ_user_icon = (CircleImageView) findViewById(R.id.civ_user_icon);
        ll_phone_number = (LinearLayout) findViewById(R.id.ll_phone_number);
        ll_nick_name = (LinearLayout) findViewById(R.id.ll_nick_name);
        btn_out = (Button) findViewById(R.id.btn_out);
        edit_nick_name = (EditText) findViewById(R.id.edit_nick_name);
        edit_phone_number = (EditText) findViewById(R.id.edit_phone_number);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            choosepic_isok = true;
            switch (requestCode) {
                case CapturePhotoUtil.CAPTURE_PHOTO_REQUEST_CODE:
                    handleCaptureResult();
                    break;
                case CODE_CHOOSE_PHOTO:
                    List<Uri> uris = Matisse.obtainResult(data);
                    mUserIconUri = uris.get(0);
                    Glide.with(this).load(mUserIconUri).into(civ_user_icon);
                    break;
                default:
                    break;
            }

        }
    }

    private void selectPicture() {

        if (Build.VERSION.SDK_INT >= 23) {
            boolean needApply = false;
            int readExternalPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int writeExternalPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]);
            if (readExternalPermission != PackageManager.PERMISSION_GRANTED
                    || writeExternalPermission != PackageManager.PERMISSION_GRANTED
                    || cameraPermission != PackageManager.PERMISSION_GRANTED) {
                needApply = true;
            }

            if (needApply) {
                ActivityCompat.requestPermissions(UserProfile.this, permissions, 1);
                return;
            }
        }

        new AlertDialog.Builder(this)
                .setItems(new String[]{"拍摄", "从相册中选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                choosePhoto();
                                break;
                        }
                    }
                }).show();
    }

    private void takePhoto() {
        if (mCapturePhotoUtil == null) {
            mCapturePhotoUtil = new CapturePhotoUtil(this, FolderManager.getPhotoFolder());
        }
        mCapturePhotoUtil.capture();
    }

    private void choosePhoto() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .theme(R.style.CustomMatisseTheme)
                .forResult(CODE_CHOOSE_PHOTO);

    }

    private void uploadGetPath() {
        String path;
        Log.d("APITest", "uri==" + mUserIconUri);
        path = FileUtils.getFilePathByUri(this, mUserIconUri);
        Log.d("APITest", "path==" + path);
        PictureUtil.compressImage(path, 600, 800, 800);
        File file = new File(path);
        mMyOkhttp.upload()
                .addHeader("cookie", username)
                .url(Constant.SFSHURL + Constant.HeadImg)
                .addFile("file", file)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONObject111:" + response);
                        Data<String> obj = (Data<String>) JSON.parseObject(response.toString(), new TypeReference<Data<String>>() {
                        });
                        resCode = obj.getCode();
                        headImgpath = obj.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resCode == Constant.SUCCESSCODE) {
                                    Toast.makeText(UserProfile.this, "上传头像成功", Toast.LENGTH_SHORT).show();
                                    uploadUserIcon();
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONArray response) {
                        Log.d("APITest", "doPostJSON onSuccess JSONArray222:" + response);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d("APITest", "doPostJSON onFailure333:" + error_msg);
                    }
                });

    }


    private void uploadUserIcon() {
        String nickname;
        if (choosepic_isok) {
            String path;
            path = FileUtils.getFilePathByUri(this, mUserIconUri);
            PictureUtil.compressImage(path, 600, 800, 800);
            File file = new File(path);
            nickname = edit_nick_name.getText().toString();
        } else {
            nickname = edit_nick_name.getText().toString();
            if (originName.equals(nickname)) {
                Toast.makeText(UserProfile.this, "修改前后的昵称一致哦", Toast.LENGTH_LONG).show();
                return;
            }
        }

        ModifyHeadImg modifyHeadImg = new ModifyHeadImg(nickname, "");
        String jsonstyle = JSON.toJSONString(modifyHeadImg);
        // 修改昵称/头像
        final MediaType JSONStyle = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSONStyle, jsonstyle);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(Constant.SFSHURL + Constant.ModifyHeadImg)
                .addHeader("cookie", username)
                .put(requestBody)
                .build();
        mMyOkhttp.getOkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserProfile.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("APITest", "xiugai onSuccess:" + response.body().toString());
                Answer obj = (Answer) JSON.parseObject(response.body().string(), new TypeReference<Answer>() {
                });
                resCode = obj.getCode();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resCode == Constant.SUCCESSCODE) {
                            Toast.makeText(UserProfile.this, "成功修改", Toast.LENGTH_LONG).show();
                            finish();
                        } else if (resCode == Constant.NOTlOG) {
                            Toast.makeText(UserProfile.this, "请先登录", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        //修改头像
    }


    private void handleCaptureResult() {
        Uri photoFileUri = mCapturePhotoUtil.getPhotoUri();
        if (photoFileUri != null) {
            mUserIconUri = photoFileUri;
            Glide.with(this).load(mUserIconUri).into(civ_user_icon);
        } else {
            showToast("返回数据有误");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                showToast("已授权");
            } else {
                showToast("未开启授权");
            }
        }
    }
}
