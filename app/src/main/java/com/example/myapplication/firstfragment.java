package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class firstfragment extends Fragment {
    LinearLayout l;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup)  inflater.inflate(R.layout.activity_first,container,false);
        l=(LinearLayout)root.findViewById(R.id.linear);
        Animation pushupin = AnimationUtils.loadAnimation(getContext(),R.anim.push_up_in);
        l.startAnimation(pushupin);

        return root;
    }

}
