package com.scex.my_rxjava;

/**
 * 自定义观察者
 *
 * @author bkw
 */
public interface MyObserver<T> {
    void onSubscribe();

    void onNext(T t);

    void onError(Throwable throwable);

    void onComplete();
}
