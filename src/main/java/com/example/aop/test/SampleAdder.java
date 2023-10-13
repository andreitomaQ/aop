package com.example.aop.test;

import org.springframework.stereotype.Component;

@Component
public class SampleAdder {
    public int add(int a, int b) {
        return a + b;
    }
}
