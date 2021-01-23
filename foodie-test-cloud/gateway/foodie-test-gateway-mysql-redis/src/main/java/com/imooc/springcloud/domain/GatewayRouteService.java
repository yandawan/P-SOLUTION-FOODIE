package com.imooc.springcloud.domain;

import java.util.Date;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayRouteService {

    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    public Integer add(GatewayRouteDto gatewayRouteDto) {
        GatewayRoute gatewayRoute = new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayRoute.setCreateDate(new Date());
        gatewayRoute.setCreatorId("");
        return gatewayRouteMapper.insertSelective(gatewayRoute);
    }

    public Integer update(GatewayRouteDto gatewayRouteDto) {
        GatewayRoute gatewayRoute = new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayRoute.setUpdateDate(new Date());
        gatewayRoute.setUpdateId("");
        return gatewayRouteMapper.updateByPrimaryKeySelective(gatewayRoute);
    }

    public Integer delete(String id) {
        return gatewayRouteMapper.deleteByPrimaryKey(Long.parseLong(id));
    }
}