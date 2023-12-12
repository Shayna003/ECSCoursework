package facilities.buildings;

import facilities.Facility;

/**
 * facilities for laboratory sessions, required for students to train their practical
 * skills.
 */
public class Lab  extends AbstractBuilding
{
  public Lab(String name)
  {
    super(name, 5, 5, 300);
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
  public int getCapacity() {
    return BuildingFactory.getCapacity(this);
  }*/
}
