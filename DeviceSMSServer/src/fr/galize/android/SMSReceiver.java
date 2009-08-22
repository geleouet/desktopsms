/**
 * 
 */
package fr.galize.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	
	 
        public void onReceive(Context context, Intent intent) {
        	Bundle bundle = intent.getExtras();

        	Object messages[] = (Object[]) bundle.get("pdus");
        	SmsMessage smsMessage[] = new SmsMessage[messages.length];
        	for (int n = 0; n < messages.length; n++) {
        	smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
        	}

        	// show first message
        	Log.i("RECEIVED", "Received SMS: " + smsMessage[0].getMessageBody());
        	Toast toast = Toast.makeText(context,
        	"Received SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
        	toast.show();
        	
        	MyService.receiveSMS(smsMessage[0].getMessageBody(),smsMessage[0].getOriginatingAddress(),smsMessage[0].getTimestampMillis());
        	
        	
//        	Main main_activity = MyService.MAIN_ACTIVITY;
//        	if (main_activity!=null)
//        		main_activity.receiveSMS(smsMessage[0].getMessageBody(),smsMessage[0].getOriginatingAddress(),smsMessage[0].getTimestampMillis());
        	
        	

        	 }
}