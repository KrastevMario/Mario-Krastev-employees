import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public class Employee {
    private int empId;
    private int projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Employee(int empId, int projectId, LocalDate dateFrom, LocalDate dateTo){
        //do verifications
        this.empId = empId;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
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