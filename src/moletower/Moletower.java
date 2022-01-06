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
		try {
			this.path = this.getPath("/path1.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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


	private Path getPath(String pathFileName) throws IOException {
		Path path = new Path();
		InputStream inputStream = this.getClass().getResourceAsStream(pathFileName);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream));
		for (String line; (line = reader.readLine()) != null; ) {
			String[] coords = line.split(";");
			if (coords.length == 2) {
				path.addPathPoint(new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
			}
		}
		reader.close();
		return path;
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
					this.gameData.getTowerToPlace().setCanBePlaced(this.gameData.getTowerToPlace().checkDistance(this.path, this.gameData.getEnemies()));
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
			if (this.gameData.getTowerToPlace().checkDistance(this.path, this.gameData.getEnemies())) {
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