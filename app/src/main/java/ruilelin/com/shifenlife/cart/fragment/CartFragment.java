package ruilelin.com.shifenlife.cart.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ruilelin.com.shifenlife.MainActivity;
import ruilelin.com.shifenlife.MyApplication;
import ruilelin.com.shifenlife.R;
import ruilelin.com.shifenlife.base.BaseFragment;
import ruilelin.com.shifenlife.cart.adapter.CartAdapter;
import ruilelin.com.shifenlife.cart.bean.ShoppingCarDataBean;
import ruilelin.com.shifenlife.cart.customview.RoundCornerDialog;
import ruilelin.com.shifenlife.cart.util.ToastUtil;
import ruilelin.com.shifenlife.json.Answer;
import ruilelin.com.shifenlife.json.DeleteOne;
import ruilelin.com.shifenlife.json.DleteMore;
import ruilelin.com.shifenlife.utils.Constant;

public class CartFragment extends BaseFragment {


    private TextView tvTitlebarCenter;
    private TextView tvTitlebarRight;
    private ExpandableListView elvShoppingCar;
    private ImageView ivSelectAll;
    private LinearLayout llSelectAll;
    private Button btnOrder;
    private Button btnDelete;
    private TextView tvTotalPrice;
    private RelativeLayout rlTotalPrice;
    private RelativeLayout rl;
    private ImageView ivNoContant;
    private RelativeLayout rlNoContant;
    private TextView tvTitlebarLeft;
    private int resCode = -1;
    private ShoppingCarDataBean cartBean;
    private List<ShoppingCarDataBean.DatasBean> datas;
    private CartAdapter cartAdapter;
    private MyOkHttp mMyOkhttp;
    private String username;
    private Button button_add;

    OnEditListener editListener;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cart, null);
        tvTitlebarCenter = (TextView)view.findViewById(R.id.tv_titlebar_center);
        tvTitlebarRight = (TextView)view.findViewById(R.id.tv_titlebar_right);
        elvShoppingCar = (ExpandableListView)view.findViewById(R.id.elv_shopping_car);
        ivSelectAll = (ImageView)view.findViewById(R.id.iv_select_all);
        llSelectAll = (LinearLayout)view.findViewById(R.id.ll_select_all);

        btnOrder = (Button)view.findViewById(R.id.btn_order);
        btnDelete = (Button)view.findViewById(R.id.btn_delete);
        button_add = (Button)view.findViewById(R.id.button_add);
        tvTotalPrice = (TextView)view.findViewById(R.id.tv_total_price);
        rlTotalPrice = (RelativeLayout)view.findViewById(R.id.rl_total_price);
        rl = (RelativeLayout)view.findViewById(R.id.rl);
        ivNoContant = (ImageView)view.findViewById(R.id.iv_no_contant);
        rlNoContant = (RelativeLayout)view.findViewById(R.id.rl_no_contant);
        tvTitlebarLeft = (TextView)view.findViewById(R.id.tv_titlebar_left);
        mMyOkhttp = MyApplication.getInstance().getMyOkHttp();
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initExpandableListView();
        initData();
        tvTitlebarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit = tvTitlebarRight.getText().toString().trim();
                if (edit.equals("编辑")) {
                    tvTitlebarRight.setText("完成");
                    rlTotalPrice.setVisibility(View.GONE);
                    btnOrder.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    tvTitlebarRight.setText("编辑");
                    rlTotalPrice.setVisibility(View.VISIBLE);
                    btnOrder.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.GONE);
                }
            }
        });

        tvTitlebarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editListener!=null){
                    editListener.onEditSelected();
                }
            }
        });

        elvShoppingCar.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                return true;
            }
        });
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        SharedPreferences sharedPreferences =mContext.getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("cookie", "");
        /**
         * 实际开发中，通过请求后台接口获取购物车数据并解析
         */
        mMyOkhttp.get()
               // .addHeader("cookie",username)
                .url(Constant.SFSHURL+Constant.ShoppingCarList)
                // .jsonParams(jsonstyle)          //与params不共存 以jsonParams优先
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        resCode = JSON.parseObject(response.toString(), ShoppingCarDataBean.class).getCode();
                        cartBean = JSON.parseObject(response.toString(),ShoppingCarDataBean.class);
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (resCode == Constant.SUCCESSCODE) {
                                        if (cartBean != null) {
                                            datas = cartBean.getData();
                                            initExpandableListViewData(datas);
                                        }
                                    } else {
                                        ToastUtil.makeText(mContext, "糟糕，出错了额");
                                    }
                                }
                            });
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

    /**
     * 初始化ExpandableListView的数据
     * 并在数据刷新时，页面保持当前位置
     *
     * @param datas 购物车的数据
     */
    private void initExpandableListViewData(List<ShoppingCarDataBean.DatasBean> datas) {
        if (datas != null && datas.size() > 0) {
            //刷新数据时，保持当前位置
            cartAdapter.setData(datas);
            //使所有组展开
            for (int i = 0; i < cartAdapter.getGroupCount(); i++) {
                elvShoppingCar.expandGroup(i);
            }

            //使组点击无效果
            elvShoppingCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });

            tvTitlebarRight.setVisibility(View.VISIBLE);
            tvTitlebarRight.setText("编辑");
            rlNoContant.setVisibility(View.GONE);
            elvShoppingCar.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            rlTotalPrice.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
            tvTitlebarLeft.setVisibility(View.VISIBLE);
            tvTitlebarCenter.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
            Log.d("cart","btnDelete");
        } else {
            tvTitlebarRight.setVisibility(View.GONE);
            rlNoContant.setVisibility(View.VISIBLE);
            elvShoppingCar.setVisibility(View.GONE);
            rl.setVisibility(View.GONE);
            tvTitlebarLeft.setVisibility(View.GONE);
            tvTitlebarCenter.setVisibility(View.GONE);
        }
    }


    /**
     * 初始化ExpandableListView
     * 创建数据适配器adapter，并进行初始化操作
     */
    private void initExpandableListView() {
        cartAdapter = new CartAdapter(mContext, llSelectAll, ivSelectAll, btnOrder, btnDelete, rlTotalPrice, tvTotalPrice);
        elvShoppingCar.setAdapter(cartAdapter);

        //删除的回调
        cartAdapter.setOnDeleteListener(new CartAdapter.OnDeleteListener() {
            @Override
            public void onDelete() {
                initDelete();

                /**
                 * 实际开发中，在此请求删除接口，删除成功后，
                 * 通过initExpandableListViewData（）方法刷新购物车数据。
                 * 注：通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
                 *                  GoodsBean的isSelect属性，判断商品是否被选中，
                 *                  （true为选中，false为未选中）
                 */
            }
        });

        //修改商品数量的回调
        cartAdapter.setOnChangeCountListener(new CartAdapter.OnChangeCountListener() {
            @Override
            public void onChangeCount(String goods_id) {
                /**
                 * 实际开发中，在此请求修改商品数量的接口，商品数量修改成功后，
                 * 通过initExpandableListViewData（）方法刷新购物车数据。
                 */
            }
        });
    }

    /**
     * 判断是否要弹出删除的dialog
     * 通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
     * GoodsBean的isSelect属性，判断商品是否被选中，
     */
    private void initDelete() {
        //判断是否有店铺或商品被选中
        //true为有，则需要刷新数据；反之，则不需要；
        boolean hasSelect = false;
        //创建临时的List，用于存储没有被选中的购物车数据
        List<ShoppingCarDataBean.DatasBean> datasTemp = new ArrayList<>();
        List<ShoppingCarDataBean.DatasBean.Commodity> deleteTemp = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            List<ShoppingCarDataBean.DatasBean.Commodity> goods = datas.get(i).getGoods();
            boolean isSelect_shop = datas.get(i).getSupplier().isSelect_shop();

            if (isSelect_shop) {
                hasSelect = true;
                deleteTemp.addAll(datas.get(i).getGoods());
                //跳出本次循环，继续下次循环。
                continue;
            } else {
                datasTemp.add(datas.get(i));
                datasTemp.get(datasTemp.size() - 1).setGoods(new ArrayList<ShoppingCarDataBean.DatasBean.Commodity>());
            }

            for (int y = 0; y < goods.size(); y++) {
                ShoppingCarDataBean.DatasBean.Commodity goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();

                if (isSelect) {
                    deleteTemp.add(goodsBean);
                    hasSelect = true;
                } else {
                    datasTemp.get(datasTemp.size() - 1).getGoods().add(goodsBean);
                }
            }
        }

        if (hasSelect) {
            showDeleteDialog(datasTemp,deleteTemp);
        } else {
            ToastUtil.makeText(mContext, "请选择要删除的商品");
        }
    }

    /**
     * 展示删除的dialog（可以自定义弹窗，不用删除即可）
     *
     * @param datasTemp
     */
    private void showDeleteDialog(final List<ShoppingCarDataBean.DatasBean> datasTemp,List<ShoppingCarDataBean.DatasBean.Commodity> deletetemp) {
        View view = View.inflate(mContext, R.layout.dialog_two_btn, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("确定要删除商品吗？");

        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
                Log.d("shanchu","deletetemp.size()=="+deletetemp.size());
                if(deletetemp!=null && deletetemp.size()==1) {
                    DeleteOne deleteOne = new DeleteOne();
                    String jsonstyle = JSON.toJSONString(deleteOne);
                    final MediaType JSONStyle = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSONStyle, jsonstyle);

                    Request request = new Request.Builder()
                            .addHeader("cookie", username)
                            .url(Constant.SFSHURL + Constant.RemoveId + deletetemp.get(0).getId())
                            .delete(requestBody)
                            .build();

                    mMyOkhttp.getOkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("APITest", "fail:");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("APITest", "xiugai onSuccess:" + response.body().toString());
                            Answer obj = (Answer) JSON.parseObject(response.body().string(), new TypeReference<Answer>() {
                            });
                            resCode = obj.getCode();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCode == Constant.SUCCESSCODE) {
                                            ToastUtil.makeText(mContext, "成功移除购物车");
                                        } else if (resCode == Constant.NOTlOG) {
                                            ToastUtil.makeText(mContext, "请先登录");
                                        } else {
                                            ToastUtil.makeText(mContext, "修改地址提交失败");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else if(deletetemp!=null && deletetemp.size()>1){
                    int[] ids = new int[deletetemp.size()];
                    List<Integer> jsonints = new ArrayList<>();
                    for(int i = 0;i<deletetemp.size();i++){
                        ids[i] = Integer.parseInt(deletetemp.get(i).getId());
                        jsonints.add(Integer.parseInt(deletetemp.get(i).getId()));
                    }

                    DleteMore dleteMore = new DleteMore(jsonints);
                    String jsonstyle = JSON.toJSONString(dleteMore);
                    Log.d("delete","jsonstyle=="+jsonstyle);
                    final MediaType JSONStyle = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSONStyle, jsonstyle);

                    Request request = new Request.Builder()
                            .addHeader("cookie", username)
                            .url(Constant.SFSHURL + Constant.RemoveIds)
                            .delete(requestBody)
                            .build();

                    mMyOkhttp.getOkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("APITest", "fail:");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("APITest", "xiugai onSuccess:" + response.body().toString());
                            Answer obj = (Answer) JSON.parseObject(response.body().string(), new TypeReference<Answer>() {});
                            resCode = obj.getCode();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resCode == Constant.SUCCESSCODE) {
                                            ToastUtil.makeText(mContext, "成功移除购物车");
                                        } else if (resCode == Constant.NOTlOG) {
                                            ToastUtil.makeText(mContext, "请先登录");
                                        } else {
                                            ToastUtil.makeText(mContext, "批量移除失败");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                datas = datasTemp;
                Log.d("0906","url=="+datasTemp.size());
                initExpandableListViewData(datas);

            }
        });
        //取消
        tv_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
            }
        });
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    public interface OnEditListener {
        void onEditSelected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            editListener = (OnEditListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }

    }

}
