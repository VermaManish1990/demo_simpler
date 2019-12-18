package example.com.demo.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import example.com.demo.MyApplication;
import example.com.demo.Network.GsonObjectRequest;
import example.com.demo.Network.NetworkUpdateListener;
import example.com.demo.Network.OnResponseReceived;
import example.com.demo.Network.RequestManager;
import example.com.demo.database.CacheORM;
import example.com.demo.model.AlbumDataModel;
import example.com.demo.model.UserInfoModel;
import example.com.demo.utils.Constants;
import example.com.demo.utils.StringUtil;
import example.com.demo.utils.Util;

public class UserDataRepositry {
    private static final String TAG = "UserDataRepositry";

    public LiveData<UserInfoModel> getUserInfo(String auth) {
        final MutableLiveData<UserInfoModel> baseResponseLiveData = new MutableLiveData<>();
        if (Util.isInternetConnected(MyApplication.getInstance())) {
            GsonObjectRequest infoRequest = new GsonObjectRequest<UserInfoModel>(MyApplication.getInstance(), Request.Method.GET, Constants.USER_INFO_REQUEST_CODE, Constants.USER_INFO_URL, getHeader(auth), null, UserInfoModel.class, new NetworkUpdateListener(new OnResponseReceived() {
                @Override
                public void onRecieve(Object object) {
                    UserInfoModel info = (UserInfoModel) object;
                    baseResponseLiveData.setValue(info);
                }

                @Override
                public void onErrorRecive(VolleyError error, String customMsg, String header) {
                    UserInfoModel info = new UserInfoModel();
                    info.isFailure = true;
                    info.error = customMsg;
                    baseResponseLiveData.setValue(info);
                }
            }));
            RequestManager.addRequest(infoRequest);
        } else {
            UserInfoModel info = new UserInfoModel();
            info.isFailure = true;
            info.error = "Internet Not Connected";
            baseResponseLiveData.setValue(info);
        }
        return baseResponseLiveData;
    }

    public LiveData<AlbumDataModel> getAlbumData(String auth) {
        final MutableLiveData<AlbumDataModel> baseResponseLiveData = new MutableLiveData<>();
        if (Util.isInternetConnected(MyApplication.getInstance())) {

            GsonObjectRequest request = new GsonObjectRequest<AlbumDataModel>(MyApplication.getInstance(), Request.Method.GET, Constants.USER_ALBUM_REQUEST_CODE, Constants.SAVED_ALBUM_URL, getHeader(auth), null, AlbumDataModel.class, new NetworkUpdateListener(new OnResponseReceived() {
                @Override
                public void onRecieve(Object object) {
                    AlbumDataModel data = (AlbumDataModel) object;
                    baseResponseLiveData.setValue(data);
                }

                @Override
                public void onErrorRecive(VolleyError error, String customMsg, String header) {
                    AlbumDataModel data = new AlbumDataModel();
                    data.isFailure = true;
                    data.error = "Internet Not Connected";
                    baseResponseLiveData.setValue(data);
                }
            }));
            request.setCache(true);
            RequestManager.addRequest(request);
        } else {
            String json = CacheORM.getCache(MyApplication.getInstance(), Constants.SAVED_ALBUM_URL);
            if (!StringUtil.isNullOrEmpty(json)) {
                Gson gson = new Gson();
                AlbumDataModel data = gson.fromJson(json, AlbumDataModel.class);
                baseResponseLiveData.setValue(data);
            } else {
                AlbumDataModel data = new AlbumDataModel();
                data.isFailure = true;
                data.error = "Internet Not Connected";
                baseResponseLiveData.setValue(data);
            }
        }
        return baseResponseLiveData;
    }


    private Map<String, String> getHeader(String auth) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + auth);
        return headers;
    }
}
