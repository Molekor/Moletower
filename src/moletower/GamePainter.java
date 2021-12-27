package moletower;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

public class GamePainter {
	
	private Moletower game;
	private Path path;
	private GameData gameData;
	private Vector<Enemy> enemies; // The enemy data used for painting
	private Vector<Shot> shots;
	private Vector<Tower> towers;
	private Tower towerToPlace;
	
	public GamePainter(Moletower game, GameData gameData, Path path) {
		this.game = game;
		this.path = path;
		this.gameData = gameData;
		this.enemies = new Vector<Enemy>();
		this.towers = new Vector<Tower>();
		this.shots = new Vector<Shot>();
	}

	/**
	 * Draw the game. This is called by the GameWindow when it is repainted.
	 * 
	 * @param g The graphics object of the GameWindow
	 */
	public void draw(Graphics g) {
		this.drawBackground(g);
		if (this.game.isRoundActive()) {
			this.drawEnemies(g);
			this.drawShots(g);
		}
		this.drawTowers(g);
		this.drawStatus(g);
		// Draw sidebar last, so it is the top layer
		this.drawSidebar(g);
		// The tower to place hovers over all
		this.drawTowerToPlace(g);
	}



	/**
	 * Paint the tower the user is trying to place
	 * 
	 * @param g
	 */
	private void drawTowerToPlace(Graphics g) {
		if (this.game.isPlacingTower() && (this.towerToPlace != null)) {
			this.towerToPlace.paintComponent(g);
		}
	}

	/**
	 * The tower menu etc.
	 * 
	 * @param g
	 */
	private void drawSidebar(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(700, 0, 100, 600);
		g.setColor(Color.GREEN);
		g.fillRect(710, 50, 80, 30);
		g.fillRect(710, 110, 80, 30);
		if (this.game.isMoneyWarningActive()) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}
		g.drawString("Firetower " + Firetower.basePrice + " $", 711, 70);
		g.drawString("Fasttower " + Fasttower.basePrice + " $", 711, 130);
		if (!this.game.isRoundActive()) {
			g.setColor(Color.GREEN);
			g.fillRect(710, 500, 80, 30);
			g.setColor(Color.BLACK);
			g.drawString("START", 715, 515);
		}
	}

	private void drawShots(Graphics g) {
		Iterator<Shot> shotIterator = this.shots.iterator();
		while (shotIterator.hasNext()) {
			Shot currentShot = shotIterator.next();
			currentShot.paintComponent(g);
		}
	}

	private void drawStatus(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 20);
		if (this.game.isMoneyWarningActive()) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.WHITE);
		}
		g.drawString(String.format("Lives: %d Money: %d Action: %s " + (this.game.isMoneyWarningActive()?"NO MONEY":"OK"), this.gameData.getLives(), this.gameData.getMoney(), this.game.getUserAction()), 10, 13);

	}

	private void drawEnemies(Graphics g) {
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
			currentEnemy.paintComponent(g);
		}
	}

	private void drawTowers(Graphics g) {
		Iterator<Tower> towerIterator = this.towers.iterator();
		while (towerIterator.hasNext()) {
			Tower currentTower = towerIterator.next();
			currentTower.paintComponent(g);
		}
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 800, 600);

		g.setColor(Color.BLACK);
		Iterator<Point> pathIterator = path.getPathPoints().iterator();
		Point lastPoint = null;
		Point nextPoint = null;
		while (pathIterator.hasNext()) {
			if (lastPoint != null) {
				nextPoint = pathIterator.next();
				g.drawLine(lastPoint.x, lastPoint.y, nextPoint.x, nextPoint.y);
				lastPoint = nextPoint;
			} else {
				lastPoint = pathIterator.next();
			}
		}
	}

	public void setEnemiesToPaint(Vector<Enemy> enemies) {
		this.enemies = enemies;
	}

	public void setShotsToPaint(Vector<Shot> shots) {
		this.shots = shots;
	}

	public void setTowersToPaint(Vector<Tower> towers) {
		this.towers = towers;
	}

	public void setTowerToPlace(Tower towerToPlace) {
		this.towerToPlace = towerToPlace;
	}
}
