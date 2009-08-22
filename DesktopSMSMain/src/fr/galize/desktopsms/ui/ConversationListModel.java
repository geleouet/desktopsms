package fr.galize.desktopsms.ui;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;

import fr.galize.desktopsms.model.Conversation;

public class ConversationListModel extends  AbstractListModel implements PropertyChangeListener {


	private static final long serialVersionUID = -198444794004371222L;
	private final Conversation conversation;

	
	
	public ConversationListModel(Conversation conversation) {
		super();
		this.conversation = conversation;
		conversation.addPropertyChangeListener("add",this);
		conversation.addPropertyChangeListener("SMS",new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {
				int index = ((IndexedPropertyChangeEvent)evt).getIndex();
				fireContentsChanged(this, index, index);
			}});
	}

	public Object getElementAt(int index) {
		return conversation.getSms().get(index);
	}

	public int getSize() {
		int size = conversation.getSms().size();
		return size;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		fireContentsChanged(this, 0, getSize());
	}


}
