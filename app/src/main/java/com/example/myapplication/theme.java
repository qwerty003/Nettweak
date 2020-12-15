package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class theme extends AppCompatActivity {
    ImageView mi1,mi2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_theme);
        mi1=findViewById(R.id.imageView1);
        mi2=findViewById(R.id.imageView2);
        SharedPreferences p=getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        mi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Intent i=new Intent(theme.this, first.class);
                SharedPreferences.Editor e=p.edit();
                e.putString("Dark","yes");
                e.apply();
                startActivity(i);
                finish();
            }
        });
        mi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Intent i=new Intent(theme.this, first.class);
                SharedPreferences.Editor e=p.edit();
                e.putString("Dark","no");
                e.apply();
                startActivity(i);
                finish();
            }
        });
    }
}