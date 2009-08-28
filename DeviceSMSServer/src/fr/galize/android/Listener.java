package fr.galize.android;

import java.io.DataInputStream;
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

	final DataInputStream _in;


	private final Accepted accepted;

	public Listener(Context context, DataInputStream _in, Main main_activity, Accepted accepted) {
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
		try {
			while (true)
			{
				int methode=_in.readInt();
				switch (methode) {
				case 1:
					readHisto(_in);
					break;
				case 2:
					readContacts(_in);
					break;
				case 3:
					sendSMS(_in);
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			Log.e("ERROR", "Error in Listener",e);
		}
		
		Log.e("ERROR", "Channel close");
		accepted.stopListener();
	}

	private void sendSMS(DataInputStream in) throws IOException {
		String body=readString(in);
		String adress=readString(in);
		String id=readString(in);
		Log.i("SEND SMS", id+"|"+adress+":"+body);
		sendSMS(adress,body,id);
		Log.i("SMS SENT", id+"|"+adress+":"+body);		
	}

	private void readContacts(DataInputStream _in2) throws IOException {
		accepted.getSendAll();		
	}

	private void readHisto(DataInputStream in) throws IOException {
		String id=readString(in);
		Log.i("HISTO","GET HISTO FROM "+ id);
		accepted.getSMS(id);
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
					accepted.sendACK("SENT",id);
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(context, "Generic failure", 
							Toast.LENGTH_SHORT).show();
					Log.i("SENTRESULT", "Generic failure");
					accepted.sendACK("GENERICFAILURE",id);
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(context, "No service", 
							Toast.LENGTH_SHORT).show();
					Log.i("SENTRESULT", "No Service");
					accepted.sendACK("NOSERVICE",id);
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(context, "Null PDU", 
							Toast.LENGTH_SHORT).show();
					Log.i("SENTRESULT", "Null Pdu");
					accepted.sendACK("NULLPDU",id);
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(context, "Radio off", 
							Toast.LENGTH_SHORT).show();
					Log.i("SENTRESULT", "Radio Off");
					accepted.sendACK("RADIOOFF",id);
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
					accepted.sendACK("DELIVERED",id);
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
					accepted.sendACK("NOTDELIVERED",id);
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
			accepted.sendACK("NOTDELIVERED",id);
		}        
	}

	private String readString(DataInputStream in) throws IOException {
		int length = in.readInt();
		if (length==0)
			return "";
		byte[] b=new byte[length];
		in.readFully(b);
		String res=new String(b,"UTF-8");
		return res;
	}
}
