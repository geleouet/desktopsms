package fr.galize.desktopsms.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements PropertyChangeListener {

	PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}
	public Conversation(Contact c, List<SMS> sms) {
		super();
		this.c = c;
		this.sms = sms;
	}
	Contact c;
	List<SMS> sms = new ArrayList<SMS>();
	public Contact getContact() {
		return c;
	}
	public void setContact(Contact c) {
		this.c = c;
	}
	public List<SMS> getSms() {
		return sms;
	}
	
	public String getId()
	{
		return c.getId();
	}
	public void addSms(SMS c2) {
		if (!sms.contains(c2))
		{
			sms.add(c2);
			c2.addPropertyChangeListener("state",this);
			support.firePropertyChange("add", false, true);
		}
		else
		{
			if (c2.isEmit())
			{
				sms.remove(c2);
				sms.add(c2);
			}
			if (c2.isEmit()&&c2.getId()>0)
			support.firePropertyChange("add", false, true);
		}
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}
	public void propertyChange(PropertyChangeEvent evt) {
		SMS source = (SMS) evt.getSource();
		int indexOf = sms.indexOf(source);
		if (indexOf!=-1)
		{
			support.fireIndexedPropertyChange("SMS", indexOf, null, source);
		}
	}
	
	
}
