import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Demo {
    public static void main(String[] args) {
        String filePath = "./employee_info.txt";
        Set<Employee> employees = (Set<Employee>) FileDataParser.getAllEmployees(filePath);
        //show the top 2 employees who have worked on a project for the longest time (not necessary the same project)
        int numberToShowEmployees = 2;
        int currentShowing = 0;
        for (Employee e : employees){
            if(currentShowing == numberToShowEmployees){
                break;
            }
            System.out.println(e.getEmpId() + ", " + e.getProjectId() + ", "+  e.getDateFrom() + ", " + e.getDateTo());
            currentShowing++;
        }
        //show the top 2 employees who have worked the longest on the same project

        //logic: take the project. extract all the employees of that project. Take the employees who have collisions
        //calculate the collision and if it is bigger than the current biggest one in the project, change.

        //each project
        HashMap<Integer, Employee> projects = new HashMap<>();
        for (Employee e : employees){
            projects.put(e.getProjectId(), e);
        }

        int globalProject;
        Employee firstGlobalEmployee = null;
        Employee secondGlobalEmployee = null;
        int timeTogether = 0;
        for (Integer projectId : projects.keySet()){
            List<Employee> employeesInProject = new ArrayList<>(projects.values());
            if(firstGlobalEmployee == null || secondGlobalEmployee == null){

            }
        }
    }

    private static boolean areColliding(Employee firstEmployee, Employee secondEmployee){
        if(firstEmployee.getDateTo().toEpochDay() < secondEmployee.getDateFrom().toEpochDay() ||
                secondEmployee.getDateFrom().toEpochDay() < firstEmployee.getDateFrom().toEpochDay()){
            return false;
        }
        if(firstEmployee.getDateFrom().toEpochDay() < secondEmployee.getDateFrom().toEpochDay()){
            
        }
        int startDate = (int) firstEmployee.getDateFrom().toEpochDay();
        return false;
    }

    private static int getEmployeeTimeLength(Employee firstEmployee, Employee secondEmployee){
        firstEmployee.getDateFrom();
        return 0;
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