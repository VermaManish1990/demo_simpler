package example.com.demo;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import example.com.demo.di.ApplicationComponent;
import example.com.demo.di.DaggerApplicationComponent;

public class MyApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent.builder().application(this).build();
        component.inject(this);
        return component;
    }

}
