package com.example.adam_malik.textmessage;

/**
 * Created by Adam_Malik on 2/6/18.
 */

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;

public class MessageReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages;
        String str = "";

        if(bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus != null ? pdus.length : 0];
            for(int i=0; i<messages.length; i++){
                messages[i] = SmsMessage.createFromPdu((byte[]) (pdus != null ? pdus[i] : null));
                str += messages[i].getDisplayOriginatingAddress();
                str +=": ";
                str += messages[i].getMessageBody();
                str += "/n";
                putSmsToDatabase(messages[i], context);
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            Intent BroadcastIntent =  new Intent();
            BroadcastIntent.setAction("SMS_RECEIVED_ACTION");
            BroadcastIntent.putExtra("message", str);
            context.sendBroadcast(BroadcastIntent);

        }
    }
    private void putSmsToDatabase(SmsMessage sms, Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context); // call on create or onUpgrade

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        // Create SMS row
        ContentValues values = new ContentValues();

        values.put("address", sms.getOriginatingAddress().toString());
        values.put("date", mydate);
        values.put("body", sms.getMessageBody().toString());
        // values.put( READ, MESSAGE_IS_NOT_READ );
        // values.put( STATUS, sms.getStatus() );
        // values.put( TYPE, MESSAGE_TYPE_INBOX );
        // values.put( SEEN, MESSAGE_IS_NOT_SEEN );

        db.insert("datatable", null, values);

        db.close();
    }

}


