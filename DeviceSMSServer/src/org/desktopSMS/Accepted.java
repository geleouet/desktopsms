package org.desktopSMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class Accepted implements Runnable {


	private static final int MESSAGE_TYPE_DRAFT = 3;
	private final Socket accept;
	private final Context context;
	private final Main main_activity;
	private BufferedWriter writer;
	private ContentResolver r;
	private final MyService myService;
	private Listener listener;
	private Thread listenerThread;

	public Accepted(Socket accept, Context context, Main main_activity, MyService myService) throws IOException {
		this.accept = accept;
		this.context = context;
		this.main_activity = main_activity;
		this.myService = myService;
		if (main_activity!=null)
			main_activity.setAccepted(this);
		CharSequence text = "New Connection Accepted";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	@Override
	public void run() {
		r = context.getContentResolver();

		// Creation du thread d'ecoute
		BufferedReader _in =null;
		try {
			_in = new BufferedReader(new InputStreamReader(accept.getInputStream()));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
		} catch (IOException e2) {
			e2.printStackTrace();
			return;
		}

		listener = new Listener(context,_in,main_activity,this);
		listenerThread = new Thread(listener);
		listenerThread.start();
		
		
		getSendAll();
		
	}
	public void getSendAll() {
		/*ArrayList<Message> msgs;
		ArrayList<Contact> ctcs;
		msgs= getSMS( r);*/
		getContact(r);
	      
		// On ecrit l'historique des sms
		// et les contacts
		Looper.prepare();
		
//		try {
//			
//			for (Contact m:ctcs)
//			{
//				sendContact(m);
//				
//			}
//			for (Message m:msgs)
//			{
//				sendSMS(m);
//				
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			main_activity.quit();
//		}
	}
	
	public void sendACK(String m, String id) throws IOException {
		writer.write("ACK");
		writer.write(id);
		writer.write(0);
		writer.write(m);
		writer.write(0);
		writer.write("OK");
		writer.flush();
	}
	private void sendContact(Contact m) throws IOException {
		String person = m.getId();
		String name = m.getName();
		String number = m.getNumber();
		writer.write("PHONE");
		writer.write(person);
		writer.write(0);
		writer.write(name);
		writer.write(0);
		writer.write(number);
		writer.write(0);
		writer.write("OK");
		writer.flush();
	}
	private void sendSMS(Message m) throws IOException {
		String body = m.getBody();
		String person = m.getPerson();
		String adress = m.getAddress();
		writer.write("SMS");
		writer.write(body);
		writer.write(0);
		writer.write(adress);
		writer.write(0);
		if (person!=null)
			writer.write(person);
		writer.write(0);
		writer.write(m.getDate());
		writer.write(0);
		writer.write(m.getRead());
		writer.write(0);
		writer.write("OK");
		writer.flush();
	}
	private void sendSMSSent(Message m) throws IOException {
		String body = m.getBody();
		String person = m.getPerson();
		String adress = m.getAddress();
		writer.write("EMIT");
		writer.write(body);
		writer.write(0);
		writer.write(adress);
		writer.write(0);
		if (person!=null)
			writer.write(person);
		writer.write(0);
		writer.write(m.getDate());
		writer.write(0);
		writer.write(m.getRead());
		writer.write(0);
		writer.write("OK");
		writer.flush();
	}
	public void getSMS(String id) {
		/*
		 * From Backup SMS
		ContentResolver r = getContentResolver();
        String selection = String.format("%s > ? AND %s <> ?",
                SmsConsts.DATE, SmsConsts.TYPE);
        String[] selectionArgs = new String[] {
                String.valueOf(getMaxSyncedDate()), String.valueOf(SmsConsts.MESSAGE_TYPE_DRAFT)
        };
        String sortOrder = SmsConsts.DATE + " LIMIT " + PrefStore.getMaxItemsPerSync(this);
        return r.query(Uri.parse("content://sms"), null, selection, selectionArgs, sortOrder);

		 */
		{
			String selection = String.format("%s > ? AND %s <> ?", "date", "type");
			String[] selectionArgs = new String[] {
					String.valueOf(id), String.valueOf(MESSAGE_TYPE_DRAFT)
			};
			Cursor cursor =
				r.query(Uri.parse("content://sms"), null, selection, selectionArgs,"date");
			//			r.query(Uri.parse("content://sms"), null, null, null,"date");
			int indexDate = cursor.getColumnIndex("date");
			String[] columns = cursor.getColumnNames();
			long maxDate = 0;

			while (cursor.moveToNext()) {

				long date = cursor.getLong(indexDate);
				if (date > maxDate) {
					maxDate = date;
				}
				Message m = new Message();
				for (int i = 0; i < columns.length; i++) {
					String string = cursor.getString(i);
					String colName = columns[i];
					if (string==null)
						continue;
					Log.e("SMS", colName+"="+string);
					String col = colName.replaceFirst(colName.substring(0,1), colName.substring(0,1).toUpperCase());
					try {
						Message.class.getMethod("set"+col, String.class).invoke(m, string);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					sendSMS(m);
					// TODO un ack ?
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {}
				} catch (IOException e) {
					Log.e("ERROR","Sendding Histo SMS:",e);
					stopListener();
					return;
				}
			}
		}
		{

			String selection = String.format("%s > ? AND %s <> ?", "date", "type");
			String[] selectionArgs = new String[] {
					String.valueOf(id), String.valueOf(MESSAGE_TYPE_DRAFT)
			};
			Cursor cursor =
				r.query(Uri.parse("content://sms/sent"), null, selection, selectionArgs,"date");
			//				r.query(Uri.parse("content://sms"), null, null, null,"date");
			int indexDate = cursor.getColumnIndex("date");
			String[] columns = cursor.getColumnNames();
			long maxDate = 0;

			while (cursor.moveToNext()) {

				long date = cursor.getLong(indexDate);
				if (date > maxDate) {
					maxDate = date;
				}
				Message m = new Message();
				for (int i = 0; i < columns.length; i++) {
					String string = cursor.getString(i);
					String colName = columns[i];
					if (string==null)
						continue;
					Log.e("EMIT", colName+"="+string);
					String col = colName.replaceFirst(colName.substring(0,1), colName.substring(0,1).toUpperCase());
					try {
						Message.class.getMethod("set"+col, String.class).invoke(m, string);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					sendSMSSent(m);
					// TODO un ack ?
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {}
				} catch (IOException e) {
					Log.e("ERROR","Sendding Histo SMS:",e);
					stopListener();
					return;
				}
			}

		}
	}
	private void getContact(ContentResolver r) {
		
		
		Uri phoneUri = android.provider.Contacts.Phones.CONTENT_URI ;

        Cursor cursor2 = r.query(phoneUri, null, null, null,null);
        int idIndex = cursor2.getColumnIndex("person");
        int nameIndex = cursor2.getColumnIndex("display_name");
        int numberIndex = cursor2.getColumnIndex("number");
        while (cursor2.moveToNext()) {
        	
        	Contact m = new Contact();
	          String num = cursor2.getString(numberIndex);
	          if (num==null)
	        	  continue;
	          String name = cursor2.getString(nameIndex);
	          String id = cursor2.getString(idIndex);
	          
	          m.setId(id);
	          m.setName(name);
	          m.setNumber(num);
	          Log.e("PHONE",id+" - "+name+" :"+num);
	          try {
				sendContact(m);
				// TODO un ack ?
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
			} catch (IOException e) {
				Log.e("ERROR","Sendding:"+id+" - "+name+" :"+num,e);
				stopListener();
				return;
			}
        }
		
	}
	public void stopListener() {
		listener.quit();
		try {
			listenerThread.join();
		} catch (InterruptedException e) {}
		try {
			writer.close();
		} catch (IOException e) {}
		try {
			accept.close();
		} catch (IOException e) {}
		
		myService.remove(this);
	}
	public void sendNew(String messageBody, String originatingAddress,
			long timestampMillis) throws IOException {
		writer.write("NEW");
		writer.write(Long.toString(timestampMillis));
		writer.write(0);
		writer.write(originatingAddress);
		writer.write(0);
		writer.write(messageBody);
		writer.write(0);
		writer.write("OK");
		writer.flush();
	}

	 


}
