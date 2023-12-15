parts of coursework attempted: 1, 2, 3, 4, 5, 6, 7

Notes:
I'm very proud of my algorithm. It attempts to find the best way to spend coins, maintain a good student to teacher ratio,
budget never drops below 0, there were never uninstructed students, incompetent always teachers (lower skill level) always considered last when hiring
(considered in the order of skill level), stamina drop is always 20 if teachers are to teach (and will teach the maximum number of students while only dropping 20 stamina),
there is a very detailed breakdown in program output (or you can choose not to have any output).
I do want to limit the rate of hiring new teachers, because they have a 20% chance to leave every year(due to the nature of the formulas).
Thus I only consider upgrading or purchasing one building per year as well.
In my opinion, the entire algorithm is affected by the staff market, not starting budget.
The probability of a staff leaving the university after 30 years of teaching is almost too low to ever happen.
It's also hard to keep reputation positive if staff skills from the market are very low. On average, you expect a staff to leave the university after 5 years or so,
and a staff with 0 skill will only to change to skill 5, and only gain a reputation of 4 if they teach 130 students "sacrificially", dropping their stamina to 0.

So I will say the strategy mostly depends on 1. target number of years to simulate, and 2. number and skill level distribution of staff market
For instance, if on the market, there's 50 staff with good skills, 50 staff with awful skills, after perhaps

How to run:

Extensions:

Coursework feedback:
For an assignment that tries to teach us the supposed way of doing OOP, I find it annoying that I had had to make things work by breaking OOP.
I might be the only person who did thorough analysis of the building types and equations using Excel, and realise the chance of staff leaving is way too high, and break the whole system.
However, things did become more interesting from part 5 and onwards.