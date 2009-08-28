/**
 * 
 */
package fr.galize.desktopsms.ui;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import fr.galize.desktopsms.comm.Communication;
import fr.galize.desktopsms.model.Conversation;
import fr.galize.desktopsms.model.SMSSender;

final class SendSMS extends AbstractAction {
	/**
	 * 
	 */
	private Conversation conversation;
	private JTextField field;
	private static final long serialVersionUID = -6376296068167825254L;


	SendSMS(Conversation conversation, String name, JTextField field) {
		super(name);
		this.conversation = conversation;
		this.field = field;
	}

	public void actionPerformed(ActionEvent e) {
		final String text = field.getText();
		field.setText("");
		new Thread(new Runnable(){

			public void run() {
				String number = conversation.getContact().getNumber();
				String body = text;
				long id=0;
				try {
					id = Communication.getInstance().sendSMS(number, body);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (id>0)
					SMSSender.getInstance().register(number,body,conversation,id);
				else
				{
					JOptionPane.showMessageDialog(field, "Error while sending", "Sending error", JOptionPane.ERROR_MESSAGE);
					field.setText(text);
				}

			}},"SendSMS").start();;

	}

}