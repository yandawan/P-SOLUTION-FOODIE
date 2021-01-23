package com.bfxy.rabbit.producer;

import java.util.SortedSet;
import java.util.TreeSet;

interface I1 { boolean f();}
interface I2 { boolean f(String s);}
interface I3 { boolean f(String s,String t);}



public class IkmTest {

    static void print(I1 i) {}
    static void print(I2 i) {}
    static void print(I3 i) {}

    private void dd(){
        print(() -> true);
    }
}