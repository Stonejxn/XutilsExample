package com.tibbytang.android.xutilsexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.elvishew.xlog.XLog;
import com.hankcs.hanlp.HanLP;

import org.xutils.DbManager;
import org.xutils.common.task.AbsTask;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.objectbox.Box;

public class MainActivity extends AppCompatActivity {
    private AbsTask<String> absTask;
    private AppCompatTextView mAppCompTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mAppCompTextView = this.findViewById(R.id.main_text_view);

//        testObjectBox();
        new Thread(new Runnable() {
            @Override
            public void run() {
                testXutilsDB();
            }
        }).start();

    }

    private void testObjectBox() {
        List<UserObjecBoxEntity> userObjecBoxEntities = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            UserObjecBoxEntity userObjecBoxEntity = new UserObjecBoxEntity();
            userObjecBoxEntity.setAge(i);
            userObjecBoxEntity.setName("tom" + i);
            userObjecBoxEntities.add(userObjecBoxEntity);
        }
        Box<UserObjecBoxEntity> userObjecBoxEntityBox = ObjectBox.get().boxFor(UserObjecBoxEntity.class);
        long start = System.currentTimeMillis();
        userObjecBoxEntityBox.put(userObjecBoxEntities);
        long end = System.currentTimeMillis();
        XLog.d("end-start=" + (end - start));

    }

    private void testXutilsDB() {
        try {
            DbManager db = x.getDb(XutilsApplication.getInstance().getDaoConfig());
            List<UserXutilsEntity> userObjecBoxEntities = new ArrayList<>();
            for (int i = 0; i < 50000; i++) {
                UserXutilsEntity userXutilsEntity = new UserXutilsEntity();
                userXutilsEntity.setAge(i);
                userXutilsEntity.setName("tom" + i);
                userObjecBoxEntities.add(userXutilsEntity);
            }
            XLog.d("开始插入数据");
            long start = System.currentTimeMillis();
            db.save(userObjecBoxEntities);
            long end = System.currentTimeMillis();
            XLog.d("end-start=" + (end - start));
        } catch (DbException e) {
            e.printStackTrace();
            XLog.d("插入数据失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
