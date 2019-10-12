package com.scex.my_rxjava.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scex.my_rxjava.Funcation;
import com.scex.my_rxjava.MyObservable;
import com.scex.my_rxjava.MyObserver;
import com.scex.my_rxjava.ObservableOnSubscribe;
import com.scex.my_rxjava.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //手写Just操作符
        MyObservable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(MyObserver<? super Integer> observer) {
                observer.onNext(01);
            }
        }).map(new Funcation<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "变换：" + integer;
            }
        }).subscribe(new MyObserver<String>() {
            @Override
            public void onSubscribe() {

            }

            @Override
            public void onNext(String s) {
                Log.e("TAG", "变换后" + s);
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
