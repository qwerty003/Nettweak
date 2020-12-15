package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class first extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences p=getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String s=p.getString("Dark","");
        if (s.equals("yes")) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.firstact);
        getSupportActionBar().setTitle("NeTTweaak");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0175D1")));
        System.out.println("Testinggggggggggggggggggggg");
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.Viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Connection"));
        tabLayout.addTab((tabLayout.newTab().setText("Log")));
        tabLayout.addTab(tabLayout.newTab().setText("Extra"));
        tabLayout.addTab((tabLayout.newTab().setText("Setting")));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final madapter adapter=new madapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(onTabSelectedListener(viewPager));


    }
    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.action_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.home:
                Intent intent=new Intent(this,first.class);
                startActivity(intent);
                break;
            case R.id.ctheme:
                Intent i=new Intent(this,theme.class);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText portField = (EditText) findViewById(R.id.portField);
        EditText usernameField = (EditText) findViewById(R.id.usernameField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);
        Button button=(Button) findViewById(R.id.button);

        String host = editText.getText().toString();
        String port = portField.getText().toString();
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        intent.putExtra("host", host);
        intent.putExtra("port", port);
        intent.putExtra("username", username);
        intent.putExtra("password", password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg="Click the notification to start shell";
                Intent intent=new Intent(getApplicationContext(),DisplayMessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",msg);
                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(), "notify")
                        .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                        .setContentTitle("Start Shell")
                        .setContentText(msg)
                        .setAutoCancel(true);
                builder.setContentIntent(pi);

                NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    String channelId = "Your_channel_id";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    nm.createNotificationChannel(channel);
                    builder.setChannelId(channelId);
                }
                nm.notify(0,builder.build());
                //   startActivity(intent);

            }
        });

        /*
        Intent intent2 = new Intent(this, SSHservice.class);
        startService(intent2);*/
    }
    public void stopMessage(View view) {
        // Do something in response to button
        Intent intent3 = new Intent(this, SSHservice.class);
        stopService(intent3);
        finish();
    }

}