package moletower;

import java.awt.Button;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

/**
 * The master class of the game.
 * 
 * @author Molekor
 *
 */
public class Moletower extends MouseAdapter implements Runnable, ActionListener {

	private long lastMoveTime; // time we last moved the shots and enemies
	private long timeSinceLastMove = 0; // how long has it been since the last move
	private static int moveInterval = 40; // minimal interval between moves in ms, this represents overall game speed
	private GameWindow gameWindow; // Main window of the game that handles basic I/O
	private Path path; // The path all enemies will follow to the exit
	private GamePanel gamePanel;
	private MainMover mover;
	private GameData gameData;
	private GraphicsHelper graphicsHelper;
	private ButtonPanel buttonPanel;
	private Button firetowerButton;
	private Button fasttowerButton;
	private Button startButton;

	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.path = new Path();
		
		this.path.addPathPoint(new Point(10, 50));
		this.path.addPathPoint(new Point(100, 60));
		this.path.addPathPoint(new Point(210, 150));
		this.path.addPathPoint(new Point(200, 250));
		this.path.addPathPoint(new Point(650, 550));
		this.path.addPathPoint(new Point(600, 50));
		this.path.addPathPoint(new Point(60, 500));
		
		this.gameData = new GameData();
		this.graphicsHelper = new GraphicsHelper();
		this.gamePanel = new GamePanel(this.graphicsHelper, this.gameData, this.path);
		this.gamePanel.addMouseListener(this);
		this.buttonPanel = new ButtonPanel();
		
		this.firetowerButton = new Button("Firetower");
		this.fasttowerButton = new Button("Fasttower");
		this.startButton = new Button("Start");
		this.firetowerButton.addActionListener(this);
		this.fasttowerButton.addActionListener(this);
		this.startButton.addActionListener(this);
		this.buttonPanel.addButton(this.firetowerButton);
		this.buttonPanel.addButton(this.fasttowerButton);
		this.buttonPanel.addButton(this.startButton);
		
		this.gameWindow = new GameWindow(this.gamePanel, this.buttonPanel);
		this.mover = new MainMover(this.gameData, this.path);
		Thread gameThread = new Thread(this);
		gameThread.start();
	}


	@SuppressWarnings("unchecked")
	public void run() {
		this.gameWindow.repaint();
		timeSinceLastMove = System.currentTimeMillis();
		/**
		 * Main game loop
		 */
		while (true) {
			try {
				timeSinceLastMove = System.currentTimeMillis() - lastMoveTime;
				// Only move if at least the moveInterval time has passed, else pause
				if (timeSinceLastMove > moveInterval) {
					if (this.gameData.isGameActive()) {
						this.mover.move();
						this.gameData.addTick();
					}
					lastMoveTime = System.currentTimeMillis();
				} else {
					Thread.sleep(moveInterval - timeSinceLastMove);
				}
				if (this.gameData.getTowerToPlace() != null) {
					int mouseX = MouseInfo.getPointerInfo().getLocation().x - this.gamePanel.getLocationOnScreen().x;
					int mouseY = MouseInfo.getPointerInfo().getLocation().y - this.gamePanel.getLocationOnScreen().y;
					this.gameData.getTowerToPlace().setCanBePlaced(this.checkDistance(this.gameData.getTowerToPlace()));
					this.gameData.getTowerToPlace().setPosition(new Point(mouseX, mouseY));
					this.gamePanel.setTowerToPlace(this.gameData.getTowerToPlace());
				} else {
					this.gamePanel.setTowerToPlace(null);
				}
				this.gamePanel.setTowersToPaint((Vector<Tower>) this.gameData.getTowers().clone());
				this.gamePanel.setEnemiesToPaint((Vector<Enemy>) this.gameData.getEnemies().clone());
				this.gamePanel.setShotsToPaint((Vector<Shot>) this.gameData.getShots().clone());
				// Draw all changes that may have happened
				this.gamePanel.repaint();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private boolean checkDistance(Tower towerToPlace) {
		double minDistance = Double.MAX_VALUE;
		Iterator<Point> pathPointIterator = this.path.getPathPoints().iterator();
		Point thisPathPoint = pathPointIterator.next();
		Point nextPathPoint;
		while (pathPointIterator.hasNext()) {
			nextPathPoint = pathPointIterator.next();
			double distance = MathHelper.getDistancePointToSegment(this.gameData.getTowerToPlace().getPosition(), thisPathPoint, nextPathPoint) - (this.path.getThickness() / 2);
			if (distance < minDistance) {
				minDistance = distance;
			}
			thisPathPoint = nextPathPoint;
		}
		Iterator<Tower> towerIterator = this.gameData.getTowers().iterator();
		while (towerIterator.hasNext()) {
			Tower otherTower = towerIterator.next();
			double distance = MathHelper.getDistance(this.gameData.getTowerToPlace().getPosition(), otherTower.getPosition()) - otherTower.getSize();
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance > (this.gameData.getTowerToPlace().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.firetowerButton) {
			this.gameData.setMoneyWarning(false);
				this.gameData.setTowerToPlace(new Firetower(this.gameData, this.gamePanel.mousePosition));		
		} else if (e.getSource() == this.fasttowerButton) {
			this.gameData.setTowerToPlace(new Fasttower(this.gameData, this.gamePanel.mousePosition));
		} else if (e.getSource() == this.startButton) {
			this.gameData.setGameActive(!this.gameData.isGameActive());
		}
		// Check if we have the money to place the selected tower
		if ((this.gameData.getTowerToPlace() != null) && (this.gameData.getTowerToPlace().getPrice() > this.gameData.getMoney())) {
			this.gameData.setMoneyWarning(true);
			this.gameData.setTowerToPlace(null);
		} else {
			this.gameData.setMoneyWarning(false);
		}
	}

	private void tryToPlaceTower() {
		if (this.gameData.getTowerToPlace() != null) {
			if (this.checkDistance(this.gameData.getTowerToPlace())) {
				this.gameData.getTowerToPlace().setActive(true);
				this.gameData.addTower(this.gameData.getTowerToPlace());
				this.gameData.adjustMoney(-this.gameData.getTowerToPlace().getPrice());
			}
		}
		this.gameData.setTowerToPlace(null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.gamePanel) {
			this.tryToPlaceTower();
		}
	}

}