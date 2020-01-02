package example.com.demo.userInterface.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import javax.inject.Inject;

import example.com.demo.model.AlbumDataModel;
import example.com.demo.model.UserInfoModel;
import example.com.demo.repo.UserDataRepositry;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<AlbumDataModel> selected = new MutableLiveData<AlbumDataModel>();
    private final MutableLiveData<AlbumDataModel.Album> selectedTrack = new MutableLiveData<AlbumDataModel.Album>();
    private final MutableLiveData<AlbumDataModel> mAlbumData = new MutableLiveData<AlbumDataModel>();
    private final UserDataRepositry repo;

    @Inject
    public HomeViewModel(UserDataRepositry repoRepository) {
        this.repo = repoRepository;
    }

    public LiveData<UserInfoModel> getUserInfo(String auth) {
        final MutableLiveData<UserInfoModel> mutableLiveData = new MutableLiveData<>();
        String authHeader = "Bearer " + auth;
        disposables.add(repo.getUserInfoFromApi(authHeader).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<UserInfoModel>>() {

                    @Override
                    public void onSuccess(Response<UserInfoModel> userInfoModelResponse) {
                        if (userInfoModelResponse.isSuccessful())
                            mutableLiveData.postValue(userInfoModelResponse.body());
                        else
                            try {
                                Log.e(TAG, userInfoModelResponse.message());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mutableLiveData.postValue(null);
                    }
                }));
        return mutableLiveData;
    }

    public LiveData<AlbumDataModel> getUserAlbumData(String auth) {
        final MutableLiveData<AlbumDataModel> mutableLiveData = new MutableLiveData<>();
        String authHeader = "Bearer " + auth;
        Log.e(TAG, authHeader);
        disposables.add(repo.getAlbumDataFromApi(authHeader).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<AlbumDataModel>>() {
                    @Override
                    public void onSuccess(Response<AlbumDataModel> albumDataModelResponse) {
                        if (albumDataModelResponse.isSuccessful()) {
                            mutableLiveData.postValue(albumDataModelResponse.body());
                        } else {
                            try {
                                Log.e(TAG, albumDataModelResponse.errorBody().charStream() + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        mutableLiveData.postValue(null);
                    }
                }));
        return mutableLiveData;
    }

    public void getAlbumData() {
        //TODO :: IMPLEMENTATION WITH DB
    }

    public void setAlbumDataModel(AlbumDataModel item) {
        selected.setValue(item);
    }

    public LiveData<AlbumDataModel> getSelectedData() {
        return selected;
    }

    public void setTrackData(AlbumDataModel.Album trackData) {
        selectedTrack.setValue(trackData);
    }

    public LiveData<AlbumDataModel.Album> getSelectedTrackData() {
        return selectedTrack;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposables != null)
            disposables.dispose();

    }


    public void something() {
        disposables.add(repo.getAlbumDataFromDB("").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Gson gson = new Gson();
                        AlbumDataModel model = gson.fromJson(s, AlbumDataModel.class);
                        mAlbumData.setValue(model);
                    }
                }));
    }

}
