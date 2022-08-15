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

/**
 * The main class of the game.
 * 
 * @author Molekor
 *
 */
public class Moletower extends MouseAdapter implements Runnable, ActionListener, BuyListener, UpgradeListener {

	private long lastMoveTime; // time we last moved the shots and enemies
	private long timeSinceLastMove = 0; // how long has it been since the last move
	private static int moveInterval = 40; // minimal interval between moves in ms, this represents overall game speed
	private GameWindow gameWindow; // Main window of the game that handles basic I/O
	private Path path; // The path all enemies will follow to the exit
	private GamePanel gamePanel;
	private MainMover mover;
	private GameData gameData;
	private ButtonPanel buttonPanel;
	private Button startButton;
	private long spawnPause = 0;
	private long spawnSpace = 0;
	private Vector<Vector<Enemy>> spawningEnemies;
	private Vector<Enemy> spawningGroup;
	private Vector<Long> spawnPauses;
	private InfoPanel infoPanel;
	private Vector<TowerData> allTowerData;
	private HashMap<String, EnemyData> enemyData;
	
	public static void main(String[] args) {
		new Moletower();
	}

	public Moletower() {
		this.gameData = new GameData();
		this.spawningEnemies = new Vector<Vector<Enemy>>();
		this.spawningGroup = new Vector<Enemy>();
		this.spawnPauses = new Vector<Long>();
		this.spawnPauses.add(Long.parseLong("0"));
		
		try {
			this.path = this.getPath("/path1.csv");
			this.enemyData = this.loadEnemyData("/enemies.csv");
			this.loadEnemyFile("/rounds1.csv");
			this.allTowerData = TowerData.create(this.parseFileToLines("/towers.csv"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.gamePanel = new GamePanel(this.gameData, this.path);
		this.gamePanel.addMouseListener(this);
		
		this.buttonPanel = new ButtonPanel();
		for (int i = 0; i < this.allTowerData.size(); i++) {
			TowerData towerData = this.allTowerData.get(i);
			TowerButton towerButton = new TowerButton(towerData.getName() + " " + towerData.getPrice(1) + "$", i);
			towerButton.setListener(this);
			this.buttonPanel.addButton(towerButton);
		}
		this.startButton = new Button("Start");
		this.startButton.addActionListener(this);
		this.buttonPanel.addButton(this.startButton);
		
		this.infoPanel = new InfoPanel();
		this.infoPanel.setUpgradeActionListener(this);
		
		this.gameWindow = new GameWindow(this.gamePanel, this.buttonPanel, this.infoPanel);
		this.mover = new MainMover(this.gameData);
		Thread gameThread = new Thread(this);
		gameThread.start();
	}

	private Vector<String> parseFileToLines(String filename) throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		Vector<String> lines = new Vector<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}
	private Path getPath(String pathFileName) throws IOException {
		Path path = new Path();
		InputStream inputStream = this.getClass().getResourceAsStream(pathFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		for (String line; (line = reader.readLine()) != null; ) {
			String[] coords = line.split(",");
			if (coords.length == 2) {
				path.addPathPoint(new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
			}
		}
		reader.close();
		return path;
	}

	private HashMap<String, EnemyData> loadEnemyData(String filename) throws IOException {
		HashMap<String, EnemyData> parsedData = new HashMap<>();
		Vector<String> lines = this.parseFileToLines(filename);
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
						if (this.spawnSpace > 0) {
							this.spawnSpace--;
						} else {
							if (this.spawnPause > 0) {
								this.spawnPause--;
							} else {
								this.addEnemies();
							}
						}
						if (this.gameData.getLives() <= 0) {
							JOptionPane.showMessageDialog(this.gameWindow, "GAME OVER!");
							System.exit(0);
						}
						if((this.spawningEnemies.size() == 0) && (this.spawningGroup.size() == 0) && (this.gameData.getEnemies().size() == 0) ) {
							JOptionPane.showMessageDialog(this.gameWindow, "YOU WIN!");
							System.exit(0);
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

	private void loadEnemyFile(String roundFileName) throws IOException, Exception {
		InputStream inputStream = this.getClass().getResourceAsStream(roundFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		for (String line; (line = reader.readLine()) != null; ) {
			if (line.startsWith("#")) {
				continue;
			}
			String[] enemyGroup = line.split(",");
			if (enemyGroup.length == 3) {
				Vector<Enemy> enemyVector = new Vector<Enemy>();
				this.spawningEnemies.add(enemyVector);
				EnemyData currentEnemyData = this.enemyData.get(enemyGroup[1]);
				for (int i = 0; i < Integer.parseInt(enemyGroup[0]); i++) {
					enemyVector.add(new Enemy(currentEnemyData, this.path));
				}
				this.spawnPauses.add(Long.parseLong(enemyGroup[2]));
			}
		}
	}
	
	private void addEnemies() throws Exception {
		if (this.spawningGroup.size() > 0) {
			Enemy newEnemy = this.spawningGroup.remove(0);
			newEnemy.setPath(this.path);
			this.gameData.addEnemy(newEnemy);
			this.spawnSpace = 10;
		} else {
			if (this.spawningEnemies.size() > 0) {
				this.spawningGroup = this.spawningEnemies.remove(0);
				this.spawnPause = this.spawnPauses.remove(0);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.startButton) {
			this.gameData.setGameActive(!this.gameData.isGameActive());
		}
		if (e.getActionCommand().equals(InfoPanel.UPGRADE_TOWER_ACTION)) {
			System.out.println(InfoPanel.UPGRADE_TOWER_ACTION);
			this.requestTowerUpgrade(this.gameData.getSelectedTower());
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

	public void requestTowerBuy(int towerTypeId) {
		this.gameData.setSelectedTower(null);
		this.infoPanel.setSelectedTower(null);
		TowerData towerData = this.allTowerData.get(towerTypeId);
		Tower newTower = new Tower(towerData);
		// Check if we have the money to place the selected tower
		if (newTower.getPrice() > this.gameData.getMoney()) {
			this.gameData.setMoneyWarning(true);
			this.gameData.setTowerToPlace(null);
		} else {
			this.gameData.setMoneyWarning(false);
			this.gameData.setTowerToPlace(newTower);
		}
	}

	public void requestTowerUpgrade(Tower towerToUpgrade) {
		int price = towerToUpgrade.getPrice();
		if (price <= this.gameData.getMoney()) {
			towerToUpgrade.upgrade();
			this.gameData.adjustMoney(-price);
		}
	}

}