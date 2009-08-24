package fr.galize.desktopsms.comm;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import fr.galize.desktopsms.ApplicationContexte;
import fr.galize.desktopsms.Logger;
import fr.galize.desktopsms.actions.ProgressionListener;
import fr.galize.desktopsms.model.MainModel;

public class Communication {

	
	private static final Communication _instance = new Communication();
	
	
	private BufferedWriter _out;
	private BufferedReader _in;
	private boolean closed=true;
	
	private PropertyChangeSupport support= new PropertyChangeSupport(this);


	private Socket s;
	
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}


	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}


	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}


	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}


	public static Communication getInstance()
	{
		return _instance;
	}

	static boolean portforwarded=false;
	
	public void forwardPort() throws CommunicationException
	{
//		if (portforwarded)
//			return;
//		portforwarded=true;
		String path2adb=ApplicationContexte.path2adb;
		System.out.println("Launch adb :"+ApplicationContexte.path2adb);
		try {
			Runtime.getRuntime().exec(path2adb+" forward tcp:5555 tcp:44000" );
		} catch (IOException e) {
			e.printStackTrace();
			throw new CommunicationException("Unable to forward port",e);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Logger.Log.erreur("Interrupted sleep", e);
		}
	}

	public boolean isUsb() {
		return ApplicationContexte.isUsb();
	}


	public void connecte() throws UnknownHostException, IOException {
		closeSocket();
		System.out.println("Try to connect...");
		if (isUsb()){
//			adresse = new InetSocketAddress("127.0.0.1",5555);
			InetSocketAddress adresse;
			InetAddress byAddress = InetAddress.getByName("127.0.0.1");
			s = new Socket();
			adresse = new InetSocketAddress(byAddress,5555);
			s.setReuseAddress(true);
			s.connect(adresse,1000);
		}
		else {
			s = new Socket();
//			s.setSoTimeout(1000);
			InetSocketAddress adresse;
//			InetAddress byAddress = InetAddress.getByAddress(
//					new byte[]{(byte) 192,(byte) 168,(byte) 1,(byte) 153});
			InetAddress byAddress = InetAddress.getByName(ApplicationContexte.getDeviceIp());
			//getByAddress(
			//		new byte[]{10,(byte) 209,(byte) 57,(byte) 178});
			adresse = new InetSocketAddress(byAddress,44000);
			s.connect(adresse);
			if (!s.isConnected())
				{
				System.out.println("Not Connected");
				return;
				}
//			s.bind(adresse);
		}
		s.setKeepAlive(true);
//			s = new Socket("10.204.146.7",5080);
//		s = new Socket("192.168.1.105",5080);
//		socket.bind(adresse);
		System.out.println("Connected");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// Firewall
		}
		
		_out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),"UTF-8"),1024);
		_in = new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"),1024);
		
			
		//new Thread(new RunnableImplementation(_out)).start();
		setClosed(false);	
		new Thread(new ClientListener(_in),"ListenerThread").start();
		getHisto(MainModel.getInstance().getLastDate());
	}


	public ProgressionListener launchServer() throws CommunicationException {
		
		String path2adb=ApplicationContexte.path2adb;
		try {
			Runtime.getRuntime().exec(path2adb+" shell am start -a android.intent.action.MAIN -n fr.galize.desktopsms/fr.galize.desktopsms.Main" );
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommunicationException("Unable to launch application",e);
		}
		final ProgressionListener progressionListener = new ProgressionListener();
		progressionListener.setState("Launching server on device");
		new Thread(new Runnable(){

			public void run() {
				for (int i=0;i<10;i++)
				{
					try {
						Thread.sleep(350);
					} catch (InterruptedException e) {
						// Firewall
					}
					progressionListener.setPercent(i*10);
				}
				progressionListener.setPercent(100);
			}},"Launch application").start();
		return progressionListener;		
	}

	public long sendSMS(String number,String body)
	{
		long l= System.currentTimeMillis();
		String id=Long.toString(l);
		//060-784-3903
		try {
			_out.write("SEND");
			_out.write(0);
			_out.write(id);
			_out.write(0);
			_out.write(number);
//			_out.write("0607843903");
			_out.write(0);
			_out.write(body);
			_out.write(0);
			_out.write("OK");
			_out.flush();
			System.out.println("SMS Sent");
		} catch (Exception e) {
			e.printStackTrace();
			closeSocket();
			return -1;
		}
		return l;
	}


	private void closeSocket()  {
		if (s!=null)
			try {
				s.close();
				System.out.println("Socket close");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	static int readChar(BufferedReader _in, char[] charCur)
	throws IOException {
		return _in.read(charCur, 0, 1);
	}


	public void getHisto(Date d) {
		try {
			_out.write("HISTO");
			_out.write(0);
			_out.write(Long.toString(d.getTime()));
			_out.write(0);
			_out.write("OK");
			_out.flush();
			System.out.println("Histo demande");
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
		}		
	}


	public void setClosed(boolean b) {
		boolean old= this.closed;
		closed=b;
		if (b)
		{
			closeSocket();
		}
		support.firePropertyChange("closed",old,b);
	}


	public boolean isClosed() {
		return closed;
	}

}
