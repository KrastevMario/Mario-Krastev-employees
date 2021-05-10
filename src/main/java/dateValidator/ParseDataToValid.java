package dateValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//takes different date formats and returns yyyy-MM-dd
public class ParseDataToValid {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("d-MMM-yyyy");
    private static final DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateValidator dateValidator = new DateValidatorUsingDateFormat("yyyy-MM-dd");
    private static final DateValidator dateValidator1= new DateValidatorUsingDateFormat("d-MMM-yyyy");
    private static final DateValidator dateValidator2 = new DateValidatorUsingLocalDate(dateTimeFormatter2);
    public static LocalDate parseYMD(String date) {
        if (dateValidator.isValid(date)) {
            return LocalDate.parse(date, dateTimeFormatter);
        } else if (dateValidator1.isValid(date)) {
            return LocalDate.parse(date, dateTimeFormatter1);
        } else if (dateValidator2.isValid(date)) {
            return LocalDate.parse(date, dateTimeFormatter2);
        } else {
            throw new IllegalArgumentException("The dateFrom contains an invalid data"
                    + ". Skipping the row. Current data formats: yyyy-MM-dd | d-MMM-yyyy | BASIC ISO DATE");
        }
    }
}
