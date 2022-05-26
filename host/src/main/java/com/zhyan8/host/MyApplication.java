package com.zhyan8.host;

import android.app.Application;
import android.content.Context;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginConfig;

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RePluginConfig c = new RePluginConfig();
        c.setVerifySign(false);
        RePlugin.App.attachBaseContext(this, c);
        RePlugin.enableDebugger(base, BuildConfig.DEBUG);
    }
}
