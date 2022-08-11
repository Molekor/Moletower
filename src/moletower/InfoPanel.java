package moletower;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel implements ActionListener {
	
	public final static String UPGRADE_TOWER_ACTION = "UPGRADE_TOWER";
	
	Tower selectedTower = null;
	JLabel position = new JLabel("-");
	JLabel name = new JLabel("-");
	JLabel header = new JLabel("Selected Tower:");
	UpgradeButton upgradeButton = new UpgradeButton("Upgrade");

	private UpgradeListener upgradeListener;
	
	public InfoPanel() {
		this.setPreferredSize(new Dimension(200,300));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(header);
		this.add(name);
		this.add(position);
		upgradeButton.addActionListener(this);
		upgradeButton.setMaximumSize(new Dimension(120,30));
		upgradeButton.setVisible(false);
		this.add(upgradeButton);
	}
	
	public void setUpgradeActionListener(UpgradeListener listener) {
		this.upgradeListener =listener;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.upgradeListener != null) {
			this.upgradeListener.requestTowerUpgrade(this.selectedTower);
			this.setSelectedTower(this.selectedTower);
		}
	}
	
	public void setSelectedTower(Tower tower) {
		this.selectedTower = tower;
		this.upgradeButton.setSelectedTower(tower);
		if (this.selectedTower != null) {
			this.position.setText(this.selectedTower.getPosition().x + "," + this.selectedTower.getPosition().y);
			this.name.setText(selectedTower.getName() + " Lvl " + selectedTower.getLevel());
			if (tower.canBeUpgraded()) {
				this.upgradeButton.setLabel("Upgrade " + tower.getUpgradePrice() + "$");
			} else {
				this.upgradeButton.setLabel("Upgrades complete");
			}
			this.upgradeButton.setEnabled(tower.canBeUpgraded());
			this.upgradeButton.setVisible(true);
		} else {
			this.position.setText("-");
			this.name.setText("-");
			this.upgradeButton.setVisible(false);
		}
	}
}
