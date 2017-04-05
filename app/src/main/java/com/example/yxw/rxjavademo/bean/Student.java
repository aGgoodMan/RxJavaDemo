package com.example.yxw.rxjavademo.bean;

import java.util.List;

/**
 * Created by yxw on 2016/12/23.
 */

public class Student {
    String name;
    List<Course> list;

    public Student(String name, List<Course> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }
}
