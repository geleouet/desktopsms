package fr.galize.desktopsms.ui;

import java.net.URL;

import javax.swing.ImageIcon;

public class AppRessources {

	private final static String DSMS_PATH = new String("fr/galize/desktopsms/images/desktopSMS.png");
	private final static String USB_PATH = new String("fr/galize/desktopsms/images/usb.png");
	private final static String WIFI_PATH = new String("fr/galize/desktopsms/images/wifi.png");
	private final static String PAGE_PATH = new String("fr/galize/desktopsms/images/folder_page.png");
	private final static String LIGHTING_PATH = new String("fr/galize/desktopsms/images/lightning.png");
	private final static String BULLET_PATH = new String("fr/galize/desktopsms/images/bullet_black.png");
	private final static String SCRIPTERROR_PATH = new String("fr/galize/desktopsms/images/scripterror.png");
	private final static String SCRIPTGO_PATH = new String("fr/galize/desktopsms/images/script_go.png");
	private final static String COMPUTER_PATH = new String("fr/galize/desktopsms/images/computer.png");
	private final static String CONNECT_PATH = new String("fr/galize/desktopsms/images/connect.png");
	private final static String DISCONNECT_PATH = new String("fr/galize/desktopsms/images/disconnect.png");
	private final static String CONNECTBLUE_PATH = new String("fr/galize/desktopsms/images/connectblue.png");
	private final static String DISCONNECTBLUE_PATH = new String("fr/galize/desktopsms/images/disconnectblue.png");

	private final static String EMO_HAPPY_PATH = new String("fr/galize/desktopsms/images/emo/emo_im_happy.png");
	
	
	public static ImageIcon EMO_HAPPY_ICON=null;
	public static ImageIcon DSMS_ICON=null;
	public static ImageIcon USB_ICON=null;
	public static ImageIcon WIFI_ICON=null;
	public static ImageIcon PAGE_ICON=null;
	public static ImageIcon LIGHTING_ICON=null;
	public static ImageIcon BULLET_ICON=null;
	public static ImageIcon SCRIPTERROR_ICON=null;
	public static ImageIcon SCRIPTGO_ICON=null;
	public static ImageIcon COMPUTER_ICON=null;
	public static ImageIcon CONNECT_ICON=null;
	public static ImageIcon DISCONNECT_ICON=null;
	public static ImageIcon CONNECTBLUE_ICON=null;
	public static ImageIcon DISCONNECTBLUE_ICON=null;
	
	public static ImageIcon getImageIcon(String path) {
		URL imgURL = AppRessources.class.getClassLoader().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public static void loadImages() {

		// ajoute les proprietes du fichier aux props systeme
			try {
				EMO_HAPPY_ICON=getImageIcon(EMO_HAPPY_PATH);
				
				DSMS_ICON=getImageIcon(DSMS_PATH);
				USB_ICON=getImageIcon(USB_PATH);
				WIFI_ICON=getImageIcon(WIFI_PATH);
				PAGE_ICON=getImageIcon(PAGE_PATH);
				LIGHTING_ICON=getImageIcon(LIGHTING_PATH);
				BULLET_ICON=getImageIcon(BULLET_PATH);
				SCRIPTERROR_ICON=getImageIcon(SCRIPTERROR_PATH);
				SCRIPTGO_ICON=getImageIcon(SCRIPTGO_PATH);
				COMPUTER_ICON=getImageIcon(COMPUTER_PATH);
				CONNECT_ICON=getImageIcon(CONNECT_PATH);
				DISCONNECT_ICON=getImageIcon(DISCONNECT_PATH);
				CONNECTBLUE_ICON=getImageIcon(CONNECTBLUE_PATH);
				DISCONNECTBLUE_ICON=getImageIcon(DISCONNECTBLUE_PATH);
			} catch (Exception e) {}
		}
}
