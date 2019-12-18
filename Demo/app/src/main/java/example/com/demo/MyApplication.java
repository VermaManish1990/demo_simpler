package example.com.demo;

import android.app.Application;

import example.com.demo.Network.RequestManager;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.initializeWith(this, null);
        sInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }
}
