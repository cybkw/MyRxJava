package com.bkw.rxjava_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class Exceptions extends AppCompatActivity {

    private static final String TAG = "Exceptions";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 异常操作符
     * onErrorReturn
     * 1.能够接受e.onError ；
     * 2.如果接受到异常，会中断上游后续反射的所有事件。
     * <p>
     * 如果不使用异常操作符，发生异常就会走onError(）
     * 使用异常操作符，会将处理异常后的事件发送给观察者onNext() ，如将标识码400传递给观察者
     *
     * @param view
     */
    public void r01(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        emitter.onError(new IllegalAccessError("我要报错了"));
                    }
                }
            }
        }).onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                return 400;
            }
        })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "发生了异常:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * onErrorResumeNext()
     *
     * @param view
     */
    public void r02(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        emitter.onError(new IllegalAccessError("我要报错了"));
                    }
                }
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                return null;
            }
        })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "发生了异常:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * onExceptionResumeNext()
     * 可接受的错误异常才使用。
     * 发生异常时可让APP不崩溃
     *
     * @param view
     */
    public void r03(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        emitter.onError(new IllegalAccessError("我要报错了"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        }).onExceptionResumeNext(new ObservableSource<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observer) {
                //可避免程序崩溃，将异常标识符发送个onNext的
                observer.onNext(404);
            }
        })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "发生了异常:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * retry 发生异常时进行重试。
     * 可指定重试次数。retry（2，new pre）
     *
     * @param view
     */
    public void r04(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        emitter.onError(new IllegalAccessError("我要报错了"));
                    } else {
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        }).retry(new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                Log.e(TAG, "test: " + throwable.getMessage());
                //false代表不重试，true代表一直重试。
                return false;
            }
        })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "发生了异常:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
