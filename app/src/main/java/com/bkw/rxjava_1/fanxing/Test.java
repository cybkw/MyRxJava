package com.bkw.rxjava_1.fanxing;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        //案例
     /*   List list=new ArrayList();
        //可以存储所有类型
        list.add("A");
        list.add(1);

        Object o=list.get(0); //这样会报类型转换异常
        String s= (String) o;*/

        //泛型出现后
        List<String> list1 = new ArrayList<>();
        list1.add("A");

//        list1.add(1);  //编译器就会报错
        String s = list1.get(0);

        //人类
//        TestPerson<Woman> person = new TestPerson<>();
//        person.add(new Woman()); //只能传递woman

        //TODO 泛型的限定，上限和下限
        //上限
        show(new TestPerson<Woman>());

        //下限
        show2(new TestPerson<Student>());
        show2(new TestPerson<Object>());


        //TODO 泛型的读写模式
        TestPerson<? extends Person> test1 = null;
        test1.add(new Person());  //不可写
        test1.add(new Object());  //不可写
        test1.add(new Student()); //不可写

        Person person = test1.get();//可读

        //TODO 可写模式
        TestPerson<? super Person> test2 = null;
        test2.add(new Person());  //可写
        test2.add(new Student()); //可写

        Object o = test2.get(); //不完全可读，需要自己强转
        Student student= (Student) o;

    }

    /**
     * extends 上限,表示Person或Person的所有子类都可以传递()
     * 限制最高类型只能是Person
     *
     * @param t
     * @param <T>
     */
    public static <T> void show(TestPerson<? extends Person> t) {

    }

    /**
     * super 下限,表示传递的限制类型只能是Student或Student的父类都可以传递()
     *
     * @param t
     * @param <T>
     */
    public static <T> void show2(TestPerson<? super Student> t) {

    }
}
