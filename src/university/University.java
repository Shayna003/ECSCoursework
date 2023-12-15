package university;

import facilities.Facility;
import facilities.buildings.AbstractBuilding;
import facilities.buildings.Building;
import facilities.buildings.CannotUpgradeException;

/**
 * The University class (within the university package) keeps information about the university.
 */
public class University
{
  //The current budget (in EcsCoins) of the University.
  private float budget; // since it's in ECSCoins it doesn't have to stick to 2 decimal places. Could be valuable like bitcoin or something

  //An instance of the Estate class for managing the university’s facilities.
  private Estate estate;

  // manages staff
  private HumanResource humanResource;

  //The current reputation of the University.
  private int reputation;

  /**
   * @param funding initial budget
   */
  public University(int funding)
  {
    estate = new Estate();
    humanResource = new HumanResource();
    this.budget = funding;
    reputation = 0;
  }

  public HumanResource getHumanResource()
  {
    return humanResource;
  }

  public Estate getEstate()
  {
    return estate;
  }

  public void setBudget(float newValue)
  {
    if (newValue < 0)
    {
      // error!
      // can be triggered only if EcsSim started simulating with a starting budget of less than 604 (minimum needed to make university "run", i.e. profit
    }
    budget = newValue;
  }

  /**
   * the specs don't say if negative values are allowed, by it makes sense to be
   * notoriously bad somehow.
   * @param newValue
   */
  public void setReputation(int newValue)
  {
      if (newValue< 0)
      {
        // error, probably
      }
      reputation = newValue;
  }

  /**
   * Use the addFacility(String, String) method from the Estate class and deduct the cost of building the facility from budget.
   * For each building built, the university gains 100 reputation points.
   * In the case where no facility is built, no amount is deducted from the budget, and null should be returned.
   * @param type
   * @param name
   * @return
   */
  public Facility build(String type, String name)
  {
    Facility facility = estate.buildFacility(type, name, budget);
    if (facility == null) return null;
    budget -= ((AbstractBuilding)facility).getBase_cost();
    reputation += 100;
    return facility;
  }

  /**
   * This method upgrades the input building and deduct the cost of upgrading from the university’s budget.
   * The method should throw an exception in the cases where the building is not a part of the university or the building is already at the maximum level.
   * For each building upgrade, the university gains 50 reputation points.
   * if no budget is available for upgrade, return false - abort upgrade
   * @param building
   * @throws Exception
   */
  public boolean upgrade(Building building) throws Exception
  {
    Facility[] facilities = estate.getFacilities();
    for (Facility facility : facilities)
    {
      if (facility == building)
      {
        AbstractBuilding ab = (AbstractBuilding) facility;

        if (ab.getLevel() == ab.getMax_level())
        {
          throw new CannotUpgradeException(ab + " cannot be upgraded because it's at its max level.");
        }
        int cost = ab.getUpgradeCost();
        if (budget < cost) return false;
        budget -= cost;
        ab.increaseLevel();
        reputation += 50;
        return true;
      }
    }
    throw new CannotUpgradeException(building + " cannot be upgraded because it's not a part of the university.");
  }

  /**
   * @return the current university budget.
   */
  public float getBudget()
  {
    return budget;
  }

  /**
   * @return the current reputation of the university
   */
  public int getReputation()
  {
    return reputation;
  }
}
