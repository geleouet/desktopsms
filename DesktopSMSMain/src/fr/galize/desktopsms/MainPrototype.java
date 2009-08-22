package fr.galize.desktopsms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPrototype {

	static String path2adb="E:\\android\\android-sdk-windows-1.5_r1\\android-sdk-windows-1.5_r1\\tools\\";
	
	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec(path2adb+"adb forward tcp:5555 tcp:5080" );
			Thread.sleep(500);
//			launchServer();
//			Thread.sleep(4000);
			System.out.println("Wait");
			Socket s= new Socket("127.0.0.1",5555);
			System.out.println("Connected");
			Thread.sleep(500);
			
			final BufferedWriter _out;
			BufferedReader _in;
				_out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			
			new Thread(new Runnable(){

				public void run() {
					/*try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					/*System.out.println("Send SMS");
					long l= System.currentTimeMillis();
					String id=Long.toString(l);
					//060-784-3903
					try {
						_out.write("SEND");
						_out.write(0);
						_out.write(id);
						_out.write(0);
						_out.write("0682887362");
//						_out.write("0607843903");
						_out.write(0);
						_out.write("Gros Bisous");
						_out.write(0);
						_out.write("OK");
						_out.flush();
						System.out.println("SMS Sent");
						_out.write("GET");
						_out.write(0);
						_out.flush();
						System.out.println("SEND GET");
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					try {
						_out.write("HISTO");
						_out.write(0);
						_out.write("1250338669082");
						_out.write(0);
						_out.write("OK");
						_out.flush();
						System.out.println("Histo demande");
					} catch (IOException e) {
						e.printStackTrace();
					}		
				}}).start();
			
			char charCur[] = new char[1]; // déclaration d'un tableau de char d'1 élement, _in.read() y stockera le char lu

//			String message = ""; // déclaration de la variable qui recevra les messages du client

			while(readChar(_in, charCur)!=-1) // attente en boucle des messages provenant du client (bloquant sur _in.read())
			{
				if (charCur[0]=='N')
				{
					// NEW
					readChar(_in, charCur);
					readChar(_in, charCur);
					String numero="";
					String body="";
					String time="";
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						time+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						numero+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						body+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					
					
					// OK
					readChar(_in, charCur);
					readChar(_in, charCur);
					
					Date d= new Date(Long.parseLong(time));
					
					System.out.println("_________________________________________________________________\n");
					System.out.println(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(d)+"/"+numero+"/"+body);
					System.out.println("_________________________________________________________________");
				}
				if (charCur[0]=='A')
				{
					// ACK
					readChar(_in, charCur);
					readChar(_in, charCur);
					String id="";
					String result="";
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						id+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						result+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					
					
					// OK
					readChar(_in, charCur);
					readChar(_in, charCur);
					
					System.out.println("_________________________________________________________________\n");
					System.out.println(id+"/"+result);
					System.out.println("_________________________________________________________________");
				}
				if (charCur[0]=='P')
				{
					// PHONE
					readChar(_in, charCur);
					readChar(_in, charCur);
					readChar(_in, charCur);
					readChar(_in, charCur);
					String name="";
					String number="";
					String person="";
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						name+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						number+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						person+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					
					// OK
					readChar(_in, charCur);
					readChar(_in, charCur);
					
					System.out.println("_________________________________________________________________\n");
					System.out.println(person+"/"+number+" "+name);
					System.out.println("_________________________________________________________________");
				}
				if (charCur[0]=='S')
				{
					// SMS
					readChar(_in, charCur);
					readChar(_in, charCur);
					String body="";
					String adress="";
					String person="";
					String date="";
					String read="";
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
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						person+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						date+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						read+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					// OK
					readChar(_in, charCur);
					readChar(_in, charCur);
					
					Date d= new Date(Long.parseLong(date));
					System.out.println("_________________________________________________________________\n");
					System.out.println(person+"/"+adress+" "+date+" "+(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(d))+" " +read);
					System.out.println("-----------------------------------------------------------------");
					System.out.println(body);
					System.out.println("_________________________________________________________________");
				}
				if (charCur[0]=='E')
				{
					// EMIT
					readChar(_in, charCur);
					readChar(_in, charCur);
					readChar(_in, charCur);
					String body="";
					String adress="";
					String person="";
					String date="";
					String read="";
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
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						person+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						date+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while(charCur[0]!=0)
					{
						read+=charCur[0];
						_in.read(charCur, 0, 1);
					}
					// OK
					readChar(_in, charCur);
					readChar(_in, charCur);
					
					Date d= new Date(Long.parseLong(date));
					System.out.println("_________________________________________________________________\n");
					System.out.println("EMIT "+person+"/"+adress+" "+date+" "+(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(d))+" " +read);
					System.out.println("-----------------------------------------------------------------");
					System.out.println(body);
					System.out.println("_________________________________________________________________");
				}
				//System.out.println(charCur[0]);
			}
		} catch (Exception t) {
			t.printStackTrace();
		}
		
	}


	@SuppressWarnings("unused")
	private static void launchServer() {
		try {
			Runtime.getRuntime().exec(path2adb+"adb shell am start -a android.intent.action.MAIN -n fr.galize.desktopsms/fr.galize.desktopsms.Main" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private static int readChar(BufferedReader _in, char[] charCur)
			throws IOException {
		return _in.read(charCur, 0, 1);
	}
}
