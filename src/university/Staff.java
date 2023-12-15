package university;

/**
 * The University requires staff to supervise the laboratory sessions and teach the lectures.
 * The staff are an essential part of the University and are in charge of instructing the students on their learning at the University.
 */
public class Staff implements Comparable<Staff>
{
  private String name;
  private int skill; // The skill of the staff, this should be between 0 and 100.
  private int yearsOfTeaching; // The number of years that the staff has been employed by the university.
  private int stamina; // A number between 0 and 100 representing the current stamina of the staff.

  public Staff(String name, int skill)
  {
    this.name = name;
    this.skill = skill;
    yearsOfTeaching = 0;
    stamina = 100;
  }

  public int getSkill()
  {
    return this.skill;
  }

  public int getYearsOfTeaching()
  {
    return this.yearsOfTeaching;
  }

  public int getStamina()
  {
    return this.stamina;
  }

  public String getName()
  {
    return this.name;
  }

  /**
   * instruct a number of students to study.
   * The method returns the number of reputation points for the university according to the following formula.
   * reputation = (100 × skill) / (100 + numberOfStudents)
   * The method increases the skill of the staff by 1, but cannot make the skill to be above
   * 100. The stamina decreases by the following formula.
   * stamina = stamina − ⌈numberOfStudents/(20 + skill)⌉ × 20
   * Here ⌈r⌉ denotes the ceiling of r (the least integer greater than or equal to r). Hint: You can use the java.lang.Math.ceil() function.
   * @param numberOfStudents
   * @return
   */
  public int instruct(int numberOfStudents)
  {
    if (skill < 100) skill++;
    stamina -= (int) (Math.ceil(numberOfStudents/((double)(20 + skill))) * 20);

    // in effect this would never happen though
    if (stamina < 0) stamina = 0;

    int reputation = (int)((100 * skill) / ((double)(100 + numberOfStudents)));
    return reputation;
  }

  /**
   * reinvigorate 20 to the staff’s stamina, however the stamina is capped above by 100.
   */
  public void replenishStamina()
  {
    stamina = stamina + 20 > 100 ? 100 : stamina + 20;
  }

  /**
   * Increase the years of teaching by 1.
   */
  public void increaseYearsOfTeaching()
  {
    yearsOfTeaching++;
  }

  /**
   * potentially useful, not used for now
   * @param competitor the object to be compared.
   * @return
   */
  @Override
  public int compareTo(Staff competitor)
  {
    return this.skill - competitor.skill;
  }

  /**
   * For meaningful prints
   */
  @Override
  public String toString()
  {
    return "Staff[name=" + name + ", skill=" + skill + ", yearsOfTeaching=" + yearsOfTeaching + "]";
  }
}
