package com.bkw.rxjava_1.net;

import io.reactivex.Observable;
import retrofit2.http.Body;

/**
 * 请求网络接口
 *
 * @author bkw
 */
public interface IRequestNetwork {
    /**
     * 注册请求接口
     *
     * @param regRequest
     * @return
     */
    public Observable<RegResponse> register(@Body RegRequest regRequest);

    /**
     * 登录请求接口
     */
    public Observable<LoginRequest> login(@Body LoginRequest request);
}
