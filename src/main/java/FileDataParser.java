import dateValidator.ParseDataToValid;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

//takes the file and extracts all the information from it using a default pattern for the employees.
public class FileDataParser {

    //return all the employees [collection] with their information (Id, project, dateStart, dateFinish)
    public static Collection<Employee> getAllEmployees(String filePath, String fileName) {
        Set<Employee> employees = new TreeSet<>((o1, o2) -> (int) o1.compareTo(o2));

        String row = null;
        File file = new File(filePath);
        AtomicBoolean fileFound = new AtomicBoolean(false);
        final Path[] filePathName = new Path[1];
        try (Stream<Path> walkStream = Files.walk(Paths.get(filePath))) {
            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                if (f.toString().endsWith(fileName)) {
                    fileFound.set(true);
                    filePathName[0] = f;
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        if(!fileFound.get()){
            try {
                throw new FileNotFoundException("The file you are looking for is invalid. Please check if the path is correct.");
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        BufferedReader csvReader = null;

        try {
            csvReader = new BufferedReader(new FileReader(filePathName[0].toString()));
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
            int maxValuesAllowed = 4;
            int minValuesAllowed = 4;
            int valuesCounter = 0;
            String[] data = row.split(",");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].trim();
                valuesCounter++;
            }
            if(valuesCounter > maxValuesAllowed || valuesCounter < minValuesAllowed){
                try{
                    throw new IllegalArgumentException("Invalid number of arguments in the row. Skipped.");
                }catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
                continue;
            }
            System.out.println(Arrays.stream(data).toList());

            //verify for null data
            int empId = -1;
            int projectId = -1;
            LocalDate dateFrom;
            LocalDate dateTo;

            //0 - empId, 1 - projectIt, 2 - dateFrom, 3 - dateTo
            try {
                if (data[0].toUpperCase().equals("NULL") || data[0].isEmpty() || data[0].isBlank() || !data[0].matches("-?\\d+")) {
                    throw new IllegalArgumentException("The empId is invalid. (it has to contain acceptable info)");
                } else {
                    empId = Integer.parseInt(data[0]);
                }
                if (data[1].toUpperCase().equals("NULL") || data[1].isEmpty() || data[1].isBlank() || !data[0].matches("-?\\d+")) {
                    throw new IllegalArgumentException("The projectId cannot be empty. (it has to contain info) at EmpId: " + data[0]);
                } else {
                    projectId = Integer.parseInt(data[1]);
                }
                if (data[2].toUpperCase().equals("NULL") || data[2].isEmpty() || data[2].isBlank()) {
                    throw new IllegalArgumentException("The dateFrom cannot be empty. (it has to contain info) at EmpId: " + data[0]);
                } else {
                    dateFrom = ParseDataToValid.parseYMD(data[2]);
                }
                if (data[3].toUpperCase().equals("NULL") || data[3].isEmpty() || data[3].isBlank()) {
                    dateTo = LocalDate.now();
                } else {
                    dateTo = ParseDataToValid.parseYMD(data[3]);
                }

                //add the employee to the collection
                Employee currentEmployee = new Employee(empId, projectId, dateFrom, dateTo);
                employees.add(currentEmployee);
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }catch (DateTimeParseException e){
                System.out.println("Illegal structured date. " + e.getMessage());
            }
        }
        System.out.println();
        return Collections.unmodifiableSet(employees);
    }
}

