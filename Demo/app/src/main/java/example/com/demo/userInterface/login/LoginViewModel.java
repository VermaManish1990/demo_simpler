package example.com.demo.userInterface.login;

import androidx.databinding.Observable;
import androidx.lifecycle.ViewModel;

import example.com.demo.utils.NavigationScreen;
import example.com.demo.utils.SingleEventLiveData;


public class LoginViewModel extends ViewModel implements Observable {

    public SingleEventLiveData navigationToDetails = new SingleEventLiveData();

    public void onLoginClicked() {
        navigationToDetails.call(NavigationScreen.HOME_ACTIVITY);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }
}
