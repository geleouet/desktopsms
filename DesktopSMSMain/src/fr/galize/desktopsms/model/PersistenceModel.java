package fr.galize.desktopsms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersistenceModel {

	private List<Contact> contacts = new ArrayList<Contact>();
	private List<SMS> smss = new ArrayList<SMS>();
	private List<SMS> emits = new ArrayList<SMS>();
	private Date lastDate=new Date(0);
	
	public List<Contact> getContacts() {
		return contacts;
	}
	public List<SMS> getEmits() {
		return emits;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public List<SMS> getSmss() {
		return smss;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public void setEmits(List<SMS> emits) {
		this.emits = emits;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public void setSmss(List<SMS> smss) {
		this.smss = smss;
	}
}
