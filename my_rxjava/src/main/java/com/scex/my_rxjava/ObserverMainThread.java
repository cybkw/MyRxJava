package com.scex.my_rxjava;

import android.os.Handler;
import android.os.Looper;

/**
 * 给观察者分配主线程
 *
 * @param <T>
 */
public class ObserverMainThread<T> implements ObservableOnSubscribe<T> {

    /**
     * 上游对象
     */
    private ObservableOnSubscribe<T> source;

    public ObserverMainThread(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    public void subscribe(MyObserver<? super T> observer) {
        PackageObserver<T> packageObserver = new PackageObserver(observer);
        source.subscribe(packageObserver);
    }

    /**
     * 包裹一层 Observer
     */
    private final class PackageObserver<T> implements MyObserver<T> {

        private MyObserver<T> observer;

        public PackageObserver(MyObserver<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {
        }

        @Override
        public void onNext(final T t) {
            //Handler
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //指定切回到Android主线程
                    observer.onNext(t);
                }
            });
        }

        @Override
        public void onError(final Throwable throwable) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //指定切回到Android主线程
                    observer.onError(throwable);
                }
            });
        }

        @Override
        public void onComplete() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //指定切回到Android主线程
                    observer.onComplete();
                }
            });
        }
    }

}
