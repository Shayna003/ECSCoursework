package university;

import facilities.Facility;
import facilities.buildings.Hall;
import facilities.buildings.Lab;
import facilities.buildings.Theatre;
import java.util.ArrayList;

public class Estate
{
  ArrayList<Facility> facilities;

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
   * Construct a new facility. The new facility is specified by its type, e.g., "Hall", "Lab", "Theatre", and its name.
   * The new facility should be added to the facilities list. In the case where the type is not known, null is returned.
   */
  public Facility addFacility(String type, String name)
  {
    Facility facility;
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
    facilities.add(facility);
    return facility;
  }

  /**
   * Return the yearly maintenance course of the facilities.
   * In particular, for a building, the cost of maintenance (in ECScoins) is equivalent to 10% of the building’s capacity.
   */
  public float getMaintenanceCost()
  {

  }

  /**
   * Return the number of students. The number is the minimal of the total capacity of the halls,
   * the total capacity of the labs, and the total capacity of the theatres.
   */
  public int getNumberOfStudents()
  {

  }
}
