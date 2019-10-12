package com.scex.my_rxjava.thread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.scex.my_rxjava.MyObservable;
import com.scex.my_rxjava.MyObserver;
import com.scex.my_rxjava.ObservableOnSubscribe;
import com.scex.my_rxjava.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void test2(View view) {
        MyObservable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(MyObserver<? super String> observer) {
                Log.e("TAG", "被观察者的线程：" + Thread.currentThread().getName());
                observer.onNext("s");
            }
        })
                .observableOn()
                .mainThreadOn()
                .subscribe(new MyObserver<String>() {
                    @Override
                    public void onSubscribe() {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("TAG", "观察者所在线程：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
