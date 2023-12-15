package facilities;

import facilities.buildings.AbstractBuilding;

public class Facility/* implements Comparable<Facility>*/
{
  private String name;

  public Facility(String name)
  {
    this.name = name;
  }

  public String getName() { return this.name; }

/*

  *//**
   * a nasty workaround to allow sorting for Facility objects inside the facilities Arraylist of Estate class.
   * since the known facilities are just Hall, Lab and Theatre, (which are all descendants of AbstractBuildingClass),
   * effectively treat them as AbstractBuildingClass object
   *//*
  @Override
  public int compareTo(Facility other)
  {
    if (other instanceof AbstractBuilding)
    {
      AbstractBuilding ab = (AbstractBuilding) other;
      if (this instanceof AbstractBuilding)
      {
        return ((AbstractBuilding)this).getCapacity() - ab.getCapacity();
      }
    }

    return 0;
  }*/
}
