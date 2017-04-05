package com.example.yxw.rxjavademo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yxw.rxjavademo.bean.Course;
import com.example.yxw.rxjavademo.bean.Person;
import com.example.yxw.rxjavademo.bean.Student;
import com.example.yxw.rxjavademo.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
//        method1();
//        method2();
//        method3();
//        method4();
//        method5();
        method6();
    }

    /**
     * RxAndroid简单使用
     */
    private void method1() {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
//                Log.e("====", "===call===" + Thread.currentThread().getName());
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onNext("4");
                subscriber.onCompleted();
            }
        });
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("====", "===onNext===" + s);

//                String name = Thread.currentThread().getName();
//                Log.e("====", "===method===" + name);
            }
        };

        observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * from,just操作符
     */
    private void method2() {
        String[] str = new String[]{"1", "y", "x", "w"};
//        Observable observable = Observable.just("1","1","xian","true");
        Observable observable = Observable.from(str);
//        Subscriber<String> sub = new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                Log.e("====", "===onCompleted===");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.e("====", "===onNext==="+s);
//                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//            }
//        };
        //代替subscriber
        Action1<String> action = new Action1<String>() {
            @Override
            public void call(String s) {
                //相当于onNext()方法
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        observable.subscribe(action);
    }

    /**
     * 线程切换
     */
    private void method3() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
//                Toast.makeText(MainActivity.this, Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
                Log.e("====", "==========call===" + Thread.currentThread().getName());
                subscriber.onNext("yxw");
                subscriber.onCompleted();
            }
        });
        Subscriber sub = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("====", "===onNext===" + s);
                Log.e("====", "===onNext===" + Thread.currentThread().getName());
            }
        };
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sub);
    }

    /**
     * map转换
     * 由图片地址转换成bitmap
     */
    private void method4() {
        Observable.just("http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        Bitmap bt = NetWorkUtils.getBt(s);
                        return bt;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        img.setImageBitmap(bitmap);
                    }
                });

    }

    /**
     * flatMap转化
     * 输出学生所对应的选修课名称
     */
    private void method5() {
        Course c1 = new Course("英语");
        Course c2 = new Course("数学");
        List<Course> list1 = new ArrayList<>();
        list1.add(c1);
        list1.add(c2);
        Student s1 = new Student("张三",list1);

        Course c3 = new Course("政治");
        Course c4 = new Course("语文");
        List<Course> list2 = new ArrayList<>();
        list2.add(c3);
        list2.add(c4);
        Student s2 = new Student("张三",list2);

        Student arr[] = new Student[]{s1,s2};

        Observable.from(arr)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        Observable<Course> from = Observable.from(student.getList());
                        return from;
                    }
                })
                .subscribe(new Action1<Course>() {
                    @Override
                    public void call(Course course) {
                        Log.e("====", "===call==="+course.getName());
                    }
                });

    }

    /**
     * filter
     * 输出年龄大于40
     */
    private void method6() {
        Person p1 = new Person("25");
        Person p2 = new Person("56");
        Person p3 = new Person("36");
        Person p4 = new Person("18");
        Person p5 = new Person("42");
        List<Person> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        Observable.from(list)
                .filter(new Func1<Person, Boolean>() {
                    @Override
                    public Boolean call(Person person) {

                        boolean b = 30 < Integer.parseInt(person.getAge());
                        return b;
                    }
                })
                .subscribe(new Action1<Person>() {
                    @Override
                    public void call(Person person) {
                        Log.e("====", "===call==="+person.getAge());
                    }
                });
    }
}
