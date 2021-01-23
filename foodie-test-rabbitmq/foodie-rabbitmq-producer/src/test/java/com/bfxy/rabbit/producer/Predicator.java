package com.bfxy.rabbit.producer;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Predicate;

class MyPredicate<T> implements Predicate<T>{
    Predicate<T> local;
    MyPredicate(Predicate<T> t){ local=t;}
    @Override public boolean test(T t) { return local.test(t);}
}

public class Predicator {
    static boolean testit(Object s, Predicate<Object> p)
    {
        return p.test(s);
    }
    public static void main(String[] args) {
        ConcurrentSkipListMap<String,Integer> cslMap = new ConcurrentSkipListMap<String,Integer>();

    }
}