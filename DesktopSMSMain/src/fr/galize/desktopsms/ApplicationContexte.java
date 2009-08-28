package fr.galize.desktopsms;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ApplicationContexte {

	
	public static String path2adb="E:\\android\\android-sdk-windows-1.5_r1\\android-sdk-windows-1.5_r1\\tools\\adb.exe";
	public static String path2save="E:\\";
//	public static String path2adb="/Users/jacques/SDKs/android/tools/";
	private static boolean isUsb=true;
	private static String deviceIp="10.209.57.178";
	private static String id=null;
	
	
	public static void init()
	{
		Preferences pref= Preferences.userNodeForPackage(ApplicationContexte.class);
		path2adb=pref.get("path2adb", "");
		System.out.println("path2adb="+path2adb);
		path2save=pref.get("path2save", "");
		System.out.println("path2save="+path2save);
		isUsb=Boolean.parseBoolean(pref.get("isUsb", "true"));
		System.out.println("isUsb="+isUsb);
		deviceIp=pref.get("deviceIp", "");
		System.out.println("deviceIp="+deviceIp);
	}


	public static void setPath2adb(String absolutePath) {
		Preferences pref= Preferences.userNodeForPackage(ApplicationContexte.class);
		pref.put("path2adb", absolutePath);
		path2adb=absolutePath;
		try {
			pref.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	public static void setPath2save(String absolutePath) {
		Preferences pref= Preferences.userNodeForPackage(ApplicationContexte.class);
		pref.put("path2save", absolutePath);
		path2save=absolutePath;
		try {
			pref.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	public static void setDeviceIp(String absolutePath) {
		Preferences pref= Preferences.userNodeForPackage(ApplicationContexte.class);
		pref.put("deviceIp", absolutePath);
		deviceIp=absolutePath;
		try {
			pref.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}


	public static boolean isUsb() {
		return isUsb;
	}


	public static void setUsb(boolean selected) {
		Preferences pref= Preferences.userNodeForPackage(ApplicationContexte.class);
		pref.put("isUsb", Boolean.toString(selected));
		isUsb=selected;
		try {
			pref.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
	}


	public static String getDeviceIp() {
		return deviceIp;
	}


	public static String getId() {
		return id;
	}


	public static void setId(String id) {
		ApplicationContexte.id = id;
	}
}
