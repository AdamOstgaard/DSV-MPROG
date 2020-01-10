package com.mprog.streamsockets;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpClient implements Closeable {
    private final String ip;
    private final int port;
    private final List<TcpMessageListener> listeners;

    private OutputStream outStream;
    private PrintWriter outWriter;
    private Socket socket;

    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.listeners = new ArrayList<>();
    }

    public void Connect() throws IOException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);

                    outStream = socket.getOutputStream();
                    outWriter = new PrintWriter(outStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void sendMessage(final String msg){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                outWriter.println(msg);
                outWriter.flush();
            }
        });

        thread.start();
    }

    public void startReadMessageLoop() throws IOException {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Wait for socket to connect
                while(socket == null || !socket.isConnected()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //noop
                    }
                }

                try(BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                    while(true) {
                        String msg = input.readLine();

                        for (TcpMessageListener listener : listeners) {
                            listener.handleMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void addListener(TcpMessageListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(TcpMessageListener listener){
        this.listeners.remove(listener);
    }

    @Override
    public void close() throws IOException {
        outStream.close();
        outWriter.close();
    }
}
