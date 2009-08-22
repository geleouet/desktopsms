package fr.galize.desktopsms.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

import fr.galize.desktopsms.comm.Communication;

public class AutoConnect extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8236082261964570232L;
	private boolean auto=false;

	public void actionPerformed(ActionEvent e) {
		auto = !auto;
		setAuto();
	}

	private void setAuto() {
		boolean closed = Communication.getInstance().isClosed();
		if (closed&&auto)
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Connection.connect();
		}
	}

	public AutoConnect() {
		super("Auto-connect", null);
		Communication.getInstance().addPropertyChangeListener("closed",new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				setAuto();
			}});
	}


}
