package com.scex.my_rxjava;

public class ObservableMap<T, R> implements ObservableOnSubscribe<R> {

    private ObservableOnSubscribe<T> source; // 上一层的能力
    private Funcation<? super T, ? extends R> function;
    private MyObserver<? super R> observableEmitter; // 下一层的能力


    public ObservableMap(ObservableOnSubscribe source, Funcation<? super T, ? extends R> function) {
        this.source = source;
        this.function = function;
    }


    @Override
    public void subscribe(MyObserver<? super R> observer) {
        this.observableEmitter = observableEmitter;

        // source.subscribe(observableEmitter); // 不应该把下一层Observer交出去 ---》 上一层， 如果交出去了，map没有控制权

        // 包裹一层  然后再丢给我们的 最顶层的source
        MapObserver<T> mapObserver = new MapObserver(observableEmitter, source, function);

        // todo 5
        // 上一层的source
        source.subscribe(mapObserver); // 把我们自己 map MapObserver 交出去了
    }

    // 真正拥有控制下一层的能力  让map拥有控制权力  observer,source,function
    class MapObserver<T> implements MyObserver<T> {

        // 为了后续可以用 - 保存一份
        private MyObserver</*? super */R> observableEmitter; // 给下一层的类型，意思是 变换后的类型 也就是给下一层的类型 R
        private ObservableOnSubscribe<T> source;
        private Funcation<? super T, ? extends R> function;


        public MapObserver(MyObserver observableEmitter, ObservableOnSubscribe<T> source, Funcation<? super T, ? extends R> function) {
            this.observableEmitter = observableEmitter;
            this.source = source;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            // observableEmitter.onSubscribe();
        }

        // todo 7
        @Override
        public void onNext(T item) { // 真正做变换的操作

            /**
             * T Integer    变换     R String
             */

            R nextMapResultSuccesType = null;
            try {
                nextMapResultSuccesType = function.apply(item);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 调用下一层 onNext 方法
            observableEmitter.onNext(nextMapResultSuccesType);
        }

        @Override
        public void onError(Throwable e) {
            observableEmitter.onError(e);
        }

        @Override
        public void onComplete() {
            observableEmitter.onComplete();
        }
    }

}
