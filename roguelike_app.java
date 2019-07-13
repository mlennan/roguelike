package roguelike;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

import java.awt.event.KeyEvent;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Random;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.*;

import javax.swing.Timer;
import javax.swing.JFrame;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class roguelike_app extends JComponent implements KeyListener, ActionListener
{	
	public static Graphics2D g2;
	
	public roguelike_app()
	{
		start();
	}
	public static char[][] try_drawing_array = { 
			{'x', 'x', 'x', 'x', 'x', 'x', 'x'}, 
			{'x', 'o', 'o', 'o', 'o', 'o', 'x'}, 
			{'x', 'o', 'x', ' ', 'x', 'o', 'x'}, 
			{'x', 'o', ' ', ' ', ' ', 'o', 'x'}, 
			{'x', 'o', 'x', ' ', 'x', 'o', 'x'}, 
			{'x', 'o', 'o', 'o', 'o', 'o', 'x'}, 
			{'x', 'x', 'x', 'x', 'x', 'x', 'x'}};
	
	public void paintComponent(Graphics g)
	{
		

		g2 = (Graphics2D)g;
//		g2.setColor(new Color(160, 255, 200, 255));
		g2.setColor(Color.black);
		for (int row = 0; row < 7; row++)
		{
			for (int column = 0; column < 7; column++)
			{
				if (try_drawing_array[column][row] == 'x')
				{
//					Rectangle2D.Double rectangle = new Rectangle2D.Double(45 + (30 * row), 45 + (30 * column), 30, 30);
					g2.fill(new Rectangle2D.Double(45 + (30 * row), 45 + (30 * column), 30, 30));
//					g2.draw(rectangle);
				}
				else if (try_drawing_array[column][row] == 'o')
				{
					Ellipse2D.Double circle = new Ellipse2D.Double(45 + (30 * row), 45 + (30 * column), 30, 30);
					g2.draw(circle);
				}
			}
		}
	}
	

	
	public void start()
	{
		class EventTimer implements ActionListener 
		{
			public void actionPerformed(ActionEvent event)
			{
				repaint();
			}
		}
		EventTimer listener = new EventTimer();
		Timer t = new Timer(250, listener);  
		t.start();
	}
	
	public void sec_hand_visible(boolean change_to)
	{
		sec_hand_visible = change_to;
	}
	
	public void change_array(int x, int y, char letter)
	{
		try_drawing_array[x][y] = letter;
	}
	
	
	public static final int TOP = 10;
	public static final int LEFT = 10;
	public static final int WIDTH = 100;
	public static final int HEIGHT = 100;

	public static final int HOFFSET = ((HEIGHT + LEFT + LEFT) / 2);
	public static final int VOFFSET = ((WIDTH + TOP + TOP) / 2);

	public static final double TOTAL_PERCENT = 100;
	public static final double RADIUS = 20;
	public static final double TWO_PI = 2.0 * Math.PI;

	public static final int SECOND_HAND_RADIUS = 40;
	public static final int MINUTE_HAND_RADIUS = 35;
	public static final int HOUR_HAND_RADIUS = 22;

	public static final double HALF_PI = (Math.PI / 2);
	public static final double SECS_PER_MIN = 60;
	public static final double MINS_PER_HOUR = 60;
	public static final double HOURS_PER_DAY = 24;
	
	private boolean secondHandVisible;
	private boolean sec_hand_visible;
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		g2.setColor(Color.blue);
		System.out.println("Hello");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		g2.setColor(Color.green);
		System.out.println("Hello");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		g2.setColor(Color.yellow);
		System.out.println("Hello");
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		g2.setColor(Color.red);
		System.out.println("Hello");
	}
}
