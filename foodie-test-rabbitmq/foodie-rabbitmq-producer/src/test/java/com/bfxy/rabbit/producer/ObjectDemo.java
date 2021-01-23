package com.bfxy.rabbit.producer;

import java.util.Formatter;
import java.util.Optional;
import java.util.stream.Stream;

public class ObjectDemo {

    public static void main(String[] args) {
        Stream<ClassA> s = Stream.of(new ClassA(),new ClassA());
        s.forEach( x ->
                { try {  x.m1(); }
                catch (Exception e) {} }
        );
    }
}

class BaseLogger {
    private static BaseLogger log = new BaseLogger();
    private BaseLogger() {}
    public synchronized static BaseLogger getInstance() {
        return log;
    }
    private StringBuilder logMessage = new StringBuilder();
    public void addLog(String logMessage) {
        this.logMessage.append(logMessage + "|");
        //Logic to write log into file.
    }
    public void printLog() {
        System.out.println(logMessage.toString());//To print log.
    }
}