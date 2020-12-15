package com.example.myapplication;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.command.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// A simple command implementation example
public class MyCommand implements Command, Runnable {
    private InputStream in;
    private OutputStream out, err;
    private ExitCallback callback;

    public MyCommand() {
        super();
    }

    @Override
    public void setInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void setErrorStream(OutputStream err) {
        this.err = err;
    }

    @Override
    public void setExitCallback(ExitCallback callback) {
        this.callback = callback;
    }

    @Override
    public void start(Environment env) throws IOException {

    }

    @Override
    public void run() {
            try {
                String cmd = readCommand(in);
                if ("exit".equals(cmd)) {
                    return;
                }
                else {
                    handleCommand(cmd, out);
                }
            } catch (Exception e) {
                writeError(err, e);
                callback.onExit(-1, e.getMessage());
                return;
            }

            callback.onExit(0);
    }

    private String readCommand(InputStream in) {
        return  "null";
    }

    private void writeError(OutputStream err, Exception e) {
    }

    private void handleCommand(String cmd, OutputStream out) {
    }


    @Override
    public void destroy() throws Exception {
       // ...release any allocated resources...
    }
}
