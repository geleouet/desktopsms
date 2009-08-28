package fr.galize.desktopsms.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import fr.galize.desktopsms.model.SMS;
import fr.galize.desktopsms.model.SendingStatus;
import fr.galize.desktopsms.util.Couple;

public class SMSCellRenderer implements ListCellRenderer {

	JPanel pane = new JPanel(new GridBagLayout()) {

		private static final long serialVersionUID = 92672839623756871L;
		private BufferedImage	createImage	= null;

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			fillGraph(g2);
			/*if (createImage == null)
				{
				refresh(false);
				}
			else
				g2.drawImage(createImage, 0, 0, this);*/
		}


		private static final double	FACTOR	= 0.66;


		public Color darker(Color c) {
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();

			int i = (int) (1.0 / (1.0 - FACTOR));
			if (r == 0 && g == 0 && b == 0) {
				return new Color(i, i, i);
			}
			if (r <= 255 && r > 255 - i)
				r -= 75;
			if (g <= 255 && g > 255 - i)
				g -= 75;
			if (b <= 255 && b > 255 - i)
				b -= 75;

			r = Math.min((int) (r * FACTOR), 255);
			g = Math.min((int) (g * FACTOR), 255);
			b = Math.min((int) (b * FACTOR), 255);
			return new Color(r, g, b,10);
		}



		@Override
		public void update(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			if (createImage == null)
			{
				fillGraph(g2);
				refresh(false);
			}
			else
				g2.drawImage(createImage, 0, 0, this);
		}



		@Override
		public void invalidate() {
			createImage = null;
			super.invalidate();
		}

		public void refresh(boolean repaint) {

			if (createImage == null && getWidth() != 0 && getHeight() != 0)
				createImage =  new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB_PRE);//(BufferedImage) createImage(getWidth(), getHeight());		

			if (createImage == null)
				return;
			Graphics2D createGraphics = createImage.createGraphics();
			fillGraph(createGraphics);
			repaint();
		}

		private void fillGraph(Graphics2D g2) {

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int w = getSize().width - 1;
			int h = getSize().height - 1;
			Point2D p1 = new Point2D.Double(0,0);
			Point2D p2 = new Point2D.Double(w, h);
			Paint paint = new GradientPaint(p1,getBackground(),  p2, darker(getBackground()), true);
			g2.setPaint(paint);
			g2.fill(new Rectangle2D.Double(0,0,w,h));

			/*Color c= (_value*_value>0.001?getForeground():getBackground());

			g2.setPaint(_panelColor);
			//g2.fillRect(0, 0, getWidth(), getHeight());
			g2.fill(new RoundRectangle2D.Double(0, 0, rnd+1, rnd+1, rnd-1, rnd-1));


			final int dh = h / 2;
			final int dw = w / 2;
			final Color d = darker(c);

			// Fond
			{
				Point2D p1 = new Point2D.Double(0,0);
				Point2D p2 = new Point2D.Double(rnd, rnd);
				Paint paint2 = new GradientPaint(p2, d, p1, c, true);
				g2.setPaint(paint2);
				g2.fill(new Ellipse2D.Double(0,0,rnd,rnd));
			}
			// Reflet
			{
				Point2D p1 = new Point2D.Double(0.0, 0.0);
				Point2D p2 = new Point2D.Double(0.0, h * .3);
				Color c1 = new Color(255, 255, 255, 153);
				Color c2 = new Color(255, 255, 255, 0);
				Paint paint = new GradientPaint(p1, c1, p2, c2);
				g2.setPaint(paint);
				g2.fill(new Ellipse2D.Double(rnd*0.05,rnd*0.05,rnd*0.9,rnd*0.9));
			} 
			// Anneau
			{
				g2.setColor(darker(d));
				final double e = (rnd*.05);
				g2.setStroke(new BasicStroke((float) e, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
				g2.draw(new Ellipse2D.Double(e*.5,e*.5,rnd-e,rnd-e));
			}
			 */
		}

	};
	JLabel name=new JLabel("-----");
	JLabel date =new JLabel("-------");
	JLabel state =new JLabel("");	
	JTextPane texte = new JTextPane();
	JPanel res=new JPanel(new GridBagLayout());

	Style regular;
	Style EMO_HAPPY;
	{
		pane.setOpaque(true);
		pane.add(name, new GridBagConstraints(0,0,1,1,0.,0.,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,20,0,0),0,0));
		pane.add(date, new GridBagConstraints(0,1,1,1,0.,0.,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,05),0,0));
		pane.add(texte, new GridBagConstraints(1,0,1,2,1.,1.,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,10,2,2),0,0));
		pane.add(state, new GridBagConstraints(2,0,1,2,0.,0.,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,05),20,20));
		pane.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
						BorderFactory.createLineBorder(Color.black))
		);
		pane.setBackground(Color.lightGray);
		texte.setOpaque(false);
		name.setFont(name.getFont().deriveFont(Font.BOLD));
		res.add(pane, new GridBagConstraints(0,0,1,1,1.,1.,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));




		//Initialize some styles.
		StyledDocument doc = texte.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().
		getStyle(StyleContext.DEFAULT_STYLE);

		regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style EMO_HAPPY = doc.addStyle("icon", regular);
		StyleConstants.setAlignment(EMO_HAPPY, StyleConstants.ALIGN_CENTER);
		ImageIcon emoHappyIcon = AppRessources.EMO_HAPPY_ICON;
		if (emoHappyIcon != null) {
			StyleConstants.setIcon(EMO_HAPPY, emoHappyIcon);
		}
		//	        s = doc.addStyle("button", regular);
		//	        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		//	        ImageIcon soundIcon = createImageIcon("images/sound.gif",
		//	                                              "sound icon");
		//	        JButton button = new JButton();
		//	        if (soundIcon != null) {
		//	            button.setIcon(soundIcon);
		//	        } else {
		//	            button.setText("BEEP");
		//	        }
		//	        button.setCursor(Cursor.getDefaultCursor());
		//	        button.setMargin(new Insets(0,0,0,0));
		//	        button.setActionCommand(buttonString);
		//	        button.addActionListener(this);
		//	        StyleConstants.setComponent(s, button);

	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		SMS c = (SMS) value;
		SendingStatus s = c.getState();
		state.setIcon(null);
		switch (s) {
		case HISTORY:
			state.setText("");
			break;
		case SENDING:
			//			state.setText("SENDING");
			state.setIcon(AppRessources.SCRIPTGO_ICON);
			break;
		case SENT:
			//			state.setText("SENT");
			state.setIcon(AppRessources.SCRIPTGO_ICON);
			break;
		case DELIVERED:
			state.setText("");
			break;
		case CANCELLED:
			//			state.setText("CANCELLED");
			state.setIcon(AppRessources.SCRIPTERROR_ICON);
			break;
		case GENERICFAILURE:
			//			state.setText("FAILURE");
			state.setIcon(AppRessources.SCRIPTERROR_ICON);
			break;
		case NOSERVICE:
			//			state.setText("NOSERVICE");
			state.setIcon(AppRessources.SCRIPTERROR_ICON);
			break;
		case NULLPDU:
			//			state.setText("NULLPDU");
			state.setIcon(AppRessources.SCRIPTERROR_ICON);
			break;
		case RADIOOFF:
			//			state.setText("RADIOOFF");
			state.setIcon(AppRessources.SCRIPTERROR_ICON);
			break;

		default:
			break;
		}
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		if (c.isEmit())	
			name.setText("Me");
		else
			name.setText(c.getContact().getName());
		date.setText(sdf.format(c.getDate()));
		String body = c.getBody();
		StyledDocument doc=cache.get(body);
		if (doc==null)
		{
			doc = new DefaultStyledDocument();
			String body_=body;
			if (body.length()>50)
				body_=ajouteEspace(body);
			Couple<List<String>, List<String>> resSmile = generateWithSmileys(body_);
			List<String> split = resSmile.getA();
			List<String> initStyles=resSmile.getB();
			try {
				for (int i=0; i < split.size(); i++) {
					doc.insertString(doc.getLength(), split.get(i),
							doc.getStyle(initStyles.get(i)));

				}
				cache.put(body, doc);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			
		}
		texte.setStyledDocument(doc);

		res.revalidate();
		pane.setBackground(new Color(230,240,255));
		if (isSelected&&!c.isEmit())
			pane.setBackground(new Color(230,240,255).darker());
		else if (isSelected&&c.isEmit())
			pane.setBackground(new Color(200,255,230).darker());
		else if (c.isEmit())	
			pane.setBackground(new Color(230,255,255));
		return res;
	}

	WeakHashMap<String, StyledDocument> cache = new WeakHashMap<String, StyledDocument>();
	private Couple<List<String>, List<String>> generateWithSmileys(String body) {
		List<String> plain=new ArrayList<String>();
		List<String> style=new ArrayList<String>();

		List<SmileyMotif> smileys = new ArrayList<SmileyMotif>();
		smileys.add(new SmileyMotif(":)",AppRessources.EMO_HAPPY_ICON));

		for (SmileyMotif sm:smileys)
		{
			int indexOf = body.indexOf(sm.getMotif());
			if (indexOf>-1)
			{
				String debut = body.substring(0, indexOf);
				String fin = body.substring(indexOf+sm.getMotif().length());
				Couple<List<String>, List<String>> d = generateWithSmileys(debut);
				Couple<List<String>, List<String>> f = generateWithSmileys(fin);
				plain.addAll(d.getA());
				plain.add(" ");
				plain.addAll(f.getA());

				style.addAll(d.getB());
				style.add("icon");
				style.addAll(f.getB());

				return new Couple<List<String>, List<String>>(plain,style);
			}
		}
		plain.add(body);
		style.add("regular");
		return new Couple<List<String>, List<String>>(plain,style);
	}

	private String ajouteEspace(String body) {
		String res=body;
		if (body.length()>40)
		{
			String sub = body.substring(0, 35);
			body=body.substring(35);
			res=sub;
			int i;
			for (i=0;i<10;i++)
			{
				if (body.length()>0&&body.substring(0, 1).equals(" "))
				{
					body=body.substring(1);
					sub = sub+"\n";
					res = sub+ajouteEspace(body);
					return res;
				}
				else if (body.length()>0)
				{
					sub = sub+body.substring(0, 1);
					body=body.substring(1);
				} 
				else
					i=10;
			}
			sub = sub+"\n";
			res = sub+ajouteEspace(body);

		}
		return res;
	}

}
