package com.scex.my_rxjava.just;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scex.my_rxjava.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //手写Just操作符
//        MyObservable.just("A",1, "C").subscribe(new MyObserver<String>() {
//            @Override
//            public void onSubscribe() {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.e("TAG", s);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }
}
