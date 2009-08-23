package fr.galize.desktopsms.comm;

public class CommunicationException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommunicationException() {
		super();
	}

	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(Throwable cause) {
		super(cause);
	}
	/*
	9:  AddContact
	java.lang.NumberFormatException: For input string: "Florian Joyeux"
	       at java.lang.NumberFormatException.forInputString(NumberFormatException.java:48)
	       at java.lang.Long.parseLong(Long.java:403)
	       at java.lang.Long.parseLong(Long.java:461)
	       at fr.galize.desktopsms.comm.ClientListener.run(ClientListener.java:187)
	       at java.lang.Thread.run(Thread.java:613)
	Socket close
	Connection true
	RefreshHisto false
	Client Listener closed
	java.lang.NullPointerException
	       at fr.galize.desktopsms.comm.ClientListener.run(ClientListener.java:78)
	       at java.lang.Thread.run(Thread.java:613)
	Socket close
	Connection true
	RefreshHisto false
	Client Listener closed
	*/
	
}
