package moletower;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

	public ButtonPanel() {
		this.setPreferredSize(new Dimension(100,600));
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.setBorder(BorderFactory.createLineBorder(Color.RED));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void addButton(Button button) {
		this.add(Box.createRigidArea(new Dimension(90, 20)));
		button.setMaximumSize(new Dimension(90,30));
		this.add(button);
	}
}
