package fr.galize.desktopsms.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;

import fr.galize.desktopsms.comm.Communication;
import fr.galize.desktopsms.comm.CommunicationException;
import fr.galize.desktopsms.ui.AppRessources;

public class Connection extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8236082261964570232L;

	public void actionPerformed(ActionEvent e) {
		connect();
	}

	static void connect() {
		new Thread(new Runnable(){

			public void run() {
				final ProgressMonitor pm = new ProgressMonitor(JFrame.getFrames()[0],"Connection...","Waiting for device",0,100);
				pm.setProgress(0);
				final Communication communication = Communication.getInstance();
				if (communication.isUsb())
				{
					pm.setProgress(1);
					pm.setNote("Forward Port");
					try {
						communication.forwardPort();
					} catch (CommunicationException e1) {
						e1.printStackTrace();
					}
					pm.setProgress(2);
					pm.setNote("Launching server");
					try {
						final ProgressionListener launchServer = communication.launchServer();
						launchServer.addPropertyChangeListener(new PropertyChangeListener(){

							public void propertyChange(PropertyChangeEvent evt) {
								pm.setProgress(5+launchServer.getPercent());
								pm.setNote(launchServer.getState());
								if (launchServer.getPercent()==100)
								{
									pm.close();
									try {
										communication.connecte();
									} catch (UnknownHostException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}	
								}
							}});
						//launchServer.waitForFinished();
					} catch (CommunicationException e1) {
						e1.printStackTrace();
					}
					
				}
				else
				{
					try {
						communication.connecte();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}				
			}},"Connection Thread").start();
		
	}

	public Connection() {
		super("Connect", AppRessources.CONNECTBLUE_ICON);
		Communication.getInstance().addPropertyChangeListener("closed",new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				Connection.this.setEnabled(Communication.getInstance().isClosed());
				System.out.println("Connection "+Connection.this.isEnabled());
			}});
	}

}
