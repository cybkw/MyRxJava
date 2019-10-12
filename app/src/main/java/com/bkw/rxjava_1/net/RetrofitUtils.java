package com.bkw.rxjava_1.net;

import com.bkw.rxjava_1.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit网络请求工具对象
 *
 * @author bkw
 */
public class RetrofitUtils {

    /**创建Retrofit对象
     * @return
     */
    public static Retrofit create(){
        //构建OkHttpClient
        OkHttpClient.Builder okhttpBuilder=new OkHttpClient.Builder();

        okhttpBuilder.readTimeout(10, TimeUnit.SECONDS);
        okhttpBuilder.connectTimeout(20,TimeUnit.SECONDS);

        if (BuildConfig.DEBUG){
            //显示请求日志信息拦截器
            HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpBuilder.addInterceptor(interceptor);
        }

        return new Retrofit.Builder().baseUrl("http://www.baidu.com")
                .client(okhttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
