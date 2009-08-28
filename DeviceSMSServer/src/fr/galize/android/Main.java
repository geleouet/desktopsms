package fr.galize.android;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	Handler handler ;
	private static Accepted accepted;
	private Intent svc;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.i("APPLICATION", "Start");
		handler = new Handler();
		try {

			// setup and start MyService
			new Thread(new Runnable(){

				@Override
				public void run() {
					svc = new Intent(Main.this, MyService.class);
					startService(svc);
					moveTaskToBack(true);
				}}).start();
			{}
			Context context = getApplicationContext();
			CharSequence text = "Launching";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

			/*Intent i = new Intent(context, SMSReceiver.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i); */
		}
		catch (Exception e) {
			Log.e("SMS","ui creation problem", e);
		}
		//          sendSMSIn("0682887362","Main 0");
		try {
			/*Process p = Runtime.getRuntime().exec("ifconfig tiwlan0");
        	InputStream in = p.getInputStream();
        	StringBuilder res= new StringBuilder();
        	try {
        		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
        		String line = "";

        		while ((line=buff.readLine())!=null){
        			res.append(line+"\n");
        		}
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	String string = res.toString();
        	String string2 = string.split("mask")[0];
        	String string3 = string2.split("ip")[1];*/

			/* WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
          WifiInfo connectionInfo = wifiManager.getConnectionInfo();
          int ipAddress = connectionInfo.getIpAddress();*/

			TelephonyManager manager=(TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
			String number = manager.getDeviceId();

			TextView findViewById = (TextView) findViewById(R.id.label);
			findViewById.setText("DesktopSMS"+"\n  Locale IP adress:"+getLocalIpAddress()+"\n" +
					"Number:"+number);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * From http://www.droidnova.com/get-the-ip-address-of-your-device,304.html
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("ERROR", ex.toString());
		}
		return null;
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.view.Menu.FIRST:
		{
			quit();
			return true;		
		}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, android.view.Menu.FIRST, 0, "STOP");
		return true;
	}


	public void post(final Runnable r) {
		handler.post(r);
	}



	public void setAccepted(Accepted accepted) {
		Main.accepted = accepted;

	}

	public void quit() {
		stopService(svc);
		//startService(svc);
		System.exit(0);

	}

}