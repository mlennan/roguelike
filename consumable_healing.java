package roguelike;

import java.util.Random;

public class consumable_healing
{
	Random rng = new Random();
	
	String name;
	int minimum_healing;
	int maximum_healing;
	
	public consumable_healing(int min_heal, int max_heal)
	{
		name = "Healing potion";
		minimum_healing = min_heal;
		maximum_healing = max_heal;
	}

	
	public int use_healing_consumable()
	{
		return rng.nextInt(maximum_healing - minimum_healing + 1) + minimum_healing;
	}
	
	public String get_name()
	{
		return name;
	}
	public String toString()
	{
		return name + ":\nHeals for " + minimum_healing + "-" + maximum_healing;
	}
	
}