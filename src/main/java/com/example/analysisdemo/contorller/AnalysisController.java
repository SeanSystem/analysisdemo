package com.example.analysisdemo.contorller;

import com.example.analysisdemo.pojo.DeadLockThread;
import com.example.analysisdemo.pojo.ThreadSwap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 问题排查demo
 *
 * @author by Sean
 * @date 2020/7/12 20:06
 */
@RestController
public class AnalysisController {

    @GetMapping("/cpu/loop")
    public void testCPULoop() {
        System.out.println("请求cpu死循环");
        Thread.currentThread().setName("loop-thread-cpu");
        int num = 0;
        while (true) {
            num++;
            if (num == Integer.MAX_VALUE) {
                System.out.println("reset");
            }
            num = 0;
        }
    }

    /**
     * 模拟内存泄漏
     */
    @GetMapping(value = "/memory/leak")
    public String leak() {
        System.out.println("模拟内存泄漏");
        ThreadLocal<Byte[]> localVariable = new ThreadLocal<Byte[]>();
        localVariable.set(new Byte[4096 * 1024]);// 为线程添加变量
        return "ok";
    }

    ExecutorService service = new ThreadPoolExecutor(4, 10,
            0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1024),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 模拟死锁
     */
    @GetMapping("/cpu/test")
    public String testCPU() throws InterruptedException {
        System.out.println("请求cpu");
        Object lock1 = new Object();
        Object lock2 = new Object();
        service.submit(new DeadLockThread(lock1, lock2), "deadLookThread-" + new Random().nextInt());
        service.submit(new DeadLockThread(lock2, lock1), "deadLookThread-" + new Random().nextInt());
        return "ok";
    }

    @GetMapping(value = "/thread/swap")
    public String theadSwap(int num) {
        System.out.println("模拟线程切换");
        for (int i = 0; i < num; i++) {
            new Thread(new ThreadSwap(new AtomicInteger(0)), "thread-swap" + i).start();
        }
        return "ok";
    }
}
