package com.imooc.order.service.center;

import com.imooc.order.pojo.Orders;
import com.imooc.order.pojo.vo.OrderStatusCountsVO;
import com.imooc.pojo.IMOOCJSONResult;
import com.imooc.pojo.PagedGridResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("foodie-order-service")
@RequestMapping("myorder-api")
public interface MyOrdersService {

    /**
     * 查询我的订单列表
     */
    @GetMapping("order/query")
    public PagedGridResult queryMyOrders(@RequestParam("userId") String userId,
                                         @RequestParam("orderStatus") Integer orderStatus,
                                         @RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 订单状态 --> 商家发货
     */
    @PostMapping("order/delivered")
    public void updateDeliverOrderStatus(@RequestParam("orderId") String orderId);

    /**
     * 查询我的订单
     */
    @GetMapping("order/details")
    public Orders queryMyOrder(@RequestParam("userId") String userId,
                               @RequestParam("orderId") String orderId);

    /**
     * 更新订单状态 —> 确认收货
     */
    @PostMapping("order/received")
    public boolean updateReceiveOrderStatus(@RequestParam("orderId") String orderId);

    /**
     * 删除订单（逻辑删除）
     */
    @DeleteMapping("order")
    public boolean deleteOrder(@RequestParam("userId") String userId,
                               @RequestParam("orderId") String orderId);

    /**
     * 查询用户订单数
     */
    @GetMapping("order/counts")
    public OrderStatusCountsVO getOrderStatusCounts(@RequestParam("userId") String userId);

    /**
     * 获得分页的订单动向
     */
    @GetMapping("order/trend")
    public PagedGridResult getOrdersTrend(@RequestParam("userId") String userId,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("pageSize") Integer pageSize);

    @GetMapping("checkUserOrder")
    public IMOOCJSONResult checkUserOrder(@RequestParam("userId") String userId,
                                          @RequestParam("orderId") String orderId);

}