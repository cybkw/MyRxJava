package com.bkw.rxjava_1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bkw.rxjava_1.utils.StatusBarUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 上游和下游 被观察者与观察者
 * @author bkw
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.setTransparent(this);
        StatusBarUtils.getStatusBarHeight(this);
        //设置状态栏颜色
        StatusBarUtils.setColor(this, ContextCompat.getColor(getApplication(), R.color.colorPrimary));

    }

    public void r01(View view) {
        //起点  被观察者
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

            }

        }).subscribe(new Observer<Integer>() {
            //终点  观察者

            @Override
            public void onSubscribe(Disposable d) {
                Log.e("TAG", d.isDisposed() + "");
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void r02(View view) {
        //上游 Observable 被观察者
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //发送事件
                Log.e("TAG", "发送事件");
                emitter.onNext(101);
                Log.e("TAG", "发送完成");
                //onComplete表示事件发送完毕。
                emitter.onComplete();
            }
        });

        //下游 Observer 观察者
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                //弹出加载框
                Log.e("TAG", "订阅了");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e("TAG", "接收处理：" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                //隐藏加载框
                Log.e("TAG", "下游接收完成");
            }
        };

        //订阅 上游(被观察者)订阅（观察者）下游
        observable.subscribe(observer);

    }

    public void r03(View view) {

        //链式调用的写法 ，效果是与r02相同
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void r04(View view) {
    }
}
