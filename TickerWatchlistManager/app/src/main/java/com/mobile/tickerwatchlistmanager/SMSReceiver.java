package com.mobile.tickerwatchlistmanager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SMSText", "On Receiver");
        final Bundle bundle = intent.getExtras();

        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){

            if(bundle != null){
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format").toString();
                String sender = "";
                String message = "";
                for(int i = 0 ; i < pdusObj.length ; i++){
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                    sender = currentMessage.getDisplayOriginatingAddress();
                    message = currentMessage.getDisplayMessageBody();
                    //String printMessage = "Sender: " + sender + " Message: " + message;
                    //Toast.makeText(context, printMessage, Toast.LENGTH_SHORT).show();


                    Intent activityIntent = new Intent(context, MainActivity.class);
                    activityIntent.putExtra("sms", message);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(activityIntent);
                }
            }
        }
    }
}



//if (message.contains("Ticker<<") && message.contains(">>")) {
//        String ticker = message.substring(message.indexOf("<") + 1, message.indexOf(">"));
//
//        } else {
//        Toast.makeText(context, "No valid watchlist entry was found.", duration).show();
//        }