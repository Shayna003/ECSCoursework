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
   * make all staff teach the maximum number of students that make their stamina drop by 20.
   * update number of uninstructed students.
   * @param cap the maximum number possible to teach (number of total students, passed by EcsSim
   */
  public void makeStaffTeach(int cap, boolean printDetails)
  {
    staffSalary.forEach((staff, salary)->
    {
      int numberOfStudents = Math.min(cap, optimalNumberOfStudentsToTeach(staff.getSkill()));
      if (numberOfStudents > 0)
      {
        int staminaBefore = staff.getStamina();
        int reputation = staff.instruct(numberOfStudents);
        int staminaAfter = staff.getStamina();
        if (printDetails) System.out.println("    " + staff + " taught " + numberOfStudents + " students and lost " + (staminaBefore-staminaAfter) + " stamina. " + reputation + " reputation gained for university.");
        uninstructedStudents -= numberOfStudents;
        if (uninstructedStudents < 0) uninstructedStudents = 0;
      }
    });
  }

  /**
   * Based on a study with Excel, I figured these numbers out, the chance of staff to leave is 20 (which you get by teaching any number > 0),
   * so might as well teach the maximum number of students while spending 20 stamina
   * @return optimal number of students for a staff to teach based on their skill.
   * optimal is solely considered in terms of stamina cost, i.e. chance for staff to leave the university afterwards
   */
  public int optimalNumberOfStudentsToTeach(int skill)
  {
    return Math.floorDiv(skill, 10) * 10 + 20 + skill % 10;
/*    //20 + skill % 10;
    if (skill < 10)
    {
      return 20 + skill;
    }
    else if (skill < 20)
    {
      return 30 + (skill - 10);
    }
    else if (skill < 30)
    {
      return 40 + (skill - 10);;
    }
    else if (skill < 40)
    {
      return 50 + (skill - 10);;
    }
    else if (skill < 50)
    {
      return 60 + (skill - 40);
    }
    else if (skill < 60)
    {
      return 70 + (skill - 10);;
    }
    else if (skill < 70)
    {
      return 80 + (skill - 10);;
    }
    else if (skill < 80)
    {
      return 90 + (skill - 10);;
    }
    else if (skill < 90)
    {
      return 100 + (skill - 10);;
    }
    else // for skill range [90, 100]
    {
      return 120;
    }*/
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
  public void decideStaffFate(boolean printDetails)
  {
    staffSalary.entrySet().removeIf(entry ->
    {
      Staff staff = entry.getKey();
      if (staff.getYearsOfTeaching() >= 30)
      {
        if (printDetails)
        {
          System.out.println("    " + staff + " retires after 30 years of hard work.");
        }
        return true;
      }
      int random = (int)(Math.random() * 101); // random number between 0 and 100 both inclusive
      boolean unlucky = random > staff.getStamina(); // leave depending on a chance based on staff stamina. it's controlled to be at 20%.
      if (printDetails & unlucky)
      {
        System.out.println("    " + staff + " unfortunately leaves due to a low stamina of " + staff.getStamina() + ".");
      }
      return unlucky;
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
