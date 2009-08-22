package fr.galize.desktopsms;

public class Logger {

	
	
	public static Logger Log = new Logger();
	
	public void erreur(String message, Throwable t)
	{
		System.err.println("ERREUR :"+message);
		t.printStackTrace();
	}
}
