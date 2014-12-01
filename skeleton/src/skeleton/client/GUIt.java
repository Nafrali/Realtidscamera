package skeleton.client;

import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class GUIt extends JFrame implements ItemListener {

	ClientMonitor m;
	ImagePanel imagePanelR;
	ImagePanel imagePanelL;
	JPanel checkboxPanel;
	JCheckBox movie;
	JCheckBox synch;
	boolean firstCall = true;
	byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];

	public GUIt(ClientMonitor m) {
		super();
		this.m = m;

		movie = new JCheckBox("Movie mode");
		movie.setMnemonic(KeyEvent.VK_M);
		movie.setSelected(true);
		movie.addItemListener(this);

		synch = new JCheckBox("Synchronized mode");
		synch.setMnemonic(KeyEvent.VK_S);
		synch.setSelected(true);
		synch.addItemListener(this);

		imagePanelR = new ImagePanel();
		imagePanelL = new ImagePanel();
		getContentPane().setLayout(new BorderLayout());
		
		//add south bar
		checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BorderLayout());
		checkboxPanel.add(movie, BorderLayout.WEST);
		checkboxPanel.add(synch, BorderLayout.EAST);
		getContentPane().add(checkboxPanel, BorderLayout.SOUTH);
		
		//add images
		getContentPane().add(imagePanelR, BorderLayout.EAST);
		getContentPane().add(imagePanelL, BorderLayout.WEST);
		
		setLocationRelativeTo(null);
		pack();
		//setResizable(false);
	
	}

	class ImagePanel extends JPanel {
		ImageIcon icon;
		JLabel label;
		
		public ImagePanel() {
			super();
			icon = new ImageIcon();
			label = new JLabel(icon);
			
			add(label, BorderLayout.CENTER);
			this.setSize(200, 200);
		}

		public void refresh(byte[] data) {

			label.setIcon(new ImageIcon(data));

		}
	}

	public void refreshImage(byte[] newPicture, boolean movieMode) {
		jpeg = newPicture;
		imagePanelR.refresh(jpeg);
		imagePanelL.refresh(jpeg);
		if(movieMode) movie.setSelected(true);
		if (firstCall) {
			this.pack();
			this.setVisible(true);
			setResizable(false);
			firstCall = false;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (source == movie) {
			if(e.getStateChange()==ItemEvent.SELECTED) 
			m.uppdateMovieMode(true);
			else if(e.getStateChange()==ItemEvent.DESELECTED) 
			m.uppdateMovieMode(false);
			
		} else if (source == synch) {
			if(e.getStateChange()==ItemEvent.SELECTED) 
			m.uppdateSynchMode(true);
			else if(e.getStateChange()==ItemEvent.DESELECTED) 
			m.uppdateSynchMode(false);
		}

	}

}
