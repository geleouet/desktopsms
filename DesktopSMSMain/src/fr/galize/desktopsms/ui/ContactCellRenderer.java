package fr.galize.desktopsms.ui;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import fr.galize.desktopsms.model.Contact;

public class ContactCellRenderer implements ListCellRenderer {

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
	JLabel numero =new JLabel("-------");
	JLabel icone =new JLabel(AppRessources.BULLET_ICON);
	JPanel res=new JPanel(new GridBagLayout());
	{
		pane.setOpaque(true);
		pane.add(icone, new GridBagConstraints(0,0,1,2,0.,0.,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,05),0,0));
		pane.add(name, new GridBagConstraints(1,0,1,1,1.,1.,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));
		pane.add(numero, new GridBagConstraints(1,1,1,1,1.,1.,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));
		pane.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
						BorderFactory.createLineBorder(Color.black))
				);
		pane.setBackground(Color.lightGray);
		res.add(pane, new GridBagConstraints(0,0,1,1,1.,1.,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Contact c = (Contact) value;
		name.setText(c.getName());
		numero.setText(c.getNumber());
		res.revalidate();
		pane.setBackground(Color.lightGray);
		if (isSelected)
			pane.setBackground(new Color(200,230,255).darker());
			
		return res;
	}

}
