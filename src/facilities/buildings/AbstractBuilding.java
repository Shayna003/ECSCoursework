package facilities.buildings;

import facilities.Facility;

/**
 * A workaround to have the same implementations for Lab, Theatre, Hall
 */
public class AbstractBuilding extends Facility implements Building/*, Comparable<AbstractBuilding>*/
{
  int level;
  int max_level;
  int base_capacity;
  int base_cost;

  public AbstractBuilding(String name, int max_level, int base_capacity, int base_cost)
  {
    super(name);
    this.level = 1;
    this.max_level = max_level;
    this.base_capacity = base_capacity;
    this.base_cost = base_cost;
  }

  @Override
  public int getLevel() {
    return level;
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

/*  @Override
  *//**
   * for sorting inside Facilities Arraylist of Estate class
   * compare using capacity
   *//*
  public int compareTo(AbstractBuilding o) {
    return getCapacity() - o.getCapacity();
  }*/
}
