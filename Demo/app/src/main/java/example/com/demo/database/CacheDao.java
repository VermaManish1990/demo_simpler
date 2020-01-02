package example.com.demo.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Maybe;

@Dao
public interface CacheDao {

    @Query("SELECT response FROM cacheResponse WHERE url LIKE (:mUrl)")
    Maybe<String> getResponseData(String mUrl);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertResponse(CacheEntity cacheEntity);

}
