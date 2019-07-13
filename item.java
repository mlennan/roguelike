package roguelike;

public class item 
{
	weapon stored_weapon = null;
	armor stored_armor = null;
	consumable_healing stored_consumable_healing = null;
	consumable_damage stored_consumable_damage = null;
	
	
	public item(weapon looted_weapon)
	{
		stored_weapon = looted_weapon;
	}
	public item(armor looted_armor)
	{
		stored_armor = looted_armor;
	}
	public item(consumable_healing looted_consumable_healing)
	{
		stored_consumable_healing = looted_consumable_healing;
	}
	public item(consumable_damage looted_consumable_damage)
	{
		stored_consumable_damage = looted_consumable_damage;
	}
	
	public String get_item_name()
	{
		if (stored_weapon != null)
		{
			return stored_weapon.get_name();
		}
		else if (stored_armor != null)
		{
			return stored_armor.get_name();
		}
		else if (stored_consumable_healing != null)
		{
			return stored_consumable_healing.get_name();
		}
		else if (stored_consumable_damage != null)
		{
			return stored_consumable_damage.get_name();
		}
		else
			return "Empty slot";
	}
	
	public String toString()
	{
		if (stored_weapon != null)
		{
			return stored_weapon.toString();
		}
		else if (stored_armor != null)
		{
			return stored_armor.toString();
		}
		else if (stored_consumable_healing != null)
		{
			return stored_consumable_healing.toString();
		}
		else if (stored_consumable_damage != null)
		{
			return stored_consumable_damage.toString();
		}
		else
			return "Empty slot";
	}
	
	public weapon return_weapon()
	{
		return stored_weapon;
	}
	public armor return_armor()
	{
		return stored_armor;
	}
	public consumable_healing return_consumable_healing()
	{
		return stored_consumable_healing;
	}
	public consumable_damage return_consumable_damage()
	{
		return stored_consumable_damage;
	}
}
