package fr.galize.android;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class MyService extends Service {

	public static Main MAIN_ACTIVITY;
	private static List<Accepted> accepted = Collections.synchronizedList(new ArrayList<Accepted>());
	private boolean stopped=false;
	private Thread serverThread;
	private ServerSocket ss;


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		final Context context = getApplicationContext();
		final CharSequence text = "Waiting for connection";
		final int duration = Toast.LENGTH_LONG;
		serverThread = new Thread(new Runnable(){


			@Override
			public void run() {
				try {
					Looper.prepare();
					ss = new ServerSocket(44000);
//					ss.setSoTimeout(5000);
					ss.setReuseAddress(true);
					ss.setPerformancePreferences(100, 100, 1);
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					while (!stopped)
					{
						Socket accept = ss.accept();
//						accept.setReuseAddress(true);
						accept.setPerformancePreferences(10, 100, 1);
						accept.setKeepAlive(true);
						Accepted a = new Accepted(accept,context,MAIN_ACTIVITY,MyService.this);
						accepted.add(a);
						new Thread(a).start();
					}
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				} // ouverture d'un socket serveur sur port
				
			}},"Server thread");
		serverThread.start();

						
	

	}


	@Override
	public void onDestroy() {
		stopped=true;
		try {
			ss.close();
		} catch (IOException e) {}
		serverThread.interrupt();
		try {
			serverThread.join();
		} catch (InterruptedException e) {}
	}


	public static void setMainActivity(Main main) {
		MAIN_ACTIVITY = main;
	}
	

	public static void receiveSMS(String messageBody,
			String originatingAddress, long timestampMillis) {
		for (Accepted a:accepted)
		try {
				a.sendNew(messageBody,originatingAddress,timestampMillis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void remove(Accepted a) {
		accepted.remove(a);
	}




}
