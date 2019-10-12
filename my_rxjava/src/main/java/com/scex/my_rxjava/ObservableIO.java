package com.scex.my_rxjava;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 被观察者的线程
 *
 * @param <T>
 */
public class ObservableIO<T> implements ObservableOnSubscribe<T> {
    /**
     * 线程池
     */
    private final static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>()
            , new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("RxJava异步线程工厂");
            return thread;
        }
    });


    /**
     * 拿到上一层被观察者
     */
    private ObservableOnSubscribe<T> source;

    public ObservableIO(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    public void subscribe(final MyObserver<? super T> observer) {
        EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                //在异步线程中执行
                source.subscribe(observer);
            }
        });
    }
}
