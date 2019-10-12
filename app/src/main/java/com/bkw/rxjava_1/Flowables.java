package com.bkw.rxjava_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * RxJava背压模式
 *
 * @author bkw
 */
public class Flowables extends AppCompatActivity {
    private static final String TAG = "Flowables";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    Subscription subscription;

    /**
     * BackpressureStrategy.ERROR-被观察者不停发送大量事件，观察者阻塞，处理不过来，放入缓存池，如果池子满了，就会抛出异常。
     * <p>
     * BackpressureStrategy.BUFFER-被观察者不停发送大量事件，观察者阻塞，处理不了放入缓存池，“等待” 观察者接收处理。
     * <p>
     * BackpressureStrategy.DROP-被观察者发送大量事件，观察者阻塞，放入缓存池，如果池子满了，就会把后面的事件抛弃。
     * <p>
     * BackpressureStrategy.LATEST-被观察者不停发送事件，下游阻塞，处理不过来，只存储128个事件。
     *
     * @param view
     */
    public void r01(View view) {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                    emitter.onNext(i);
                                }
                            }
                        },
                BackpressureStrategy.BUFFER
        ).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                //只请求输出5次，给观察者。
                s.request(5);
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void r02(View view) {
        if (subscription != null) {
            subscription.request(1);
        }
    }


    /**
     * RxJava2.0
     * 背压模式打印for循环输出
     *
     * @param view
     */
    public void r03(View view) {
        final String[] strings = new String[]{"java", "hb", "vc"};
        Flowable.fromArray(strings).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s);
            }
        });

    }
}
