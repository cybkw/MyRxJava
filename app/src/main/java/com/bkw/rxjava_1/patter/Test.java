package com.bkw.rxjava_1.patter;

public class Test {

    public static void main(String[] args) {

        Observer observer1 = new ObserverImpl();
        Observer observer2 = new ObserverImpl();
        Observer observer3 = new ObserverImpl();

        //一个小偷 被观察者
        Observable observable = new ObservableImpl();

        //关联 注册， 警察监视着小偷
        observable.registerObserver(observer1);
        observable.registerObserver(observer2);


        //小偷发生动作
        observable.notifyObservers();
    }
}
