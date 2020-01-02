package example.com.demo.repo;

import example.com.demo.model.AlbumDataModel;
import example.com.demo.model.UserInfoModel;
import example.com.demo.utils.Constants;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface RepoService {

    @Headers("Content-Type: application/json")
    @GET(Constants.USER_INFO_URL)
    Single<Response<UserInfoModel>> getUserInfo(@Header("Authorization") String auth);

    @Headers("Content-Type:application/json")
    @GET(Constants.SAVED_ALBUM_URL)
    Single<Response<AlbumDataModel>> getAlbumData(@Header("Authorization") String auth);

}
