package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import android.text.method.ScrollingMovementMethod;

public class DisplayMessageActivity extends AppCompatActivity {
    ClientChannel channel;
    EditText shellInput;
    TextView shellOutput;
    String host,port,username,password;
    String command, output;
    Boolean isActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //set I/O
        shellInput = (EditText) findViewById(R.id.shellText);

        //Logging Preferences
        SharedPreferences logp = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);

        //Update log string value by appending
        String logString=logp.getString("logText","");
        logString += "\nStarting command shell\n";
        SharedPreferences.Editor e=logp.edit();
        e.putString("logText",logString);
        e.apply();

        shellOutput = findViewById(R.id.textView);
        shellOutput.setMovementMethod(new ScrollingMovementMethod());

        //Get user credentials from indent
        Intent intent = getIntent();
        host = intent.getStringExtra("host");
        port = intent.getStringExtra("port");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        isActive = TRUE;
        command ="pwd\n";

        //Logging
        System.out.println("host:"+host+"\nusername:"+username);
        output = "username:"+username+"\n"+"password:"+password;
        shellOutput.setText(output);

        float v=0;
        shellOutput.setTranslationX(800);
        shellOutput.setAlpha(v);

        shellOutput.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();


        //Setting user.com property manually since isn't set by default
        String key = "user.home";
        Context Syscontext;
        Syscontext = getApplicationContext();
        String val = Syscontext.getApplicationInfo().dataDir;
        System.setProperty(key, val);

        //SSH
        SshClient client = SshClient.setUpDefaultClient();
        client.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
        client.start();

        //Starting new thread because network processes can interfere with UI if started in main thread
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Connection establishment and authentication
                    try (ClientSession session = client.connect("abigit03", "tty.sdf.org", 22).verify(10000).getSession()) {
                        session.addPasswordIdentity("OFhjwdWgsZLvw");
                        session.auth().verify(50000);
                        System.out.println("Connection establihed");

                        startShell(session);

                        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                        channel.setOut(responseStream);
                        while (true) {
                            if ("exit".equals(command)) {
                                channel.close(false);
                                break;
                            }
                            else if (!isActive){
                                Thread.sleep(2000);
                            }
                            else {
                                channel.open().verify(5, TimeUnit.SECONDS);
                                try (OutputStream pipedIn = channel.getInvertedIn()) {
                                    pipedIn.write(command.getBytes());
                                    pipedIn.flush();
                                }

                                channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
                                        TimeUnit.SECONDS.toMillis(5));
                                String responseString = new String(responseStream.toByteArray());
                                System.out.println(responseString);
                                output(responseString);

                                //set shell-flag to inactive
                                isActive = FALSE;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        client.stop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }

    public void startShell(ClientSession session) throws IOException {
        this.channel = session.createChannel(Channel.CHANNEL_SHELL);
        System.out.println("Starting shell");
    }

    public void execute(View view) throws IOException {
        //update command variable
        isActive = TRUE;
        command += shellInput.getText().toString() + "\n";
    }

    public void output(String out) throws IOException {
        shellOutput.setText(out);
    }
}