package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private static int screenTime=3500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences p=getSharedPreferences("PREFERENCES", MODE_PRIVATE);
                String s=p.getString("FirstInstall","");
                if(s.equals("yes")){
                    Intent i=new Intent(MainActivity.this, first.class);
                    startActivity(i);
                    finish();
                }else{
                    SharedPreferences.Editor e=p.edit();
                    e.putString("FirstInstall","yes");
                    e.apply();
                    Intent i=new Intent(MainActivity.this, theme.class);
                    startActivity(i);
                    finish();
                }
            }
        },screenTime);

    }
}