package facilities.buildings;


/**
 * facilities providing accommodation for students.
 */
public class Hall extends AbstractBuilding
{

  /**
   * use the superclass constructor to set max level, base capacity, and base count.
   * @param name
   */
  public Hall(String name)
  {
    super(name, 4, 6, 100);
  }
}
