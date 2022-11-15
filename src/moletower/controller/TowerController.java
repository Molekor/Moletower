package moletower.controller;

import java.util.Vector;

import moletower.model.GameData;
import moletower.model.Tower;
import moletower.model.TowerData;

public class TowerController implements BuyListener, UpgradeListener {
	private GameData gameData;
	private Vector<TowerData> allTowerData;
	
	public TowerController(GameData gameData) {
		this.gameData= gameData;
	}
	
	public Vector<TowerData> getAllTowerData() {
		return allTowerData;
	}

	public void setAllTowerData(Vector<TowerData> allTowerData) {
		this.allTowerData = allTowerData;
	}

	public void requestTowerBuy(int towerTypeId) {
		this.gameData.setSelectedTower(null);

		TowerData towerData = this.allTowerData.get(towerTypeId);
		Tower newTower = new Tower(towerData);
		// Check if we have the money to place the selected tower
		if (newTower.getPrice() > this.gameData.getMoney()) {
			this.gameData.setMoneyWarning(true);
			this.gameData.setTowerToPlace(null);
		} else {
			this.gameData.setMoneyWarning(false);
			this.gameData.setTowerToPlace(newTower);
		}
	}

	public void requestTowerUpgrade(Tower towerToUpgrade) {
		int price = towerToUpgrade.getPrice();
		if (price <= this.gameData.getMoney()) {
			towerToUpgrade.upgrade();
			this.gameData.adjustMoney(-price);
		}
	}
}
