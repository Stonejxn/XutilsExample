package com.tibbytang.android.xutilsexample;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-05-14 07:15
 */
@Table(name = "User")
public class UserXutilsEntity {
    @Column(name = "id", isId = true)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
