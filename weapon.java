package roguelike;
import java.util.Random;
public class weapon
{
	Random rng = new Random();
	
	String name = "Unnamed weapon";
	int minimum_damage;
	int maximum_damage;
	int critical_threshold = 94;
	double critical_multiplier = 2;
	int fumble_threshold = 7;
	
	public weapon(int miniumum_damage, int maximum_damage, double crit_mult_deviance, int crit_chance_deviance, int fumble_chance_deviance)
	{
		this.minimum_damage = miniumum_damage;
		this.maximum_damage = maximum_damage;
		critical_multiplier = 2 + crit_mult_deviance;
		critical_threshold = 94 + crit_chance_deviance;
		fumble_threshold = 7 + fumble_chance_deviance;
		switch (rng.nextInt(6))
		{
			case 0:
				name = "sword";
				break;
			case 1:
				name = "quarterstaff";
				break;
			case 2:
				name = "mace";
				break;
			case 3:
				name = "spear";
				break;
			case 4:
				name = "axe";
				break;
			case 5:
				name = "halberd";
				break;
		}
	}
	
	public int attack(int base_attack_bonus)
	{
		int result = rng.nextInt(100) + base_attack_bonus;
		if (result >= critical_threshold)
		{
			int damage = (int) (rng.nextInt((int)(maximum_damage*critical_multiplier - minimum_damage*critical_multiplier + 1)) + minimum_damage*critical_multiplier);
			System.out.println("CRITICAL HIT for " + damage);
			return damage;
		}
		else if (result > fumble_threshold)
		{
			int damage = rng.nextInt(maximum_damage - minimum_damage + 1) + minimum_damage;
			System.out.println("Hit monster for " + damage);
			return damage;
		}
		else
		{
			System.out.println("You missed");
			return 0;
		}
	}
	
	public int get_max_dmg()
	{
		return maximum_damage;
	}
	public int get_min_dmg()
	{
		return minimum_damage;
	}
	public void give_name(String name_given)
	{
		name = name_given;
	}
	public String get_name()
	{
		return name;
	}
	public String toString()
	{
		return name + "\nDamage = " + minimum_damage + "-" + maximum_damage + 
					  "\nCrits on " + critical_threshold + "+  for " + critical_multiplier + "x damage\n" + 
					  "Miss on " + fumble_threshold + " and below";
	}
	
}
