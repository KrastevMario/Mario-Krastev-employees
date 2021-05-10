import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Demo {
    public static void main(String[] args) {
        String filePath = "./";
        String fileName = "employee_info.txt";
        Set<Employee> employees = (Set<Employee>) FileDataParser.getAllEmployees(filePath, fileName);
        //show the top 2 employees who have worked on a project for the longest time (not necessary the same project)
        int numberToShowEmployees = 2;
        int currentShowing = 0;

        if(employees == null){
            System.out.println("Information missing.");
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
                if(insideEmployee.getProjectId() == partnerEmployee.getProjectId() && areColliding(insideEmployee, partnerEmployee)){
                    //verify how much time and check if the employees have as max that time.
                    int projectId = insideEmployee.getProjectId();
                    long timeWorked = getEmployeesTimeLength(insideEmployee, partnerEmployee);
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
            if(employee.getTimeSpentOnProjectWithPartner() > mostTimeEmployee.getTimeSpentOnProjectWithPartner()){
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
    }

    private static boolean areColliding(Employee firstEmployee, Employee secondEmployee){
        if(firstEmployee.getDateTo().toEpochDay() < secondEmployee.getDateFrom().toEpochDay() ||
                secondEmployee.getDateTo().toEpochDay() < firstEmployee.getDateFrom().toEpochDay()){
            return false;
        }
        return true;
    }

    //use it after checking the collision
    private static long getEmployeesTimeLength(Employee firstEmployee, Employee secondEmployee){
        //calculate the total time that they have spent together.
        long sharedStartingTime;
        long sharedFinishingTime;
        if(firstEmployee.getDateFrom().toEpochDay() < secondEmployee.getDateFrom().toEpochDay()){
            sharedStartingTime = firstEmployee.getDateFrom().toEpochDay();
        }else{
            sharedStartingTime = secondEmployee.getDateFrom().toEpochDay();
        }

        if(firstEmployee.getDateTo().toEpochDay() < secondEmployee.getDateTo().toEpochDay()){
            sharedFinishingTime = firstEmployee.getDateTo().toEpochDay();
        }else{
            sharedFinishingTime = secondEmployee.getDateTo().toEpochDay();
        }
        return sharedFinishingTime - sharedStartingTime; //returns the days they have been working together
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