package fr.galize.desktopsms.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

public class SMS implements Comparable<SMS>{

	PropertyChangeSupport support = new PropertyChangeSupport(this);
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}
	public SMS() {
		super();
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}
	String body="";
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adress == null) ? 0 : adress.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((read == null) ? 0 : read.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SMS other = (SMS) obj;
		if (adress == null) {
			if (other.adress != null)
				return false;
		} else if (!adress.equals(other.adress))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (read == null) {
			if (other.read != null)
				return false;
		} else if (!read.equals(other.read))
			return false;
		return true;
	}
	String adress="";
	String person="";
	Date date;
	String read="";
	private boolean emit=false;
	private SendingStatus state=SendingStatus.HISTORY;
	private long id=-1;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	private Contact contact;
	
	
	public SMS(String body, String adress, String person, Date date,
			String read) {
		super();
		this.body = body;
		this.adress = adress;
		this.person = person;
		this.date = date;
		this.read = read;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRead() {
		return read;
	}
	public void setRead(String read) {
		this.read = read;
	}
	@Override
	public String toString() {
		return person+":"+adress+":"+date+":"+read+":"+body;
	}
	public int compareTo(SMS o) {
		return this.getDate().compareTo(o.getDate());
	}
	public void setEmit(boolean b) {
		this.emit = b;
		
	}
	public boolean isEmit() {
		return emit;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
		
	}
	public Contact getContact() {
		return contact;
	}
	public void setState(SendingStatus state) {
		SendingStatus old=this.state;
		this.state = state;
		support.firePropertyChange("state", old, state);
	}
	public SendingStatus getState() {
		return state;
	}
	
	
}
