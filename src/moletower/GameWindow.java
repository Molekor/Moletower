package moletower;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	public Point mousePosition = new Point(1,1);
	public boolean mouseIsPressed;
	private JFrame frame;
	private JPanel buttonPanel;
	private Button firetowerButton;
	private Button fasttowerButton;
	private GamePainter gamePainter;

	public GameWindow(GamePainter gamePainter) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.gamePainter = gamePainter;

		this.buttonPanel = new JPanel();
		this.buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, BoxLayout.Y_AXIS));
		this.firetowerButton = new Button("Firetower");
		this.fasttowerButton = new Button("Fasttower");
		this.firetowerButton.setMaximumSize(new Dimension(150,30));
		this.fasttowerButton.setMaximumSize(new Dimension(150,30));
		this.buttonPanel.add(this.firetowerButton);
		this.buttonPanel.add(Box.createRigidArea(new Dimension(105, 20)));
		this.buttonPanel.add(this.fasttowerButton);
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Moletower");
		frame.add(this.gamePainter, BorderLayout.LINE_START);
		frame.add(this.buttonPanel, BorderLayout.LINE_END);
		//frame.setContentPane(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
//
//	@Override
//	public void paintComponent(Graphics g) {
//		
//		this.gamePainter.paintComponent(g);
//		this.buttonPanel.paintComponents(g);
//	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.mousePosition = new Point(e.getX(), e.getY());
		this.mouseIsPressed = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.mousePosition = new Point(e.getX(), e.getY());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.mousePosition = new Point(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.mousePosition = new Point(e.getX(), e.getY());
		this.mouseIsPressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.mousePosition = new Point(e.getX(), e.getY());
		this.mouseIsPressed = false;

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.mousePosition = new Point(e.getX(), e.getY());
		this.mouseIsPressed = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mousePosition = new Point(e.getX(), e.getY());
		this.mouseIsPressed = false;

	}

	public Dimension getMinimumSize() {
		return new Dimension(1024, 600);
	}

	public Dimension getMaximumSize() {
		return new Dimension(1024, 600);
	}

	public Dimension getPreferredSize() {
		return new Dimension(1024, 600);
	}
}
