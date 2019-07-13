package roguelike;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class roguelike_menu extends JFrame
{
	public roguelike_menu(roguelike_app clock)
	{
		given = clock;
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		menuBar.add(createShowMenu());
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	public JMenu createFileMenu()
	{
		JMenu menu = new JMenu("File");
		menu.add(createFileExitItem());
		return menu;
	}
	
	
	public JMenuItem createFileExitItem()
	{
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		}
		ActionListener listener = new MenuItemListener();
		item.addActionListener(listener);
		return item;
	}
	
	public JMenu createShowMenu()
	{
		JMenu menu = new JMenu("Show");
		menu.add(createShowToggleItem());
		menu.add(createHideToggleItem());
		return menu;
	}
	
	public JMenuItem createShowToggleItem()
	{
		JMenuItem item = new JMenuItem("Show Second Hand");
		class MenuItemListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				given.sec_hand_visible(true);
				given.change_array(3, 3, 'x');
			}
		}
		ActionListener listener = new MenuItemListener();
		item.addActionListener(listener);
		return item;
	}
	
	
	public JMenuItem createHideToggleItem()
	{
		JMenuItem item = new JMenuItem("Hide Second Hand");
		class MenuItemListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				given.sec_hand_visible(false);
				given.change_array(3, 3, ' ');
			}
		}
		ActionListener listener = new MenuItemListener();
		item.addActionListener(listener);
		return item;
	}
	
	
	private roguelike_app given;
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 200;
	private static boolean sec_hand_visible = false;
}