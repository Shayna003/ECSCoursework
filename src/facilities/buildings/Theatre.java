package facilities.buildings;
import facilities.Facility;

/**
 * facilities for lectures, required for students to learn theoretical knowledge.
 */
public class Theatre extends Facility implements Building
{
  int level = 1;

  public Theatre(String name)
  {
    super(name);
  }

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
  }
}
