package roguelike;

import java.util.Scanner;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JFrame;
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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;


public class MainDriver
{
	public static Scanner Keyboard = new Scanner(System.in);
	public static Random RNG = new Random();
	public static enemy[] monsters = new enemy[20];
	
	public static boolean[][] traversalMap = new boolean[90][90];
	public static tile[][] currentMap;
	public static int currentFloor = 0;
	public static int deepestFloor = 0;
	
	public static int characterRow;
	public static int characterColumn;
	public static character Player;
	
	
	
	public static void main(String[] args)
	{
		char input = '[';
		characterCreation();
		currentMap = getNewMap();
		
		
		while (input != 'q')
		{
			relativeMapPrint();
			input = getValidInput();
			useInput(input);
			enemyDirector();
		}
		
		
	}
	
	public static void characterCreation()
	{
		System.out.println("What is your name?");
		Player = new character(Keyboard.nextLine());
	}
	public static tile[][] getNewMap()	//whenever we get a new map
	{
		tile[][] newMap;
		for (int currentMonsterIndex = 0; currentMonsterIndex < monsters.length; currentMonsterIndex++)
		{
			monsters[currentMonsterIndex] = null;
		}
		
		char[][] unformattedMap = getUnformattedMap();
		newMap = convertUnformattedMap(unformattedMap);
		return newMap;
	}
	public static void storeCurrentMap()
	{
		try	//store current map to a new file
		{
			String mapStorage = Player.get_name() + "_floor_" + deepestFloor + ".txt";
			
			File ensureFileDoesNotExist = new File(mapStorage);
			if (ensureFileDoesNotExist.exists())
			{
				ensureFileDoesNotExist.delete();
			}
			
			PrintWriter writeFloorToFile = new PrintWriter(new BufferedWriter(new FileWriter(mapStorage, true)));
			for(int currentMapRow = 0; currentMapRow < currentMap.length; currentMapRow++)
			{
				String currentRow = "";
				for (int currentRowTile = 0; currentRowTile < currentMap[currentMapRow].length; currentRowTile++)
				{
					if (currentMap[currentMapRow][currentRowTile].get_character() != 'i')
					{
						currentRow += currentMap[currentMapRow][currentRowTile].get_character();
					}
					else
					{
						currentRow += '-';
					}
				}
				writeFloorToFile.println(currentRow);
			}
			writeFloorToFile.close();
		}
		catch(Exception e)
		{
			System.out.println("Failed to store map to the file, please do not return to the floor");
		}
	}
	public static char[][] getUnformattedMap()	//put the map into a character array for conversion
	{
		char[][] unformattedMap;
		try
		{
			BufferedReader readMapFile = new BufferedReader(new FileReader("level_maps.txt"));
			String fileLine;
			int nextMap = RNG.nextInt(4)+1;
//			int nextMap = 1;
			
			fileLine = readMapFile.readLine();
			while (Character.getNumericValue(fileLine.charAt(0)) != nextMap)	//search for the correct section
			{
				fileLine = readMapFile.readLine();
			}
			fileLine = readMapFile.readLine();	//once the correct section is found we go to where the map actually starts
			ArrayList<String> rawMap = new ArrayList<String>();
			while (fileLine.charAt(0) != '#')	//keep adding parts of the map until it ends
			{
				rawMap.add(fileLine);
				fileLine = readMapFile.readLine();
			}
			//the entire map is now in rawMap
			unformattedMap = new char[rawMap.size()][rawMap.get(0).length()];//get the correct dimensions, maps are always rectangles
			for (int currentMapRow = 0; currentMapRow < rawMap.size(); currentMapRow++)	//convert the map to characters
			{
				String currentRow = rawMap.get(currentMapRow);
				for (int currentMapColumn = 0; currentMapColumn < currentRow.length(); currentMapColumn++)
				{
					unformattedMap[currentMapRow][currentMapColumn] = currentRow.charAt(currentMapColumn);
				}
			}
			readMapFile.close();
		}
		catch(Exception e)	//for anything that happens a super basic map is made just so you can progress
		{
			System.out.println("Failed to create unformatted map");
			char[][] debugMap = {{'X','X','X','X','X'},
	    			 			 {'X','<','-','>','X'},
	    			 			 {'X','X','X','X','X'}};
			return debugMap;
		}
		return unformattedMap;
	}
	public static tile[][] convertUnformattedMap(char[][] unformattedMap)
	{
		tile[][] formattedMap = new tile[unformattedMap.length][unformattedMap[0].length];
		for (int currentRow = 0; currentRow < unformattedMap.length; currentRow++)
		{
			for (int currentColumn = 0; currentColumn < unformattedMap[0].length; currentColumn++)
			{
				switch (unformattedMap[currentRow][currentColumn])
				{
				case 'x':
				case 'X':
					formattedMap[currentRow][currentColumn] = new tile(false, 'X', "wall", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = false;
					break;
					
				case '-':
					formattedMap[currentRow][currentColumn] = new tile(true, '-', "empty space", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					break;
					
				case '<':
					formattedMap[currentRow][currentColumn] = new tile(true, '<', "up stairs", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					characterRow = currentRow;			//we should start at
					characterColumn = currentColumn;	//the stairs we come down
					break;
					
				case '>':
					formattedMap[currentRow][currentColumn] = new tile(true, '>', "down stairs", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					break;
					
				case 'm':
				case 'M':
					formattedMap[currentRow][currentColumn] = new tile(true, '-', "empty space", currentRow, currentColumn);
					formattedMap[currentRow][currentColumn].toggleMonster();
					traversalMap[currentRow][currentColumn] = true;
					boolean indexFound = false;
					for (int findANullMonsterIndex = 0; (findANullMonsterIndex < monsters.length) && (!indexFound); findANullMonsterIndex++)
					{
						if (monsters[findANullMonsterIndex] == null)
						{
							monsters[findANullMonsterIndex] = new enemy("Exciting Name", 5, 7, 4, 1);
							monsters[findANullMonsterIndex].setPosition(currentRow, currentColumn);
							indexFound = true;
						}
					}
					break;
				default:
					formattedMap[currentRow][currentColumn] = new tile(true, '?', "an error", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					break;
				}
			}
		}
		return formattedMap;
	}
	public static char[][] getOldUnformattedMap()	//put the map into a character array for conversion
	{
		char[][] unformattedMap;
		try
		{
			String oldFloorFile = Player.get_name() + "_floor_" + currentFloor + ".txt";
			BufferedReader readMapFile = new BufferedReader(new FileReader(oldFloorFile));
			String fileLine;

			fileLine = readMapFile.readLine();	//once the correct section is found we go to where the map actually starts
			ArrayList<String> rawMap = new ArrayList<String>();
			while (fileLine != null)	//keep adding parts of the map until it ends
			{
				rawMap.add(fileLine);
				fileLine = readMapFile.readLine();
			}
			//the entire map is now in rawMap
			unformattedMap = new char[rawMap.size()][rawMap.get(0).length()];//get the correct dimensions, maps are always rectangles
			for (int currentMapRow = 0; currentMapRow < rawMap.size(); currentMapRow++)	//convert the map to characters
			{
				String currentRow = rawMap.get(currentMapRow);
				for (int currentMapColumn = 0; currentMapColumn < currentRow.length(); currentMapColumn++)
				{
					unformattedMap[currentMapRow][currentMapColumn] = currentRow.charAt(currentMapColumn);
				}
			}
			readMapFile.close();
		}
		catch(Exception e)	//for anything that happens a super basic map is made just so you can progress
		{
			System.out.println("Failed to read old unformatted map");
			char[][] debugMap = {{'X','X','X','X','X'},
	    			 			 {'X','<','-','>','X'},
	    			 			 {'X','X','X','X','X'}};
			return debugMap;
		}
		return unformattedMap;
	}
	public static tile[][] convertOldUnformattedMap(char[][] unformattedMap)
	{
		for (int currentMonsterIndex = 0; currentMonsterIndex < monsters.length; currentMonsterIndex++)
		{
			monsters[currentMonsterIndex] = null;
		}
		tile[][] formattedMap = new tile[unformattedMap.length][unformattedMap[0].length];
		for (int currentRow = 0; currentRow < unformattedMap.length; currentRow++)
		{
			for (int currentColumn = 0; currentColumn < unformattedMap[0].length; currentColumn++)
			{
				switch (unformattedMap[currentRow][currentColumn])
				{
				case 'x':
				case 'X':
					formattedMap[currentRow][currentColumn] = new tile(false, 'X', "wall", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = false;
					break;
					
				case '-':
					formattedMap[currentRow][currentColumn] = new tile(true, '-', "empty space", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					break;
					
				case '<':
					formattedMap[currentRow][currentColumn] = new tile(true, '<', "up stairs", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					break;
					
				case '>':
					formattedMap[currentRow][currentColumn] = new tile(true, '>', "down stairs", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					characterRow = currentRow;			//we're coming up stairs so we need to be on the down stairs
					characterColumn = currentColumn;	
					break;
					
				case 'm':
				case 'M':
					formattedMap[currentRow][currentColumn] = new tile(true, '-', "empty space", currentRow, currentColumn);
					formattedMap[currentRow][currentColumn].toggleMonster();
					traversalMap[currentRow][currentColumn] = true;
					boolean indexFound = false;
					for (int findANullMonsterIndex = 0; (findANullMonsterIndex < monsters.length) && (!indexFound); findANullMonsterIndex++)
					{
						if (monsters[findANullMonsterIndex] == null)
						{
							monsters[findANullMonsterIndex] = new enemy("Exciting Name", 10, 7, 3, 1);
							monsters[findANullMonsterIndex].setPosition(currentRow, currentColumn);
							indexFound = true;
						}
					}
					break;
				default:
					formattedMap[currentRow][currentColumn] = new tile(true, '?', "an error", currentRow, currentColumn);
					traversalMap[currentRow][currentColumn] = true;
					break;
				}
			}
		}
		return formattedMap;
	}
	public static void enemyDirector()	//drop items if dead, else move at player
	{
		for (int currentEnemy = 0; (currentEnemy < monsters.length) && (monsters[currentEnemy] != null); currentEnemy++)
		{
			if (monsters[currentEnemy].is_dead())	//if the current monster is dead we need to clean it up
			{
				int monsterX = monsters[currentEnemy].getX();
				int monsterY = monsters[currentEnemy].getY();
				
				Player.add_experience(monsters[currentEnemy].getExperienceValue());
				
				if (RNG.nextInt(2) == 0)
				{
					weapon toDrop = new weapon(RNG.nextInt(2)+currentFloor, RNG.nextInt(2)+4+currentFloor, RNG.nextInt(3)+1, RNG.nextInt(3)+1, RNG.nextInt(3)+1);
					currentMap[monsterX][monsterY].place_item(new item(toDrop));
				}
				else
				{
					armor toDrop = new armor(RNG.nextInt(3) + currentFloor);
					currentMap[monsterX][monsterY].place_item(new item(toDrop));
				}
				currentMap[monsterX][monsterY].place_gold(RNG.nextInt(10)+5);
				
				
				currentMap[monsterX][monsterY].toggleMonster();
				for (int currentIndex = currentEnemy; currentIndex < (monsters.length-1); currentIndex++)
				{
					monsters[currentIndex] = monsters[currentIndex+1];
				}
				monsters[19] = null;			//everything shifts down, and 19 becomes null
			}
		}
		monsterPathing(traversalMap);
	}
	public static void monsterPathing(boolean[][] traversalMap)
	{
		boolean[][] traversalMapCopy = new boolean[90][90];
		for (int i = 0; i < 90; i++)
		{
			for (int j = 0; j < 90; j++)
			{
				traversalMapCopy[i][j] = traversalMap[i][j];
			}
		}
		
		ArrayList<tile> tileQueue = new ArrayList<tile>();
		tileQueue.add(currentMap[characterRow][characterColumn]);
		while (!tileQueue.isEmpty())	//while we haven't checked every tile
		{
			tile tileCurrentlyBeingChecked = tileQueue.remove(0);	//take off the first tile in the queue
			tile currentAdjacentTile = currentMap[tileCurrentlyBeingChecked.getX()+1][tileCurrentlyBeingChecked.getY()];
				//the tile we are currently looking at
			if ( (currentAdjacentTile.is_passable()) &&	//if the tile is something we can walk on
			(traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()]) )	// and if it hasn't been walked on yet
			{
				if (currentAdjacentTile.hasMonster())	//if there is a monster on the tile it needs to maybe move
				{
					enemy monsterOnTile = findTileMonster(currentAdjacentTile);
					if ( (tileCurrentlyBeingChecked.getX() == characterRow) &&		//if the tile is the player
						 (tileCurrentlyBeingChecked.getY() == characterColumn) )	//the monster shouldn't move onto the player
					{
						Player.receive_attack(monsterOnTile.attack());
							//the player is attacked if the monster is on an adjacent tile
					}
					else	//monster moves since it cannot attack
					{
						currentAdjacentTile.toggleMonster();
						tileCurrentlyBeingChecked.toggleMonster();
						monsterOnTile.setY(tileCurrentlyBeingChecked.getY());
						monsterOnTile.setX(tileCurrentlyBeingChecked.getX());
					}
				}
				traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()] = false;
				tileQueue.add(currentAdjacentTile);	//a tile is added to the queue after the monster on it moves/attacks
					
			}
			currentAdjacentTile = currentMap[tileCurrentlyBeingChecked.getX()-1][tileCurrentlyBeingChecked.getY()];
			if ( (currentAdjacentTile.is_passable()) &&
				(traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()]))
			{
				if (currentAdjacentTile.hasMonster())	//if there is a monster on the tile it needs to maybe move
				{
					enemy monsterOnTile = findTileMonster(currentAdjacentTile);
					if ( (tileCurrentlyBeingChecked.getX() == characterRow) &&		//if the tile is the player
						 (tileCurrentlyBeingChecked.getY() == characterColumn) )	//the monster shouldn't move onto the player
					{
						Player.receive_attack(monsterOnTile.attack());
							//the player is attacked if the monster is on an adjacent tile
					}
					else
					{
						currentAdjacentTile.toggleMonster();
						tileCurrentlyBeingChecked.toggleMonster();
						monsterOnTile.setY(tileCurrentlyBeingChecked.getY());
						monsterOnTile.setX(tileCurrentlyBeingChecked.getX());
					}
				}
				traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()] = false;
				tileQueue.add(currentAdjacentTile);
			}
			currentAdjacentTile = currentMap[tileCurrentlyBeingChecked.getX()][tileCurrentlyBeingChecked.getY()+1];
			if ( (currentAdjacentTile.is_passable())&& 
				(traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()]) )
			{
				if (currentAdjacentTile.hasMonster())	//if there is a monster on the tile it needs to maybe move
				{
					enemy monsterOnTile = findTileMonster(currentAdjacentTile);
					if ( (tileCurrentlyBeingChecked.getX() == characterRow) &&		//if the tile is the player
						 (tileCurrentlyBeingChecked.getY() == characterColumn) )	//the monster shouldn't move onto the player
					{
						Player.receive_attack(monsterOnTile.attack());
							//the player is attacked if the monster is on an adjacent tile
					}
					else
					{
						currentAdjacentTile.toggleMonster();
						tileCurrentlyBeingChecked.toggleMonster();
						monsterOnTile.setY(tileCurrentlyBeingChecked.getY());
						monsterOnTile.setX(tileCurrentlyBeingChecked.getX());
					}
				}
				traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()] = false;
				tileQueue.add(currentAdjacentTile);
			}
			currentAdjacentTile = currentMap[tileCurrentlyBeingChecked.getX()][tileCurrentlyBeingChecked.getY()-1];
			//look 
			if ( (currentAdjacentTile.is_passable()) &&
				(traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()]) )

			{
				if (currentAdjacentTile.hasMonster())	//if there is a monster on the tile it needs to maybe move
				{
					enemy monsterOnTile = findTileMonster(currentAdjacentTile);
					if ( (tileCurrentlyBeingChecked.getX() == characterRow) &&		//if the tile is the player
						 (tileCurrentlyBeingChecked.getY() == characterColumn) )	//the monster shouldn't move onto the player
					{
						Player.receive_attack(monsterOnTile.attack());
							//the player is attacked if the monster is on an adjacent tile
					}
					else
					{
						currentAdjacentTile.toggleMonster();
						tileCurrentlyBeingChecked.toggleMonster();
						monsterOnTile.setY(tileCurrentlyBeingChecked.getY());
						monsterOnTile.setX(tileCurrentlyBeingChecked.getX());
					}
				}
				traversalMapCopy[currentAdjacentTile.getX()][currentAdjacentTile.getY()] = false;
				tileQueue.add(currentAdjacentTile);
			}
		}
		/* √ start at player
		 * each tile checks adjacent tiles if they are passable, and traversalMap for if the tile has already been visited
		 * if the tile has a monster and the tile has not been visited the monster moves
		 */
	}
	public static enemy findTileMonster(tile currentAdjacentTile)
	{
		int currentTileX = currentAdjacentTile.getX();
		int currentTileY = currentAdjacentTile.getY();
		for (int currentMonsterIndex = 0; (currentMonsterIndex < monsters.length); currentMonsterIndex++)
		{
			if ( (monsters[currentMonsterIndex].getX() == currentTileX) && (monsters[currentMonsterIndex].getY() == currentTileY) )
			{
				return monsters[currentMonsterIndex];
			}
		}
		return null;	//program will  crash if this is reached, try/catch would surround where
						//this method is called if more time were allotted
	}
	public static void relativeMapPrint()
	{
		for (int currentPrintRow = -5; currentPrintRow < 6; currentPrintRow++)
		{
			for (int currentPrintColumn = -5; currentPrintColumn < 6; currentPrintColumn++)
			{
				int relativeRow = currentPrintRow + characterRow;
				int relativeColumn = currentPrintColumn + characterColumn;
				if ((relativeRow < 0) || (relativeRow >= currentMap.length) ||
					(relativeColumn < 0) || (relativeColumn >= currentMap[0].length))
					//we do not want to try and print something outside the array since it will crash the program
				{
					System.out.print(" ");
				}
				else if ((currentPrintRow == 0) && (currentPrintColumn == 0))
				{
					System.out.print("P");
				}
				else
				{
					System.out.print(currentMap[relativeRow][relativeColumn].get_character());
				}
			}
			System.out.println("");
		}
	}
	public static char getValidInput()
	{
		String attemptedInput = "y";
		boolean wantValidInput = true;
		while (wantValidInput)
		{
			System.out.printf("Hp: %d/%d\t Exp:%d/%d\n", Player.get_hp(), Player.get_hp_max(), Player.get_current_xp(), Player.get_next_level_xp());
			System.out.println("Input movement direction (wasd movement), q brings up actions\np exits, > goes down a floor, < goes up, 5 skips a turn");
			attemptedInput = Keyboard.nextLine();
			if (attemptedInput.length() == 1)	//only one character
			{
				switch (attemptedInput.charAt(0))
				{
				case 'w':
				case 'a':
				case 's':
				case 'd':
				case 'g':
				case '<':
				case '>':
				case 'q':
				case 'i':
				case '5':
					wantValidInput = false;	//we have something
					break;
				default:	//other characters are no good
					System.out.println("Not a valid input");
				}
			}
			else
			{
				System.out.println("Not a valid input");
			}
		}
		
		return attemptedInput.charAt(0);
	}
	public static void useInput(char input)
	{
		Player.iterate_heal_counter();
		if (input == 'w')
		{
			if (checkMovement(0,-1)) //if movement succeeds, it's -1 because moving upwards is moving closer to the origin
			{
				characterRow--;
			}
		}
		else if (input == 'a')
		{
			if (checkMovement(-1,0)) //if movement succeeds, -1 because moving left is closer to the origin
			{
				characterColumn--;
			}
		}
		else if (input == 's')
		{
			if (checkMovement(0,1))
			{
				characterRow++;
			}
		}
		else if (input == 'd')
		{
			if (checkMovement(1,0))
			{
				characterColumn++;
			}
		}
		else if (input == '<')
		{

			if (currentMap[characterRow][characterColumn].get_character() == '<')
			{
				if (currentFloor == 0)
				{
					System.out.println("Really leave the dungeon? y/n");
					String answer = Keyboard.nextLine();
					if (answer.charAt(0) == 'y')
					{
						System.out.println("You fled the dungeon");
						System.exit(0);
					}
				}
				currentFloor--;
				storeCurrentMap();
				char[][] oldUnformattedMap = getOldUnformattedMap();
				currentMap = convertOldUnformattedMap(oldUnformattedMap);
			}
			else
			{
				System.out.println("No stairs to go up here");
			}
		}
		else if (input == '>')
		{
			if (currentFloor < deepestFloor)	//if we've gone backwards
			{
				char[][] oldMap = getOldUnformattedMap();
				currentMap = convertOldUnformattedMap(oldMap);
				currentFloor++;
			}
			else
			{
				storeCurrentMap();
				currentMap = getNewMap();
				currentFloor++;
				deepestFloor++;
			}
		}
		else if (input == 'i')
		{
			insideInventory();
		}
		else if (input == 'g')
		{
			currentMap[characterRow][characterColumn].grab_gold(Player);
			Player.grab_item(currentMap[characterRow][characterColumn].get_item());
		}
	}
	public static void insideInventory()
	{
		String input = "p";
		while (input.charAt(0) != 'q')
		{
			Player.get_equipment_info();
			Player.get_inventory();
			System.out.println("Q exits the inventory, e lets you equip something, d drops an item,\ni gets info about an item");
			input = Keyboard.nextLine();
			switch(input.charAt(0))
			{
				case 'e':
					System.out.println("Input the index you would like to equip");
					try
					{
						input = Keyboard.nextLine();
						Player.equip(Integer.parseInt(input));
					}
					catch(Exception e)
					{
						System.out.println("That's not an index");
					}
					break;
				case 'i':
					System.out.println("Input the index you would like info on");
					try
					{
						input = Keyboard.nextLine();
						Player.get_item_info(Integer.parseInt(input));
						System.out.println("");
					}
					catch(Exception e)
					{
						System.out.println("That's not an index");
					}
					break;
				case 'd':
					if (!currentMap[characterRow][characterColumn].check_item())
					{
						System.out.println("Input the index you would like to drop");
						try
						{
							input = Keyboard.nextLine();
							currentMap[characterRow][characterColumn].place_item(Player.drop_item(Integer.parseInt(input)));
						}
						catch(Exception e)
						{
							System.out.println("That's not an index");
						}
					}
					else
					{
						System.out.println("There is already an item on this tile");
					}
					break;
				default:
					System.out.println("That does not correspond to any valid inputs");
				case 'q':
					break;
			}
		}
	}
	public static boolean checkMovement(int horizontalMovement, int verticalMovement)
	{
		int destinationX = characterColumn + horizontalMovement;
		int destinationY = characterRow + verticalMovement;
		if ( (currentMap[destinationY][destinationX].is_passable()) && (!currentMap[destinationY][destinationX].hasMonster()) )
		{
			return true;
		}
		else if (currentMap[destinationY][destinationX].hasMonster())
		{
			for (int currentMonster = 0; currentMonster < monsters.length; currentMonster++)	//look for which monster it is
			{
				if (monsters[currentMonster].checkPosition(destinationY, destinationX))
				{
					monsters[currentMonster].receive_attack(Player.give_attack());
					currentMonster = monsters.length;
				}
			}
			return false;
		}
		return false;
	}
	
}
