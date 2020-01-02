package example.com.demo.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import example.com.demo.userInterface.home.HomeActivity;
import example.com.demo.userInterface.login.LoginActivity;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract HomeActivity bindHomeActivity();

    abstract LoginActivity bindLoginActivity();
}
