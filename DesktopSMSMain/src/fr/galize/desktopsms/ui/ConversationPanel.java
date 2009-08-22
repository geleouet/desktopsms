package fr.galize.desktopsms.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import fr.galize.desktopsms.model.Conversation;

public class ConversationPanel extends JPanel {

	private static final long serialVersionUID = -2936815732382933211L;

	
	Conversation c;


	public ConversationPanel(Conversation c) {
		super(new GridBagLayout());
		this.c = c;
		init();
	}
	
	final static ExecutorService tpe=Executors.newSingleThreadExecutor();
	
	private void init() {
		final JTextField field = new JTextField();
		AbstractAction sendSMS = new SendSMS(c, "Send", field);
		field.setAction(sendSMS);
		JButton send = new JButton(sendSMS);
		final ConversationListModel dataModel = new ConversationListModel(c);
		final JList conv= new JList(dataModel);
		dataModel.addListDataListener(new ListDataListener(){

			public void contentsChanged(ListDataEvent e) {
				if (isDisplayable())
				{
					tpe.execute(new Runnable(){
						
						public void run() {
							try {
								Thread.sleep(100); // Wait refresh of JList before scrolling
							} catch (InterruptedException e) {
							} 
							final int index2 = dataModel.getSize();
							final int index1 = index2-1;
							SwingUtilities.invokeLater(new Runnable(){
								
								public void run() {
									final Rectangle cellBounds = conv.getCellBounds(index1, index2);
									conv.scrollRectToVisible(cellBounds);
								}});
							
						}});
				}
				else
					System.out.println("Ignore");
				
			}

			public void intervalAdded(ListDataEvent e) {
				
			}

			public void intervalRemoved(ListDataEvent e) {
				
			}});
		conv.setCellRenderer(new SMSCellRenderer());
		JScrollPane comp = new JScrollPane(conv);
		comp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		comp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(comp, new GridBagConstraints(0,0,2,1,10.,10.,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,05),0,0));
		add(field, new GridBagConstraints(0,1,1,1,1.,0.,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,05),0,0));
		add(send, new GridBagConstraints(1,1,1,1,0.,0.,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,05),0,0));

	}
}
