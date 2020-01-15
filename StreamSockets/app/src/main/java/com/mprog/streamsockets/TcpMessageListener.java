package com.mprog.streamsockets;

public interface TcpMessageListener {
    /**
     * Called when there is a new message read.
     * @param message the new message.
     */
    void handleMessage(String message);
}
