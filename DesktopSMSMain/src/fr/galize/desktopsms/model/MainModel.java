package fr.galize.desktopsms.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.galize.desktopsms.ApplicationContexte;

public class MainModel {

	
	static MainModel _instance = new MainModel();
	
	List<Contact> contacts = new ArrayList<Contact>();
	List<SMS> smss = new ArrayList<SMS>();
	private List<SMS> emits = new ArrayList<SMS>();
	List<Conversation> conversations = new ArrayList<Conversation>();
	Map<String,Conversation> map= new HashMap<String, Conversation>();
	
	PropertyChangeSupport support = new PropertyChangeSupport(this);

	private Date lastDate=new Date(0);

	
	public List<Conversation> getConversations() {
		return conversations;
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public List<SMS> getSmss() {
		return smss;
	}
	
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

	public static MainModel getInstance()
	{
		return _instance;
	}
	
	public MainModel() {
		super();
	}

	public void addContact(Contact c)
	{
		synchronized (contacts) {

			if (!contacts.contains(c))
			{
				contacts.add(c);
				Collections.sort(contacts);
				support.firePropertyChange("addContact",null,c);
			}
		}
	}
	public void addSms(SMS c)
	{
		synchronized (smss) {

			if (!smss.contains(c))
			{
				smss.add(c);
				updateLastDate(c);
				updateConversation(c);
				support.firePropertyChange("addSMS",null,c);
			}
		}

	}
	private void updateConversation(SMS c) {
		// Search associated contact
		if (c.isEmit())
		{
			String adress = c.getAdress();
			Contact contact = getContactByAddress(adress);
			if (contact==null)
				return;
			Conversation conv=map.get(contact.getId());
			c.setContact(contact);
			if (conv!=null)
			{
				conv.addSms(c);
			}
		}
		else
		{
			String adress = c.getAdress();
			Contact contact = getContactByAddress(adress);
			if (contact==null)
			{
				String person = c.getPerson();
				contact = getContactByPerson(person);
			}
			if (contact==null)
				return;
			Conversation conv=map.get(contact.getId());
			c.setContact(contact);
			if (conv!=null)
			{
				conv.addSms(c);
			}
		}
	}

	private Contact getContactByPerson(String person) {
		for (Contact c:contacts)
		{
			if (c.getPerson().equals(person))
			{
				return c;
			}
		}
		return null;		
	}

	public Contact getContactByAddress(String adress) {
		adress = extractNumber(adress);
		for (Contact c:contacts)
		{
			String number = c.getNumber();
			number = extractNumber(number);
			
			if (number.equals(adress))
			{
				return c;
			}
		}
		return null;
	}

	public static String extractNumber(String number) {
		number=number.replace("-", "");// Enleve le format Americain
		number=number.replace(" ", "");// Enleve les espaces
		if (number.startsWith("+"))
		{
			number="0"+number.substring(3);
		}
		return number;
	}

	public void addConversation(Contact c)
	{
		ArrayList<SMS> smslist = new ArrayList<SMS>();
		Conversation cc= new Conversation(c,smslist);
		conversations.add(cc);
		map.put(cc.getId(), cc);
		for (SMS s:smss)
		{
			updateConversation(s);
		}
		for (SMS s:getEmits())
		{
			updateConversation(s);
		}
		Collections.sort(cc.sms);
		support.firePropertyChange("addConversation",null,cc);
	}
	public Conversation getConversation(String id)
	{
		return map.get(id);
	}

	public void addEmit(SMS c) {
		synchronized (emits) {

			if (!getEmits().contains(c))
			{
				c.setEmit(true);
				getEmits().add(c);
				updateLastDate(c);
				updateConversation(c);
				support.firePropertyChange("addEmit",null,c);
			}
		}

	}

	private void updateLastDate(SMS c) {
		if (c.getDate().after(lastDate))
		{
			Date old = lastDate;
			lastDate=c.getDate();
			support.firePropertyChange("lastDate",old,lastDate);
		}
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setEmits(List<SMS> emits) {
		this.emits = emits;
	}

	public List<SMS> getEmits() {
		return emits;
	}

	public void init() {
		// Cherche un historique de sms
		File f = new File(ApplicationContexte.path2save+"/SMSDesktop/"+ApplicationContexte.getId()+"/histo.xml");
		System.out.println("Loading historique :"+f.getAbsolutePath());
		try {
			XMLDecoder decoder= new XMLDecoder(new FileInputStream(f));
			PersistenceModel pm =(PersistenceModel) decoder.readObject();
			List<Contact> contacts_ = pm.getContacts();
			for (Contact c:contacts_)
				addContact(c);
			List<SMS> smss_ = pm.getSmss();
			for (SMS s:smss_)
				addSms(s);
			List<SMS> emits_ = pm.getEmits();
			for (SMS s:emits_)
				addEmit(s);
			lastDate=pm.getLastDate();
			
		} catch (Exception e) {
		}		
	}

	public void save() {
		if (ApplicationContexte.getId()==null)
			return;
		File f = new File(ApplicationContexte.path2save+"/SMSDesktop/"+ApplicationContexte.getId()+"/histo.xml");
		System.out.println("Saving historique :"+f.getAbsolutePath());
		try {
			try {
				f.getParentFile().mkdirs();
			} catch (Exception e1) {
			}
			
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(f));
			PersistenceModel pm= new PersistenceModel();
			pm.setContacts(MainModel.getInstance().getContacts());
			pm.setEmits(MainModel.getInstance().getEmits());
			pm.setSmss(MainModel.getInstance().getSmss());
			pm.setLastDate(MainModel.getInstance().getLastDate());
			encoder.writeObject(pm);
			encoder.flush();
			encoder.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
