package facilities.buildings;

import facilities.Facility;

/**
 * facilities providing accommodation for students.
 */
public class Hall extends AbstractBuilding
{
  public Hall(String name)
  {
    super(name, 4, 6, 100);
  }

/*  @Override
  public int getLevel() {
    return level;
  }

  @Override
  public void increaseLevel()
  {
    if (level >= BuildingFactory.getMaxLevel(this))
    {
      return;
    }
    level++;
  }

  @Override
  public int getUpgradeCost() {
    return BuildingFactory.getUpgradeCost(this);
  }

  @Override
  public int getCapacity()
  {
    return BuildingFactory.getCapacity(this);
  }*/
}
