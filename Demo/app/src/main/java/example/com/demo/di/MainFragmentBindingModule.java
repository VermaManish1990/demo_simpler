package example.com.demo.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import example.com.demo.userInterface.home.DetailFragment;
import example.com.demo.userInterface.home.HomeFragment;

@Module
public abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @ContributesAndroidInjector
    abstract DetailFragment detailFragment();
}
