package com.mprog.phone;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallLogManager {
    private Cursor cursor;

    public CallLogManager(Context context) {
        cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
    }

    public CallLogEntry getCallLogEntry(int position) {
        cursor.moveToPosition(position);

        int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);

        String number = cursor.getString(numberIndex);
        String date = cursor.getString(dateIndex);

        Date callDayTime = new Date(Long.valueOf(date));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return new CallLogEntry(number, sdf.format(callDayTime));
    }

    public int getCount() {
        return cursor.getCount();
    }
}
