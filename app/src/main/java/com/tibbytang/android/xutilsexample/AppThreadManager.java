package com.tibbytang.android.xutilsexample;


import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池处理所有请求、耗时操作 TODO 类描述
 * <p/>
 * 创建时间: 2014年10月22日 上午11:32:26 <br/>
 *
 * @author xnjiang
 * @since v0.0.1
 */
public class AppThreadManager {
    // 线程池的对象实例
    private static volatile AppThreadManager instance = null;
    // 线程池的对象
    private ThreadPoolExecutor m_ThreadPoolExecutor;

    /**
     * 说明：下面这些常量我是根据AsyncTask的源码配置的，大家可以根据自己需求自行配置
     */
    //根据cpu的数量动态的配置核心线程数和最大线程数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数 = CPU核心数 + 1
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //线程池最大线程数 = CPU核心数 * 2 + 1
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    //非核心线程闲置时超时1s,线程活跃时间 秒，超时线程会被回收
    private static final int KEEP_ALIVE = 1;
    //等待队列大小
    private int QUEUE_SIZE = 128;


    public synchronized static AppThreadManager getInstance() {
        if (instance == null) {
            instance = new AppThreadManager();
        }
        return instance;
    }

    public AppThreadManager() {
        m_ThreadPoolExecutor = getThreadPoolExecutor();
        Log.d("AppThreadManager", "CPU INFO " +
                "CORE_POOL_SIZE=" + CORE_POOL_SIZE +
                "MAXIMUM_POOL_SIZE=" + MAXIMUM_POOL_SIZE);
    }

    // 要确保该类只有一个实例对象，避免产生过多对象消费资源，所以采用单例模式
    private ThreadPoolExecutor getThreadPoolExecutor() {
        if (m_ThreadPoolExecutor == null) {
            /**
             * tag 针对每个TAG 获取对应的线程池
             * corePoolSize:核心线程数
             * maximumPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
             * keepAliveTime：非核心线程闲置时间超时时长
             * unit：keepAliveTime的单位
             * workQueue：等待队列，存储还未执行的任务
             * threadFactory：线程创建的工厂
             * handler：异常处理机制
             */
            m_ThreadPoolExecutor = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(QUEUE_SIZE),
                    threadFactory,
                    rejectHandler);
            //允许核心线程闲置超时时被回收
            m_ThreadPoolExecutor.allowCoreThreadTimeOut(true);
        }
        return m_ThreadPoolExecutor;
    }

    /**
     * 线程工厂接口，只有一个new Thread(Runnable r)方法，可为线程池创建新线程
     */
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "myThreadPool thread:" + integer.getAndIncrement());
        }
    };
    //线程池任务满载后采取的任务拒绝策略
    RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.AbortPolicy() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//            super.rejectedExecution(r, e);
            Log.d("", "RejectedExecutionHandler  rejectedExecution----");
        }
    };


    public void start(Runnable runnable) {
        if (runnable != null && getThreadPoolExecutor() != null) {
            getThreadPoolExecutor().execute(runnable);
        }
    }

    public void execute(FutureTask futureTask) {
        if (futureTask != null && getThreadPoolExecutor() != null) {
            getThreadPoolExecutor().execute(futureTask);
        }
    }

    public static void cancel(FutureTask futureTask) {
        if (futureTask != null) {
            futureTask.cancel(true);
        }
    }

    public Future<?> submitTask(Runnable task) {
        if (task != null && getThreadPoolExecutor() != null) {
            return getThreadPoolExecutor().submit(task);
        }
        return null;
    }

    /**
     * 关闭线程池
     */
    public void stop() {
        if (instance == null) {
            return;
        } else {
            if (instance.m_ThreadPoolExecutor != null) {
                //shutdown()：关闭线程池后不影响已经提交的任务
                //shutdownNow()：关闭线程池后会尝试去终止正在执行任务的线程
                instance.m_ThreadPoolExecutor.shutdownNow();// 关闭线程池
                instance.m_ThreadPoolExecutor = null;
                instance = null;
            }
        }
    }
}
