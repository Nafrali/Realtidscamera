package skeleton.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class GUIt extends JFrame implements ItemListener {

	ClientMonitor m;
	ImagePanel imagePanelR;
	ImagePanel imagePanelL;
	JPanel displayPanel;
	JCheckBox movie;
	JCheckBox synch;
	JLabel[] delays = new JLabel[2];
	JTextArea actionLogArea = new JTextArea();
	boolean firstCall = true;
	byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];

	public GUIt(ClientMonitor m) {
		super();
		this.m = m;

		delays[0] = new JLabel("Camera 1");
		delays[1] = new JLabel("Camera 2");

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
		JPanel camDisplay = new JPanel();
		camDisplay.setLayout(new GridLayout(0, 2));

		// add south bar
		displayPanel = new JPanel();
		JPanel checkBoxPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(0, 2));
		checkBoxPanel.setLayout(new BorderLayout());
		checkBoxPanel.add(movie, BorderLayout.WEST);
		checkBoxPanel.add(synch, BorderLayout.EAST);
		displayPanel.add(delays[0]);
		displayPanel.add(delays[1]);
		displayPanel.add(actionLogArea);
		displayPanel.add(checkBoxPanel);
		camDisplay.add(imagePanelL);
		camDisplay.add(imagePanelR);
		getContentPane().add(camDisplay, BorderLayout.NORTH);
		getContentPane().add(displayPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		pack();

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
		if (movieMode)
			movie.setSelected(true);
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
			if (e.getStateChange() == ItemEvent.SELECTED)
				m.uppdateMovieMode(true);
			else if (e.getStateChange() == ItemEvent.DESELECTED)
				m.uppdateMovieMode(false);

		} else if (source == synch) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				m.uppdateSynchMode(true);
			else if (e.getStateChange() == ItemEvent.DESELECTED)
				m.uppdateSynchMode(false);
		}

	}

}
