package com.bkw.rxjava_1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bkw.rxjava_1.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * 变换型操作符
 *
 * @author bkw
 */
public class Changes extends AppCompatActivity {

    private static final String TAG = "MainActivity3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.setTransparent(this);
        StatusBarUtils.getStatusBarHeight(this);
        //设置状态栏颜色
        StatusBarUtils.setColor(this, ContextCompat.getColor(getApplication(), R.color.colorPrimary));

    }

    /**
     * 变换操作符 map
     * 应用场景：数据类型转换，如将Integer转为String类型
     *
     * @param view
     */
    public void r01(View view) {
        //上游 发送
        int k = 1;
        Observable<Integer> observable = Observable.just(k);

        //在上游与下游之间变换,将 Integer变换为String
        observable.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                //将Integer变换为String
                Log.e(TAG, "apply：" + integer);
                return "" + integer;
            }
        })
                //再次变换，将String变成double
                .map(new Function<String, Double>() {
                    @Override
                    public Double apply(String s) throws Exception {

                        return Double.parseDouble(s);
                    }
                })

                //订阅
                .subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double integer) {
                        Log.e(TAG, "下游 onNext ：" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 变换操作符 flatMap
     * <p>
     * 应用场景：无序的将被观察者发送的整个事件序列进行变换
     *
     * @param view
     */
    public void r02(View view) {
        //上游
        Observable.just(111, 222, 333)

                //变换操作符,变换之前是Integer,ObservableSource表示再次发送事件<?> 传要变换的类型:String
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer integer) throws Exception {
                        //ObservableSource表示再次手动发送事件
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                //发送
                                emitter.onNext(integer + "flatMap变换操作符");
                            }
                        });
                    }
                })

                //订阅 下游
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept:" + s);
                    }
                });

    }

    /**
     * 变换操作符，flatMap 是不排序的
     * 应用场景：无序的将被观察者发送的整个事件序列进行变换
     * 注：新合并生成的事件序列顺序是无序的，即 与旧序列发送事件的顺序无关
     *
     * @param view
     */
    public void r03(View view) {
        //上游
        // 采用RxJava基于事件流的链式操作
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }

            // 采用flatMap（）变换操作符
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

    }

    /**
     * concatMap()
     * 应用场景：有序的将被观察者发送的整个事件序列进行变换
     * 注：新合并生成的事件序列顺序是有序的，即 严格按照旧序列发送事件的顺序
     */
    public void r04(View view) {
        // 采用RxJava基于事件流的链式操作
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }

            // 采用concatMap（）变换操作符
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过concatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    /**
     * Buffer
     * 应用场景
     * 缓存被观察者发送的事件
     * 定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
     *
     * @param view
     */
    public void r05(View view) {
        // 被观察者 需要发送5个数字
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 1) // 设置缓存区大小 & 步长
                // 缓存区大小 = 每次从被观察者中获取的事件数量
                // 步长 = 每次获取新事件的数量
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> stringList) {
                        //
                        Log.d(TAG, " 缓存区里的事件数量 = " + stringList.size());
                        for (Integer value : stringList) {
                            Log.d(TAG, " 事件 = " + value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * 分组变换 groupBy
     *
     * @param view
     */
    public void r06(View view) {
        //被观察者
        Observable.just(600, 700, 800, 900)

                //分组变换操作符
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return integer > 800 ? "高端" : "中端"; //分组
                    }
                })

        //使用groupBy 观察者使用方式
        .subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                //下面这句话只是拿到了分组的key
                final String key = stringIntegerGroupedObservable.getKey();

                //细节信息 GroupedObservable=被观察者
                stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: 类别:"+ key +",价格："+integer);
                    }
                });
            }
        });
    }
}
