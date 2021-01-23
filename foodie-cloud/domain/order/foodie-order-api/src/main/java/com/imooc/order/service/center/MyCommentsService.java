package com.imooc.order.service.center;

import com.imooc.order.pojo.OrderItems;
import com.imooc.order.pojo.bo.center.OrderItemsCommentBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("foodie-order-service")
@RequestMapping("order-comments-api")
public interface MyCommentsService {

    /**
     * 根据订单id查询关联的商品
     */
    @GetMapping("orderItems")
    public List<OrderItems> queryPendingComment(@RequestParam("orderId") String orderId);

    /**
     * 保存用户的评论
     */
    @PostMapping("saveOrderComments")
    public void saveComments(@RequestParam("orderId") String orderId,
                             @RequestParam("userId") String userId,
                             @RequestBody List<OrderItemsCommentBO> commentList);

}
