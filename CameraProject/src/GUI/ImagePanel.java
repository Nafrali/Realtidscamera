package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ImagePanel extends JPanel {
	private JLabel picture;
	private JLabel text;
	private int camNbr;

	/**
	 * Creates an area in the GUI for one image
	 * @param camNbr The number of the applied camera 
	 */
	public ImagePanel(int camNbr) {
		super();
		this.camNbr = camNbr;
		setLayout(new BorderLayout());
		picture = new JLabel(new ImageIcon());
		text = new JLabel("Camera " + (camNbr + 1)
				+ ". Network travel time: 0ms");
		text.setForeground(Color.RED);
		add(text, BorderLayout.SOUTH);
		add(picture, BorderLayout.NORTH);
		this.setSize(200, 200);
	}

	/**
	 * Refreshes the image
	 * @param data The image byte array
	 * @param nettraveltime The send time for the image
	 * @param modeChange Displays if the camera has triggered movie mode
	 */
	public void refresh(byte[] data, long nettraveltime, String modeChange) {
		text.setText("Camera " + (camNbr + 1) + ". Network travel time: "
				+ nettraveltime + "ms" + "   " + modeChange);
		picture.setIcon(new ImageIcon(data));

	}
}