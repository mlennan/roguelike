package roguelike;

import java.util.Random;

public class consumable_damage 
{
	Random rng = new Random();
	
	String name;
	int minimum_damage;
	int maximum_damage;
	int critical_threshold;
	
	public consumable_damage(int minimum_damage, int maximum_damage, int critical_threshold)
	{
		name = "Stabby dagger";
		this.minimum_damage = minimum_damage;
		this.maximum_damage = maximum_damage;
		this.critical_threshold = 95 - critical_threshold;
	}
	
	public int use_consumable()
	{
		if (rng.nextInt(100) >= critical_threshold)
		{
			int damage = (int) (rng.nextInt((int)(maximum_damage*2.5 - minimum_damage*2.5 + 1)) + minimum_damage*2.5);
			System.out.println("CRITICAL HIT for " + damage);
			return damage;
		}
		else
		{
			int damage = rng.nextInt(maximum_damage - minimum_damage + 1) + minimum_damage;
			System.out.println("Hit monster for " + damage);
			return damage;
		}
	}
	
	public String toString()
	{
		return name + ";\ndamage:" + minimum_damage + "-" + maximum_damage + "\n crits on " + critical_threshold + "+";
	}
	public String get_name()
	{
		return name;
	}
}