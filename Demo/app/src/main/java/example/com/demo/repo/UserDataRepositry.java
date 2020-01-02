package example.com.demo.repo;

import javax.inject.Inject;

import example.com.demo.MyApplication;
import example.com.demo.database.CacheDao;
import example.com.demo.model.AlbumDataModel;
import example.com.demo.model.UserInfoModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class UserDataRepositry {
    private static final String TAG = "UserDataRepositry";

    private RepoService repoService;
    private CacheDao mCacheDao;
    private MyApplication mInstance;


    @Inject
    public UserDataRepositry(RepoService repoService, CacheDao cacheDao) {
        this.repoService = repoService;
        this.mCacheDao = cacheDao;
    }

    public Single<Response<UserInfoModel>> getUserInfoFromApi(String auth) {
        return repoService.getUserInfo(auth);
    }

    public Single<Response<AlbumDataModel>> getAlbumDataFromApi(String auth) {
        return repoService.getAlbumData(auth);
    }

    public Observable<String> getAlbumDataFromDB(String url) {
        return mCacheDao.getResponseData(url).toObservable();
    }
}
