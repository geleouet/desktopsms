/**
 * 
 */
package org.desktopSMS.comm;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.desktopSMS.model.Contact;
import org.desktopSMS.model.MainModel;
import org.desktopSMS.model.SMS;
import org.desktopSMS.model.SMSSender;
import org.desktopSMS.model.SendingStatus;

final class ClientListener implements Runnable {
	private final BufferedReader _in;

	ClientListener(BufferedReader _in) {
		this._in = _in;
	}

	public void run() {
		
		System.out.println("Launching client listener");

		char charCur[] = new char[1]; // déclaration d'un tableau de char d'1
										// élement, _in.read() y stockera le
										// char lu

		try {
			while (Communication.readChar(_in, charCur) != -1) // attente en
																// boucle des
																// messages
																// provenant du
																// client
																// (bloquant sur
																// _in.read())
			{
				if (charCur[0] == 'N') {
					// NEW
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					String numero = "";
					String body = "";
					String time = "";
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						time += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						numero += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						body += charCur[0];
						_in.read(charCur, 0, 1);
					}

					// OK
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);

					Date d = new Date(Long.parseLong(time));

					System.out
							.println("_________________________________________________________________\n");
					System.out.println(new SimpleDateFormat("dd/MM/yyyy hh:mm")
							.format(d)
							+ "/" + numero + "/" + body);
					System.out
							.println("_________________________________________________________________");

					SMS s = new SMS(body, numero, MainModel.getInstance()
							.getContactByAddress(numero).getPerson(), d,
							"false");
					MainModel.getInstance().addSms(s);
				}
				if (charCur[0] == 'A') {
					// ACK
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					String id = "";
					String result = "";
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						id += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						result += charCur[0];
						_in.read(charCur, 0, 1);
					}

					// OK
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					SendingStatus status = SendingStatus.valueOf(result);
					SMSSender.getInstance().reconciliate(Long.parseLong(id),
							status);
					System.out
							.println("_________________________________________________________________\n");
					System.out.println(id + "/" + result);
					System.out
							.println("_________________________________________________________________");
				}
				if (charCur[0] == 'P') {
					// PHONE
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					String name = "";
					String number = "";
					String person = "";
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {// person
						person += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {// name
						name += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {// numero
						number += charCur[0];
						_in.read(charCur, 0, 1);
					}

					// OK
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);

					System.out
							.println("_________________________________________________________________\n");
					System.out.println(person + "/" + number + " " + name);
					System.out
							.println("_________________________________________________________________");

					Contact c = new Contact(person, name, number);
					MainModel.getInstance().addContact(c);
				}
				if (charCur[0] == 'S') {
					// SMS
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					String body = "";
					String adress = "";
					String person = "";
					String date = "";
					String read = "";
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						body += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						adress += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						person += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						date += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						read += charCur[0];
						_in.read(charCur, 0, 1);
					}
					// OK
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);

					Date d = new Date(Long.parseLong(date));
					System.out
							.println("_________________________________________________________________\n");
					System.out.println(person
							+ "/"
							+ adress
							+ " "
							+ date
							+ " "
							+ (new SimpleDateFormat("dd/MM/yyyy hh:mm")
									.format(d)) + " " + read);
					System.out
							.println("-----------------------------------------------------------------");
					System.out.println(body);
					System.out
							.println("_________________________________________________________________");

					SMS s = new SMS(body, adress, person, d, read);
					MainModel.getInstance().addSms(s);
				}
				if (charCur[0] == 'E') {
					// EMIT
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);
					String body = "";
					String adress = "";
					String person = "";
					String date = "";
					String read = "";
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						body += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						adress += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						person += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						date += charCur[0];
						_in.read(charCur, 0, 1);
					}
					_in.read(charCur, 0, 1);
					while (charCur[0] != 0) {
						read += charCur[0];
						_in.read(charCur, 0, 1);
					}
					// OK
					Communication.readChar(_in, charCur);
					Communication.readChar(_in, charCur);

					Date d = new Date(Long.parseLong(date));
					System.out
							.println("_________________________________________________________________\n");
					System.out.println(person
							+ "/"
							+ adress
							+ " "
							+ date
							+ " "
							+ (new SimpleDateFormat("dd/MM/yyyy hh:mm")
									.format(d)) + " " + read);
					System.out
							.println("-----------------------------------------------------------------");
					System.out.println(body);
					System.out
							.println("_________________________________________________________________");

					SMS s = new SMS(body, adress, person, d, read);
					MainModel.getInstance().addEmit(s);
				}
				// System.out.println(charCur[0]);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Communication.getInstance().setClosed(true);
		System.err.println("Client Listener closed");
	}
}