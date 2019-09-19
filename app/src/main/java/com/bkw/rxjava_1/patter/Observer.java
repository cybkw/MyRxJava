package com.bkw.rxjava_1.patter;

/**
 * 观察者标准接口
 *
 * @author bkw
 */
public interface Observer {

    /**
     * 被观察者发生了改变
     *
     * @param observableInfo
     * @param <T>
     */
    <T> void changeAction(T observableInfo);
}
