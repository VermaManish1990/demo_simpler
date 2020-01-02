package example.com.demo.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;
import example.com.demo.MyApplication;
import example.com.demo.database.DemoDataBaseModule;

@Singleton
@Component(modules = {ContextModule.class, AndroidSupportInjectionModule.class, ApplicationModule.class, DemoDataBaseModule.class, ActivityBindingModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    void inject(MyApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}