package facilities.buildings;
import facilities.Facility;

/**
 * facilities for lectures, required for students to learn theoretical knowledge.
 */
public class Theatre extends AbstractBuilding implements Building
{
  public Theatre(String name)
  {
    super(name, 6, 10, 200);
  }
/*
  @Override
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
