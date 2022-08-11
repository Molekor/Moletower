package moletower;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Evil things that follow a path along the map to reach the exit.
 * 
 * @author Molekor
 *
 */
public abstract class Enemy {

	protected Path path;
	protected int nextPathPoint = 1;
	protected double speed;
	protected double x;
	protected double y;
	protected boolean hasReachedExit = false;
	protected long diedAt = 0; // at what time has the enemy been disposed
	protected long deadDuration; // how long shall we show the dead enemy image
	protected boolean isLiving = true;
	protected boolean canBeDeleted = false; // we have shown the dead enemy image long enough
	protected int value; // how much money do we earn for disposing this enemy
	protected int lives; // how many lives has the enemy left
	protected int baseLives; // starting lives of the enemy class
	protected String imagePath;
	protected int size;
	protected BufferedImage image;
	
	/**
	 * Constructor. Sets the path points this enemy will follow, and sets the first
	 * path point as its first target.
	 * @param path
	 * @throws Exception
	 */
	public Enemy(String imagePath, double speed, int value, int lives, int deadDuration, int baseSize) {
		this.speed = speed;
		this.value = value;
		this.lives = lives;
		this.baseLives = lives;
		this.deadDuration = deadDuration;
		this.speed = speed;
		this.imagePath = imagePath;
		this.size = baseSize;
	}

	/**
	 * Advance this enemy on its path.
	 */
	public void move() {
		// Enemies that have reached the exit don't move anymore
		if (this.hasReachedExit) {
			return;
		}
		// Dead enemies don't move.
		if (!this.isLiving ) {
			// Check how long the enemy has been shown as dead
			if (System.currentTimeMillis() - this.diedAt > this.deadDuration) {
				this.canBeDeleted = true;
			}
			return;
		}
		
		// Find the coordinates of the next path point
		Point destination = this.path.getPathPoints().get(nextPathPoint);
		double dx = destination.x - this.x;
		double dy = destination.y - this.y;
		
		// Determine how many pixels we must move horizontally and vertically
		double angle = MathHelper.calculateAngle(this.x, this.y, destination.x, destination.y);
		double xMove = speed * Math.cos(angle);
		double yMove = speed * Math.sin(angle);
		this.x = this.x + xMove;
		this.y = this.y + yMove;
		
		// If we moved further than the distance to the path position,
		// move on to the following path point.
		double distanceTraveled = Math.sqrt(xMove * xMove + yMove * yMove);
		double distanceToNextWaypoint = Math.sqrt(dx * dx + dy * dy);
		if (distanceToNextWaypoint <= distanceTraveled) {
			if (nextPathPoint == this.path.getPathPoints().size() - 1) {
				this.hasReachedExit = true;
			} else {
				this.x = destination.x;
				this.y = destination.y;
				nextPathPoint++;
			}
		}
	}

	/**
	 * @return Map position of this enemy
	 */
	public Point getPosition() {
		return new Point((int) this.x, (int) this.y);
	}

	public void setPosition(Point position) {
		this.x = position.x;
		this.y = position.y;
	}
	
	public boolean hasReachedExit() {
		return this.hasReachedExit;
	}

	/**
	 * Enemy is viewed as a circle to keep things simple.
	 * @return radius of the enemy in pixels
	 */
	public int getSize() {
		return size;
	}

	/**
	 * The enemy has been hit by a shot of a tower and loses a life point.
	 * It dies at zero lives.
	 */
	public void hit() {
		this.lives--;
		if (this.lives <= 0) {
			this.diedAt = System.currentTimeMillis();
			this.isLiving = false;
		}
		this.image = GraphicsHelper.getDarkenedImage(this.imagePath, (float) this.lives / (float) this.baseLives);
	}

	/**
	 * 
	 * @return true if we no longer need to show the dead enemy
	 */
	public boolean canBeDeleted() {
		return this.canBeDeleted;
	}

	/**
	 * 
	 * @return true if the enemy has lives left, false if lives are at zero
	 */
	public boolean isLiving() {
		return this.isLiving;
	}

	protected int getValue() {
		return this.value;
	}

	protected  String getImagePath() {
		return this.imagePath;
	}
	
	protected int getLives() {
		return this.lives;
	}
	
	protected double getSpeed() {
		return this.speed;
	}

	public void setPath(Path path) {
		this.path = path;	
		Point start = this.path.getPathPoints().get(0);
		this.x = start.x;
		this.y = start.y;
	}

	public float getHealthStatus() {
		return (float) this.lives / (float) this.baseLives;
	}

	public Image getImage() {
		if (this.image == null) {
			this.image = GraphicsHelper.getImage(imagePath);
		}
		return this.image;
	}
}