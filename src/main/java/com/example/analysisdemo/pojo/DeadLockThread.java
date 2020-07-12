package com.example.analysisdemo.pojo;

import java.util.concurrent.TimeUnit;

/**
 * @author by Sean
 * @date 2020/7/12 21:29
 */
public class DeadLockThread implements Runnable {
    private Object lock1;
    private Object lock2;

    public DeadLockThread(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        synchronized (lock2) {
            System.out.println(Thread.currentThread().getName()+"get lock2 and wait lock1");
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName()+"get lock1 and lock2 ");
            }
        }
    }
}
