package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import skeleton.client.ClientMonitor;

public class CameraModeRadioButtonPane extends JPanel implements ActionListener {

	private GUI gui;
	private ClientMonitor m;
	private JRadioButton movieButton, idleButton, autoButton;

	public CameraModeRadioButtonPane(GUI gui, ClientMonitor m) {
		super();
		this.gui = gui;
		this.m = m;
		movieButton = new JRadioButton("Movie");
		movieButton.setMnemonic(KeyEvent.VK_M);
		movieButton.setActionCommand("Movie");
		movieButton.setSelected(false);
		movieButton.addActionListener(this);
		idleButton = new JRadioButton("Idle");
		idleButton.setMnemonic(KeyEvent.VK_I);
		idleButton.setActionCommand("Idle");
		idleButton.setSelected(false);
		idleButton.addActionListener(this);
		autoButton = new JRadioButton("Auto");
		autoButton.setMnemonic(KeyEvent.VK_A);
		autoButton.setActionCommand("Auto");
		autoButton.setSelected(true);
		autoButton.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(movieButton);
		group.add(idleButton);
		group.add(autoButton);
		this.add(movieButton);
		this.add(idleButton);
		this.add(autoButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(movieButton)) {
			m.setMode(m.MOVIE);
		} else if (e.getSource().equals(idleButton)) {
			m.setMode(m.IDLE);
		} else if (e.getSource().equals(autoButton)) {
			m.setMode(m.AUTO);
		}
	}

}
