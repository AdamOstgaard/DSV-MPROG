package com.mprog.phone;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Wrapper class of the call logs for easier use together with RecycleViewAdapter.
 */
public class CallLogManager {
    private Cursor cursor;

    public CallLogManager(Context context) {
        cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
    }

    /**
     * Get an item at the specified position in the call logs.
     * @param position pos of the item to get.
     * @return A CallLogEntry representation of the item at the specified position.
     */
    public CallLogEntry getCallLogEntry(int position) {
        cursor.moveToPosition(position);

        int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);

        String number = cursor.getString(numberIndex);
        String date = cursor.getString(dateIndex);

        Date callDayTime = new Date(Long.valueOf(date));

        // Format date string to be human readable.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return new CallLogEntry(number, sdf.format(callDayTime));
    }

    /**
     * gets the element count of the call logs
     * @return total count of elements.
     */
    public int getCount() {
        return cursor.getCount();
    }
}
