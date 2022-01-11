package moletower;

public class TowerData {

	private String name;
	private int[] prices;
	private int[] ranges;
	private int[] cooldowns;
	private int[] damageValues;
	private int levels;
	private int size;
	
	public TowerData(int levels) {
		this.levels = levels;
		this.prices = new int[levels];
		this.ranges = new int[levels];
		this.cooldowns = new int[levels];
		this.damageValues = new int[levels];
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setPrice(int price, int level) {
		if (level < 0 || level >= levels) {
			return;
		}
		this.prices[level] = price;
	}

	public int getPrice(int level) {
		if (level < 0 || level >= levels) {
			return -1;
		}
		return this.prices[level];
	}
	
	public void setRange(int range, int level) {
		if (level < 0 || level >= levels) {
			return;
		}
		this.ranges[level] = range;
	}

	public int getRange(int level) {
		if (level < 0 || level >= levels) {
			return -1;
		}
		return this.ranges[level];
	}
	
	public void setCooldown(int cooldown, int level) {
		if (level < 0 || level >= levels) {
			return;
		}
		this.cooldowns[level] = cooldown;
	}

	public int getCooldown(int level) {
		if (level < 0 || level >= levels) {
			return -1;
		}
		return this.cooldowns[level];
	}
	
	public void setDamage(int damage, int level) {
		if (level < 0 || level >= levels) {
			return;
		}
		this.damageValues[level] = damage;
	}
	
	public int getDamage(int level) {
		if (level < 0 || level >= levels) {
			return -1;
		}
		return this.damageValues[level];
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
}
