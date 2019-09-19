package com.bkw.rxjava_1.patter;

/**
 * 观察者
 *
 * @author bkw
 */
public class ObserverImpl implements Observer {
    @Override
    public <T> void changeAction(T observableInfo) {
        System.out.println(observableInfo);
    }
}
