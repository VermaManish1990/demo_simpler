package example.com.demo.userInterface.home;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import example.com.demo.R;
import example.com.demo.adapters.DrawerItemCustomAdapter;
import example.com.demo.adapters.TrackListAdapter;
import example.com.demo.databinding.ActivityHomeBinding;
import example.com.demo.model.AlbumDataModel;
import example.com.demo.model.UserInfoModel;
import example.com.demo.utils.Constants;

public class HomeActivity extends AppCompatActivity implements TrackListAdapter.OnItemClicklistener {


    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    ArrayList<AlbumDataModel.Items> drawerItem;
    String auth = "";
    DrawerItemCustomAdapter adapter;
    ActivityHomeBinding binding;
    HomeViewModel homeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setHomeViewModel(homeViewModel);
        drawerItem = new ArrayList<>();
        initUI();

        auth = getIntent().getStringExtra("Auth");

        homeViewModel.getUserInfo(auth).observe(this, new Observer<UserInfoModel>() {
            @Override
            public void onChanged(UserInfoModel userInfoModel) {
                binding.setUserInfo(userInfoModel);
            }
        });
        homeViewModel.getUserAlbumData(auth).observe(this, new Observer<AlbumDataModel>() {
            @Override
            public void onChanged(AlbumDataModel albumDataModel) {
                if (!albumDataModel.isFailure) {
                    Log.e("Manish", "Inside observer:" + albumDataModel.items.size());
                    drawerItem = albumDataModel.items;
                    adapter.setData(albumDataModel.items);
                    homeViewModel.setAlbumDataModel(albumDataModel);
                    //
                    HomeFragment fragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "").commit();
                } else {
                    Toast.makeText(HomeActivity.this, "Data is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUI() {
        adapter = new DrawerItemCustomAdapter(this, drawerItem, Constants.DRAWER_TYPE, this);
        binding.leftDrawerList.setLayoutManager(new LinearLayoutManager(this));
        binding.leftDrawerList.setAdapter(adapter);
//        binding.drawerLayout.setDrawerListener(mDrawerToggle);
        binding.drawerLayout.addDrawerListener(mDrawerToggle);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Home");
        setupDrawerToggle();

       /* mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }


    @Override
    public void onItemClicklistener(int parentPos, int childPos) {
        // Show back button
        setToolBarDetail();
        homeViewModel.setTrackData(drawerItem.get(parentPos).album.tracks.items.get(childPos));
        DetailFragment fragment = new DetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "").addToBackStack("detail").commit();
        binding.drawerLayout.closeDrawer(Gravity.LEFT, false);
    }

    private void setToolBarDetail() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setTitle("Detail");
//        getSupportActionBar().invalidateOptionsMenu();
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        setToolBarHome();

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            removeFragments();

        } else {
            super.onBackPressed();
        }

    }

    private void setToolBarHome() {
        initUI();
    }

    private void removeFragments() {
        do {
            getSupportFragmentManager().popBackStackImmediate();
        }
        while (getSupportFragmentManager().getBackStackEntryCount() > 0);

    }
}
