/**
 * 
 */
package fr.galize.desktopsms.comm;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.JProgressBar;

import fr.galize.desktopsms.ApplicationContexte;
import fr.galize.desktopsms.model.Contact;
import fr.galize.desktopsms.model.MainModel;
import fr.galize.desktopsms.model.SMS;
import fr.galize.desktopsms.model.SMSSender;
import fr.galize.desktopsms.model.SendingStatus;

public final class ClientListener implements Runnable {
	private final DataInputStream _in;
	private final JProgressBar progressBar;

	ClientListener(DataInputStream _in, JProgressBar progressBar) {
		this._in = _in;
		this.progressBar = progressBar;
	}

	public void run() {
		
		System.out.println("Launching client listener");

		try {
			while (true)
			{
				int methode=_in.readInt();
				switch (methode) {
				case 1:
					readSMS(_in);
					break;
				case 2:
					readContact(_in);
					break;
				case 3:
					readAck(_in);
					break;
				case 4:
					readPhoto(_in);
					break;
				case 5:
					readNew(_in);
					break;
				case 6:
					readNumber(_in);
					break;

				default:
					System.err.println("Unknown methode id :"+methode);
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Communication.getInstance().setClosed(true);
		System.err.println("Client Listener closed");
	}
	
	
	private void readNumber(DataInputStream in) throws IOException {
		String body=readString(in);
		ApplicationContexte.setId(body);
		MainModel.getInstance().init();
	}

	private void readPhoto(DataInputStream in) throws IOException {

		// IMG
		int id=in.readInt();
		long length=in.readLong();
		FileOutputStream fos = new FileOutputStream(new File(ApplicationContexte.path2save+"/SMSDesktop/"+ApplicationContexte.getId()+"/contact"+id+".png"));
		byte buffer[]=new byte[512*1024];
		int nbLecture;
		int lu=0;
		while( lu<length&&(nbLecture = in.read(buffer,0,Math.min(buffer.length, (int) (length-lu)))) != -1 ) {
			fos.write(buffer, 0, nbLecture);
			lu+=nbLecture;
		} 
		
		fos.flush();
		fos.close();
//		JOptionPane.showMessageDialog(null, panel);
			
	}

	private void readNew(DataInputStream in) throws IOException {
		long time=in.readLong();
		String numero=readString(in);
		String body=readString(in);
		
		Date d = new Date(time);
		SMS s = new SMS(body, numero, MainModel.getInstance()
				.getContactByAddress(numero).getPerson(), d,
				"false");
		MainModel.getInstance().addSms(s);		
	}

	
	
	private void readAck(DataInputStream in) throws IOException {
		long id= in.readLong();
		String result=readString(in);
		SendingStatus status = SendingStatus.valueOf(result);
		SMSSender.getInstance().reconciliate(id,status);
		System.out
				.println("_________________________________________________________________\n");
		System.out.println(id + "/" + result);
		System.out
				.println("_________________________________________________________________");
	
		
	}
	private void readContact(DataInputStream in) throws IOException {
		String person=readString(in);
		String name=readString(in);
		String number=readString(in);
		Contact c = new Contact(person, name, number);
		MainModel.getInstance().addContact(c);
	}
	
	private void readSMS(DataInputStream in) throws IOException {
		String body=readString(in);
		String adress=readString(in);
		String person=readString(in);
		String date=readString(in);
		String read=readString(in);
		String type=readString(in);
		
		Date d = new Date(Long.parseLong(date));
		SMS s = new SMS(body, adress, person, d, read);
		
		if ("2".equals(type))
			MainModel.getInstance().addEmit(s);
		else
			MainModel.getInstance().addSms(s);
			
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