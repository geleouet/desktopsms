package fr.galize.android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Accepted implements Runnable {

	final ThreadPoolExecutor tpe = 
		new ThreadPoolExecutor(1,1,5,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>()) {

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
			}
		
	};
	
	private static final int MESSAGE_TYPE_DRAFT = 3;
	private final Socket accept;
	private final Context context;
	private final Main main_activity;
	private DataOutputStream dis;
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
		DataInputStream _in =null;
		try {
			_in = new DataInputStream(new BufferedInputStream(accept.getInputStream(),1024));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		dis = null;
		try {
//			writer = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()),1024*512);
			dis = new DataOutputStream(new BufferedOutputStream(accept.getOutputStream(),1024));
		} catch (Exception e2) {
			e2.printStackTrace();
			return;
		}

		listener = new Listener(context,_in,main_activity,this);
		listenerThread = new Thread(listener);
		listenerThread.start();
		
		sendId();
		
		getSendAll();
		
	}
	public void getSendAll() {
		/*ArrayList<Message> msgs;
		ArrayList<Contact> ctcs;
		msgs= getSMS( r);*/
		getContact(r);
	    getPhoto(r);  
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
	
	private void getPhoto(ContentResolver r) {
		Uri photoUri = android.provider.Contacts.Photos.CONTENT_URI;
		Cursor cursorPhoto = r.query(photoUri, null, null, null, null);
//		String[] columnNames = cursorPhoto.getColumnNames();
		int idIndex = cursorPhoto.getColumnIndex("person");
		while (cursorPhoto.moveToNext()) {
			byte[] blob = cursorPhoto.getBlob(0);
			if (blob!=null)
			{
				String id = cursorPhoto.getString(idIndex);
				try {
					Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

					File f = File.createTempFile("contactPict", "tmp");
					FileOutputStream fos = new FileOutputStream(f);

					bitmap.compress(CompressFormat.PNG, 75, fos);

					fos.flush();
					fos.close();
					

					sendImage(f, id);
					// TODO un ack ?
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) 
							{}
				} catch (IOException e) {
					Log.e("ERROR","Sendding Photo SMS:",e);
					stopListener();
					return;
				}
			}
//			for (int i = 1; i < columnNames.length; i++) {
//				String string = cursorPhoto.getString(i);
//				String colName = columnNames[i];
//				Log.e("PHOTO", colName+"="+string);
//			}
			Log.e("PHOTO", "-------------");
		}
		
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

			
			//MMS Attachement
			//http://forum.xda-developers.com/archive/index.php/t-448361.html
			
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
					String col = colName.replaceFirst(colName.substring(0,1), colName.substring(0,1).toUpperCase());
					try {
						Message.class.getMethod("set"+col, String.class).invoke(m, string);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					sendSMS(m);
					// TODO un ack ?
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
	          sendContact(m);
				// TODO un ack ?
        }
        
        
        
		
	}
	public void stopListener() {
		Log.e("ACCEPTED", "CLOSE");
		listener.quit();
		try {
			listenerThread.join(5000);
		} catch (InterruptedException e) {}
		try {
			dis.close();
		} catch (IOException e) {}
		try {
			accept.close();
		} catch (IOException e) {}
		
		myService.remove(this);
	}
	public void sendNew(final String messageBody, final String originatingAddress,
			final long timestampMillis)  {
		tpe.submit(new Runnable(){

			@Override
			public void run() {
				
				try {
					dis.writeInt(4);
					dis.writeLong(Long.valueOf(timestampMillis));
					sendString(originatingAddress);
					sendString(messageBody);
					dis.flush();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("ERROR","Sendding new SMS:",e);
					stopListener();
				}
			}});
	}
	public void sendId()  {
		tpe.submit(new Runnable(){

			@Override
			public void run() {
				
				try {
					TelephonyManager manager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					String number = manager.getDeviceId();

					dis.writeInt(6);
					sendString(number);
					dis.flush();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("ERROR","Sendding new SMS:",e);
					stopListener();
				}
			}});
	}
	public void sendImage(final File f, final String id) {
		tpe.submit(new Runnable(){

			@Override
			public void run() {

				try {
					dis.writeInt(4);
					dis.writeInt(Integer.parseInt(id));
					dis.writeLong(f.length());

					FileInputStream fileInputStream = new FileInputStream(f);
					BufferedInputStream bis= new BufferedInputStream(fileInputStream,512*1024);
					byte buffer[]=new byte[1024];
					int nbLecture;
					while( (nbLecture = bis.read(buffer)) != -1 ) {
						dis.write(buffer, 0, nbLecture);
					} 
					dis.flush();
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("ERROR","Sendding Image:",e);
					stopListener();
				}
			}});
	}
	public void sendACK(final String m, final String id)  {
		tpe.submit(new Runnable(){

			@Override
			public void run() {

				try {
					dis.writeInt(3);
					dis.writeLong(Long.parseLong(id));
					sendString(m);
					dis.flush();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("ERROR","Sendding Ack:",e);
					stopListener();
				}
			}});
	}
	private void sendContact(final Contact m)  {
		tpe.submit(new Runnable(){

			@Override
			public void run() {

				try {
				String person = m.getId();
				String name = m.getName();
				String number = m.getNumber();
					dis.writeInt(2);
				sendString(person);
				sendString(name);
				sendString(number);
				dis.flush();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("ERROR","Sendding Contact:",e);
					stopListener();
				}
			}});
	}
	private void sendSMS(final Message m)  {
		tpe.submit(new Runnable(){

			@Override
			public void run() {

				String body = m.getBody();
				String person = m.getPerson();
				String adress = m.getAddress();
				try {
					dis.writeInt(1);
				sendString(body);
				sendString(adress);
				if (person!=null)
					sendString(person);
				else
					sendString("");
				sendString(m.getDate());
				sendString(m.getRead());
				sendString(m.getType());
				dis.flush();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("ERROR","Sendding Histo SMS:",e);
					stopListener();
				}
			}});
	}
	private void sendString(String body) throws IOException,
			UnsupportedEncodingException {
		byte[] bytes = body.getBytes("UTF-8");
		dis.writeInt(bytes.length);
		dis.write(bytes);
	}

	 


}
