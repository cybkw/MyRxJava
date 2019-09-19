package com.bkw.rxjava_1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bkw.rxjava_1.utils.StatusBarUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 过滤型操作符
 *
 * @author bkw
 */
public class Filters extends AppCompatActivity {

    private static final String TAG = "MainActivity4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.setTransparent(this);
        StatusBarUtils.getStatusBarHeight(this);
        //设置状态栏颜色
        StatusBarUtils.setColor(this, ContextCompat.getColor(getApplication(), R.color.colorPrimary));

    }

    /**
     * Filter（） 作用
     * 过滤 特定条件的事件
     *
     * @param view
     */
    public void r01(View view) {
        //被观察者
        Observable.just("东风", "西南", "小北")

                //过滤操作符
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        //return false 全部都不会发送，return true全部发送
                        return s.equals("东风");
                    }
                })

                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //只接收符合条件的
                        Log.e(TAG, "过滤后得到的事件是: " + s);
                    }
                });
    }

    /**
     * ofType（）
     * 作用
     * 过滤 特定数据类型的数据
     *
     * @param view
     */
    public void r02(View view) {
        //上游
        Observable.just("cy", 1, 2.1, "里")
                .ofType(Integer.class) //筛选出整数类型
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "获取到的整型事件元素是：" + integer);
                    }
                });
    }

    /**
     * skip（） / skipLast（）
     * 作用： 跳过某个事件
     *
     * @param view
     */
    public void r03(View view) {
        // 使用1：根据顺序跳过数据项
        Observable.just(1, 2, 3, 4, 5)
                .skip(1) // 跳过正序的前1项
                .skipLast(2) // 跳过正序的后2项
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的整型事件元素是： " + integer);
                    }
                });

// 使用2：根据时间跳过数据项
        // 发送事件特点：发送数据0-5，每隔1s发送一次，每次递增1；第1次发送延迟0s
        Observable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                .skip(1, TimeUnit.SECONDS) // 跳过第1s发送的数据
                .skipLast(1, TimeUnit.SECONDS) // 跳过最后1s发送的数据
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long along) throws Exception {
                        Log.d(TAG, "获取到的整型事件元素是： " + along);
                    }
                });
    }

    /**
     * distinct（） / distinctUntilChanged（）
     * 过滤事件序列中重复的事件 / 连续重复的事件
     */
    public void r04(View view) {
        // 使用1：过滤事件序列中重复的事件
        Observable.just(1, 2, 3, 1, 2)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "不重复的整型事件元素是： " + integer);
                    }
                });

        // 使用2：过滤事件序列中 连续重复的事件
        // 下面序列中，连续重复的事件 = 3、4
        Observable.just(1, 2, 3, 1, 2, 3, 3, 4, 4)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "不连续重复的整型事件元素是： " + integer);
                    }
                });
    }

    /**
     * 根据 指定事件数量 过滤事件
     * take（） & takeLast（）
     * <p>
     * 需求场景
     * 通过设置指定的事件数量，仅发送特定数量的事件
     *
     * @param view
     */
    public void r05(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 1. 发送5个事件
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
            }

            // 采用take（）变换操作符
            // 指定了观察者只能接收2个事件
        }).take(2)
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "过滤后得到的事件是：" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });

        // 实际上，可理解为：被观察者还是发送了5个事件，只是因为操作符的存在拦截了3个事件，最终观察者接收到的是2个事件
    }

    /**
     * 根据指定时间过滤操作符
     * ： throttleFirst（）/ throttleLast（）
     * 在某段时间内，只发送该段时间内第1次事件 / 最后1次事件
     *
     * @param view
     */
    public void r06(View view) {


        //在某段时间内，只发送该段时间内第1次事件 -> >
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);

                e.onNext(2);
                Thread.sleep(400);

                e.onNext(3);
                Thread.sleep(300);

                e.onNext(4);
                Thread.sleep(300);

                e.onNext(5);
                Thread.sleep(300);

                e.onNext(6);
                Thread.sleep(400);

                e.onNext(7);
                Thread.sleep(300);
                e.onNext(8);

                Thread.sleep(300);
                e.onNext(9);

                Thread.sleep(300);
                e.onComplete();
            }
        }).throttleFirst(1, TimeUnit.SECONDS)//每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });


        //在某段时间内，只发送该段时间内最后1次事件 -> >
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);

                e.onNext(2);
                Thread.sleep(400);

                e.onNext(3);
                Thread.sleep(300);

                e.onNext(4);
                Thread.sleep(300);

                e.onNext(5);
                Thread.sleep(300);

                e.onNext(6);
                Thread.sleep(400);

                e.onNext(7);
                Thread.sleep(300);
                e.onNext(8);

                Thread.sleep(300);
                e.onNext(9);

                Thread.sleep(300);
                e.onComplete();
            }
        }).throttleLast(1, TimeUnit.SECONDS)//每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }


    /**
     * 在某段时间内，只发送该段时间内最新（最后）1次事件
     * Sample()
     * 仅需要把throttleLast（） 改成Sample（）操作符即可，此处不作过多描述
     *
     * @param view
     */
    public void r07(View view) {

    }

    /**
     * throttleWithTimeout() / debounce()
     * 应用场景：在某个事件段内判断是否有最新数据。
     * <p>
     * 发送数据事件时，若2次发送事件的间隔＜指定时间，就会丢弃前一次的数据，直到指定时间内都没有新数据发射时才会发送后一次的数据
     *
     * @param view
     */
    public void r08(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 隔段事件发送时间
                e.onNext(1);
                Thread.sleep(500);
                e.onNext(2); // 1和2之间的间隔小于指定时间1s，所以前1次数据（1）会被抛弃，2会被保留
                Thread.sleep(1500);  // 因为2和3之间的间隔大于指定时间1s，所以之前被保留的2事件将发出
                e.onNext(3);
                Thread.sleep(1500);  // 因为3和4之间的间隔大于指定时间1s，所以3事件将发出
                e.onNext(4);
                Thread.sleep(500); // 因为4和5之间的间隔小于指定时间1s，所以前1次数据（4）会被抛弃，5会被保留
                e.onNext(5);
                Thread.sleep(500); // 因为5和6之间的间隔小于指定时间1s，所以前1次数据（5）会被抛弃，6会被保留
                e.onNext(6);
                Thread.sleep(1500); // 因为6和Complete实践之间的间隔大于指定时间1s，所以之前被保留的6事件将发出

                e.onComplete();
            }
        }).throttleWithTimeout(1, TimeUnit.SECONDS)//每1秒中采用数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * firstElement（） / lastElement（）
     * 仅选取第1个元素 / 最后一个元素
     *
     * @param view
     */
    public void r09(View view) {

        // 获取第1个元素
        Observable.just(1, 2, 3, 4, 5)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的第一个事件是： " + integer);
                    }
                });

        // 获取最后1个元素
        Observable.just(1, 2, 3, 4, 5)
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的最后1个事件是： " + integer);
                    }
                });
    }

    /**
     * elementAt（）
     * 指定接收某个元素（通过 索引值 确定）
     * 注：允许越界，即获取的位置索引 ＞ 发送事件序列长度
     *
     * @param view
     */
    public void r10(View view) {
        // 使用1：获取位置索引 = 2的 元素
        // 位置索引从0开始
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的事件元素是： " + integer);
                    }
                });

        // 使用2：获取的位置索引 ＞ 发送事件序列长度时，设置默认参数
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(6, 10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的事件元素是： " + integer);
                    }
                });
    }

    /**
     * elementAtOrError（）
     * 在elementAt（）的基础上，当出现越界情况（即获取的位置索引 ＞ 发送事件序列长度）时，即抛出异常
     *
     * @param view
     */
    public void r11(View view) {
        Observable.just(1, 2, 3, 4, 5)
                .elementAtOrError(6)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "获取到的事件元素是： " + integer);
                    }
                });
    }
}
