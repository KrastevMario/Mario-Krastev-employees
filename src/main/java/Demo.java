import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Demo {
    public static void main(String[] args) {
        String filePath = "./";
        String fileName = "employee_info.txt";

        /*
                        TEST WITH INPUT BY HAND
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Insert the file path (without the file name: ./folder/):");
        filePath = keyboard.nextLine();
        System.out.println("Insert the file Name (without the path: name.txt):");
        fileName = keyboard.nextLine();
         */

        Set<Employee> employees = (Set<Employee>) FileDataParser.getAllEmployees(filePath, fileName);
        //show the top 2 employees who have worked on a project for the longest time (not necessary the same project)
        int numberToShowEmployees = 2;
        int currentShowing = 0;

        if(employees == null){
            System.out.println("Information missing. (path or file or employees)");
            return;
        }
        if(employees.isEmpty()){
            System.out.println("The file is empty (no employees found inside).");
            return;
        }
        for (Employee e : employees){
            if(currentShowing == numberToShowEmployees){
                break;
            }
            System.out.println(e.getEmpId() + ", " + e.getProjectId() + ", "+  e.getDateFrom() + ", " + e.getDateTo());
            currentShowing++;
        }
        /*
        check if emp1 is colliding with emp2
        if yes then calculate how much time they have worked on a project together
        if the time is more than the last project, replace it (check for both).
         */
        for (Employee insideEmployee : employees){
            for (Employee partnerEmployee : employees){
                //if they have worked at the same project and they have collision, check the time and do the actions.
                if(insideEmployee.getEmpId() == partnerEmployee.getEmpId()){
                    continue;
                }
                if(insideEmployee.getProjectId() == partnerEmployee.getProjectId() && insideEmployee.isColliding(partnerEmployee)){
                    //verify how much time and check if the employees have as max that time.
                    int projectId = insideEmployee.getProjectId();
                    long timeWorked = insideEmployee.getEmployeesTimeLength(partnerEmployee);
                    long insideEmpProjectTime = insideEmployee.getTimeSpentOnProjectWithPartner();
                    long partnerEmpProjectTime = partnerEmployee.getTimeSpentOnProjectWithPartner();
                    if(timeWorked > insideEmpProjectTime && timeWorked > partnerEmpProjectTime){
                        //set the new info about their longest project
                        insideEmployee.setTimeSpentOnProjectWithPartner(timeWorked);
                        partnerEmployee.setTimeSpentOnProjectWithPartner(timeWorked);

                        insideEmployee.setPartnerId(partnerEmployee.getEmpId());
                        partnerEmployee.setPartnerId(insideEmployee.getEmpId());

                        insideEmployee.setLongestTeamProject(projectId);
                        partnerEmployee.setLongestTeamProject(projectId);
                    }
                }
            }
        }

        //get the 2 employees with the highest time on the same project
        Employee mostTimeEmployee = new Employee();
        for (Employee employee : employees){
            if(employee.getTimeSpentOnProjectWithPartner() >= mostTimeEmployee.getTimeSpentOnProjectWithPartner()){
                mostTimeEmployee = employee;
            }
        }

        if(mostTimeEmployee.getProjectId() == -1){
            System.out.println("There are no employees (or projects) that are colliding");
        }else{
            System.out.println("employee " + mostTimeEmployee.getEmpId() + "| project " + mostTimeEmployee.getProjectId()
                    + " | employee " + mostTimeEmployee.getPartnerId() + " | have worked together for "
                    + mostTimeEmployee.getTimeSpentOnProjectWithPartner() + " days");
        }

        //- get the team who has worked the most together
        /*
        check the first employee, check the 2nd employee. If there is no combination with this people in a team in our collection
        create a new team. Insert in the new team the time that they have worked together.
        If there's a combination, add the time to the team.

         */
        List<Team> teams = new CopyOnWriteArrayList<>();
        for (Employee mainEmployee : employees){
            for (Employee checkingEmployee : employees){
                if(mainEmployee.getEmpId() == checkingEmployee.getEmpId() || mainEmployee.getProjectId() != checkingEmployee.getProjectId()){
                    continue;
                }
                if(!mainEmployee.isColliding(checkingEmployee) && !checkingEmployee.isColliding(mainEmployee)){
                    System.out.println("SKIPPED " + checkingEmployee.getEmpId() + " " + mainEmployee.getEmpId());
                    continue;
                }
                if(teams.isEmpty()){
                    teams.add(new Team(mainEmployee.getEmpId(), checkingEmployee.getEmpId(), mainEmployee.getEmployeesTimeLength(checkingEmployee)));
                    continue;
                }
                System.out.println(mainEmployee.getEmpId() + " , " + checkingEmployee.getEmpId());
                boolean teamExists = false;
                int teamPosition = -1;
                for (Team team : teams){
                    if(team.getFirstEmpId() == mainEmployee.getEmpId() && team.getSecondEmpId() == checkingEmployee.getEmpId()
                        || team.getSecondEmpId() == mainEmployee.getEmpId() && team.getFirstEmpId() == checkingEmployee.getEmpId()){
                        teamExists = true;
                        teamPosition++;
                        break;
                    }
                }
                if(!teamExists){
                    System.out.println("NEW " + checkingEmployee.getEmpId() + " " + mainEmployee.getEmpId());
                    teams.add(new Team(mainEmployee.getEmpId(), checkingEmployee.getEmpId(), mainEmployee.getEmployeesTimeLength(checkingEmployee)));
                }else{
                    teams.set(teamPosition, teams.get(teamPosition).setTimeWorkedTogetherReturningTeam(
                            teams.get(teamPosition).getTimeWorkedTogether() + mainEmployee.getEmployeesTimeLength(checkingEmployee)
                    ));
                    //System.out.println(teams.get(teamPosition).getTimeWorkedTogether());
                }
            }
        }

        for (Team team : teams){
            team.setTimeWorkedTogether(team.getTimeWorkedTogether() / 2);
        }

        System.out.println("============= TEAMS INFO ================");
        Team bestBuddiesTeam = null;
        for (Team team : teams){
            if(bestBuddiesTeam == null || bestBuddiesTeam.getTimeWorkedTogether() < team.getTimeWorkedTogether()){
                bestBuddiesTeam = team;
            }
            System.out.println("employee " + team.getFirstEmpId() + " with " + team.getSecondEmpId() + " worked together for "
                    + team.getTimeWorkedTogether() + " days");
        }

        if(bestBuddiesTeam == null){
            System.out.println("Not enough people to form a team.");
        }else {
            System.out.println("\nThe team that has worked together for the longest is:\n"
                    + bestBuddiesTeam.getFirstEmpId() + " and " + bestBuddiesTeam.getSecondEmpId() + " have worked for "
                    + bestBuddiesTeam.getTimeWorkedTogether());
        }

        LocalDate localDate = LocalDate.parse("16-Aug-2016", DateTimeFormatter.ofPattern("d-MMM-yyyy"));
        System.out.println(localDate.toEpochDay());
        LocalDate dc = LocalDate.parse("2016-08-16", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(dc.toEpochDay() - localDate.toEpochDay());
    }
}


/*
reading the file
- main functions:
open the file. read the different values of it. Put in a collection the different results of values.

- verifications:
If a value is not valid - skip that line.

The comparing will be by days. The input cannot accept millis, seconds, nanoseconds etc.
 */

/*
EPOCH DAY -> returns the days from 1970
 */