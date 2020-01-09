package com.mprog.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String from = smsMessage.getDisplayOriginatingAddress();

                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context,
                        "From: "+ from + ", message: " + messageBody, duration);
                toast.show();
            }
        }
    }
}
