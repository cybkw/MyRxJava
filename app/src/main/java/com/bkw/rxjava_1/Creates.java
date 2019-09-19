package com.bkw.rxjava_1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bkw.rxjava_1.utils.StatusBarUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 创建型操作符
 * @author bkw
 */
public class Creates extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.setTransparent(this);
        StatusBarUtils.getStatusBarHeight(this);
        //设置状态栏颜色
        StatusBarUtils.setColor(this, ContextCompat.getColor(getApplication(), R.color.colorPrimary));

    }

    public void r01(View view) {
        //操作符 创建Observable
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//
//            }
//        }).subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        /*
        just 可传多个参数，内部自己去发送事件的
        * */
        Observable<String> just = Observable.just("A", "B", "C");
        just.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void r02(View view) {
        String[] strings = new String[]{"a", "b", "c"};

        /**
         * 多个可变参数，也是内部自己去发送事件的，像是执行Java 的for循环遍历一样。
         * */
//        Observable.fromArray(strings).subscribe(
//                new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.e(TAG, s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                }
//        );

        //简写版使用观察者Consumer
        Observable.fromArray(strings).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, s);
            }
        });
    }

    public void r03(View view) {

        /**
         * 为什么只支持Object?
         * 上游没有发送有值的时间，下游无法确定类型，默认Object,RxJava泛型， 泛型默认类型==Object
         *
         * 使用场景，如：做一个耗时操作，但不需要任何数据来刷新UI。
         * */
        //empty()内部一定会调用onComplete完成事件
        Observable.empty().subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                //显示加载框
            }

            @Override
            public void onNext(Object o) {
                //没有事件可接收
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                //隐藏加载框
            }
        });

        Observable.empty().subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                //简化版也是接收不到事件的
            }
        });

    }

    public void r04(View view) {

        /**
         * range:内部发送事件，从10开始+1，共操作5次。
         * */
//        Observable.range(10,5).subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.e(TAG, String.valueOf(integer));
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });


        final int start=5;

        Observable.intervalRange(1, start, 1, 0, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.e(TAG, "" + (start- aLong));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
