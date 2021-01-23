package com.imooc.springcloud.domain;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface GatewayRouteMapper {
    List<GatewayRoute> queryAllRoutes();

    Integer insertSelective(GatewayRoute gatewayRoute);

    Integer updateByPrimaryKeySelective(GatewayRoute gatewayRoute);

    Integer deleteByPrimaryKey(long parseLong);
}
