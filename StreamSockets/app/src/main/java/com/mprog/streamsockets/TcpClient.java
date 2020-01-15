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

/**
 * A simple TcpClient implementation.
 */
public class TcpClient implements Closeable {
    private final String ip;
    private final int port;
    private final List<TcpMessageListener> listeners;

    private OutputStream outStream;
    private PrintWriter outWriter;
    private Socket socket;

    /**
     * Prepares a new TcpClient
     * @param ip server ip
     * @param port server port
     */
    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.listeners = new ArrayList<>();
    }

    /**
     * Connects the client to the server.
     */
    public void Connect() {
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

    /**
     * Sends a message to the server using this client.
     * @param msg message to be sent.
     */
    public void sendMessage(final String msg){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Write the message to the stream.
                outWriter.println(msg);
                outWriter.flush();
            }
        });

        thread.start();
    }

    /**
     * Starts lsitening to new messages from the server and notifies all listeners wwhen a new message is read.
     * @throws IOException
     */
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

                // get the input stream and start listening for messages.
                try(BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                    while(true) {
                        // Read the message from the stream. This call blocks until there is data to  be read.
                        String msg = input.readLine();

                        // Notify subscribers of the new message.
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

    /**
     * Add a message subscriber to this TcpClient
     * @param listener to be called when a new message is read.
     */
    public void addListener(TcpMessageListener listener){
        this.listeners.add(listener);
    }

    /**
     * remove a listener from this tcp client.
     * @param listener listener to be removed
     */
    public void removeListener(TcpMessageListener listener){
        this.listeners.remove(listener);
    }

    /**
     * Free up resources used by closing streams and sockets.
     * @throws IOException thrown if there is a error when closing one of the resources.
     */
    @Override
    public void close() throws IOException {
        outStream.close();
        outWriter.close();
        socket.close();
    }
}
