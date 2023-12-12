package university;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * The HumanResource class (within the university package) manages the staff within the University.
 * It has a member variable HashMap<Staff, Float> staffSalary representing
 * the salary of the staff employed by the university.
 */
public class HumanResource
{
  HashMap<Staff, Float> staffSalary;

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
   * @return an iterator to the key set of the staffSalary
   * map. (This allows manipulation of the staffSalary through the iterator).
   */
  public Iterator<Staff> getStaff()
  {
    return staffSalary.keySet().iterator();
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
