package fr.galize.desktopsms.actions;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import fr.galize.desktopsms.ApplicationContexte;
import fr.galize.desktopsms.ui.AppRessources;

public class PreferenceSettings extends AbstractAction {

	
	public PreferenceSettings(){
		super("Preferences", AppRessources.PAGE_ICON);
	}
	private static final long serialVersionUID = 3378327581309545118L;

	public void actionPerformed(ActionEvent e) {
		final JPanel pane = new JPanel(new GridBagLayout());
		final JTextField adbfield =new JTextField(ApplicationContexte.path2adb);
		final JTextField savefield =new JTextField(ApplicationContexte.path2save);
		final JTextField deviceIp =new JTextField(ApplicationContexte.getDeviceIp());
//		adbfield.setEditable(false);
//		savefield.setEditable(false);
		JButton adbfc =new JButton(new AbstractAction("Adb Path ..."){

			private static final long serialVersionUID = -7690787652566623387L;

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showOpenDialog(pane);
				File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile!=null)
				{
					adbfield.setText(selectedFile.getAbsolutePath());
					try {
						((Dialog) pane.getRootPane().getParent()).pack();
					} catch (Exception e1) {
					}
				}
			}
		}
		);
		JButton savefc =new JButton(new AbstractAction("Save dir ..."){
			
			private static final long serialVersionUID = -7690787652566623387L;
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.showSaveDialog(pane);
				File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile!=null)
				{
					savefield.setText(selectedFile.getAbsolutePath());
					try {
						((Dialog) pane.getRootPane().getParent()).pack();
					} catch (Exception e1) {
					}
				}
			}
		}
		);
		
		JRadioButton usbRadio = new JRadioButton();
		JRadioButton wifiRadio = new JRadioButton();
		ButtonGroup group = new ButtonGroup();
		group.add(usbRadio);
		group.add(wifiRadio);
		usbRadio.setSelected(ApplicationContexte.isUsb());
		wifiRadio.setSelected(!ApplicationContexte.isUsb());
		
		
		int v=0;
		pane.add(adbfc, new GridBagConstraints(0,v,GridBagConstraints.REMAINDER,1,0.,0.,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
		v++;
		pane.add(adbfield, new GridBagConstraints(0,v,GridBagConstraints.REMAINDER,1,1.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,20,0,0),0,0));
		v++;
		pane.add(savefc, new GridBagConstraints(0,v,GridBagConstraints.REMAINDER,1,0.,0.,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(20,0,0,0),0,0));
		v++;
		pane.add(savefield, new GridBagConstraints(0,v,GridBagConstraints.REMAINDER,1,1.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,20,0,0),0,0));
		v++;
		pane.add(usbRadio, new GridBagConstraints(0,v,1,1,0.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(20,20,0,0),0,0));
		pane.add(new JLabel(AppRessources.USB_ICON), new GridBagConstraints(1,v,1,1,1.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(20,0,0,0),0,0));
		pane.add(wifiRadio, new GridBagConstraints(2,v,1,1,0.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(20,20,0,0),0,0));
		pane.add(new JLabel(AppRessources.WIFI_ICON), new GridBagConstraints(3,v,1,1,1.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(20,0,0,0),0,0));
		v++;
		pane.add(new JLabel("IP adress of Android device (on WiFi network):"), new GridBagConstraints(0,v,GridBagConstraints.REMAINDER,1,1.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0));
		v++;
		pane.add(deviceIp, new GridBagConstraints(0,v,GridBagConstraints.REMAINDER,1,1.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,20,0,0),0,0));
		v++;
		
		

		int n = JOptionPane.showConfirmDialog((Component) e.getSource(),
				pane,
				"Preferences",
				JOptionPane.OK_CANCEL_OPTION
				);
		if (n==JOptionPane.OK_OPTION)
		{
			System.out.println("Save");
			ApplicationContexte.setPath2adb(adbfield.getText());
			ApplicationContexte.setPath2save(savefield.getText());
			ApplicationContexte.setUsb(usbRadio.isSelected());
			ApplicationContexte.setDeviceIp(deviceIp.getText());
			
		}
	}


}
