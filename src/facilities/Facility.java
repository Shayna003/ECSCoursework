package facilities;

/**
 * Meant to include stuff like gardens according to the designer of this coursework
 * in reality this class' coexistence with Building interface just makes things more complicated.
 */
public class Facility
{
  private String name;

  public Facility(String name)
  {
    this.name = name;
  }

  public String getName() { return this.name; }
}
