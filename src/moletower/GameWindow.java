package moletower;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JPanel {
	private static final long serialVersionUID = 1L;
	public Point mousePosition = new Point(1,1);
	public boolean mouseIsPressed;
	private JFrame frame;
	private JPanel buttonPanel;
	private GamePanel gamePanel;

	public GameWindow(GamePanel gamePanel, ButtonPanel buttonPanel) {
		this.gamePanel = gamePanel;
		this.buttonPanel = buttonPanel;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Moletower");
		frame.add(this.gamePanel, BorderLayout.LINE_START);
		frame.add(this.buttonPanel, BorderLayout.LINE_END);
		//frame.setContentPane(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public Dimension getMinimumSize() {
		return new Dimension(1200, 600);
	}

	public Dimension getMaximumSize() {
		return new Dimension(1200, 600);
	}

	public Dimension getPreferredSize() {
		return new Dimension(1200, 600);
	}

}
