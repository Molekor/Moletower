package moletower.view;

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
	private InfoPanel infoPanel;

	public GameWindow(GamePanel gamePanel, ButtonPanel buttonPanel, InfoPanel infoPanel) {
		this.gamePanel = gamePanel;
		this.buttonPanel = buttonPanel;
		this.infoPanel = infoPanel;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Moletower");
		frame.add(this.gamePanel, BorderLayout.LINE_START);
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setMinimumSize(infoPanel.getMinimumSize());
		sidePanel.add(this.buttonPanel, BorderLayout.PAGE_START);
		sidePanel.add(this.infoPanel, BorderLayout.PAGE_END);
		frame.add(sidePanel, BorderLayout.LINE_END);
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
