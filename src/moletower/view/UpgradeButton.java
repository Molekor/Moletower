package moletower.view;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import moletower.controller.UpgradeListener;
import moletower.model.Tower;

public class UpgradeButton extends Button implements ActionListener {

	private UpgradeListener listener;
	private Tower selectedTower;
	
	public UpgradeButton(String caption) {
		super(caption);
		this.addActionListener(this);
	}
	public void setListener(UpgradeListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ((this.listener != null) && (this.selectedTower != null)) {
			this.listener.requestTowerUpgrade(this.selectedTower);
		}
	}
	public void setSelectedTower(Tower tower) {
		this.selectedTower = tower;
	}
}
