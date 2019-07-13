package roguelike;

import java.util.Random;
import java.util.Scanner;

public class enemy
{
	Random rng = new Random();
	String monster_name;
	int health_current = 0;
	int health_max = 0;
	int maximum_damage = 0;
	int minimum_damage = 0;
	int armor = 0;
	int x;
	int y;
	
	public enemy(String name, int hp, int max_dmg, int min_dmg, int dmg_reduc)
	{
		monster_name = name;
		health_current = hp;
		health_max = hp;
		maximum_damage = max_dmg;
		minimum_damage = min_dmg;
		armor = dmg_reduc;

	}
	public enemy(double debug)
	{
		monster_name = "bug of d's";
		health_current = 999;
		health_max = 999;
		maximum_damage = 40;
		minimum_damage = 10;
		armor = 10;
	}
	
//[getter]	
	public int get_max_dmg()
	{
		return maximum_damage;
	}
	public int get_min_dmg()
	{
		return minimum_damage;
	}
	public String get_name()
	{
		return monster_name;
	}
	public int get_hp()
	{
		return health_current;
	}
	public void receive_attack(int damage)
	{
		health_current = health_current - damage;
	}
	public boolean is_dead()
	{
		if (health_current <= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public boolean checkPosition(int x, int y)
	{
		if ( (this.x == x) && (this.y == y) )
		{
			return true;
		}
		return false;
	}
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public int getExperienceValue()
	{
		return health_max * maximum_damage * armor;
	}
	
//[combat]
	public int attack()
	{
		int result = rng.nextInt(100);
		if (result >= 98)
		{
			int damage = rng.nextInt(maximum_damage*2 - minimum_damage*2 + 1) + minimum_damage*2;
			System.out.println(monster_name + " CRITS for " + damage);
			return damage;
		}
		else if (result >= 33)
		{
			int damage = rng.nextInt(maximum_damage - minimum_damage + 1) + minimum_damage;
			System.out.println(monster_name + " strikes for " + damage);
			return  damage;
		}
		else
		{
			System.out.println(monster_name + " has missed");
			return 0;
		}
	}
	
}
