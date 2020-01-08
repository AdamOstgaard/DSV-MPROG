package com.mprog.phone;

public class CallLogEntry {
    private String mNumber;
    private String mTime;

    public CallLogEntry(String number, String time) {
        mNumber = number;
        mTime = time;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getTime() {
        return mTime;
    }
}
