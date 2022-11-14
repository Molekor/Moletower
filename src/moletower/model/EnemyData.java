package moletower.model;

public class EnemyData {

	public static final String ID = "Id";
	public static final String HEALTH = "Health";
	public static final String VALUE = "Value";
	public static final String SIZE = "Size";
	public static final String IMAGE = "Image";
	public static final String SPEED = "Speed";

	private int health;
	private String image;
	private int size;
	private int value;
	private float speed;
	
	public int getHealth() {
		return health;
	}
	
	public String getImage() {
		return image;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getValue() {
		return value;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
