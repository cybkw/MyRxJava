package com.bkw.rxjava_1.patter;

/**
 * 被观察者标准接口
 *
 * @author bkw
 */
public interface Observable {
    /**
     * 注册观察者
     *
     * @param observer 观察者
     */
    void registerObserver(Observer observer);

    /**
     * 移除观察者
     *
     * @param observer 观察者
     */
    void removeObserver(Observer observer);

    /**
     * 通知所有注册的观察者
     */
    void notifyObservers();
}
