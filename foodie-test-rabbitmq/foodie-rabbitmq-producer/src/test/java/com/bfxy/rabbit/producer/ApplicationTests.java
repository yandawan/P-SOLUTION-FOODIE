package com.bfxy.rabbit.producer;

import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;


public class ApplicationTests {
    @Test
    public void testSender() throws Exception {
        Arrays.asList("red","green","blue")
                .stream()
                .map(s -> !s.startsWith("b"))
                .forEach(System.out::println);
    }
}

class MyException extends RuntimeException{
    final Optional<String> reason;
    MyException() {
        super();
        reason = Optional.empty();
    }
    MyException(String s) {
        super(s);
        reason = Optional.ofNullable(s);
    }
    @Override public String getLocalizedMessage() {
        return reason.orElse("No Reason Given");
    }
}