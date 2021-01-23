package com.imooc.springcloud.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRouteDto {
    private Long id;

    private String serviceId;

    private String uri;

    private String predicates;

    private String filters;

    private String order;

    private String remarks;
}