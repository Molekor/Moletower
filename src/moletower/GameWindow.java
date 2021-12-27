package moletower;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private GamePainter gamePainter;
	public Point mousePosition = new Point(1,1);
	public boolean mouseIsPressed;
	private JFrame frame;

	public GameWindow(GamePainter gamePainter) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.gamePainter = gamePainter;

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Moletower");
		frame.setContentPane(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		this.gamePainter.draw(g);
	}

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
		return new Dimension(800, 600);
	}

	public Dimension getMaximumSize() {
		return new Dimension(800, 600);
	}

	public Dimension getPreferredSize() {
		return new Dimension(800, 600);
	}
}
