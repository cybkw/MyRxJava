package com.scex.my_rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyObservable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(MyObserver<? super String> observer) {
                //第二步 发送事件
                observer.onNext("ww"); //可写
                observer.onComplete();
//                observer.onNext("wo"); //? extends 报错，不可写
            }

        }).subscribe(new MyObserver<String>() {
            @Override
            public void onSubscribe() {
                //第一步
            }

            @Override
            public void onNext(String s) {
                //第三步
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                //最后一步
            }
        });
    }
}
