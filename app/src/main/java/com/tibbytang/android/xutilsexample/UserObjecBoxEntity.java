package com.tibbytang.android.xutilsexample;

import androidx.annotation.IntDef;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-05-14 07:15
 */
@Entity
public class UserObjecBoxEntity {
    @Id
    private long id;
    private String name;
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
