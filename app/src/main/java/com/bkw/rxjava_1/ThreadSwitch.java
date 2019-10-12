package com.bkw.rxjava_1;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava线程切换
 *
 * @author bkw
 */
public class ThreadSwitch extends AppCompatActivity {

    private static final String TAG = "ThreadSwitch";

    String path = "https://upload-images.jianshu.io/upload_images/944365-cb83a0941586cc7c.png?imageMogr2/auto-orient/strip|imageView2/2/w/711/format/webp";

    private ImageView imageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
    }

    /**
     * 线程切换
     * Schedulers.io() ：常见，用于耗时操作，IO,网络
     * Schedulers.newThread() : 普通线程
     * Schedulers.computation():代表CPU大量计算所需要的线程
     * <p>
     * AndroidSchedulers.mainThread() ：Android特有的，主线程。
     * ------------------------------------------
     * Schedulers.io()-给上游分配多次，只会在第一次切换有效，后续不再切换。
     * <p>
     * AndroidSchedulers.mainThread()-给观察者设置，分配多次就会切换多次。
     *
     * @param view
     */
    public void r01(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.e(TAG, "被观察者所在线程: " + Thread.currentThread());
            }
        })
                //被观察者运行在子线程，
                .subscribeOn(Schedulers.io())
                //观察者运行在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "观察者所在线程: " + Thread.currentThread());
                    }
                });

    }

    public void r02(View view) {
        //默认情况都是在主线程中
        //TODO 默认主线程的情况下，被观察者发送一个事件，观察者就会接收到一个事件。
        //TODO 被观察者运行在子线程，则会在子线程中将所有事件发出后，观察者才会接收到。
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        })
                //给被观察者分配异步线程
                .subscribeOn(Schedulers.io())
                //给观察者分配主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 使用RxJava下载图片
     *
     * @param view
     */
    public void r03(View view) {

//        Observable.create(new ObservableOnSubscribe<Bitmap>() {
//            @Override
//            public void subscribe(ObservableEmitter<Bitmap> emitter) {
//                try {
//                    URL url = new URL(path);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        InputStream inputStream = connection.getInputStream();
//                        if (inputStream != null) {
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            emitter.onNext(bitmap);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Bitmap>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        progressDialog = new ProgressDialog(ThreadSwitch.this);
//        progressDialog.show();
//                    }
//
//                    @Override
//                    public void onNext(Bitmap bitmap) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                     //发生异常时执行 如：给image设置一张本地加载失败图片
//                    }
//
//                    @Override
//                    public void onComplete() {
//        progressDialog.dismiss();
//                    }
//                });

        //TODO 第二种方式
        Observable.just(path)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        Bitmap bitmap = null;

                        try {
                            URL url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                InputStream inputStream = connection.getInputStream();
                                if (inputStream != null) {
                                    bitmap = BitmapFactory.decodeStream(inputStream);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return bitmap;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        //给图片加水印
                        Paint paint = new Paint();
                        paint.setStrokeWidth(8);
                        paint.setTextSize(20f);
                        paint.setColor(Color.RED);
                        Bitmap b = drawTextToBitmap(bitmap, "RxJava下载图片", paint, 60, 60);
                        return b;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    private Bitmap drawTextToBitmap(Bitmap bitmap, String text, Paint paint, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

}
