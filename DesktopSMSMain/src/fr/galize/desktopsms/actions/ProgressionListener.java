package fr.galize.desktopsms.actions;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ProgressionListener {

	
	String state;
	int percent=0;
	boolean unknown=true;

	public ProgressionListener() {
	}
	
	PropertyChangeSupport support= new PropertyChangeSupport(this);
	
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		String old=this.state;
		this.state = state;
		support.firePropertyChange("state",old,state);
	}
	public int getPercent() {
		return percent;
	}
	public synchronized void setPercent(int percent) {
		int old=this.percent;
		this.percent = percent;
		unknown=false;
		support.firePropertyChange("percent",old,state);
		if (percent==100)
			notifyAll();
	}
	public boolean isUnknown() {
		return unknown;
	}
	public void setUnknown(boolean unknown) {
		boolean old=this.unknown;
		this.unknown = unknown;
		support.firePropertyChange("unknown",old,unknown);
	}
	
	public synchronized void waitForFinished()
	{
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
