package roguelike;

public class character 
{
	/*tags;
	 * [getter][setters][mutators][combat][fight][item][inventory][other]
	 * [experience][level]
	 * 
	 * 
	 */
	
	String character_name;
	int health_current;
	int health_max;
	int heal_counter_threshold;
	int heal_counter;
	weapon equipped_weapon;
  	armor equipped_armor;
	item[] inventory = new item[15];
	int gold;
	int current_xp;
	int next_level_xp;
	int current_level;
	int base_attack_bonus;
	int current_dungeon_floor;

	
	public character(String name)
	{
		character_name = name;
		health_max = 20;
		health_current = 20;
		equipped_weapon = new weapon(2, 5, 0, 0, 0);
		equipped_weapon.give_name("stick");
		equipped_armor = new armor(3);
		equipped_armor.give_name("clothes");
		
		heal_counter_threshold = 15;
		heal_counter = 0;
		gold = 0;
		current_xp = 0;
		next_level_xp = 100;
		current_level = 0;
		base_attack_bonus = 0;
		current_dungeon_floor = 0;
	}
	
	public character(int debug)
	{
		character_name = "Debug";
		health_current = 999;
		health_max = 999;
		equipped_weapon = new weapon(20, 99, 5, -15, -6);
		equipped_weapon.give_name("Amazing weapon");
		equipped_armor = new armor(25);
		equipped_armor.give_name("Awesome armor");
		
		heal_counter_threshold = 3;
		heal_counter = 0;
		gold = 999999;
		current_xp = 9999;
		next_level_xp = 100;
		current_level = 0;
		base_attack_bonus = 0;
		current_dungeon_floor = 0;
	}
	
//[getter]
	public String get_name()
	{
		return character_name;
	}
	public int get_hp()
	{
		return health_current;
	}
	public int get_hp_max()
	{
		return health_max;
	}
	public int get_current_xp()
	{
		return current_xp;
	}
	public int get_next_level_xp()
	{
		return next_level_xp;
	}
	public int get_max_dmg()
	{
		return equipped_weapon.get_max_dmg();
	}
	public int get_min_dmg()
	{
		return equipped_weapon.get_min_dmg();
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
	
//[setters][mutators]
	public void modify_max_health(int health_change)
	{
		if (health_change > 0) //if max health is increasing, heal by the increase
		{
			health_max += health_change;
			health_current += health_change;
		}
		else //else, if max health decreases, if new max is less than current hp, set current hp to new max
		{
			health_max += health_change;
			if (health_current > health_max)
			{
				health_current = health_max;
			}
		}
	}
	public void modify_current_health(int health_change)
	{													
		health_current+=health_change;
	}
	public void modify_name(String new_name) //changes the character's name
	{
		character_name = new_name;
	}
	public void change_current_gold(int gold_change)
	{
		gold+=gold_change;
	}
	public boolean spend_gold(int required_gold, item item_to_purchase)
	{
		if (required_gold > gold)	//if we don't have enough gold to buy the item
		{
			System.out.println("Insufficient gold");
			return false;		//do not remove item from store
		}
		else //we have enough gold to buy the item
		{
			System.out.println("Item bought");
			gold-=required_gold;
			grab_item(item_to_purchase);
			return true;		//remove item from store if limited quantity
		}
	}
	
//[combat][fight]
	public void receive_attack(int damage) //get smacked
	{
		if (damage > equipped_armor.get_damage_reduction()) //damage taken is greater than damage reduction
		{
			System.out.println(damage + " attack received, " + (damage - equipped_armor.get_damage_reduction()) + " damage received, " + equipped_armor.get_damage_reduction() + " blocked");
			health_current = health_current - damage + equipped_armor.get_damage_reduction();
		}
		else //damage reduction is greater than damage taken
		{
			System.out.println("All " + damage + " blocked by armor");
		}
	}
	public int give_attack()
	{
		return equipped_weapon.attack(base_attack_bonus);
	}
	public void heal(int heal_for)
	{
		health_current+=heal_for;
		if (health_current > health_max)
		{
			health_current = health_max;
		}
	}
	
	
//[item][inventory]
	public void get_inventory() //printing the inventory
	{
		if (inventory[0] != null)
		{
			System.out.println("Items in inventory:");
			for (int inventory_slot = 0; inventory_slot < 15; inventory_slot++)
			{
				if (inventory[inventory_slot] != null)
				{
					System.out.println(inventory_slot+1 + ") " + inventory[inventory_slot].get_item_name());
				}
				else
				{
					inventory_slot = 999;
				}
			}
		}
		else
		{
			System.out.println("No items in inventory");
		}
		/* should look like
		 * Items in inventory:
		 * 1) sword
		 * 2) healing potion
		 * etc
		 */
		
	}
	public void get_item_info(int inventory_number)
	{
		if ( (inventory_number < 0) || (inventory_number > inventory.length-1) )
		{
			System.out.println("Number does not correspond to inventory slots");
		}
		else
		{
			System.out.println(inventory[inventory_number-1].toString());
		}
	}
	public boolean grab_item(item looted_item)		//return true = item picked up
	{												//return false = item not picked up

		for (int item_number_to_check = 0; item_number_to_check < inventory.length - 1; item_number_to_check++)
		{
			if (inventory[item_number_to_check] == null)
			{
				inventory[item_number_to_check] = looted_item;
				return true;
			}
		}
		return false;
	}
	public item drop_item(int inventory_number)
	{
		if ( (inventory_number < 0) || (inventory_number > inventory.length - 1) )
		{
			System.out.println("Number does not correspond to inventory slots");
			return null;
		}
		else
		{
			item itemToReturn = inventory[inventory_number - 1];
			inventory[inventory_number - 1] = null;
			for (int inventory_slot = inventory_number; inventory_slot < inventory.length-1; inventory_slot++)
			{//this loop is for moving items so that the inventory doesn't go 1, 2, 3, 8
				inventory[inventory_slot] = inventory[inventory_slot+1];
				if (inventory_slot == inventory.length-2)
				{
					inventory[inventory.length-1] = null;
				}
			}
			return itemToReturn;
		}
	}
	public void equip(int inventory_number)
	{
		inventory_number--;
		if (inventory[inventory_number].return_weapon() != null)
		{
			weapon initial_weapon = equipped_weapon;
			equipped_weapon = inventory[inventory_number].return_weapon();
			inventory[inventory_number] = new item(initial_weapon);
		}
		else if(inventory[inventory_number].return_armor() != null)
		{
			armor initial_armor = equipped_armor;
			equipped_armor = inventory[inventory_number].return_armor();
			inventory[inventory_number] = new item(initial_armor);
		}
		else
		{
			System.out.println("That is not an equippable item");
		}
	}
	public void get_equipment_info() //prints the info of the equipped weapon and armor
	{
		System.out.println("Weapon: \n" + equipped_weapon);
		System.out.println("Armor: \n" + equipped_armor);
	}
	public String get_item_name(int items_inventory_slot)
	{
		return inventory[items_inventory_slot].get_item_name();
	}

	
//[other]
	public void iterate_heal_counter() //every so often increment hp
	{
		if (health_current < health_max)
		{
			heal_counter = (heal_counter + 1) % heal_counter_threshold;
			if (heal_counter == 0)
			{
				health_current++;
			}
		}
	}
	
//[experience][level]
	public void add_experience(int gained_exp)
	{
		current_xp += gained_exp;
		if (current_xp >= next_level_xp)
		{
			System.out.print("\n\nLevel up!\n\tLevel " + (current_level+1) + " stat increases:\n\tHealth: " + get_hp_max() + "---");
			modify_max_health(current_level + 5);
			System.out.println((current_level + 5) + "-->" + get_hp_max());
			current_level++;
			current_xp = current_xp - next_level_xp;
			next_level_xp *= 1.15;
			if (current_level%3 == 0)
			{
				System.out.println("\tBase attack bonus: " + base_attack_bonus++ + "-->" + base_attack_bonus);//[isokay], using postfix inside of a print statement
			}
			if (current_xp >= next_level_xp)
			{
				add_experience(0);
			}
			else
			{
				System.out.println("Experience to level " + (current_level+1) + ": " + current_xp + " of " + next_level_xp + " needed");
			}
		}
	}
}
