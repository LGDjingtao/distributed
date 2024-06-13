package org.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试线程池
 */
@Slf4j
public class ThreadPoolTest {
    private final AtomicInteger index = new AtomicInteger();
    //核心线程数 线程池中会维护一个最小的线程数量
    int corePoolSize = 1;
    //最大线程数
    int maximumPoolSize = 1;
    //一个线程如果处于空闲状态，并且当前的线程数量大于corePoolSize，那么在指定时间后，这个空闲线程会被销毁
    long keepAliveTime = 1000;
    // keepAliveTime 单位
    TimeUnit unit = TimeUnit.MILLISECONDS;
    /**
     * 工作队列
     * ①ArrayBlockingQueue
     * 基于数组的有界阻塞队列，按FIFO排序。新任务进来后，会放到该队列的队尾，有界的数组可以防止资源耗尽问题。当线程池中线程数量达到corePoolSize后，再有新任务进来，则会将任务放入该队列的队尾，等待被调度。如果队列已经是满的，则创建一个新线程，如果线程数量已经达到maxPoolSize，则会执行拒绝策略。
     * ②LinkedBlockingQuene
     * 基于链表的无界阻塞队列（其实最大容量为Interger.MAX），按照FIFO排序。由于该队列的近似无界性，当线程池中线程数量达到corePoolSize后，再有新任务进来，会一直存入该队列，而基本不会去创建新线程直到maxPoolSize（很难达到Interger.MAX这个数），因此使用该工作队列时，参数maxPoolSize其实是不起作用的。
     * ③SynchronousQuene
     * 一个不缓存任务的阻塞队列，生产者放入一个任务必须等到消费者取出这个任务。也就是说新任务进来时，不会缓存，而是直接被调度执行该任务，如果没有可用线程，则创建新线程，如果线程数量达到maxPoolSize，则执行拒绝策略。
     * ④PriorityBlockingQueue
     * 具有优先级的无界阻塞队列，优先级通过参数Comparator实现。
     */
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(1);
    //threadFactory 线程工厂
    ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "线程-core-" + index.incrementAndGet());
        }
    };

    /**
     * 拒绝策略
     * ①CallerRunsPolicy
     * 该策略下，在调用者线程中直接执行被拒绝任务的run方法，除非线程池已经shutdown，则直接抛弃任务。
     * ②AbortPolicy
     * 该策略下，直接丢弃任务，并抛出RejectedExecutionException异常。
     * ③DiscardPolicy
     * 该策略下，直接丢弃任务，什么都不做。
     * ④DiscardOldestPolicy
     * 该策略下，抛弃进入队列最早的那个任务，然后尝试把这次拒绝的任务放入队列
     */
    ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, callerRunsPolicy);


    /**
     * ThreadPoolExecutor
     */
    @Test
    public void threadPoolExecutor_Test() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            threadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String name = Thread.currentThread().getName();
                System.out.println(name);
            });
        }

    }
}
