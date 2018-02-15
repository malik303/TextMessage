package com.example.adam_malik.textmessage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    Button btnSend;
    EditText tvMessage;
    EditText tvNumber;
    IntentFilter IntentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView inTxt = (TextView) findViewById(R.id.textMsg);
            inTxt.setText(intent.getExtras().getString("message"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        DataBaseHelper helper = new DataBaseHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =  db.query("SMS", new String[]{"ADDRESS","BODY"},null,null,null,null,null);



        btnSend = (Button) findViewById(R.id.btnSend);
        tvMessage = (EditText) findViewById(R.id.tvMessage);
        tvNumber = (EditText) findViewById(R.id.tvNumber);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MyMsg = tvMessage.getText().toString();
                String theNumber = tvNumber.getText().toString();
                sendMsg (theNumber, MyMsg);
            }
        });

        if (cursor.moveToFirst()){
            String address = cursor.getString(0);
            String body = cursor.getString(2);

            tvMessage.setText(body);
            tvNumber.setText(address);
        }

    }
    protected void sendMsg (String theNumber, String MyMsg){
       String SENT = "Message sent";
       String DELIVERED = "Message Delivered";


       PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber,null, MyMsg, sentPI, deliveredPI);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}
