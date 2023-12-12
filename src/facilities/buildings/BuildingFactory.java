package facilities.buildings;

import facilities.Facility;
import java.util.HashMap;

/**
 * Class to handle building and upgrading buildings. Knows the formula for building upgrade cost, and contains information about base building cost and
 */
public class BuildingFactory
{
  // a way to implement table 1 from the specs, by using a factory class to take care of building and upgrading buildings
  public static final HashMap<String, BuildingData> buildingDataRecords;

  static
  {
    buildingDataRecords = new HashMap<>();
    buildingDataRecords.put("Hall", new BuildingData(4, 6, 100));
    buildingDataRecords.put("Lab", new BuildingData(5, 5, 300));
    buildingDataRecords.put("Theatre", new BuildingData(6, 10, 200));
  }

  /**
   * upgradeCost = baseCost × (currentLevel + 1)
   * @return upgrade cost for a building
   */
  public static int getUpgradeCost(Building building)
  {
    BuildingData data = buildingDataRecords.get(building.getClass());
    if (building.getLevel() >= data.max_level) return -1;
    return data.base_cost * (building.getLevel() + 1);
  }

  public static int getMaxLevel(Building building)
  {
    BuildingData data = buildingDataRecords.get(building.getClass());
    return data.max_level;
  }

  /**
   * @return capacity of the specific building supplied according to the formula from specs
   */
  public static int getCapacity(Building building)
  {
    BuildingData data = buildingDataRecords.get(building.getClass());
    return (int) (data.base_capacity * Math.pow(2, building.getLevel() - 1));
  }

  static class BuildingData
  {
    int max_level;
    int base_capacity;
    int base_cost;

    public BuildingData(int max_level, int base_capacity, int base_cost)
    {
      this.max_level = max_level;
      this.base_capacity = base_capacity;
      this.base_cost = base_cost;
    }
  }
}
