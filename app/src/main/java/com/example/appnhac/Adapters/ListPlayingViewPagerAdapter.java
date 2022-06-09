package com.example.appnhac.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appnhac.Fragments.FragmentDiaNhac;
import com.example.appnhac.Fragments.FragmentListPlaying;

import java.util.ArrayList;


public class ListPlayingViewPagerAdapter extends FragmentStateAdapter {

    public final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public ListPlayingViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentArrayList.add(fragment);
    }

    public void removeFragment(){
        Log.d("ListPlayingViewPager", "removeFragment: da xoa");
        fragmentArrayList.remove(fragmentArrayList.size()-1);
    }
    
}
