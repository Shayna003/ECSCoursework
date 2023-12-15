package university;

import facilities.Facility;
import facilities.buildings.AbstractBuilding;
import facilities.buildings.Building;
import facilities.buildings.Hall;
import facilities.buildings.Lab;
import facilities.buildings.Theatre;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Estate
{
  private ArrayList<Facility> facilities;
  public static final HashMap<String, BuildingData> buildingDataRecords;

  // a way to store the building data information, by mapping type string to their data
  // the mechanism can be improved but this is sufficient
  static
  {
    buildingDataRecords = new HashMap<>();
    buildingDataRecords.put("Hall", new BuildingData(4, 6, 100));
    buildingDataRecords.put("Lab", new BuildingData(5, 5, 300));
    buildingDataRecords.put("Theatre", new BuildingData(6, 10, 200));
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

  /**
   * called by the EcsSim
   * tries to find a building of the type with the maximum level, then do the upgrade on it
   * @return null if no building of such type is in facilities or if not enough funds to upgrade any/all are at max level,
   * or the building to perform the upgrade passed back to the caller (university)
   */
  public Building findBuildingToUpgrade(String type, float availableFunds)
  {
    if (availableFunds <= 0) return null;
    // prioritizes availableFunds for upgrading the building with the highest capacity, as graphs from excel showed me that
    // buildings with higher capacity/level tend to have higher capacity increase/upgrade cost ratio, thus more "lucrative".
    PriorityQueue<AbstractBuilding> pq = new PriorityQueue<>();
    for (Facility facility : facilities)
    {
     if (facility.getClass().getSimpleName().equals(type)) // easier mapping from string type name to class
     {
       pq.add((AbstractBuilding) facility);
     }
    }

    if (pq.isEmpty())
    {
      return null;
    }
    AbstractBuilding ab;
    do
    {
      ab = pq.poll();
      if (ab.getUpgradeCost() <= availableFunds && ab.getLevel() < ab.getMax_level())
      {
        return ab;
      }
    }
    while(!pq.isEmpty());
    return null;
  }

  public BuildingData getBuildingData(String type)
  {
    return buildingDataRecords.get(type);
  }

  public Estate()
  {
    facilities = new ArrayList<>();
  }

  /**
   * @return an array of facilities currently maintained by the estate.
   */
  public Facility[] getFacilities()
  {
    return facilities.toArray(new Facility[0]);
  }

  /**
   * Maps a String type to a concrete class
   * Does an additional check for budget deficit
   * and adds to the facility arraylist
   * @return a Facility/Building/AbstractBuilding whatever! denoted by the String type
   */
  public Facility buildFacility(String type, String name, float budget)
  {
    if (budget <= 0) return null;
    BuildingData data = getBuildingData(type);
    if (data == null || data.base_cost > budget) return null;

    return addFacility(type, name);
  }

  /**
   * Simple maps a String type to an object of a concrete class
   * does not add to the facilities arraylist
   * @return a Facility/Building/AbstractBuilding whatever! denoted by the String type
   */
  public Facility buildFacility(String type, String name)
  {
    AbstractBuilding facility;
    if (type.equals("Hall"))
    {
      facility = new Hall(name);
    }
    else if (type.equals("Lab"))
    {
      facility = new Lab(name);
    }
    else if (type.equals("Theatre"))
    {
      facility = new Theatre(name);
    }
    else
    {
      return null;
    }
    return facility;
  }

  /**
   * Construct a new facility. The new facility is specified by its type, e.g., "Hall", "Lab", "Theatre", and its name.
   * The new facility should be added to the facilities list. In the case where the type is not known, null is returned.
   */
  public Facility addFacility(String type, String name)
  {
    Facility facility = buildFacility(type, name);
    if (facility == null) return null;
    facilities.add(facility);

    return facility;
  }

  /**
   * Return the yearly maintenance course of the facilities.
   * In particular, for a building, the cost of maintenance (in ECScoins) is equivalent to 10% of the building’s capacity.
   */
  public float getMaintenanceCost()
  {
    float cost = 0;
    for (Facility f : facilities)
    {
      cost += ((AbstractBuilding)f).getCapacity() * 0.1f;
    }
    return cost;
  }

  // to lazy for getters setters
  // for recording the number of each type of building, and the total capacity of each
  public int hallsCapacity;
  public int labsCapacity;
  public int theatresCapacity;
  public int capacity;

  public int halls;
  public int labs;
  public int theatres;

  public int getNumberOfFacilities()
  {
    return facilities.size();
  }

  /**
   * figure out the capacities of each facility. Called by EcsSim.
   * @return string representing the type with the least capacity.
   */
  public String updateCapacities()
  {
    hallsCapacity = 0;
    labsCapacity = 0;
    theatresCapacity = 0;
    capacity = 0;

    halls = 0; labs = 0;theatres = 0;
    String least = null; // in theory null would never be returned so it doesn't matter

    for (Facility f : facilities)
    {
      int capacity = ((AbstractBuilding) f).getCapacity();

      if (f instanceof Hall)
      {
        halls++;
        hallsCapacity += capacity;
      }
      else if (f instanceof Lab)
      {
        labs++;
        labsCapacity += capacity;
      }
      else if (f instanceof Theatre)
      {
        theatres++;
        theatresCapacity += capacity;
      }
    }

    // this default hall > theatre > lab order helps with deciding which building to prioritize building
    if (hallsCapacity <= labsCapacity && hallsCapacity <= theatresCapacity)
    {
      least = "Hall";
    }
    else if (theatresCapacity <= labsCapacity && theatresCapacity <= hallsCapacity)
    {
      least = "Theatre";
    }
    else if (labsCapacity <= hallsCapacity && labsCapacity <= theatresCapacity)
    {
      least = "Lab";
    }
    capacity = Math.min(Math.min(hallsCapacity, labsCapacity), theatresCapacity);
    return least;
  }

  /**
   * Return the number of students. The number is the minimal of the total capacity of the halls,
   * the total capacity of the labs, and the total capacity of the theatres.
   */
  public int getNumberOfStudents()
  {
    if (facilities.size() == 0) return 0;
    int halls = 0;
    int labs = 0;
    int theatres = 0;

    for (Facility f : facilities)
    {
      int capacity = ((AbstractBuilding) f).getCapacity();

      if (f instanceof Hall) halls += capacity;
      else if (f instanceof Lab) labs += capacity;
      else if (f instanceof Theatre) theatres += capacity;
    }

    return Math.min(Math.min(halls, labs), theatres);
  }
}
