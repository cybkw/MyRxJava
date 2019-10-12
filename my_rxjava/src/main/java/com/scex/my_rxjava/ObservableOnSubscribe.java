package com.scex.my_rxjava;

public interface ObservableOnSubscribe<T> {

    /**
     * ? super 代表可写
     *
     * @param observer
     */
    void subscribe(MyObserver<? super  T> observer);
}
