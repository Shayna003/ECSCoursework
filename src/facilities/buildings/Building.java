package facilities.buildings;

/**
 * The Building interface is common for all types of buildings.
 * Each building can be at a certain level (starting from Level 1). Further-more, each type of buildings, e.g., Hall, Lab or Theatre, will have a maximum level that cannot be exceeded (see Table 1).
 */
public interface Building
{

  /**
   * @return the current level of the building
   */
  int getLevel();

  /**
   * increase the level of the build-ing. The assumption is that this method can only be called when the building can be upgraded (i.e., it is not yet at its maximum level).
   */
  void increaseLevel();

  /**
   * @return the cost for upgrading the building to the next level. This cost depends on the type of the building and the current building level. Return -1 if the building cannot be upgraded (i.e., it is already at its maximum level).
   */
  int getUpgradeCost();


  /**
   * @return the current capacity of the building. This depends on the type of the building and the current building level.
   */
  int getCapacity();
}
