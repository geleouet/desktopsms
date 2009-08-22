package fr.galize.desktopsms.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

import fr.galize.desktopsms.comm.Communication;
import fr.galize.desktopsms.model.MainModel;
import fr.galize.desktopsms.ui.AppRessources;

public class RefreshHisto extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8236082261964570232L;

	public void actionPerformed(ActionEvent e) {
		Communication communication = Communication.getInstance();
		communication.getHisto(MainModel.getInstance().getLastDate());
	}

	public RefreshHisto() {
		super("Refresh", AppRessources.LIGHTING_ICON);
		setEnabled(false);
		Communication.getInstance().addPropertyChangeListener("closed",new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				RefreshHisto.this.setEnabled(!Communication.getInstance().isClosed());
				System.out.println("RefreshHisto "+RefreshHisto.this.isEnabled());
			}});
	}

}
