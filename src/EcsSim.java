import java.util.ArrayList;
import university.Staff;
import university.University;

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
    sim.simulate(10);
  }
  public EcsSim()
  {
    university = new University(1000); // initializes university with initial funding
    staffMarket = new ArrayList<>();
    year = 0;
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
   f) Finally, for all remaining employed staff, replenish their stamina.

   the strategy is that you need a minimum initial funding to be able to build any buildings, in order to be able to hire students.
   TODO: outputs for everything
   */
  public void simulate()
  {
    if (printSimulation) out.println("year 1");
    //1a TODO: build additional facilities or upgrade existing ones. cannot do nothing here

    //1b increase budget by of number of students * 10
    university.setReputation(university.getReputation() + university.getEstate().getNumberOfStudents() * 10);
    if (printSimulation) out.printf("reputation increased by %d" + lineSeparator(), university.getEstate().getNumberOfStudents() * 10);

    //1c TODO: hire additional staff from the market to teach students. cannot do nothing here
    if (printSimulation) out.println("hiring staff:");

    //background task: set uninstructed students to number of students
    university.getHumanResource().setUninstructedStudents(university.getEstate().getNumberOfStudents());

    //2 TODO: let staff teach student. each staff can only teach once. this increases reputation
    if (printSimulation) out.println("teaching students:");

    //3a pay estate's maintenance cost
    if (printSimulation) out.println("paying building maintenance cost of " + university.getEstate().getMaintenanceCost());
    if (printSimulation) out.printf("budget before= %d, ", university.getBudget());
    university.setBudget(university.getBudget() - university.getEstate().getMaintenanceCost());
    if (printSimulation) out.printf("budget after= %d", university.getBudget());
    if (printSimulation) out.println();

    //3b pay staff's salary
    if (printSimulation) if (printSimulation) out.println("paying staff salary of " + university.getHumanResource().getTotalSalary());
    out.printf("budget before= %d, ", university.getBudget());
    university.setBudget(university.getBudget() - university.getHumanResource().getTotalSalary());
    if (printSimulation) out.printf("budget after= %d", university.getBudget());
    if (printSimulation) out.println();


    //3c increase year os teaching for all employed staff
    university.getHumanResource().increaseYearsOfTeaching();

    //3d deduct 1 reputation for each uninstructed student
    if (printSimulation) out.printf("deducting reputation for %d uninstructed students. reputation before=%d", university.getHumanResource().getUninstructedStudents(), university.getReputation());
    university.setReputation(university.getReputation() - university.getHumanResource().getUninstructedStudents());
    if (printSimulation) out.printf(", reputation after=%d", university.getReputation());

    //3e decide the fate of employed staff
    if (printSimulation) out.println("deciding staff fate:");
    university.getHumanResource().decideStaffFate();

    //3f replenish staff stamina for all remaining staff
    university.getHumanResource().replenishStaminas();

    if (printSimulation) out.printf("end of year reached. budget=%.2f, reputation=%d, # of students=%d, # of staff=%d" + lineSeparator(),
        university.getBudget(), university.getReputation(), university.getEstate().getNumberOfStudents(), university.getHumanResource().getStaffSize());
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
