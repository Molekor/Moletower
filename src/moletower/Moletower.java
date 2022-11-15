package moletower;

import java.awt.Button;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import moletower.controller.BuyListener;
import moletower.controller.IOHelper;
import moletower.controller.MainMover;
import moletower.controller.MathHelper;
import moletower.controller.TowerController;
import moletower.controller.UpgradeListener;
import moletower.model.Enemy;
import moletower.model.EnemyData;
import moletower.model.EnemyGroup;
import moletower.model.FileLevelDataLoader;
import moletower.model.GameData;
import moletower.model.LevelData;
import moletower.model.LevelDataFactory;
import moletower.model.Path;
import moletower.model.Shot;
import moletower.model.Tower;
import moletower.model.TowerData;
import moletower.view.ButtonPanel;
import moletower.view.GamePanel;
import moletower.view.GameWindow;
import moletower.view.InfoPanel;
import moletower.view.TowerButton;

/**
 * The main class of the game.
 * 
 * @author Molekor
 *
 */
public class Moletower extends MouseAdapter implements Runnable, ActionListener {

	private static final int SPAWN_SPACE = 10; // wait so many ticks between spawning enemies
	private static final int MAX_LEVEL = 2;
	private long lastMoveTime; // time we last moved the shots and enemies
	private long timeSinceLastMove = 0; // how long has it been since the last move
	private static int moveInterval = 40; // minimal interval between moves in ms, this represents overall game speed
	private GameWindow gameWindow; // Main window of the game that handles basic I/O
	private Path path; // The path all enemies will follow to the exit
	private GamePanel gamePanel;
	private TowerController towerController;
	private MainMover mover;
	private GameData gameData;
	private ButtonPanel buttonPanel;
	private Button startButton;
	
	private LevelData levelData;
	private long spawnPause = 0;
	private long spawnSpace = 0;
	private InfoPanel infoPanel;
	
	private HashMap<String, EnemyData> enemyData;
	private Vector<EnemyGroup> spawningGroups;
	private EnemyGroup spawningGroup;
	
	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.gameData = new GameData();
		this.gameData.setLevel(1);
		this.spawningGroups = new Vector<EnemyGroup>();
		this.towerController = new TowerController(gameData);
		init();
		
		this.gamePanel = new GamePanel(this.gameData, this.path);
		this.gamePanel.addMouseListener(this);
		
		this.buttonPanel = new ButtonPanel();
		for (int i = 0; i < this.towerController.getAllTowerData().size(); i++) {
			TowerData towerData = this.towerController.getAllTowerData().get(i);
			TowerButton towerButton = new TowerButton(towerData.getName() + " " + towerData.getPrice(1) + "$", i);
			towerButton.setListener(this.towerController);
			this.buttonPanel.addButton(towerButton);
		}
		this.startButton = new Button("Start");
		this.startButton.addActionListener(this);
		this.buttonPanel.addButton(this.startButton);
		
		this.infoPanel = new InfoPanel();
		this.infoPanel.setUpgradeActionListener(this.towerController);
		
		this.gameWindow = new GameWindow(this.gamePanel, this.buttonPanel, this.infoPanel);
		this.mover = new MainMover(this.gameData);
		Thread gameThread = new Thread(this);
		gameThread.start();
	}

	private void init() {
		this.gameData.reset();
		try {
			this.enemyData = this.loadEnemyData("/enemies.csv");
			this.towerController.setAllTowerData(TowerData.create(IOHelper.parseFileToLines("/towers.csv")));
			this.levelData = LevelDataFactory.getLevelData(this.gameData.getLevel(), new FileLevelDataLoader());
			this.path = this.levelData.getPath();
			this.spawningGroups = this.levelData.getEnemyGroups();
			this.gameData.setSpawning(true);
			this.spawnPause = 0;
			this.spawnSpace = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, EnemyData> loadEnemyData(String filename) throws IOException {
		HashMap<String, EnemyData> parsedData = new HashMap<>();
		Vector<String> lines = IOHelper.parseFileToLines(filename);
		String[] fieldNames=null;
		String[] rawData;
		Iterator<String> linesIterator = lines.iterator();
		// First line must hold the field names
		String line = linesIterator.next();
		if (line != null) {
			fieldNames = line.split(",");
		}
		while (linesIterator.hasNext()) {
			line = linesIterator.next();
			rawData = line.split(",");
			EnemyData nextEnemyType = new EnemyData();
			String id = "";
			for (int i = 0; i < fieldNames.length; i++) {
				String key = fieldNames[i];
				if (key.equals(EnemyData.ID)) {
					id = rawData[i];
				} else if (key.equals(EnemyData.HEALTH)) {
					nextEnemyType.setHealth(Integer.parseInt(rawData[i]));
				}  else if (key.equals(EnemyData.IMAGE)) {
					nextEnemyType.setImage(rawData[i]);
				} else if (key.equals(EnemyData.SIZE)) {
					nextEnemyType.setSize(Integer.parseInt(rawData[i]));
				} else if (key.equals(EnemyData.VALUE)) {
					nextEnemyType.setValue(Integer.parseInt(rawData[i]));
				}  else if (key.equals(EnemyData.SPEED)) {
					nextEnemyType.setSpeed(Float.parseFloat(rawData[i]));
				}
			}
			parsedData.put(id, nextEnemyType);
		}
		return parsedData;
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
						if (this.gameData.isSpawning()) {
							if (this.spawnSpace > 0) {
								this.spawnSpace--;
							} else {
								if (this.spawnPause > 0) {
									this.spawnPause--;
								} else {
									this.gameData.setSpawning(this.addEnemy());
									// Reset the pause between enemies of the same group
									this.spawnSpace = Moletower.SPAWN_SPACE;
								}
							}
						}
						if (this.gameData.getLives() <= 0) {
							JOptionPane.showMessageDialog(this.gameWindow, "GAME OVER!");
							System.exit(0);
						}
						if ((this.spawningGroups.size() == 0) && (this.gameData.getEnemies().size() == 0) ) {
							if (this.gameData.getLevel() < Moletower.MAX_LEVEL) {
								this.gameData.setLevel(this.gameData.getLevel() + 1);
								this.init();
								// TODO: refactor GamePanel and spawningGroup!
								this.gamePanel.setPath(this.path);
								this.gamePanel.setGameData(this.gameData);
								this.spawningGroup = null;
								this.gameData.setGameActive(false);
								JOptionPane.showMessageDialog(this.gameWindow, "Level: " + this.gameData.getLevel());
							} else {
								JOptionPane.showMessageDialog(this.gameWindow, "YOU WIN!");
								System.exit(0);
							}
						}
					}
					lastMoveTime = System.currentTimeMillis();
				} else {
					Thread.sleep(moveInterval - timeSinceLastMove);
				}
				Tower placingTower = this.gameData.getTowerToPlace();
				if (placingTower != null) {
					Point mousePosition = this.getMousePosition();
					placingTower.setCanBePlaced(MathHelper.checkDistance(placingTower, this.path, this.gameData.getTowers()));
					placingTower.setPosition(mousePosition);
					this.gamePanel.setTowerToPlace(placingTower);
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

	private Point getMousePosition() {
		int mouseX = MouseInfo.getPointerInfo().getLocation().x - this.gamePanel.getLocationOnScreen().x;
		int mouseY = MouseInfo.getPointerInfo().getLocation().y - this.gamePanel.getLocationOnScreen().y;
		return new Point(mouseX, mouseY);
		
	}
	
	private boolean addEnemy() throws Exception {
		boolean moreEnemies = true;
		if (this.spawningGroup == null) {
			// Activate the first group
			this.spawningGroup = this.spawningGroups.remove(0);
		}
		// If the current group is empty, activate it's pause and get the next enemy group
		if (this.spawningGroup.getAmount() < 1) {
			// The pause time is stored in the enemy group we just emptied
			this.spawnPause = this.spawningGroup.getPause();
			// Activate the next group
			if (this.spawningGroups.size() > 0) {
				this.spawningGroup = this.spawningGroups.remove(0);
			} else {
				// No more groups - no more enemies to spawn
				moreEnemies = false;
			}
		}
		// Create a new enemy of the group's type and add it to the game data.
		Enemy newEnemy = new Enemy(this.enemyData.get(String.valueOf(spawningGroup.getEnemyType())), this.path);
		this.gameData.addEnemy(newEnemy);
		// Reduce the amount of enemies to spawn in this group by one
		this.spawningGroup.setAmount(this.spawningGroup.getAmount() - 1);
		return moreEnemies;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.startButton) {
			this.gameData.setGameActive(!this.gameData.isGameActive());
		}
		if (e.getActionCommand().equals(InfoPanel.UPGRADE_TOWER_ACTION)) {
			this.towerController.requestTowerUpgrade(this.gameData.getSelectedTower());
		}
	}

	private void tryToPlaceTower(Tower tower) {
			if (MathHelper.checkDistance(tower, this.path, this.gameData.getTowers())) {
				tower.setActive(true);
				this.gameData.addTower(tower);
				this.gameData.adjustMoney(-tower.getPrice());
			}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.gamePanel) {
			Tower placingTower = this.gameData.getTowerToPlace();
			if (placingTower != null) {
				this.tryToPlaceTower(placingTower);
				this.gameData.setTowerToPlace(null);
			} else {
				Point mousePosition = this.getMousePosition();
				Iterator<Tower> towerIterator = this.gameData.getTowers().iterator();
				Tower selectedTower = null;
				while (towerIterator.hasNext()) {
					Tower tower = towerIterator.next();
					if (MathHelper.getDistance(mousePosition, tower.getPosition()) <= tower.getSize()) {
						selectedTower = tower;
						break;
					}
				}
				this.gameData.setSelectedTower(selectedTower);
				this.infoPanel.setSelectedTower(selectedTower);
			}	
		} else {
			this.gameData.setSelectedTower(null);
			this.infoPanel.setSelectedTower(null);
		}
	}



}