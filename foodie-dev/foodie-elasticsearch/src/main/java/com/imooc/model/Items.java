package com.imooc.model;

import lombok.Data;

@Data
public class Items {

    private String itemId;

    private String itemName;

    private String imgUrl;

    private Integer price;

    private Integer sellCounts;
}