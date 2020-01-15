package com.mprog.locationshare;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple Tcp client that can be used to send and receive data to arbitrary servers using stream sockets.
 */
public class TcpClient implements Closeable {
    private final String ip;
    private final int port;
    private final List<TcpMessageListener> listeners;

    private OutputStream outStream;
    private PrintWriter outWriter;
    private Socket socket;
    private boolean shouldReconnect = true;
    private boolean isConnecting = false;

    /**
     * gets the connected state of the client.
     * @return true if connected otherwise returns false.
     */
    public boolean isConnected(){
        return socket != null && socket.isConnected();
    }

    /**
     * Creates a new TcpClient instance with the provided connection parameters.
     * @param ip the ip address of the server.
     * @param port the remote port of the server.
     */
    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.listeners = new ArrayList<>();
    }

    /**
     * Connect to the server and start listening for messages.
     */
    public void start() {
        shouldReconnect = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reconnect();
                    startReadMessageLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    /**
     * Send a textmessage to the server.
     * @param msg the message to send.
     */
    public void sendMessage(final String msg){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        // Check if connection is lost and reconnect if needed
                        if(shouldReconnect && !isConnected()){
                            reconnect();
                        }

                        outWriter.println(msg);
                        outWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        });

        thread.start();
    }

    /**
     * Starts a thread for reading new messages.
     */
    public void startReadMessageLoop() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(shouldReconnect){
                    // Wait for socket to connect
                    while(socket == null || !socket.isConnected()){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            //noop
                        }
                    }
                    try {
                        readLoop();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                    // reconnect if connection is lost.
                    if(shouldReconnect && !isConnected()){
                        try {
                            reconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        thread.start();
    }

    /**
     * Adds a listening object that will be called every time there is a new message received.
     * @param listener an object implementing the TcpMessageListener interface that will be called on each new message.
     */
    public void addListener(TcpMessageListener listener){
        this.listeners.add(listener);
    }

    /**
     * Remove a listener
     * @param listener the listener to be remove from listeners.
     */
    public void removeListener(TcpMessageListener listener){
        this.listeners.remove(listener);
    }

    /**
     * Free up resources and close the connection.
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        shouldReconnect = false;
        outStream.close();
        outWriter.close();
        socket.close();
    }

    /**
     * Disposes any previous connection and reconnects to the server
     * @throws IOException thrown if the connection fails
     */
    private void reconnect() throws IOException {
        if(isConnecting || !shouldReconnect){
            return;
        }

        isConnecting = true;

        if(socket!= null) {
            socket.close();
        }

        socket = new Socket(ip, port);

        OutputStream newOutStream = socket.getOutputStream();
        if(newOutStream != outStream){
            if(outStream != null){
                outStream.close();
            }

            outStream = newOutStream;

            if(outWriter != null){
                outWriter.close();
            }

            outWriter = new PrintWriter(outStream);
        }

        isConnecting = false;
    }

    /**
     * Wait for messages and notify listeners when a new message is received.
     * @throws SocketException
     */
    private void readLoop() throws SocketException {
        try(BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String msg = input.readLine();

                for (TcpMessageListener listener : listeners) {
                    listener.handleMessage(msg);
                }
            }
        }catch (SocketException e){
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
