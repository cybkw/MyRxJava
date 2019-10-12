package com.bkw.rxjava_1.net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bkw.rxjava_1.R;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxJavaRetrofitAc extends AppCompatActivity {

    TextView tvLogin;
    TextView tvRegit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_retrofit);

        tvLogin = findViewById(R.id.tv_login);
        tvRegit = findViewById(R.id.tv_regit);


    }

    /**
     * 1.请求服务器注册
     * 2.注册完成之后，更新UI
     * 3.登录服务器，登录成功，更新UI
     *
     * @param view
     */
    public void request(View view) {
        //第一种方式，分开写
        //调用注册接口功能方法
        RetrofitUtils.create().create(IRequestNetwork.class)
                .register(new RegRequest())   //这里是被观察者，执行耗时操作
                .subscribeOn(Schedulers.io()) //给被观察者指定子线程运行耗时操作
                .observeOn(AndroidSchedulers.mainThread()) //执行完毕，切换到主线程更新UI
                .subscribe(new Consumer<RegResponse>() {
                    @Override
                    public void accept(RegResponse regResponse) throws Exception {
                        //处理请求结果，进行UI更新
                    }
                });
        ;

        //3.登录服务器
        RetrofitUtils.create().create(IRequestNetwork.class)
                .login(new LoginRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginRequest>() {
                    @Override
                    public void accept(LoginRequest request) throws Exception {
                        //更新登录UI
                    }
                });


    }

    public void request2(View view) {
        //第二种方式，合并写法,一行代码解决
        //注册操作执行完后，接着执行登录操作
        RetrofitUtils.create().create(IRequestNetwork.class)
                .register(new RegRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                //doOnNext() 可以在没有注册观察者的情况下更新UI操作,这个方法运行在主线程中
                .doOnNext(new Consumer<RegResponse>() {
                    @Override
                    public void accept(RegResponse regResponse) throws Exception {
                        //更新注册完成后的UI
                        tvRegit.setText("注册成功");
                    }
                })
                //紧接着做登录操作，需要先切换到异步线程
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<RegResponse, Observable<LoginRequest>>() {

                    @Override
                    public Observable<LoginRequest> apply(RegResponse regResponse) throws Exception {
                        //拿到注册请求的响应对象
                        //执行登录请求
                        Observable<LoginRequest> loginRequestObservable = RetrofitUtils.create().create(IRequestNetwork.class)
                                .login(new LoginRequest());
                        Log.e("TAG", "执行登录操作");
                        return loginRequestObservable;
                    }
                })
                //登录请求完成后，切换主线程更新UI
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginRequest>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //第一步，subscribe.onSubscribe 订阅关系
                        //第二步，register 执行注册被观察者
                        //第三步，doOnNext更新注册UI
                        //第四步，flatMap apply登录操作，
                        //第五步，onNext 更新登录UI
                        Log.e("TAG", "绑定被观察者与观察者");
                    }

                    @Override
                    public void onNext(LoginRequest request) {
                        tvLogin.setText("登录完成 更新登录UI");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //第六步，关闭对话框，流程结束
                        Log.e("TAG", "流程结束");
                    }
                });
    }
}
