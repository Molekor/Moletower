package moletower;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private Path path;
	private GameData gameData;
	private Vector<Enemy> enemies; // The enemy data used for painting
	private Vector<Shot> shots;
	private Vector<Tower> towers;
	private Tower towerToPlace;
	public Point mousePosition = new Point(1,1);
	public boolean mouseIsPressed = false;
	
	public GamePanel(GameData gameData, Path path) {
		this.setPreferredSize(new Dimension(700,600));
		this.path = path;
		this.gameData = gameData;
		this.enemies = new Vector<Enemy>();
		this.towers = new Vector<Tower>();
		this.shots = new Vector<Shot>();
	}

	/**
	 * Draw the game.
	 * 
	 * @param g The graphics object of the GameWindow
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.drawBackground(g);
		this.drawTowers(g);
		this.drawEnemies(g);
		this.drawShots(g);
		this.drawStatus(g);
		// The tower to place hovers over all
		this.drawTowerToPlace(g);
	}

	/**
	 * Paint the tower the user is trying to place
	 * 
	 * @param g
	 */
	private void drawTowerToPlace(Graphics g) {
		if (this.towerToPlace != null) {
			this.drawTower(g, towerToPlace);
		}
	}

	private void drawShots(Graphics g) {
		Iterator<Shot> shotIterator = this.shots.iterator();
		while (shotIterator.hasNext()) {
			Shot currentShot = shotIterator.next();
				if (currentShot.isLiving()) {
					g.setColor(Color.CYAN);
					GraphicsHelper.drawThickLineFromAngle(g, currentShot.getPosition(), currentShot.getAngle(), 2 , 20);
				} else {
					g.setColor(Color.BLACK);
					g.fillArc((int) currentShot.getPosition().x - 2, (int) currentShot.getPosition().y - 2, 4, 4, 0, 360);

				}
		}
	}

	private void drawStatus(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 20);
		if (this.gameData.isMoneyWarningActive()) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.WHITE);
		}
		g.drawString(String.format("Lives: %d Money: %d " + (this.gameData.isMoneyWarningActive()?"NO MONEY":"OK"), this.gameData.getLives(), this.gameData.getMoney()), 10, 13);

	}

	private void drawEnemies(Graphics g) {
		Iterator<Enemy> enemyIterator = this.enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy currentEnemy = enemyIterator.next();
				if (currentEnemy.hasReachedExit) {
					return;
				}
				if (currentEnemy.isLiving) {		
					Image image = currentEnemy.getImage();
					g.drawImage(image, (int) currentEnemy.x - image.getWidth(null) / 2, (int) currentEnemy.y - image.getHeight(null) / 2, null);
				} else {
					g.setColor(Color.GREEN);
					g.fillRect((int) currentEnemy.x - 2, (int) currentEnemy.y - 2, 4, 4);
				}
			}		
	}

	private void drawTowers(Graphics g) {
		Iterator<Tower> towerIterator = this.towers.iterator();
		while (towerIterator.hasNext()) {
			Tower currentTower = towerIterator.next();
			drawTower(g, currentTower);
		}
	}

	private void drawTower(Graphics g, Tower currentTower) {
		g.setColor(currentTower.getColor());
		if (currentTower.isActive() || currentTower.canBePlaced()) {
			g.setColor(currentTower.getColor());
		} else {
			g.setColor(Color.DARK_GRAY);
		}
		g.fillArc(
				(int) (currentTower.getPosition().x - currentTower.getSize()), (int) (currentTower.getPosition().y - currentTower.getSize()),
				(int) currentTower.getSize() * 2, (int) currentTower.getSize() * 2, 0, 360
			);
		g.drawArc(currentTower.getPosition().x - currentTower.getRange(), currentTower.getPosition().y - currentTower.getRange(), currentTower.getRange() * 2, currentTower.getRange() * 2, 0, 360);

		if (currentTower.isSelected()) {
			g.setColor(Color.CYAN);
			g.drawArc(
					(int) (currentTower.getPosition().x - currentTower.getSize()) - 1, (int) (currentTower.getPosition().y - currentTower.getSize() - 1),
					(int) currentTower.getSize() * 2 + 2, (int) currentTower.getSize() * 2 + 2, 0, 360
				);
		}
		g.setColor(Color.BLACK);
		g.fillArc(currentTower.getPosition().x - 5, currentTower.getPosition().y - 5, 10, 10, 0,360);
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 700, 600);
		Iterator<Point> pathIterator = path.getPathPoints().iterator();
		Point lastPoint = null;
		Point nextPoint = null;
		while (pathIterator.hasNext()) {
			if (lastPoint != null) {
				nextPoint = pathIterator.next();
				g.setColor(Color.DARK_GRAY);
				GraphicsHelper.drawThickLine(g, lastPoint, nextPoint, this.path.getThickness());
				g.fillArc(nextPoint.x - (this.path.getThickness()/2)+1, nextPoint.y - (this.path.getThickness()/2)+1, this.path.getThickness()-2, this.path.getThickness()-2, 0, 360);
				g.setColor(Color.RED);
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
