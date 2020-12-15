package com.example.myapplication;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class madapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;
    public madapter(FragmentManager fm, Context c, int totalTabs){
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context=c;
        this.totalTabs=totalTabs;
    }
    @Override
    public int getCount(){
        return totalTabs;
    }
    public Fragment getItem(int position){
        switch(position){
            case 0:
                firstfragment first=new firstfragment();
                return first;
            case 1:
                secondfragment second=new secondfragment();
                return second;
            case 2:
                thirdfragment third=new thirdfragment();
                return third;
            case 3:
                fourthfragment fourth=new fourthfragment();
                return fourth;
            default:
                return null;
        }
    }
}
