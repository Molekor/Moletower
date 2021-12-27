package moletower;

import java.awt.Point;
import java.util.Vector;

/**
 * The master class of the game.
 * 
 * @author Molekor
 *
 */
public class Moletower implements Runnable {

	private long lastMoveTime; // time we last moved the shots and enemies
	private long timeSinceLastMove = 0; // how long has it been since the last move
	private static int moveInterval = 40; // minimal interval between moves in ms, this represents overall game speed
	private GameWindow gameWindow; // Main window of the game that handles basic I/O


	private Path path; // The path all enemies will follow to the exit
	private boolean roundActive = false;
	private boolean placingTower = false; // Is the user actively placing a tower?
	private Tower towerToPlace; // The tower instance that the user is trying to place

	private boolean moneyWarning = false; // Indicator that the player tries something too expensive, so we can issue a warning on screen
	
	private GamePainter gamePainter;
	private MainMover mover;
	private GameData gameData;
	
	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.path = new Path();
		this.gameData = new GameData(this);
		this.gamePainter = new GamePainter(this, this.gameData, this.path);
		this.gameWindow = new GameWindow(this.gamePainter);
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
				// Always check what the user is doing
				// @TODO Replace with proper mouse listeners etc.
				this.checkUserAction();
				// Game element actions, if the game is active
				if (this.roundActive) {
					timeSinceLastMove = System.currentTimeMillis() - lastMoveTime;
					// Only move if at least the moveInterval time has passed, else pause
					if (timeSinceLastMove > moveInterval) {
						this.mover.move();
						lastMoveTime = System.currentTimeMillis();
						this.gamePainter.setEnemiesToPaint((Vector<Enemy>) this.gameData.getEnemies().clone());
						this.gamePainter.setShotsToPaint((Vector<Shot>) this.gameData.getShots().clone());
					} else {
						Thread.sleep(moveInterval - timeSinceLastMove);
					}
				}
				// Towers may be bought before the round is started, so fill the paint data
				this.gamePainter.setTowerToPlace(this.towerToPlace);
				this.gamePainter.setTowersToPaint((Vector<Tower>) this.gameData.getTowers().clone());
				// Draw all changes that may have happened
				this.gameWindow.repaint();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * Resolves all user action (Mouseclicks)
	 */
	private void checkUserAction() {
		
		this.moneyWarning = false;
		
		// Check for start of round button
		if (this.roundActive == false && this.gameWindow.mouseIsPressed) {
			int mouseX = this.gameWindow.mousePosition.x;
			int mouseY = this.gameWindow.mousePosition.y;
			if (mouseX > 710 && mouseX < 790 && mouseY > 500 && mouseY < 530) {
				this.roundActive = true;
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		// Check for tower placement
		// TODO: Use proper Java UI items!
		// User is placing tower and releases the mouse button: Drop the tower at the
		// mouse position
		if (this.placingTower) {
			if (!this.gameWindow.mouseIsPressed && this.gameWindow.mousePosition.x < 700) {
				this.placingTower = false;
				this.towerToPlace.setActive(true);
				this.gameData.addTower(towerToPlace);
				this.gameData.adjustMoney(-towerToPlace.getPrice());
			} else {
				this.towerToPlace.setPosition(this.gameWindow.mousePosition);
			}
		} else {
			if (this.gameWindow.mouseIsPressed) {
				int mouseX = this.gameWindow.mousePosition.x;
				int mouseY = this.gameWindow.mousePosition.y;
				if (mouseX > 710 && mouseX < 790 && mouseY > 50 && mouseY < 80) {
					this.placingTower = true;
					this.towerToPlace = new Firetower(new Point(0, 0));
				}
				if (mouseX > 710 && mouseX < 790 && mouseY > 110 && mouseY < 140) {
					this.placingTower = true;
					this.towerToPlace = new Fasttower(new Point(0, 0));
				}
				// Check if we have the money to place the selected tower
				if ((this.towerToPlace != null) && (this.towerToPlace.getPrice() > this.gameData.getMoney())) {
					this.placingTower = false;
					this.moneyWarning = true;
					this.towerToPlace = null;
				}
			}
		}
	}

	public boolean isRoundActive() {
		return this.roundActive;
	}

	public boolean isMoneyWarningActive() {
		return this.moneyWarning;
	}

	public boolean isPlacingTower() {
		return this.placingTower;
	}

	public Object getUserAction() {
		if(this.isPlacingTower()) {
			return "placing tower";
		};
		return "-";
	}
}