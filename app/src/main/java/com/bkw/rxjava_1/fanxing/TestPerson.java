package com.bkw.rxjava_1.fanxing;

public class TestPerson<T> {
    private T t;

    public void add(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }
}
