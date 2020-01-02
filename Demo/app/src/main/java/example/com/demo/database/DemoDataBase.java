package example.com.demo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CacheEntity.class}, version = 1, exportSchema = false)
public abstract class DemoDataBase extends RoomDatabase {
    public abstract CacheDao getCacheDao();
}
