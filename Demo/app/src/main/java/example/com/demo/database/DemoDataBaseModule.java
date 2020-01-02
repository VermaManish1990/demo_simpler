package example.com.demo.database;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoDataBaseModule {

    @Singleton
    @Provides
    public DemoDataBase provideDemoDataBase(Context context) {
        return Room.databaseBuilder(context, DemoDataBase.class, "demo_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    @Singleton
    @Provides
    CacheDao providesProductDao(DemoDataBase demoDatabase) {
        return demoDatabase.getCacheDao();
    }
}
