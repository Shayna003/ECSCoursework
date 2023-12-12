package facilities.buildings;

import facilities.Facility;

/**
 * A workaround to have the same implementations for Lab, Theatre, Hall
 */
public class AbstractBuilding extends Facility implements Building/*, Comparable<AbstractBuilding>*/
{
  private int level;
  private int max_level;
  private int base_capacity;
  private int base_cost;

  public AbstractBuilding(String name, int max_level, int base_capacity, int base_cost)
  {
    super(name);
    this.level = 1;
    this.max_level = max_level;
    this.base_capacity = base_capacity;
    this.base_cost = base_cost;
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
   * for easier debugging
   */
  @Override
  public String toString()
  {
    return this.getClass().getSimpleName() + "[name=" + getName() + "," + "level=" + level + "]";
  }

/*  @Override
  *//**
   * for sorting inside Facilities Arraylist of Estate class
   * compare using capacity
   *//*
  public int compareTo(AbstractBuilding o) {
    return getCapacity() - o.getCapacity();
  }*/
}
