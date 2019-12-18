package example.com.demo.userInterface.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import example.com.demo.model.AlbumDataModel;
import example.com.demo.model.UserInfoModel;
import example.com.demo.repo.UserDataRepositry;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<AlbumDataModel> selected = new MutableLiveData<AlbumDataModel>();
    private final MutableLiveData<AlbumDataModel.Album> selectedTrack = new MutableLiveData<AlbumDataModel.Album>();
    UserDataRepositry repo = new UserDataRepositry();


    public LiveData<UserInfoModel> getUserInfo(String auth) {
        return repo.getUserInfo(auth);
    }

    public LiveData<AlbumDataModel> getUserAlbumData(String auth) {
        return repo.getAlbumData(auth);
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

}
