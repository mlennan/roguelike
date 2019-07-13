package roguelike;
import java.util.Random;

public class armor 
{
	String name = "Unnamed armor";
	int damage_reduction;
	

	public armor(int damage_reduction)
	{
		this.damage_reduction = damage_reduction;
		Random rng = new Random();
		switch (rng.nextInt(6))
		{
			case 0:
				name = "cloth armor";
				break;
			case 1:
				name = "plate armor";
				break;
			case 2:
				name = "wooden armor";
				break;
			case 3:
				name = "paper armor";
				break;
			case 4:
				name = "half plate armor";
				break;
			case 5:
				name = "chainmail armor";
				break;
		}
	}
	public int get_damage_reduction()
	{
		return damage_reduction;
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
		return name + "\nDamage reduction = " + damage_reduction;
	}
	
}