package com.mprog.phone;

/**
 * A simple representation of an entry of the call logs.
 */
public class CallLogEntry {
    private String mNumber;
    private String mTime;

    public CallLogEntry(String number, String time) {
        mNumber = number;
        mTime = time;
    }

    /**
     * Gets the phone number of the entry
     * @return the phone number as a string
     */
    public String getNumber() {
        return mNumber;
    }

    /**
     * get the time of the call.
     * @return a string representation of the time.
     */
    public String getTime() {
        return mTime;
    }
}
