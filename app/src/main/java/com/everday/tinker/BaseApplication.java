package com.everday.tinker;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.everday.library.FixDexUtils;

import java.io.InputStream;

public class BaseApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        InputStream inputStream;
        //加载热修复
        FixDexUtils.loadFixedDex(this);
    }
}
