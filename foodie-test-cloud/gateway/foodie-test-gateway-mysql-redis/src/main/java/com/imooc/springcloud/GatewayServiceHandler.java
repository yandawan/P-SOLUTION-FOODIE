package com.imooc.springcloud;

import java.net.URI;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.imooc.springcloud.domain.GatewayRoute;
import com.imooc.springcloud.domain.GatewayRouteMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 核心配置类，项目初始化加载数据库的路由配置到redis
 */
@Slf4j
@Service
public class GatewayServiceHandler implements ApplicationEventPublisherAware, CommandLineRunner {

    @Autowired
    private RedisRouteDefinitionRepository routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 自己的获取数据dao
     */
    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args){
        this.loadRouteConfig();
    }

    public void loadRouteConfig() {
        log.info("【路由开始加载】");
        // 删除redis里面的路由配置信息
        redisTemplate.delete(RedisRouteDefinitionRepository.GATEWAY_ROUTES);

        // 从数据库拿到基本路由配置
        List<GatewayRoute> gatewayRouteList = gatewayRouteMapper.queryAllRoutes();
        gatewayRouteList.forEach(gatewayRoute -> {
            RouteDefinition definition=handleData(gatewayRoute);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        });

        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        log.info("【路由加载完成】");
    }

    @SuppressWarnings("unchecked")
    public List<GatewayRoute> queryAllRoutes()
    {
        List<GatewayRoute> gatewayRouteInfos = new ArrayList<GatewayRoute>();
        redisTemplate.opsForHash().values(RedisRouteDefinitionRepository.GATEWAY_ROUTES).stream()
                .forEach(routeDefinition -> {
                    RouteDefinition definition = JSON.parseObject(routeDefinition.toString(), RouteDefinition.class);
                    gatewayRouteInfos.add(convert2GatewayRouteInfo(definition));
                });
        return gatewayRouteInfos;
    }

    public void saveRoute(GatewayRoute gatewayRoute){
        RouteDefinition definition=handleData(gatewayRoute);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(GatewayRoute gatewayRoute) {
        RouteDefinition definition=handleData(gatewayRoute);
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GatewayRoute convert2GatewayRouteInfo(Object obj)
    {
        RouteDefinition routeDefinition = (RouteDefinition) obj;
        GatewayRoute gatewayRouteInfo = new GatewayRoute();
        gatewayRouteInfo.setUri(routeDefinition.getUri().toString());
        gatewayRouteInfo.setServiceId(routeDefinition.getId());
        List<PredicateDefinition> predicates = routeDefinition.getPredicates();
        // 只有一个
        if (CollectionUtils.isNotEmpty(predicates)) {
            String predicatesString = predicates.get(0).getArgs().get("pattern");
            gatewayRouteInfo.setPredicates(predicatesString);
        }
        List<FilterDefinition> filters = routeDefinition.getFilters();
        if (CollectionUtils.isNotEmpty(filters)) {
            String filterString = filters.get(0).getArgs().get("_genkey_0");
            gatewayRouteInfo.setFilters(filterString);
        }
        gatewayRouteInfo.setOrder(String.valueOf(routeDefinition.getOrder()));;
        return gatewayRouteInfo;
    }

    public void deleteRoute(String routeId){
        routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 路由数据转换公共方法
     */
    private RouteDefinition handleData(GatewayRoute gatewayRoute){
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicate = new PredicateDefinition();
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);

        URI uri = null;
        if(gatewayRoute.getUri().startsWith("http")){
            //http地址
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRoute.getUri()).build().toUri();
        }else{
            //注册中心
            uri = UriComponentsBuilder.fromUriString("lb://"+gatewayRoute.getUri()).build().toUri();
        }

        definition.setId(gatewayRoute.getServiceId());
        // 名称是固定的，spring gateway会根据名称找对应的PredicateFactory
        predicate.setName("Path");
        predicateParams.put("pattern",gatewayRoute.getPredicates());
        predicate.setArgs(predicateParams);

        // 名称是固定的, 路径去前缀
        filterDefinition.setName("StripPrefix");
        filterParams.put("_genkey_0", gatewayRoute.getFilters().toString());
        filterDefinition.setArgs(filterParams);

        definition.setPredicates(Arrays.asList(predicate));
        definition.setFilters(Arrays.asList(filterDefinition));
        definition.setUri(uri);
        definition.setOrder(Integer.parseInt(gatewayRoute.getOrder()));

        return definition;
    }
}