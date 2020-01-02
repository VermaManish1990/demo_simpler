package example.com.demo.userInterface.login;

import androidx.databinding.Observable;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import example.com.demo.utils.NavigationScreen;
import example.com.demo.utils.SingleEventLiveData;


public class LoginViewModel extends ViewModel implements Observable {

    @Inject
    public LoginViewModel() {

    }

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
