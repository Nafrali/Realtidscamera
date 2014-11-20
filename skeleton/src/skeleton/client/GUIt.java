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
		getContentPane().add(imagePanelR, BorderLayout.NORTH);
		checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new BorderLayout());
		add(checkboxPanel, BorderLayout.SOUTH);
		checkboxPanel.add(movie, BorderLayout.WEST);
		checkboxPanel.add(synch, BorderLayout.EAST);
		setLocationRelativeTo(null);
		pack();
		setResizable(false);
		setVisible(true);
	}

	class ImagePanel extends JPanel {
		ImageIcon icon;

		public ImagePanel() {
			super();
			icon = new ImageIcon();
			JLabel label = new JLabel(icon);
			add(label, BorderLayout.CENTER);
			this.setSize(200, 200);
		}

		public void refresh(byte[] data) {
			Image theImage = getToolkit().createImage(data);
			getToolkit().prepareImage(theImage, -1, -1, null);
			icon.setImage(theImage);
			icon.paintIcon(this, this.getGraphics(), 5, 5);
		}
	}

	public void refreshImage(byte[] newPicture) {
		jpeg = newPicture;
		imagePanelR.refresh(jpeg);
		imagePanelL.refresh(jpeg);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (source == movie) {

		} else if (source == synch) {

		}

	}

}
