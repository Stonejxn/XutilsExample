package com.tibbytang.android.xutilsexample;

import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * 作者:tibbytang
 * 微信:tibbytang19900607
 * 有问题加微信
 * 创建于:2020-04-21 17:03
 */
public class XutilsApplication extends Application {
    private DbManager.DaoConfig mDaoConfig;

    private static XutilsApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        initDB();
        ObjectBox.init(this);
        initXLog();
    }

    public static XutilsApplication getInstance() {
        return mInstance;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return mDaoConfig;
    }

    private void initXLog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG ? LogLevel.ALL
                        : LogLevel.NONE)
                .tag("tibbytang-android")
                .build();
        XLog.init(config);
    }

    private void initDB() {
        mDaoConfig = new DbManager.DaoConfig()
                .setDbName("test.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                });
    }
}
