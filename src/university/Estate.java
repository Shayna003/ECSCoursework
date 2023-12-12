package university;

import facilities.Facility;
import facilities.buildings.AbstractBuilding;
import facilities.buildings.Hall;
import facilities.buildings.Lab;
import facilities.buildings.Theatre;
import java.util.ArrayList;
import java.util.Collections;

public class Estate
{
  ArrayList<Facility> facilities;
  //int number_of_students;
  //float maintenance_cost;

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
   * traverse the entire arraylist to find facility with mininum capacity
   */
/*  private void update_number_of_students()
  {
    int min = Integer.MAX_VALUE;
    for (Facility f : facilities)
    {
      int c = ((AbstractBuilding)f).getCapacity();
      if (c < min) min = c;
    }
    //number_of_students = min;
  }*/

  /**
   * Construct a new facility. The new facility is specified by its type, e.g., "Hall", "Lab", "Theatre", and its name.
   * The new facility should be added to the facilities list. In the case where the type is not known, null is returned.
   */
  public Facility addFacility(String type, String name)
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

/*    if (facilities.size() == 0)
    {
      number_of_students = facility.getCapacity();
    }
    else
    {
      number_of_students = Math.min(number_of_students, facility.getCapacity());
    }*/

    facilities.add(facility);
    //maintenance_cost += facility.getCapacity() * 0.1f;

    //int index = Collections.binarySearch(facilities, facility);
    //(-(insertion point) - 1
    //if (index >= 0) facilities.add(index, facility);
    //else facilities.add(-index - 1, facility);

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
    //return maintenance_cost;
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

    //int students = Integer.MAX_VALUE;
    for (Facility f : facilities)
    {
      int capacity = ((AbstractBuilding) f).getCapacity();

      if (f instanceof Hall) halls += capacity;
      else if (f instanceof Lab) labs += capacity;
      else if (f instanceof Theatre) theatres += capacity;
    }

    return Math.min(Math.min(halls, labs), theatres);
    //return students;

    //return number_of_students;
    //((AbstractBuilding)facilities.get(0)).getCapacity();
  }
}
