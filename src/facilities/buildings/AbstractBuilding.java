package facilities.buildings;

import facilities.Facility;

/**
 * A workaround to have the same implementations for Lab, Theatre, Hall
 * also a way to fix the class model in the specification where sometimes Facility is used and sometimes Building is used.
 * You can use force conversion to convert between AbstractBuilding, Facility, Building easily than if Labs/Theatres etc directly interit from Facility
 * and implement building.
 * It's not "abstract" because I want to use a static variable to generate uniqueIDs.
 * Due to actual usage, being abstract or not does not really change program behaviour.
 */
public class AbstractBuilding extends Facility implements Building, Comparable<AbstractBuilding>
{
  private int level;
  private int max_level;
  private int base_capacity;
  private int base_cost;
  private static int IDCounter; // static and incremented to ensure unique IDs

  // a unique ID given to each building, to help differentiate them
  // (even though they are guarantee to have unique names based on year number, and that only 1 building can possibly be built each year)
  private int ID;

  public AbstractBuilding(String name, int max_level, int base_capacity, int base_cost)
  {
    super(name);
    this.level = 1;
    this.max_level = max_level;
    this.base_capacity = base_capacity;
    this.base_cost = base_cost;
    this.ID = ++IDCounter; // increment IDCounter to ensure unique IDs
  }

  @Override
  public int getLevel()
  {
    return level;
  }

  public int getBase_cost()
  {
    return base_cost;
  }

  public int getBase_capacity()
  {
    return base_capacity;
  }

  public int getMax_level()
  {
    return max_level;
  }

  /**
   * simulates an "upgrade". Not called directly by EcsSim
   */
  @Override
  public void increaseLevel()
  {
    if (level >= max_level) return;
    level++;
  }

  @Override
  public int getUpgradeCost() {
    if (level >= max_level) return -1;
    return base_cost * (level + 1);
  }

  @Override
  public int getCapacity() {
    return (int) (base_capacity * Math.pow(2, level - 1));
  }

  /**
   * for easier debugging, and useful print information
   */
  @Override
  public String toString()
  {
    return this.getClass().getSimpleName() + "[name=" + getName() + "," + "level=" + level + ",ID=" + ID + "]";
  }

  @Override
  /**
   * for use in priority queues
   * compare using capacity, to find the most suitable building to upgrade.
   */
  public int compareTo(AbstractBuilding other)
  {
    return other.getCapacity() - getCapacity();
  }
}
