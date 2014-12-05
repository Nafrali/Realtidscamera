package GUI;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class MessageArea extends JPanel {

	private JTextArea textArea;

	public MessageArea() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		textArea = new JTextArea(10, 10);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
	}

	public void setText(String text) {
		textArea.setText(text);
	}

	public void append(String text) {
		textArea.append(text);
	}
}