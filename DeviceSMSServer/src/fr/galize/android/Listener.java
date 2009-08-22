package fr.galize.android;

import java.io.BufferedReader;
import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Listener implements Runnable {

	public static final String ADDRESS = "address";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String READ = "read";
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String BODY = "body";
	public static final int MESSAGE_TYPE_INBOX = 1;
	public static final int MESSAGE_TYPE_SENT = 2;
	
	private final Context context;
	
	final BufferedReader _in;


	private final Accepted accepted;
	
	public Listener(Context context, BufferedReader _in, Main main_activity, Accepted accepted) {
		super();
		this.context = context;
		this._in = _in;
		this.accepted = accepted;
	}

	public void quit()
	{
		try {
			_in.close();
		} catch (IOException e) {}
	}
	
	@Override
	public void run() {
		char charCur[] = new char[1]; // déclaration d'un tableau de char d'1 élement, _in.read() y stockera le char lu
		try {
			while(_in.read(charCur, 0, 1)!=-1) // attente en boucle des messages provenant du client (bloquant sur _in.read())
			{
				if (charCur[0]=='H')
				{
					//HISTO
					readChar(_in, charCur);
					readChar(_in, charCur);
					readChar(_in, charCur);
					readChar(_in, charCur);
					_in.read(charCur, 0, 1);
					String id="";
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						id+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					// OK
					readChar(_in, charCur);
					readChar(_in, charCur);
					Log.i("HISTO","GET HISTO FROM "+ id);
					accepted.getSMS(id);
				}
				if (charCur[0]=='G')
				{
					//GET // ALL CONTACTS
					readChar(_in, charCur);
					readChar(_in, charCur);
					readChar(_in, charCur);
					accepted.getSendAll();
				}
				if (charCur[0]=='S')
				{	//SEND
					readChar(_in, charCur);
					readChar(_in, charCur);
					readChar(_in, charCur);
					_in.read(charCur, 0, 1);
					String body="";
					String adress="";
					String id="";
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						id+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						body+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						adress+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					// OK
					readChar(_in, charCur);
					readChar(_in, charCur);
					Log.i("SEND SMS", id+"|"+adress+":"+body);
					
					sendSMS(body,adress,id);

					Log.i("SMS SENT", id+"|"+adress+":"+body);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("ERROR", "Channel close");
		accepted.stopListener();
	}

	private static int readChar(BufferedReader _in, char[] charCur)
	throws IOException {
		return _in.read(charCur, 0, 1);
	}
	
	public void sendSMS(final String phoneNumber, final String message, final String id)
    {        
		final long date = System.currentTimeMillis();
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
            	context.unregisterReceiver(this); // XXX 
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        Log.i("SENTRESULT", "Sent");
                        try {
							accepted.sendACK("SENT",id);
						} catch (IOException e) {
							e.printStackTrace();
						}
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        Log.i("SENTRESULT", "Generic failure");
                        try {
							accepted.sendACK("GENERICFAILURE",id);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", 
                                Toast.LENGTH_SHORT).show();
                        Log.i("SENTRESULT", "No Service");
                        try {
							accepted.sendACK("NOSERVICE",id);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        Log.i("SENTRESULT", "Null Pdu");
                        try {
							accepted.sendACK("NULLPDU",id);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        Log.i("SENTRESULT", "Radio Off");
                        try {
							accepted.sendACK("RADIOOFF",id);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
            	context.unregisterReceiver(this); // XXX 
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        Log.i("DELIVERED", "SMS delivered");
                        try {
							accepted.sendACK("DELIVERED",id);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ContentValues values = new ContentValues();
						values.put(ADDRESS, phoneNumber);
						values.put(DATE, date);
						values.put(READ, 1);
						values.put(STATUS, -1);
						values.put(TYPE, 2);
						values.put(BODY, message);
						context.getContentResolver().insert(Uri.parse("content://sms"), values); 
						Log.i("SAVE","SMS saved on phone");
						
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        Log.i("DELIVERED", "SMS not delivered");
                        try {
							accepted.sendACK("NOTDELIVERED",id);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        try {
			sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ERROR","Sending error",e);
			try {
				accepted.sendACK("NOTDELIVERED",id);
			} catch (IOException e2) {
				Log.e("ERROR","Sending error ACK ",e);
				e2.printStackTrace();
			}
		}        
    }
	
}
