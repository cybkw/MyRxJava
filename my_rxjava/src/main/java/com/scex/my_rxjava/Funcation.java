package com.scex.my_rxjava;

public interface Funcation<T, R> {
    R apply(T t) throws Exception;
}
