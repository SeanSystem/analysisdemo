package com.example.analysisdemo.pojo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by Sean
 * @date 2020/7/12 21:53
 */
public class ThreadSwap implements Runnable{


    private AtomicInteger integer;

    public ThreadSwap(AtomicInteger integer) {
        this.integer = integer;
    }

    @Override
    public void run() {
        while (true) {
            integer.addAndGet(1);
            Thread.yield(); //让出CPU资源
        }
    }

}
