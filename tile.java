package roguelike;

public class tile 
{
	String name;
	boolean passable;
	char icon;
	item item_on_ground = null;
	int gold_on_tile = 0;
	boolean monster;
	int x;
	int y;
	
	public tile(boolean given_passability, char given_icon, String given_name, int x, int y)
	{
		passable = given_passability;
		icon = given_icon;
		name = given_name;
		monster = false;
		this.x = x;
		this.y = y;
	}
	
//[getter]
	public char get_character()
	{
		if (monster)
		{
			return 'M';
		}
		else if (item_on_ground != null)
		{
			return 'i';
		}
		else if (gold_on_tile != 0)
		{
			return 'g';
		}
		else
		{
			return icon;
		}
	}
	public String toString()
	{
		return "" + icon;
	}
	public boolean is_passable()
	{
		return passable;	//if tile is not passable we say so
	}
	public boolean hasMonster()
	{
		return monster;
	}
	public void toggleMonster()
	{
		monster = !monster;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
//[gold]	
	public void place_gold(int gold_dropped)
	{
		gold_on_tile+=gold_dropped;
	}
	public void grab_gold(character Player)
	{
		Player.change_current_gold(gold_on_tile);
		System.out.printf("Grabbed %d gold\n", gold_on_tile);
		gold_on_tile = 0;
	}
	
	
//[item]	
	public boolean place_item(item item_placed)
	{
		if (item_on_ground == null)
		{
			item_on_ground = item_placed;
			return true;
		}
		else
			return false;
	}
	public boolean check_item()
	{
		if (item_on_ground == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public item get_item()
	{
		if (item_on_ground != null)
		{
			item item_to_return = item_on_ground;
			item_on_ground = null;
			System.out.println("Picked up a " + item_to_return.get_item_name());
			return item_to_return;
		}
		else
		{
			System.out.println("No item to pick up");
			return null;
		}
	}
}