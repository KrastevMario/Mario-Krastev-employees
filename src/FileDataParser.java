import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//takes the file and extracts all the information from it using a default pattern for the employees.
public class FileDataParser {
    public static Collection<Employee> getAllEmployees(String filePath) {
        Set<Employee> employees = new TreeSet<>((o1, o2) -> (int) o1.compareTo(o2));

        String row = null;
        File file = new File(filePath);
        if(!file.exists()){
            try {
                throw new FileNotFoundException("The file you are looking for is invalid. Please check if the path is correct.");
            } catch (FileNotFoundException e) {
                System.out.println("There was an unexpected error while trying to throw fileNotFoundException.");
                e.printStackTrace();
            }
        }
        BufferedReader csvReader = null;

        try {
            csvReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //read the lines from the file if exists
        while (file.exists()) {
            try {
                row = csvReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(row == null){
                break;
            }

            //split by csv
            String[] data = row.split(",");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].trim();
            }
            System.out.println(Arrays.stream(data).toList());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            //verify for null data
            int empId;
            int projectId;
            LocalDate dateFrom;
            LocalDate dateTo;
            if(data[0].toUpperCase().equals("NULL")){
                throw new IllegalArgumentException("The empId cannot be null. (it has to contain info)");
            }else{
                empId = Integer.parseInt(data[0]);
            }
            if(data[1].toUpperCase().equals("NULL")){
                throw new IllegalArgumentException("The projectId cannot be null. (it has to contain info)");
            }else{
                projectId = Integer.parseInt(data[1]);
            }
            if(data[2].toUpperCase().equals("NULL")){
                throw new IllegalArgumentException("The dateFrom cannot be null. (it has to contain info)");
            }else{
                dateFrom = LocalDate.parse(data[2], dateFormatter);
            }
            if(data[3].toUpperCase().equals("NULL")){
                dateTo = null;
            }else{
                dateTo = LocalDate.parse(data[3], dateFormatter);
            }

            //add the employee to the collection
            Employee currentEmployee = new Employee(empId, projectId, dateFrom, dateTo);
            employees.add(currentEmployee);
        }
        return Collections.unmodifiableSet(employees);
    }

}

