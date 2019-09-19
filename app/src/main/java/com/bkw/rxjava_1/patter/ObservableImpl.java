package com.bkw.rxjava_1.patter;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者
 *
 * @author bkw
 */
public class ObservableImpl implements Observable {

    /**
     * 容器：存储观察者
     */
    private List<Observer> observableList = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observableList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observableList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observableList) {
            //通知所有的观察者
            observer.changeAction("被观察者发生动作，通知观察者。。");
        }
    }
}
