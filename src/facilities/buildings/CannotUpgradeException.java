package facilities.buildings;

/**
 * error thrown when trying to upgrade a building if it's at the maximum level or the building is not part of the unviersity's facilities.
 * might be thrown if there is not enough budget to do the upgrade
 */
public class CannotUpgradeException extends Exception
{
  public CannotUpgradeException(String message)
  {
    super(message);
  }
}
