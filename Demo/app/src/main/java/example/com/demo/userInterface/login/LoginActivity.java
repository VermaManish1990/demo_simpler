package example.com.demo.userInterface.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import example.com.demo.R;
import example.com.demo.databinding.ActivityMainBinding;
import example.com.demo.userInterface.home.HomeActivity;
import example.com.demo.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the ViewModel component.
        LoginViewModel loginViewModel = ViewModelProviders.of(this)
                .get(LoginViewModel.class);

        // Inflate view and obtain an instance of the binding class.
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Assign the component to a property in the binding class.
        binding.setLoginViewModel(loginViewModel);


        loginViewModel.navigationToDetails.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(Constants.CLIENT_ID, AuthenticationResponse.Type.TOKEN, Constants.REDIRECT_URL);
                builder.setScopes(new String[]{"user-read-private user-library-read user-read-email"});
                AuthenticationRequest request = builder.build();
                AuthenticationClient.openLoginActivity(LoginActivity.this, Constants.LOGIN_REQUEST_CODE, request);
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == Constants.LOGIN_REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.e(TAG, "Success:" + response.toString());
                    Intent dataIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    dataIntent.putExtra("Auth", response.getAccessToken());
                    startActivity(dataIntent);
                    finish();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.e(TAG, "Error:" + response.toString());
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}
