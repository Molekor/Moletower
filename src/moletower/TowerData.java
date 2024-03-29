package moletower;

import java.util.Iterator;
import java.util.Vector;

public class TowerData {

	public static final String NAME = "name";
	public static final String SIZE = "size";
	public static final String PRICE_1 = "price1";
	public static final String PRICE_2 = "price2";
	public static final String PRICE_3 = "price3";
	public static final String COOLDOWN_1 = "cooldown1";
	public static final String COOLDOWN_2 = "cooldown2";
	public static final String COOLDOWN_3 = "cooldown3";
	public static final String DAMAGE_1 = "damage1";
	public static final String DAMAGE_2 = "damage2";
	public static final String DAMAGE_3 = "damage3";
	public static final String RANGE_1 = "range1";
	public static final String RANGE_2 = "range2";
	public static final String RANGE_3 = "range3";
	
	public static final int MAX_LEVEL = 3;
	
	private String name;
	private int[] prices;
	private int[] ranges;
	private int[] cooldowns;
	private int[] damageValues;
	
	private int size;
	
	protected TowerData() {
		this.prices = new int[MAX_LEVEL];
		this.ranges = new int[MAX_LEVEL];
		this.cooldowns = new int[MAX_LEVEL];
		this.damageValues = new int[MAX_LEVEL];
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setPrice(int price, int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return;
		}
		this.prices[level-1] = price;
	}

	public int getPrice(int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return -1;
		}
		return this.prices[level-1];
	}
	
	public void setRange(int range, int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return;
		}
		this.ranges[level-1] = range;
	}

	public int getRange(int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return -1;
		}
		return this.ranges[level-1];
	}
	
	public void setCooldown(int cooldown, int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return;
		}
		this.cooldowns[level-1] = cooldown;
	}

	public int getCooldown(int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return -1;
		}
		return this.cooldowns[level-1];
	}
	
	public void setDamage(int damage, int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return;
		}
		this.damageValues[level-1] = damage;
	}
	
	public int getDamage(int level) {
		if (level < 1 || level > MAX_LEVEL) {
			return -1;
		}
		return this.damageValues[level-1];
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

	public static Vector<TowerData> create(Vector<String> lines) {
		Vector<TowerData> allTowerData = new Vector<TowerData>();
		String[] fieldNames=null;
		String[] rawData;
		Iterator<String> linesIterator = lines.iterator();
		// First line must hold the field names
		String line = linesIterator.next();
		if (line != null) {
			fieldNames = line.split(",");
		}
		// Following lines each hold data for one tower type
		while (linesIterator.hasNext()) {
			line = linesIterator.next();
			rawData = line.split(",");
			TowerData nextTower = new TowerData();
			for (int i = 0; i < fieldNames.length; i++) {
				String key = fieldNames[i];
				if (key.equals(TowerData.NAME)) {
					nextTower.name = rawData[i];
				} else if (key.equals(TowerData.SIZE)) {
					nextTower.setSize(Integer.parseInt(rawData[i]));
				} else if (key.equals(TowerData.PRICE_1)) {
					nextTower.setPrice(Integer.parseInt(rawData[i]), 1);
				} else if (key.equals(TowerData.PRICE_2)) {
					nextTower.setPrice(Integer.parseInt(rawData[i]), 2);
				} else if (key.equals(TowerData.PRICE_3)) {
					nextTower.setPrice(Integer.parseInt(rawData[i]), 3);
				} else if (key.equals(TowerData.COOLDOWN_1)) {
					nextTower.setCooldown(Integer.parseInt(rawData[i]), 1);
				} else if (key.equals(TowerData.COOLDOWN_2)) {
					nextTower.setCooldown(Integer.parseInt(rawData[i]), 2);
				} else if (key.equals(TowerData.COOLDOWN_3)) {
					nextTower.setCooldown(Integer.parseInt(rawData[i]), 3);
				} else if (key.equals(TowerData.DAMAGE_1)) {
					nextTower.setDamage(Integer.parseInt(rawData[i]), 1);
				} else if (key.equals(TowerData.DAMAGE_2)) {
					nextTower.setDamage(Integer.parseInt(rawData[i]), 2);
				} else if (key.equals(TowerData.DAMAGE_3)) {
					nextTower.setDamage(Integer.parseInt(rawData[i]), 3);
				} else if (key.equals(TowerData.RANGE_1)) {
					nextTower.setRange(Integer.parseInt(rawData[i]), 1);
				} else if (key.equals(TowerData.RANGE_2)) {
					nextTower.setRange(Integer.parseInt(rawData[i]), 2);
				} else if (key.equals(TowerData.RANGE_3)) {
					nextTower.setRange(Integer.parseInt(rawData[i]), 3);
				}
			}
			allTowerData.add(nextTower);
		}
		return allTowerData;
	}
}
