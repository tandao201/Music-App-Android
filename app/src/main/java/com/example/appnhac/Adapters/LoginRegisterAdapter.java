package com.example.appnhac.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appnhac.Fragments.FragmentLogin;
import com.example.appnhac.Fragments.FragmentRegister;

public class LoginRegisterAdapter extends FragmentStateAdapter {


    public LoginRegisterAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentLogin();
            case 1:
                return new FragmentRegister();
            default:
                return new FragmentLogin();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
