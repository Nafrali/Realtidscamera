package GUI;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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