package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class secondfragment extends Fragment {
    LinearLayout l2;
    TextView logOutput;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup)  inflater.inflate(R.layout.secondfragment,container,false);
        l2=(LinearLayout)root.findViewById(R.id.linear);

        //set I/O
        logOutput = root.findViewById(R.id.textView4);
        logOutput.setMovementMethod(new ScrollingMovementMethod());

        // Preferences
        SharedPreferences logp = this.getActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        /* Update log string value by appending
        String logString=logp.getString("logText","");
        logString += "Log Displayed\n";
        SharedPreferences.Editor e=logp.edit();
        e.putString("logText",logString);
        e.apply();*/

        //Print logs in textView
        String logString=logp.getString("logText","");
        logOutput.setText(logString);


        return root;
    }
}
