import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserInputValidation {
    
    // Validate UserID: Alphanumeric, 6-12 characters
    public static boolean isValidUserID(String userID) {
        return userID.matches("^[a-zA-Z0-9]{6,12}$");
    }

    // Validate Name: Only letters, max 50 characters
    public static boolean isValidName(String name) {
        return name.matches("^[A-Za-z]{1,50}$");
    }

    // Validate Password: At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character
    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    // Validate Phone Number: 10-digit numeric
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{10}$");
    }

    // Validate Address: Minimum 10 characters
    public static boolean isValidAddress(String address) {
        return address.length() >= 10;
    }

    // Validate Date of Birth: Format (yyyy-MM-dd), Must be 18+ years old
    public static boolean isValidDateOfBirth(String dob) {
        try {
        dob = dob.trim();
            
        // Parse the input date using the specified format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(dob, formatter);

        // Get today's date
        LocalDate today = LocalDate.now();

        // Check if the person is 18 or older
        int age = Period.between(birthDate, today).getYears();
        
        // Return true if the person is 18 or older
        return age >= 18;

        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
}
