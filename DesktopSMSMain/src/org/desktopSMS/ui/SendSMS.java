/**
 * 
 */
package org.desktopSMS.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import org.desktopSMS.comm.Communication;
import org.desktopSMS.model.Conversation;
import org.desktopSMS.model.SMSSender;

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
				long id=Communication.getInstance().sendSMS(number, body);
				SMSSender.getInstance().register(number,body,conversation,id);

			}},"SendSMS").start();;

	}

}