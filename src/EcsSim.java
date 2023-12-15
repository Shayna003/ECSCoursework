import facilities.Facility;
import facilities.buildings.Building;
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

  public static void main(String[] args)
  {
    EcsSim sim = new EcsSim();
    for (int i = 0; i < 100; i++)
    {
      sim.staffMarket.add(new Staff("staff" + i, (int)(Math.random() * 101))); // populate staff market with some initial staff for sim to run
    }
    sim.staffMarket.sort((a, b) -> b.getSkill() - a.getSkill());
    sim.simulate(50);
  }

  {
    staffMarket = new ArrayList<>();
    year = 1;
  }
  public EcsSim()
  {
    university = new University(1000); // initializes university with initial funding
  }

  public EcsSim(int funding)
  {
    university = new University(funding); // initializes university with initial funding
  }

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
    if (printSimulation) out.println("Upgrading or buying new buildings: ");
    //1a TODO: build additional facilities or upgrade existing ones. cannot do nothing here
    // compare halls capacity, labs capacity, theatre capacity. Try upgrading a building of the building category with the least capacity,
    // if not build a new building of such category, if not then don't spend money at this stage for this year.
    String least = university.getEstate().updateCapacities();
    if (printSimulation) out.println("    targeting building type with least capacity: " + least);

    // to avoid negative budget at end of year
    float buildingStageBudget = Math.min(university.getBudget(), university.getBudget() - university.getEstate().getMaintenanceCost() -university.getHumanResource().getTotalSalary() + university.getEstate().getNumberOfStudents() * 10);
    if (printSimulation) out.printf("    building stage budget: %.2f" + lineSeparator(), buildingStageBudget);
    Building toUpgrade = university.getEstate().findBuildingToUpgrade(least, buildingStageBudget);

    if (toUpgrade != null)
    {
      try
      {
        university.upgrade(toUpgrade); // perform upgrade on the most suitable building found by Estate if there is any.
        if (printSimulation) out.println("    " + toUpgrade + " was upgraded. reputation +50");
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
      university.setReputation(university.getReputation() + 100);

      if (newBuiltFacility == null)
      {
        if (printSimulation) out.println("    No buildings were built or upgraded.");
      }
      else
      {
        if (printSimulation) out.println("    " + newBuiltFacility + " was built. reputation +100");
      }
    }
    int reputationAfter = university.getReputation();
    if (printSimulation)
    {
      out.println("    total increase in reputation: " + (reputationAfter-reputationBefore));
    }

    //1b increase budget by of number of students * 10
    university.setBudget(university.getBudget() + university.getEstate().getNumberOfStudents() * 10);
    if (printSimulation) out.printf("Budget increased by %d from students" + lineSeparator(), university.getEstate().getNumberOfStudents() * 10);

    //1c TODO: hire additional staff from the market to teach students. cannot do nothing here
    if (printSimulation) out.println("Hiring staff:");

    int hireCount = 0; // might want to hire 1-3 staff per purchase/upgrade of a building, because there's a high chance for staff to leave even if they only teach 10 students with a skill of 100 (20%)
    // and considering the speed of students increasing by upgrading/purchasing new buildings.
    if (staffMarket.size() != 0) // only consider if there is staff to hire
    {
      // cannot end up with negative budget, so will need at least the amount of money to pay staff salary and
      float staffStageBudget = university.getBudget() - university.getEstate().getMaintenanceCost() - university.getHumanResource().getTotalSalary();
      out.printf("    staff stage budget: %.2f" + lineSeparator(), staffStageBudget);
      out.printf("    staff market size: %d" + lineSeparator(), staffMarket.size());

      // if student-staff ratio is saturated, don't hire new staff
      int students = university.getEstate().getNumberOfStudents();
      int staffSize = university.getHumanResource().getStaffSize();
      if (staffSize * 20 > students * 1.3) // arbitrary value here.
      {
        if (printSimulation)
        {
          out.println("    Student to staff ratio is good. No need to hire.");
        }
      }
      else // attempt to hire staff
      {
        // to prevent concurrentmodificationerror
        Iterator<Staff> staffIterator = staffMarket.iterator();//university.getHumanResource().getStaff();
        while (staffIterator.hasNext() && staffStageBudget > 0 && hireCount < 3)
        {
          Staff staff = staffIterator.next();
          // prepare for the worst: compute theoretical salary by the highest value possible, 10.5% of skill
          float salary = staff.getSkill() * 0.105f;
          if (salary < staffStageBudget) // hire staff
          {
            if (printSimulation) out.printf("    " + staff + " got hired." + lineSeparator());
            university.getHumanResource().addStaff(staff);
            staffStageBudget = university.getBudget() - university.getEstate().getMaintenanceCost() - university.getHumanResource().getTotalSalary();
            staffIterator.remove();
            hireCount++;
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
    if (printSimulation) if (printSimulation) out.printf("Paying staff salary of %.2f" + lineSeparator(), university.getHumanResource().getTotalSalary());
    out.printf("    budget before= %.2f, ", university.getBudget());
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
    if (printSimulation) out.println("    " + (staffSize2==staffSize ? "No" : "Unfortunately, " + (staffSize-staffSize2)) + " staff left this year.");

    //3f replenish staff stamina for all remaining staff
    university.getHumanResource().replenishStaminas();

    if (printSimulation) out.printf("End of year reached. budget=%.2f, reputation=%d, # of students=%d, # of staff=%d" + lineSeparator(),
        university.getBudget(), university.getReputation(), university.getEstate().getNumberOfStudents(), university.getHumanResource().getStaffSize());
    if (printSimulation)
    {
      university.getEstate().updateCapacities();
      out.println("capacities breakdown: halls=" + university.getEstate().hallsCapacity + ", labs=" + university.getEstate().labsCapacity + ", theatres=" + university.getEstate().theatresCapacity);
    }
    if (printSimulation) out.println();
    year++;
  }

  /**
   * Method to simulate the university for several years.
   * @param years
   */
  public void simulate(int years)
  {
    for (int i = 0; i < years; i++)
    {
      simulate();
      //pause(100);
    }
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
