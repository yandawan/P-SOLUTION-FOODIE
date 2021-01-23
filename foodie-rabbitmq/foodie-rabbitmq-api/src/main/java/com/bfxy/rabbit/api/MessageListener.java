package com.bfxy.rabbit.api;

public interface MessageListener {
    void onMessage(Message message);
}