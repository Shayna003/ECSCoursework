import facilities.Facility;
import facilities.buildings.AbstractBuilding;
import facilities.buildings.Building;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import university.Estate;
import university.Staff;
import university.University;

import static java.lang.System.in;
import static java.lang.System.lineSeparator;
import static java.lang.System.out;

/**
 * TODO: extension, readme.txt, comments, tests, refactor
 */
public class EcsSim
{
  University university;
  ArrayList<Staff> staffMarket;
  int year;
  boolean printSimulation = true;

  /**
   * java EcsSim staff.txt 2000 50
   * @param args [0] is configuration file, [1] is starting EcsCoins, [2] is number of years to simulate
   */
  public static void main(String[] args)
  {
    File configFile = new File(args[0]);

    // defaults
    int startingCoins = 1000;
    int years = 100;

    try
    {
      startingCoins = Integer.parseInt(args[1]);
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }

    try
    {
      years = Integer.parseInt(args[2]);
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }

    EcsSim sim = new EcsSim(startingCoins);

    try
    {
      loadConfiguration(configFile, sim);
    }
    catch (Exception e)
    {
      System.out.println("loading configuration file failed. resorting to default staff market of 100 randomly skilled staff");
      e.printStackTrace();

      for (int i = 0; i < 100; i++)
      {
        sim.staffMarket.add(new Staff("staff" + i, (int)(Math.random() * 101))); // populate staff market with some initial staff for sim to run
      }
      sim.staffMarket.sort((a, b) -> b.getSkill() - a.getSkill());
    }

    System.out.println("start simulation, staffMarketSize=" + sim.staffMarket.size() + ", targetYears=" + years + ", startingEcsCoins=" + startingCoins);
    sim.simulate(years, true);
  }

  /**
   * Initializes the staff market of EcsSim sim according to the configuration file
   * @param configFile
   * @param sim
   * @throws Exception
   */
  public static void loadConfiguration(File configFile, EcsSim sim) throws Exception
  {
    try (BufferedReader reader = new BufferedReader(new FileReader(configFile)))
    {
      String line;
      while ((line = reader.readLine()) != null)
      {
        //line = line.strip();
        String[] expressions = line.split("\\(");
        String name = expressions[0].strip();
        int skill = Integer.parseInt(expressions[1].substring(0, expressions[1].length() - 1));
        sim.staffMarket.add(new Staff(name, skill));
      }
    }
    System.out.println(sim.staffMarket);
  }

  /**
   * testing code used for part 5
   */
  private void internalTesting()
  {
    EcsSim sim = new EcsSim();
    int marketSize = 100;
    int years = 100;
    int coins = 603;
    System.out.println("start simulation, staffMarketSize=" + marketSize + ", targetYears=" + years + ", startingEcsCoins=" + coins);

    for (int i = 0; i < marketSize; i++)
    {
      sim.staffMarket.add(new Staff("staff" + i, (int)(Math.random() * 101))); // populate staff market with some initial staff for sim to run
    }
    sim.staffMarket.sort((a, b) -> b.getSkill() - a.getSkill());

    sim.simulate(years, true);
  }

  {
    staffMarket = new ArrayList<>();
    year = 1;
  }
  public EcsSim()
  {
    university = new University(604); // initializes university with minimal initial funding to profit
  }

  public EcsSim(int funding)
  {
    university = new University(funding); // initializes university with initial funding
  }

  /**
   * arbitrary formula used to determine a good staff to student (or vice versa) ratio.
   * @return whether or not the current ratio is a good ratio. i.e, if true, no need to hire new staff
   */
  public boolean staffToStudentsRatioGood(int staffSize, int studentSize)
  {
    return staffSize * 20 > studentSize * 1.3;
  }

  boolean depletedMarket = false;

  /**
   * Method to simulate a year of running the university. Each year the following sequence of events happens.
   *
   1. At the beginning of the year:
   a) Build any additional facilities or upgrade some existing facilities for the university.
   must do something, and budget cannot drop below 0.
   b) Increase the budget depending on the number of students. Each student contributes 10 ECScoins to the budget.
   c) Hire any additional staff from the market to supervise the labs and teach the lecture. Removes any hired staff from the market. Cannot do nothing.

   2. During the year, allocate the staff to instruct the students.
   This contributes reputation points for the university.
   Each staff only allowed to instruct one group of students per year (i.e., call the instruct(int) method only once).

   3. At the end of the year:
   a) Pay the estate’s maintenance cost
   b) Pay the staff’s salary.
   c) Increase the years of teaching for all employed staff.
   d) For each uninstructed student, deduct 1 reputation point.
   e) For any employed staff, they can leave the university according to the following
   – If the staff has 30 years of teaching, they will leave the university
   – Otherwise, the chance that the staff stays is the staff’s stamina.
   f) Finally, for all remaining employed staff, replenish their stamina (stamina + 20).

   the strategy is that you need a minimum initial funding to be able to build any buildings, in order to be able to hire students.
   TODO: outputs for everything
   */
  public void simulate()
  {
    int reputationBefore = university.getReputation();
    if (printSimulation) out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~year " + year + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    float yearStartingBudget = university.getBudget();
    int yearStartingRepuation = university.getReputation();
    int yearStartingStudents = university.getEstate().getNumberOfStudents();
    int buildingsBuilt = 0;
    int buildingsUpgraded = 0;
    int startingStaff = university.getHumanResource().getStaffSize();
    int staffHired = 0;
    int staffLeft = 0;

    if (printSimulation) out.printf("starting budget=%.2f" + lineSeparator(), yearStartingBudget);
    if (printSimulation) out.println("Upgrading or buying new buildings: ");
    //1a TODO: build additional facilities or upgrade existing ones. cannot do nothing here
    // compare halls capacity, labs capacity, theatre capacity. Try upgrading a building of the building category with the least capacity,
    // if not build a new building of such category, if not then don't spend money at this stage for this year.
    String least = university.getEstate().updateCapacities();
    if (printSimulation) out.println("    targeting building type with least capacity: " + least);

    // to avoid negative budget at end of year
    float budgetBefore = university.getBudget();
    float buildingStageBudget = Math.min(university.getBudget(), university.getBudget() - university.getEstate().getMaintenanceCost() -university.getHumanResource().getTotalSalary() + university.getEstate().getNumberOfStudents() * 10);
    if (printSimulation) out.printf("    building stage budget: %.2f" + lineSeparator(), buildingStageBudget);

    boolean ratioGood = staffToStudentsRatioGood(startingStaff, yearStartingStudents);

    // do not expand if having a bad teacher to student ratio or have no staff while there is staff to hire. This leads to more negative reputation.
    // but build buildings if not enough were built to have number of students > 0
    //16000 - 21350

    // strangely enough, testing results show that not having this feature tends to lead to better average reputation of different simulations
    // I guess the reputation loss from not teaching is less than the potential gain from an expansionist strategy
    //35287 37169
    boolean skipExpansion = false;//yearStartingStudents > 0 && (startingStaff == 0 || !ratioGood) && staffMarket.size() > 0;
    if (skipExpansion)
    {
      if (printSimulation) out.println("    skipping expansion due to current bad staff to student ratio.");
    }
    else
    {
      Building toUpgrade = university.getEstate().findBuildingToUpgrade(least, buildingStageBudget);

      if (toUpgrade != null)
      {
        try
        {
          int cost = toUpgrade.getUpgradeCost();
          university.upgrade(toUpgrade); // perform upgrade on the most suitable building found by Estate if there is any.
          buildingsUpgraded++;
          if (printSimulation) out.println("    " + toUpgrade + " was upgraded. " + cost + " EcsCoins spent. reputation +50");
        }
        catch (Exception e) // the exception effectively cannot be triggered because of the extra checks involved
        {
          e.printStackTrace();
        }
      }
      else // try to build a new building
      {
        // if null, then did not build due to budget deficit.
        Facility newBuiltFacility = university.getEstate().buildFacility(least, least + "_built_in_year_" + year, buildingStageBudget);

        if (newBuiltFacility == null)
        {
          if (printSimulation) out.println("    No buildings were built or upgraded.");
        }
        else
        {
          buildingsBuilt++;
          university.setReputation(university.getReputation() + 100);
          int cost = ((AbstractBuilding)newBuiltFacility).getBase_cost();
          university.setBudget(university.getBudget() - cost);
          if (printSimulation) out.println("    " + newBuiltFacility + " was built. " + cost + " EcsCoins spent. reputation +100");
        }
      }
      int reputationAfter = university.getReputation();
      if (printSimulation)
      {
        out.println("    total increase in reputation: " + (reputationAfter-reputationBefore));
        out.printf("    total EcsCoins spent: %.2f" + lineSeparator(), (budgetBefore - university.getBudget()));
        out.printf("    remaining budget: %.2f" + lineSeparator(), university.getBudget());
      }
    }

    //1b increase budget by of number of students * 10
    university.setBudget(university.getBudget() + university.getEstate().getNumberOfStudents() * 10);
    if (printSimulation) out.printf("Budget increased by %d from students" + lineSeparator(), university.getEstate().getNumberOfStudents() * 10);

    //1c TODO: hire additional staff from the market to teach students. cannot do nothing here
    if (printSimulation) out.println("Hiring staff:");
    if (printSimulation) out.printf("    staff market size: %d" + lineSeparator(), staffMarket.size());


    int hireCount = 0; // might want to hire 1-3 staff per purchase/upgrade of a building, because there's a high chance for staff to leave even if they only teach 10 students with a skill of 100 (20%)
    // and considering the speed of students increasing by upgrading/purchasing new buildings.
    if (staffMarket.size() == 0 && !depletedMarket)
    {
      if (printSimulation) out.println("STAFF MARKET DEPLETED AT YEAR " + year + "! fate of university remains unknown");
      depletedMarket = true;
    }
    if (staffMarket.size() != 0) // only consider if there is staff to hire
    {
      // cannot end up with negative budget, so will need at least the amount of money to pay staff salary and
      float staffStageBudget = university.getBudget() - university.getEstate().getMaintenanceCost() - university.getHumanResource().getTotalSalary();
      if (printSimulation) out.printf("    staff stage budget: %.2f" + lineSeparator(), staffStageBudget);

      // if student-staff ratio is saturated, don't hire new staff
      int students = university.getEstate().getNumberOfStudents();
      int staffSize = university.getHumanResource().getStaffSize();
      if (staffToStudentsRatioGood(staffSize, students)) // arbitrary value here.
      {
        if (printSimulation)
        {
          out.println("    Student to staff ratio is good. No need to hire.");
        }
      }
      else if (university.getEstate().getNumberOfStudents() == 0 && university.getBudget() < 603.9f)
        // 603.9 is the bare minimum number of starting EcsCoins to start profiting (buying 1 of each building each year, and paying their maintenance costs.
      {
        if (printSimulation)
        {
          out.println("    Not hiring staff due to (potential) budget deficit.");
        }
      }
      else // attempt to hire staff
      {
        int studentSize = university.getEstate().getNumberOfStudents();
        // to prevent concurrentmodificationerror
        Iterator<Staff> staffIterator = staffMarket.iterator();//university.getHumanResource().getStaff();

        // commented out area: somehow the final reputation tend to be higher if you aggressively hire staff for the first part of your program and get as much reputation as possible, even if you run out of staff quicker this wqy
        while (staffIterator.hasNext() && staffStageBudget > 0 && hireCount < 3 /*&& !staffToStudentsRatioGood(university.getHumanResource().getStaffSize(), studentSize)*/) // hireCount really depends on the size of the staff market.
        {
          //13534, 69, 5, yes
          //17030, 76, 3, no
          //11699, 72, 3, yes
          Staff staff = staffIterator.next();//17948 19000, year of depletion 73, 77
          // prepare for the worst: compute theoretical salary by the highest value possible, 10.5% of skill
          float salary = staff.getSkill() * 0.105f;
          if (salary < staffStageBudget) // hire staff
          {
            if (printSimulation) out.printf("    " + staff + " got hired." + lineSeparator());
            university.getHumanResource().addStaff(staff);
            staffStageBudget = university.getBudget() - university.getEstate().getMaintenanceCost() - university.getHumanResource().getTotalSalary();
            staffIterator.remove();
            hireCount++;
            staffHired++;
          }
        }
      }
    }
    if (hireCount == 0)
    {
      if (printSimulation) out.println("    No staff were hired.");
    }

    //background task: set uninstructed students to number of students
    university.getHumanResource().setUninstructedStudents(university.getEstate().getNumberOfStudents());

    //2 TODO: let staff teach student. each staff can only teach once. this increases reputation
    if (printSimulation) out.println("Teaching students:");
    int total = university.getEstate().getNumberOfStudents();
    // since the maths make staff more like a commodity, might as well "spend" them without considering how long they stay at the university
    // but staff market is a thing that gets supplied(not controlled by program), which can vary... so just give up on university reputation to make things safe
    // will just make every staff teach the maximum number of students that make their stamina drop by 20 (which any number above 0 would do)
    // that way their stamina always stay at 80 or 100
    int increaseInReputation = university.getHumanResource().makeStaffTeach(total, printSimulation);
    university.setReputation(university.getReputation() + increaseInReputation);

    if (printSimulation)
    {
      int untaught = university.getHumanResource().getUninstructedStudents();
      int taught = total - untaught;
      out.printf("    total students: %d, taught: %d, untaught: %d" + lineSeparator(), total, taught, untaught);
      out.printf("    total increase in reputation: %d" + lineSeparator(), increaseInReputation);
    }

    //3a pay estate's maintenance cost
    if (printSimulation) out.printf("Paying building maintenance cost of %.2f" + lineSeparator(), university.getEstate().getMaintenanceCost());
    if (printSimulation) out.printf("    budget before= %.2f, ", university.getBudget());
    university.setBudget(university.getBudget() - university.getEstate().getMaintenanceCost());
    if (printSimulation) out.printf(" budget after= %.2f", university.getBudget());
    if (printSimulation) out.println();

    //3b pay staff's salary
    if (printSimulation) out.printf("Paying staff salary of %.2f" + lineSeparator(), university.getHumanResource().getTotalSalary());
    if (printSimulation) out.printf("    budget before= %.2f, ", university.getBudget());
    university.setBudget(university.getBudget() - university.getHumanResource().getTotalSalary());
    if (printSimulation) out.printf("budget after= %.2f", university.getBudget());
    if (printSimulation) out.println();


    //3c increase year os teaching for all employed staff
    university.getHumanResource().increaseYearsOfTeaching();

    //3d deduct 1 reputation for each uninstructed student
    if (printSimulation) out.printf("Deducting reputation for %d uninstructed students." + lineSeparator(), university.getHumanResource().getUninstructedStudents());
    if (printSimulation)  out.printf("    reputation before=%d, ", university.getReputation());
    university.setReputation(university.getReputation() - university.getHumanResource().getUninstructedStudents());
    if (printSimulation) out.printf("reputation after=%d" + lineSeparator(), university.getReputation());

    //3e decide the fate of employed staff
    if (printSimulation) out.println("Deciding staff fate:");
    int staffSize = university.getHumanResource().getStaffSize();
    university.getHumanResource().decideStaffFate(printSimulation); // printing here
    int staffSize2 = university.getHumanResource().getStaffSize();
    staffLeft = (staffSize - staffSize2);
    if (printSimulation && staffSize > 0) out.println("    " + (staffSize2==staffSize ? "Luckily, no" : "Unfortunately, " + staffLeft) + " staff left this year.");

    //3f replenish staff stamina for all remaining staff
    university.getHumanResource().replenishStaminas();
    if (printSimulation && staffSize2 > 0) out.println("    staff at the university enjoy a big holiday, and their stamina have been replenished (to 100, effectively).");

    if (printSimulation) out.printf("End of year reached. budget=%.2f, reputation=%d, # of students=%d, # of staff=%d." + lineSeparator(),
        university.getBudget(), university.getReputation(), university.getEstate().getNumberOfStudents(), university.getHumanResource().getStaffSize());
    if (printSimulation)
    {
      out.printf("    Total budget change=%.2f, total reputation change=%d, " + lineSeparator() +
              "    number of students change=%d, staff size change=%d, staff hired=%d, staff left=%d" + lineSeparator() +
              "    buildings built=%d, buildings upgraded=%d" + lineSeparator(),
          (university.getBudget() - yearStartingBudget), (university.getReputation() - yearStartingRepuation), (university.getEstate().getNumberOfStudents() - yearStartingStudents),
          (university.getHumanResource().getStaffSize() - startingStaff), staffHired, staffLeft, buildingsBuilt, buildingsUpgraded);

      /**
       *     int yearStartingRepuationt = university.getReputation();
       *     int yearStartingStudents = university.getEstate().getNumberOfStudents();
       *     int buildingsBuilt = 0;
       *     int buildingsUpgraded = 0;
       *     int startingStaff = university.getHumanResource().getStaffSize();
       *     int staffHired = 0;
       *     int staffLeft = 0;
       */
      university.getEstate().updateCapacities();
      out.println("Facilities Composition: ");
      out.println("    Capacities: halls=" + university.getEstate().hallsCapacity + ", labs=" + university.getEstate().labsCapacity + ", theatres=" + university.getEstate().theatresCapacity);
      out.println("    Building Count: " + university.getEstate().getNumberOfFacilities() + " total, " + university.getEstate().halls + " halls, " +  university.getEstate().labs + " labs, and " +  university.getEstate().theatres + " threatres.");
    }
    if (printSimulation) out.println();
    year++;
  }

  /**
   * Method to simulate the university for several years.
   * @param years
   */
  public void simulate(int years, boolean printSimulation)
  {
    this.printSimulation = printSimulation;
    for (int i = 0; i < years; i++)
    {
      simulate();
      //pause(100);
    }
    System.out.println("simulation complete.");
  }

  /**
   * pauses the current thread to allow a delay in printing
   * @param delay
   */
  private void pause(int delay)
  {
    try
    {
      Thread.sleep(delay);
    }
    catch (InterruptedException e)
    {
      // Terminate the simulation
    }
  }
}
