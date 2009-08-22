package org.desktopSMS.model;

public enum SendingStatus {
	
	
	HISTORY(""),
	SENDING(""),
	SENT("SENT"),
	GENERIC_FAILURE("GENERICFAILURE"),
	NOSERVICE("NOSERVICE"),
	NULLPDU("NULLPDU"),
	RADIOOFF("RADIOOFF"),
	DELIVERED("DELIVERED"),
	CANCELLED("NOTDELIVERED"),
	
	;
	String key;

	private SendingStatus(String key) {
		this.key = key;
	}
	
}
