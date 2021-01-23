package com.imooc.springcloud.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRoute {
    private Long id;

    private String serviceId;

    private String uri;

    private String predicates;

    private String filters;

    private String order;

    private String creatorId;

    private Date createDate;

    private String updateId;

    private Date updateDate;

    private String remarks;

    private String delFlag;
}
