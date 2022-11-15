package moletower.controller;

import moletower.model.Tower;

public interface UpgradeListener {

	void requestTowerUpgrade(Tower towerToUpgrade);
}
