package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

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

public class SSHservice extends Service {

    public SSHservice() {
    }

    private static final String TAG ="com.aaapp.SSHservice";

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Log.i(TAG, "Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Setting user.com property
        String key = "user.home";
        Context Syscontext;
        Syscontext = getApplicationContext();
        String val = Syscontext.getApplicationInfo().dataDir;
        System.setProperty(key, val);

        //SSH
        final SshClient client = SshClient.setUpDefaultClient();
        client.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
        client.start();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try (ClientSession session = client.connect("abigit03", "tty.sdf.org", 22).verify(1000).getSession()) {
                        session.addPasswordIdentity("OFhjwdWgsZLvw");
                        session.auth().verify(50000);
                        try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                             ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)) {
                            channel.setOut(responseStream);
                            try {
                                String command = "pwd\n";
                                channel.open().verify(5, TimeUnit.SECONDS);
                                try (OutputStream pipedIn = channel.getInvertedIn()) {
                                    pipedIn.write(command.getBytes());
                                    pipedIn.flush();
                                }

                                channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
                                        TimeUnit.SECONDS.toMillis(5));
                                String responseString = new String(responseStream.toByteArray());
                                System.out.println(responseString);
                            } finally {
                                channel.close(false);
                            }
                        }


                        /*
                        session.addPortForwardingEventListener(new PortForwardingEventListener() {

                            @Override
                            public void establishedDynamicTunnel(Session session, SshdSocketAddress local,
                                                                 SshdSocketAddress boundAddress, Throwable reason) throws IOException {
                                // TODO Auto-generated method stub
                                PortForwardingEventListener.super.establishedDynamicTunnel(session, local, boundAddress, reason);

                                System.out.println(
                                        "ColpMina.main(...).new PortForwardingEventListener() {...}.establishedDynamicTunnel()");
                            }
                        });


                        SshdSocketAddress sshdSocketAddress = session
                                .startDynamicPortForwarding(new SshdSocketAddress("localhost", 8000));

                        System.out.println("Host: " + sshdSocketAddress.getHostName());
                        System.out.println("Port: " + sshdSocketAddress.getPort());


//			ChannelDirectTcpip channel=session.createDirectTcpipChannel(new SshdSocketAddress("localhost", 8000), new SshdSocketAddress("http://10.7.65.250:9080/status/detailed", 0));
//			System.out.println("Signal:"+channel.getExitSignal());
//			Scanner reader=new Scanner(channel.getIn());
//
//			while(reader.hasNext()) {
//				System.out.println("Data:"+reader.next());
//			}
//			reader.close();

                        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(sshdSocketAddress.getHostName(), sshdSocketAddress.getPort()));
                        HttpURLConnection connection =(HttpURLConnection)new URL("http://www.google.com").openConnection(proxy);

                        System.out.println("Proxy work:"+connection.usingProxy()); */

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
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy");
    }
}


