package fr.galize.desktopsms.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;
import javax.swing.SwingUtilities;

import fr.galize.desktopsms.model.MainModel;

public class ContactListModel extends  AbstractListModel {


	private static final long serialVersionUID = -198444794004371222L;

	
	
	public ContactListModel() {
		super();
		MainModel.getInstance().addPropertyChangeListener("addContact", new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				SwingUtilities.invokeLater(new Runnable(){

					public void run() {
						fireContentsChanged(ContactListModel.this, 0, getSize());						
					}});
			}});
	}

	public Object getElementAt(int index) {
		return MainModel.getInstance().getContacts().get(index);
	}

	public int getSize() {
		return MainModel.getInstance().getContacts().size();
	}


}
