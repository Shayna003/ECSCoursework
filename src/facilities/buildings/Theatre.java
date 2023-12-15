package facilities.buildings;

/**
 * facilities for lectures, required for students to learn theoretical knowledge.
 */
public class Theatre extends AbstractBuilding
{
  /**
   * use the superclass constructor to set max level, base capacity, and base count.
   * @param name
   */
  public Theatre(String name)
  {
    super(name, 6, 10, 200);
  }
}
