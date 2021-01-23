package com.imooc.springcloud;

import com.imooc.springcloud.domain.GatewayRoute;
import com.imooc.springcloud.domain.GatewayRouteDto;
import com.imooc.springcloud.domain.GatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private GatewayServiceHandler gatewayServiceHandler;

    @Autowired
    private GatewayRouteService gatewayRouteService;

    /**
     * 刷新路由配置
     */
    @GetMapping("/refresh")
    public void refresh() throws Exception {
         this.gatewayServiceHandler.loadRouteConfig();
    }

    /**
     * 增加路由记录
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRouteDto gatewayRouteDto) throws Exception {
        gatewayRouteService.add(gatewayRouteDto);
        GatewayRoute gatewayRoute=new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayServiceHandler.saveRoute(gatewayRoute);
        return "success";
    }

    @PostMapping("/update")
    public String update(@RequestBody GatewayRouteDto gatewayRouteDto) throws Exception {
        gatewayRouteService.update(gatewayRouteDto);
        GatewayRoute gatewayRoute=new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayServiceHandler.update(gatewayRoute);
        return "success";
    }

    @GetMapping("/delete")
    public String delete(@PathVariable String id) throws Exception {
        gatewayRouteService.delete(id);
        gatewayServiceHandler.deleteRoute(id);
        return "success";
    }

}
