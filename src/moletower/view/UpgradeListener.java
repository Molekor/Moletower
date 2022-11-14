package moletower.view;

import moletower.model.Tower;

public interface UpgradeListener {

	void requestTowerUpgrade(Tower towerToUpgrade);
}
