package Infrastructure;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.sound.sampled.Line;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static java.lang.Math.*;

public class pendulum extends JPanel implements KeyListener, ActionListener {
	public static final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	public static final JFrame frame = new JFrame("Double Pendulum");
	public Timer refresher = new Timer(100, this);
	public static player player;
	public static List<Point> joint1Points = new ArrayList<>();
	public static List<Point>joint2_points = new ArrayList<>();
	public static List<joint2Path>joint2Lines = new ArrayList<>();
	public static final int SCALE = 2;
	int ballX=300, ballY = 0, ball_v;
	public static double a1 = PI/2, a2 = PI, 
			r1 = 100, r2 = 150, 
			a1_v, a1_a, 
			a2_v, a2_a;
	public static double m1 = 100, m2 = 60;
	public static Polygon joint2_path = new Polygon();
	public static double dt = 0.9;
	public static final double G = 9.81;
	public static int time = 0;
	public static int alpha = 255;
	private double x1,x2,y1,y2;
	
	public timers clock = new timers(this);
	
	JSlider mass2 = new JSlider();
	JButton restartBtn = new JButton("Restart");
	JButton startBtn = new JButton("Start");
	JSlider mass1 = new JSlider();
	JSlider length1 = new JSlider();
	JSlider length2 = new JSlider();
	JSlider angleA = new JSlider();
	JSlider angleB = new JSlider();

	public pendulum() {

		this.setSize(frame.getSize());
		this.addKeyListener(this);
		this.setFocusCycleRoot(false);
		this.setFocusable(true);
		mass1.setBounds(100, 50, 100, 50);
		mass1.setMaximum(100);
		mass1.setMinimum(4);
		mass1.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				double newMass = mass1.getValue();
				m1 = newMass;
			}
		});
		
			mass2.setMaximum(200);
			mass2.setMinimum(0);
			mass2.setBounds(mass1.getX(),mass1.getY()+100,100,50);
			mass2.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					m2 = mass2.getValue();
				}
			});
		length1.setMaximum(100);
		length2.setMaximum(200);
		length1.setBackground(Color.black);
		length1.setBounds(100,200,100,50);
		length2.setBounds(100,250,100,50);
		angleA.setMaximum(100);
		angleA.setBounds(100,350,100,50);
		length1.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				r1 = length1.getValue();
			}
		});
		length2.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				r2 = length2.getValue();
				}
		});
		length1.setName("Rod length 1");
		length2.setName("Rod length 2");
		length2.setMajorTickSpacing(5);
		length2.setBackground(new Color(150, 245, 45, 45));
		mass1.setName("Mass 1");
		mass2.setName("Mass 2");
		this.add(length1);
		this.add(length2);
		this.add(mass1);
		this.add(mass2);
		this.add(angleA);
		setBackground(Color.WHITE);
		refresher.start();
		//clock.roboPress();
		clock.half();

	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		frame.setUndecorated(true);
		frame.setSize(900, 800);
		frame.setLocation(dim.width / 2 - frame.getWidth() / 2, dim.height / 2 - frame.getHeight() / 2);
		frame.setContentPane(new pendulum());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);

	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		this.requestFocus();
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawString(mass1.getName(), mass1.getX()+mass1.getWidth(),mass1.getHeight()+mass1.getY());
		g2d.drawString(mass2.getName(), mass2.getX()+mass2.getWidth(),mass2.getHeight()+mass2.getY());
		g2d.drawString(length1.getName(), length1.getX()+length1.getWidth(),length1.getHeight()+length1.getY());
		g2d.drawString(length2.getName(), length2.getX()+length2.getWidth(),length2.getHeight()+length2.getY());
		g2d.setColor(Color.BLUE);
		g2d.drawLine(getWidth(), getHeight() / 2, 0, getHeight() / 2);
		g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
	
		AffineTransform old = g2d.getTransform();
		g2d.translate(frame.getWidth() / 2, frame.getHeight() / 2);
		
		g2d.setStroke(new BasicStroke(3f));

		

		
		
		calculateAngleAcceleration();
		
		a1_v += a1_a*.97;
		a2_v += a2_a*.59;
		
		a1 += a1_v;
		a2 += a2_v;
		
		
	

		
		a1_v*=0.999;
		a2_v*=0.999;
		
		 x1 = r1 * sin(a1);
		 y1 = r1 * cos(a1);
		
		 x2 = x1 + r2 * sin(a2);
		 y2 = y1 + r2 * cos(a2);

		try {
			DecimalFormat df = new DecimalFormat("#.##"); // Format coords of joints to 2 decimal places
			x1 = Double.parseDouble(df.format(x1));
			y1 = Double.parseDouble(df.format(y1));
			x2 = Double.parseDouble(df.format(x2));
			y2 = Double.parseDouble(df.format(y2));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			reset();
		}
		

		g2d.scale(SCALE,SCALE);
		
		Line2D rod1 = new Line2D.Double();
		g2d.setColor(Color.black);
		rod1.setLine(0, 0, x1, y1);
		g2d.draw(rod1);

		g2d.setColor(new Color(150, 245, 45, 45));
		Line2D rod2 = new Line2D.Double();
		rod2.setLine(x1, y1, x2, y2);
		g2d.draw(rod2);
				
		g2d.setColor(Color.DARK_GRAY);
		for (Point p : joint1Points) {
			g2d.fillOval(p.x, p.y, 2, 2);
		}
		
		g2d.setColor(Color.RED);
		Ellipse2D joint1 = new Ellipse2D.Double(x1,y1,sqrt(m1)*2,sqrt(m1)*2);
		g2d.fill(joint1);
		 
		g2d.setColor(Color.red);
		Ellipse2D joint2 = new Ellipse2D.Double(x2, y2, sqrt(m2)*2, sqrt(m2)*2);
		g2d.fill(joint2);
		
		
		
		
		
	
	
		g2d.setColor(new Color(32, 59, 100, 55));
		g2d.rotate(-a1,(int)x1,(int)y1);
		g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
		g2d.drawString("(" + x1 + "," + y1 + ")", (int) x1, (int) y1);	
		mass1.setValue((int) m1);
		mass2.setValue((int) m2);
		length1.setValue((int) r1);
		length2.setValue((int) r2);
		g2d.setTransform(old);
		angleA.setValue((int) a1);
		angleA.setMinimum((int) -100);
		
		

	}
	public static void reset() {
		a1 = PI/2;
		a2 = PI/8;
		a1_a = 0;
		a2_a = 0;
		a1_v = 0;
		a2_v = 0;
		time = 0;
		
	}


	

	public static void calculateAngleAcceleration() {
		double a1_aN = - G * (2*m1 + m2) * sin(a1) - m2 * G * sin(a1 - 2*a2) - 2 * sin(a1 - a2) * 
				m2 * (a2_v*a2_v*r2 + a1_v*a1_v*r1 * cos(a1-a2));
	
		double a2_aN = 2 * sin(a1 - a2) * (a1_v*a1_v*r1 * (m1 + m2) + G * (m1 + m2) *cos(a1) + a2_v*a2_v * r2 
				* m2 * cos(a1-a2));
		
		double den = 2*m1+m2-m2*cos(2*a1 - 2*a2);
		
		a1_a = (a1_aN) / (r1*den);
		a2_a = (a2_aN) / (r2*den);
	}
	



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setSize(frame.getSize());
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		switch (key) {
		case (KeyEvent.VK_UP):
			player.setAccel(1.5);
			break;
		case (KeyEvent.VK_DOWN):
			player.setAccel(-1.5);
			break;
		case (KeyEvent.VK_RIGHT):
			player.setRotation(player.getRotation() + 1);
			break;
		case (KeyEvent.VK_LEFT):
			player.setRotation(player.getRotation() - 1);
			break;
		case (KeyEvent.VK_ESCAPE):
			System.exit(0);
			break;
		case (KeyEvent.VK_F11):

			frame.setVisible(false);
			clock.frame_transformation();
			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		// player.setAccel(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	

}
