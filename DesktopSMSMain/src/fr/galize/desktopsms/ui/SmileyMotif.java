package fr.galize.desktopsms.ui;

import javax.swing.ImageIcon;

public class SmileyMotif {

	
	String motif;
	ImageIcon icone;
	public SmileyMotif() {
		super();
	}
	public SmileyMotif(String motif, ImageIcon icone) {
		super();
		this.motif = motif;
		this.icone = icone;
	}
	public String getMotif() {
		return motif;
	}
	public void setMotif(String motif) {
		this.motif = motif;
	}
	public ImageIcon getIcone() {
		return icone;
	}
	public void setIcone(ImageIcon icone) {
		this.icone = icone;
	}
}
