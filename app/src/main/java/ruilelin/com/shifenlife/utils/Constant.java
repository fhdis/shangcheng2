package ruilelin.com.shifenlife.utils;

public class Constant {

    public static final String BASEURL = "https://coding.net/u/tfx919/p/server/git/raw/master/ShoppingMallServer";
    public static final String HOMEURL = BASEURL + "/json/HOME_URL.json";
    public static final String IMGURL = BASEURL + "/img";
    public static final String GoodDetail = BASEURL + "/detail.jpg";
    //https://coding.net/u/tfx919/p/server/git/raw/master/ShoppingMallServer/json/HOME_URL.json

    public static final String APP_ID = "wxb090b32121969d14";
    public static final String APP_SECRET_WX = "6a7f3284be1018b87979a5b6a9aba31a";

    //public static String SFSHURL = "http://192.168.11.81:8080";  //chenglong
    public static String SFSHURL = "http://192.168.11.121:8080";
    // public static String SFSHURL = "http://sfsh.ruilelin.com";
    //public static String SFSHURL = "http://192.168.11.170:8081";  //wengqiao

    //是否设置过余额支付密码
    public static String HasBalancePassword = "/api/user/hasBalancePassword";
    //是否设置过登录密码
    public static String HasPassword = "/api/user/hasPassword";
    //充值&余额记录
    public static String RechargeRecord = "/api/user/rechargeRecord";
    //余额明细记录
    public static String BalanceRecord = "/api/user/balanceRecord/";   //{page}/{size}
    //在线充值
    public static String Online = "/api/balance/recharge/online";
    //福利卡充值
    public static String WelfareCard = "/api/balance/recharge/welfareCard";

    //获取所有收货地址
    public static String AllAddress = "/api/address/all";
    //新增收货地址
    public static String AddAddress = "/api/address";
    //编辑收货地址
    public static String EditAddress = "/api/address";
    //删除收货地址
    public static String deletsAddress = "/api/address/"; ///api/address/{id}
   //获取个人信息
    public static String UserInfo = "/api/user/info";
    //上传头像
    public static String HeadImg ="/api/upload/headImg";
    //修改昵称/头像
    public static String ModifyHeadImg ="/api/user/modify";
    //获取未读消息数量
    public static String UnReadInfo = "/api/privateMessage/notReadNum";
    //消息列表
    public static String InfoList = "/api/privateMessage/";  //{page}/{size}
    //支付
    public static String OrderPay = "/api/order/pay";  //{page}/{size}
     //购物车列表
    public static String ShoppingCarList = "/api/shoppingCar";
    //新增商品至购物车/修改购物车商品数量
    public static String AddToCart = "/api/shoppingCar";
    //移除一个商品
    public static String RemoveId = "/api/shoppingCar/"; //{goodsId}
    //移除多个商品
    public static String RemoveIds = "/api/shoppingCar/deleteBatch";
    //创建订单
    public static String CreateOrder = "/api/order";
    //订单列表
    public static String AllOrderList ="/api/order/";  //{page}/{size};
    //我的订单-进行中
    public static String Handling ="/api/order/handling/";  //{page}/{size};
    //取消订单
    public static String CancelOrder ="/api/order/cancel/";  //{orderId}
    //确认收货
    public static String ConfirmOrder ="/api/order/confirm";
    //推荐商品
    public static String RecommendGoods ="/api/goods/recommend/";   //{orderId}
    //广告图展示
    public static String Advertisings ="/api/advertisings";  // {page}/{size}/{type}
    //商品详情
    public static String GoodsDetail ="/api/goods/goodsDetail/";  // {id}
    //附近门店信息
    public static String Nearby ="/api/supplier/nearby";  // {id}
    //店铺热卖
    public static String HOT ="/api/supplier/hot/";  // {id}

    //账号密码登录
    public static String LoginWithPassword ="/api/user/login";
    //用户退出登录
    public static String LoginOut ="/api/user/loginOut";
    //获取验证码
    public static String GetVerCode = "/api/user/sms";
    //手机号+验证码登陆
    public static String LoginWithSecurityCode ="/api/user/loginWithSecurityCode";
    //手机号注册
    public static String REGISTER ="/api/user/reg";
    //找回登录密码
    public static String FindPassword ="/api/user/findPassword";
    //余额支付密码修改
    public static String ModifyBalancePassword ="/api/user/modifyBalancePassword";
    //余额支付密码设置
    public static String ResetBalancePassword ="/api/user/resetBalancePassword";
    //登录密码设置
    public static String ChangeLoginPassword ="/api/user/setPassword";
    //登录密码修改
    public static String ModifyPassword="/api/user/modifyPassword";
    //登录密码设置
    public static String ResetPassword ="/api/user/resetPassword";
    //用户反馈
    public static String Feedback ="/api/user/feedback";
    //是否绑定微信状态
    public static String IsBindWx ="/api/user/isBindWx";
    //微信登录
    public static String LoginWithWX ="/api/user/loginWithWX";
    //所有一级分类
    public static String AllCategories ="/api/goodsCategory/allCategories/"; //{id}
    //一级分类详情
    public static String FirsClassification ="/api/goodsCategory/firsClassification/";  //{id}
    //所有一级分类及分类商品(安卓专用)
    public static String AllCategoriesAndGoods ="/api/goodsCategory/allCategoriesAndGoods/";  //{id}
    //全部商品
    public static String AllProduct ="/api/goods/all/";  //{supplierId}
    //搜索
    public static String Search ="/api/goods/search/";  //{supplierId}
    //立即购买
    public static String BuyNow ="/api/order/buyNow";

    //Success
    public static int SUCCESSCODE = 0;
    //Fail
    public static int FAIL = 1;
    //尚未登录
    public static int NOTlOG = 110001;
    //修改密码 校验密码输入是否正确
    public static String OldPassword = "/api/user/oldPassword";
    //删除消息
    public static String DelateInfomation = "/api/privateMessage/delete/";
    //更新消息状态为    已读
    public static String InfomationReadout = "/api/privateMessage/modifyInfomationReadout";
}
