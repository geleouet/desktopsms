package fr.galize.desktopsms.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SMSSender {

	
	
	public static SMSSender _instance = new SMSSender();

	Map<Long,SendingStatus> attente = new HashMap<Long, SendingStatus>(); 
	Map<Long,SMS> attente2 = new HashMap<Long, SMS>(); 

	
	private SMSSender() {
		super();
	}


	public static SMSSender getInstance() {
		return _instance;
	}


	public void register(String number, String body, Conversation conversation, long id) {
		SMS newSMS = new SMS(body,number,conversation.getContact().getPerson(),new Date(),"true");
		newSMS.setId(id);
		newSMS.setEmit(true);
		attente2.put(id, newSMS);
		newSMS.setState(SendingStatus.SENDING);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		reconciliate(id,null);
		MainModel.getInstance().addEmit(newSMS);
	}
	
	public synchronized void reconciliate(long id,SendingStatus newstatus) {
		SendingStatus status = attente.get(id);
		SMS sms = attente2.get(id);
		if (newstatus!=null&&sms!=null)
		{
			attente.put(id,newstatus);
			sms.setState(newstatus);
		} else if (newstatus!=null&&sms==null)
		{
			if (status!=null
					&&status!=SendingStatus.DELIVERED
					&&status!=SendingStatus.CANCELLED)
				attente.put(id,newstatus);
			else if (status==null)
				attente.put(id,newstatus);
				
			
		}else if (newstatus==null&&sms!=null)
		{
			if (status!=null)
				sms.setState(status);
		}else {
			// Should never happen
			System.err.println("Sould never happen");
		}
			
	}
	
	
}
