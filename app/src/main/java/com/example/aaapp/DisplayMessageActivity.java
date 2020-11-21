package com.example.aaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;

import java.io.IOException;

public class DisplayMessageActivity extends AppCompatActivity {
    ClientChannel channel;
    String command ="pwd\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //Get user credentials from indent
        Intent intent = getIntent();
        String host = intent.getStringExtra("host");
        String port = intent.getStringExtra("port");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        System.out.println("host:"+host+"\nusername:"+username);
        TextView textView = findViewById(R.id.textView);
        String res = "username:"+username+"\n"+"password:"+password;
        textView.setText(res);

        /*
        //Setting user.com property
        String key = "user.home";
        Context Syscontext;
        Syscontext = getApplicationContext();
        String val = Syscontext.getApplicationInfo().dataDir;
        System.setProperty(key, val);

        //SSH
        SshClient client = SshClient.setUpDefaultClient();
        client.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
        client.start();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Connection establishment
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
                            else if ("null".equals(command)){
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
                                command = "null";
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

         */
    }

    public void startShell(ClientSession session) throws IOException {
        this.channel = session.createChannel(Channel.CHANNEL_SHELL);
        System.out.println("Starting shell");
    }

    public void execute(View view) throws IOException {
        EditText inputText = (EditText) findViewById(R.id.shellText);
        command = inputText.getText().toString() + "\n";
    }
}