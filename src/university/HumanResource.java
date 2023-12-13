package university;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * The HumanResource class (within the university package) manages the staff within the University.
 * It has a member variable HashMap<Staff, Float> staffSalary representing
 * the salary of the staff employed by the university.
 * Keeps track of uninstructed students for reputation deduction purposes.
 */
public class HumanResource
{
  HashMap<Staff, Float> staffSalary;
  private int uninstructedStudents;

  public int getUninstructedStudents()
  {
    return uninstructedStudents;
  }

  /**
   * set to number of all students at the start of each year by EcsSim
   */
  public void setUninstructedStudents(int newValue)
  {
    uninstructedStudents = newValue;
  }

  /**
   * @return number of staff currently in the unviersity
   */
  public int getStaffSize()
  {
    return staffSalary.size();
  }

  public HumanResource()
  {
    staffSalary = new HashMap<>();
  }

  /**
   * add staff to the University. The salary of the new staff is randomly
   * chosen between 9.5% and 10.5% of the staff’s skill.
   */
  public void addStaff(Staff staff)
  {
    int skill = staff.getSkill();

    // generate a random percent between 9.5% and 10.5%
    float percent = (float) (0.01 * (9.5 + (int)(Math.random() * ((10.5 - 9.5) + 1))));
    staffSalary.put(staff, skill * percent);
  }

  /**
   * Replenish all staminas of all staff at the end of the teaching year
   */
  public void replenishStaminas()
  {
    staffSalary.forEach((k, v) -> k.replenishStamina());
  }


  /**
   * @return an iterator to the key set of the staffSalary
   * map. (This allows manipulation of the staffSalary through the iterator).
   */
  public Iterator<Staff> getStaff()
  {
    return staffSalary.keySet().iterator();
  }

  /**
   * For any employed staff, they can leave the university according to the following:
   * If the staff has 30 years of teaching, they will leave the university
   * Otherwise, the chance that the staff stays is the staff’s stamina.
   */
  public void decideStaffFate()
  {
    staffSalary.entrySet().removeIf(entry ->
    {
      Staff staff = entry.getKey();
      if (staff.getYearsOfTeaching() >= 30) return true;
      int random = (int)(Math.random() * 101); // random number between 0 and 100 both inclusive
      return random > staff.getStamina(); // leave depending on a chance based on staff stamina
    });
  }

  /**
   * Increase years of teaching of all staff by 1.
   */
  public void increaseYearsOfTeaching()
  {
    staffSalary.forEach((k, v) -> k.increaseYearsOfTeaching());
  }

  /**
   * @return the total salary of all the staff.
   */
  public float getTotalSalary()
  {
    final float[] sum = {0f};
    staffSalary.forEach((k, v) -> sum[0] += v);
    return sum[0];
  }
}
