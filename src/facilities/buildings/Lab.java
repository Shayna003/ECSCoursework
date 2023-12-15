package facilities.buildings;

/**
 * facilities for laboratory sessions, required for students to train their practical
 * skills.
 */
public class Lab  extends AbstractBuilding
{
  /**
   * use the superclass constructor to set max level, base capacity, and base count.
   * @param name
   */
  public Lab(String name)
  {
    super(name, 5, 5, 300);
  }
}
