package com.example.hunkerchat_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class pageAdapter extends FragmentPagerAdapter {

    int tabcount;






    public pageAdapter(@NonNull FragmentManager fm, int behavior) {

        super(fm, behavior);
        tabcount=behavior;



    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
    {
        case 0:
            return new  chatfragment();
        case 1:
            return new statusfragments();
        case 2:
            return new callfragments();
        default:
            return null;



        }

    }

    @Override
    public int getCount() {
        return tabcount;

    }
}
