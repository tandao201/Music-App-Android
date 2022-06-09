package com.example.appnhac.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appnhac.Fragments.FragmentBaiHatOffline;
import com.example.appnhac.Fragments.FragmentTimKiem;
import com.example.appnhac.Fragments.FragmentTrangChu;


public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentTrangChu();
            case 1:
                return new FragmentTimKiem();
            case 2:
                return new FragmentBaiHatOffline();

            default:
                return new FragmentTrangChu();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
