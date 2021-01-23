package com.bfxy.rabbit.producer;

public class SalesInvoice extends Invoice {
    public static String formatId(String oldId) {
        return oldId + "_SalesInvoice";
    }
}