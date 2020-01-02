package example.com.demo.userInterface.home;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import example.com.demo.R;
import example.com.demo.adapters.DrawerItemCustomAdapter;
import example.com.demo.adapters.TrackListAdapter;
import example.com.demo.databinding.FragmentHomeBinding;
import example.com.demo.model.AlbumDataModel;
import example.com.demo.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends DaggerFragment implements TrackListAdapter.OnItemClicklistener {
    HomeViewModel homeViewModel;

    @Inject
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        initUI(binding);
        return binding.getRoot();
    }

    private void initUI(FragmentHomeBinding binding) {
        binding.albumListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getActivity(), homeViewModel.getSelectedData().getValue().items, Constants.HOME_TYPE, HomeFragment.this);
        binding.albumListView.setAdapter(adapter);
    }


    @Override
    public void onItemClicklistener(int parentPos, int pos) {

    }
}
