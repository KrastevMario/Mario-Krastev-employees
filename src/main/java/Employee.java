import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Employee {
    private int empId;
    private int projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private int longestTeamProject; //insert the project in which you've worked the most with a partner
    private int partnerId; //the partner from the project
    private long timeSpentOnProjectWithPartner; //the time spent on the project

    public Employee(int empId, int projectId, LocalDate dateFrom, LocalDate dateTo){
        //do verifications
        this.empId = empId;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.longestTeamProject = -1;
        this.partnerId = -1;
        this.timeSpentOnProjectWithPartner = -1;
    }

    public Employee(){
        this.empId = -1;
        this.projectId = -1;
        this.longestTeamProject = -1;
        this.partnerId = -1;
        this.timeSpentOnProjectWithPartner = -1;
    }

    public int getLongestTeamProject() {
        return longestTeamProject;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public long getTimeSpentOnProjectWithPartner() {
        return timeSpentOnProjectWithPartner;
    }

    public int getEmpId() {
        return empId;
    }

    public int getProjectId() {
        return projectId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setLongestTeamProject(int longestTeamProject) {
        //TODO: make it throw an exception on wrong data
        if(longestTeamProject < 0){
            return;
        }
        this.longestTeamProject = longestTeamProject;
    }

    public void setPartnerId(int partnerId) {
        if(partnerId <= 0){
            return;
        }
        this.partnerId = partnerId;
    }

    public void setTimeSpentOnProjectWithPartner(long timeSpentOnProjectWithPartner) {
        this.timeSpentOnProjectWithPartner = timeSpentOnProjectWithPartner;
    }

    //sorting by worker's time
    public long compareTo(Employee emp2) {
        LocalDate dateFromEmp1 = this.getDateFrom();
        LocalDate dateToEmp1 = this.getDateTo();
        if(dateToEmp1 == null){
            dateToEmp1 = LocalDate.now();
        }

        LocalDate dateFromEmp2 = emp2.getDateFrom();
        LocalDate dateToEmp2 = emp2.getDateTo();
        if(dateToEmp2 == null){
            dateToEmp2 = LocalDate.now();
        }

        LocalDate periodBetweenEmp1;
        periodBetweenEmp1 = dateFromEmp1.minus(dateToEmp1.getDayOfYear(), ChronoUnit.DAYS);
        periodBetweenEmp1 = dateFromEmp1.minus(dateToEmp1.getMonthValue(), ChronoUnit.MONTHS);
        periodBetweenEmp1 = dateFromEmp1.minus(dateToEmp1.getYear(), ChronoUnit.YEARS);

        LocalDate periodBetweenEmp2;
        periodBetweenEmp2 = dateFromEmp2.minus(dateToEmp2.getDayOfYear(), ChronoUnit.DAYS);
        periodBetweenEmp2 = dateFromEmp2.minus(dateToEmp2.getMonthValue(), ChronoUnit.MONTHS);
        periodBetweenEmp2 = dateFromEmp2.minus(dateToEmp2.getYear(), ChronoUnit.YEARS);
        //System.out.println(" --- " + (periodBetweenEmp1.get(ChronoUnit.DAYS) - periodBetweenEmp2.get(ChronoUnit.DAYS)));
        return periodBetweenEmp1.compareTo(periodBetweenEmp2);
    }

    public boolean isColliding(Employee secondEmployee){
        if(this.getDateTo().toEpochDay() < secondEmployee.getDateFrom().toEpochDay() ||
                secondEmployee.getDateTo().toEpochDay() < this.getDateFrom().toEpochDay()){
            return false;
        }
        return true;
    }

    //use it after checking the collision
    public long getEmployeesTimeLength(Employee secondEmployee){
        //calculate the total time that they have spent together.
        long sharedStartingTime;
        long sharedFinishingTime;
        if(this.getDateFrom().toEpochDay() < secondEmployee.getDateFrom().toEpochDay()){
            sharedStartingTime = this.getDateFrom().toEpochDay();
        }else{
            sharedStartingTime = secondEmployee.getDateFrom().toEpochDay();
        }

        if(this.getDateTo().toEpochDay() < secondEmployee.getDateTo().toEpochDay()){
            sharedFinishingTime = this.getDateTo().toEpochDay();
        }else{
            sharedFinishingTime = secondEmployee.getDateTo().toEpochDay();
        }
        return sharedFinishingTime - sharedStartingTime; //returns the days they have been working together
    }
}



/*
class employee:
- empId
- projectId
- dateFrom
- dateTo

retrieve from a file the strings with coma separated values and then insert them into the object of class Employee (collection)
after that sort the employees (or take the first 2 oldest)
 */