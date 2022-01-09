package moletower;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {
	
	public final static String UPGRADE_TOWER_ACTION = "UPGRADE_TOWER";
	
	Tower selectedTower = null;
	JLabel position = new JLabel("");
	JLabel name = new JLabel("");
	Button upgradeButton = new Button("Upgrade");
	
	public InfoPanel(ActionListener actionListener) {
		this.setPreferredSize(new Dimension(100,300));
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.setBorder(BorderFactory.createLineBorder(Color.CYAN));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new JLabel("Selected Tower:"));
		this.add(name);
		this.add(position);
		upgradeButton.setMaximumSize(new Dimension(90,30));
		upgradeButton.setActionCommand(InfoPanel.UPGRADE_TOWER_ACTION);
		upgradeButton.addActionListener(actionListener);
		upgradeButton.setVisible(false);
		this.add(upgradeButton);
	}
	
	public void setSelectedTower(Tower tower) {
		this.selectedTower = tower;
		if (this.selectedTower != null) {
			this.position.setText(this.selectedTower.getPosition().x + "," + this.selectedTower.getPosition().y);
			this.name.setText(selectedTower.getName());
			this.upgradeButton.setVisible(true);
		} else {
			this.position.setText("");
			this.name.setText("");
			this.upgradeButton.setVisible(false);
		}
	}
}
