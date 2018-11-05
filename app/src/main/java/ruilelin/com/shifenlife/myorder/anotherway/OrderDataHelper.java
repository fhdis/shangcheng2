package ruilelin.com.shifenlife.myorder.anotherway;

import java.util.ArrayList;
import java.util.List;

import ruilelin.com.shifenlife.json.PayOrder;

public class OrderDataHelper {
    public static List<Object> getDataAfterHandle(List<PayOrder> resultList) {
        List<Object> dataList = new ArrayList<Object>();
        if(resultList!=null && resultList.size()>0){
          for(PayOrder payOrder:resultList){
            //订单头部
              GoodsOrderInfo goodsOrderInfo = new GoodsOrderInfo();
              goodsOrderInfo.setSn(payOrder.getSn());
              goodsOrderInfo.setSupplierName(payOrder.getSupplierName());
              goodsOrderInfo.setStatus(payOrder.getStatus());

              //订单中部 商品信息
              List<OrderGoodsItem> itemList = new ArrayList<>();
              if(payOrder!=null && payOrder.getGoods().size()>0){
                  for(int i=0;i<payOrder.getGoods().size();i++){
                      OrderGoodsItem orderGoodsItem = new OrderGoodsItem();
                      orderGoodsItem.setGoodsName(payOrder.getGoods().get(i).getGoodsName());
                      orderGoodsItem.setGoodsNumber(payOrder.getGoods().get(i).getGoodsNumber());
                      orderGoodsItem.setGoodsPrice(payOrder.getGoods().get(i).getGoodsPrice());
                      orderGoodsItem.setSpec(payOrder.getGoods().get(i).getSpec());
                      itemList.add(orderGoodsItem);
                  }
              }

              //订单尾部
              OrderGoodsFooter orderGoodsFooter = new OrderGoodsFooter();
              orderGoodsFooter.setBeizhu("备注信息");
              orderGoodsFooter.setCreateTime(payOrder.getCreateTime());
              orderGoodsFooter.setPrice(payOrder.getPrice());
              orderGoodsFooter.setNum(String.valueOf(payOrder.getGoods().size()));
              orderGoodsFooter.setStatus(payOrder.getStatus());
              orderGoodsFooter.setId(payOrder.getId());
              orderGoodsFooter.setSn(payOrder.getSn());

              dataList.add(goodsOrderInfo);
              dataList.add(itemList);
              dataList.add(orderGoodsFooter);
          }
        }
        return dataList;
    }
}
