package com.bfxy.rabbit.producer;

public class Invoice {
    public static String formatId(String oldId) {
        return oldId + "_Invoice";
    }
}
