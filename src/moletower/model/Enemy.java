package moletower.model;

import java.awt.Point;

import moletower.controller.MathHelper;

/**
 * Evil things that follow a path along the map to reach the exit.
 * 
 * @author Molekor
 *
 */
public class Enemy {

	protected Path path;
	protected int nextPathPoint = 1;
	protected double speed;
	private double x;
	private double y;
	protected boolean hasReachedExit = false;
	protected long diedAt = 0; // at what time has the enemy been disposed
	protected long deadDuration = 500; // how long shall we show the dead enemy image
	protected boolean isLiving = true;
	protected boolean canBeDeleted = false; // we have shown the dead enemy image long enough
	protected int value; // how much money do we earn for disposing this enemy
	protected int lives; // how many lives has the enemy left
	protected int baseLives; // starting lives of the enemy class
	protected String imagePath;
	protected int size;
	
	public Enemy() {
		
	}
	
	public Enemy(EnemyData enemyData, Path path) {
		this.setPath(path);
		this.speed = enemyData.getSpeed();
		this.value = enemyData.getValue();
		this.lives = enemyData.getHealth();
		this.baseLives = lives;
		this.imagePath = enemyData.getImage();
		this.size = enemyData.getSize();
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
		double dx = destination.x - this.getX();
		double dy = destination.y - this.getY();
		
		// Determine how many pixels we must move horizontally and vertically
		double angle = MathHelper.calculateAngle(this.getX(), this.getY(), destination.x, destination.y);
		double xMove = speed * Math.cos(angle);
		double yMove = speed * Math.sin(angle);
		this.setX(this.getX() + xMove);
		this.setY(this.getY() + yMove);
		
		// If we moved further than the distance to the path position,
		// move on to the following path point.
		double distanceTraveled = Math.sqrt(xMove * xMove + yMove * yMove);
		double distanceToNextWaypoint = Math.sqrt(dx * dx + dy * dy);
		if (distanceToNextWaypoint <= distanceTraveled) {
			if (nextPathPoint == this.path.getPathPoints().size() - 1) {
				this.hasReachedExit = true;
			} else {
				this.setX(destination.x);
				this.setY(destination.y);
				nextPathPoint++;
			}
		}
	}

	/**
	 * @return Map position of this enemy
	 */
	public Point getPosition() {
		return new Point((int) this.getX(), (int) this.getY());
	}

	public void setPosition(Point position) {
		this.setX(position.x);
		this.setY(position.y);
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
	 * @param currentShot 
	 */
	public void hit(Shot currentShot) {
		this.lives = this.lives - currentShot.getDamage();
		if (this.lives <= 0) {
			this.diedAt = System.currentTimeMillis();
			this.isLiving = false;
		}
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

	public int getValue() {
		return this.value;
	}

	public  String getImagePath() {
		return this.imagePath;
	}
	
	public int getLives() {
		return this.lives;
	}
	
	public double getSpeed() {
		return this.speed;
	}

	public void setPath(Path path) {
		this.path = path;	
		Point start = this.path.getPathPoints().get(0);
		this.setX(start.x);
		this.setY(start.y);
	}

	public float getHealthStatus() {
		return (float) this.lives / (float) this.baseLives;
	}

	public boolean isHasReachedExit() {
		return hasReachedExit;
	}

	public void setHasReachedExit(boolean hasReachedExit) {
		this.hasReachedExit = hasReachedExit;
	}

	public void setLiving(boolean isLiving) {
		this.isLiving = isLiving;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}