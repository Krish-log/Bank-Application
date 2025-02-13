package com.example;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserInputValidation {
    
    private static final int MAX_ATTEMPTS = 3;
    private static int loginAttempts = 0;
    
    // Regex patterns
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[A-Za-z0-9]{6,12}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{2,50}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern DOB_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public static boolean validateUserId(String userId) {
        return userId != null && !userId.trim().isEmpty() && USER_ID_PATTERN.matcher(userId).matches();
    }
    
    public static boolean validateName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }
    
    public static boolean validatePassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    public static boolean validateMobileNumber(String mobileNumber) {
        return mobileNumber != null && MOBILE_PATTERN.matcher(mobileNumber).matches();
    }
    
    public static boolean validateUserDOB(String dob) {
        if (dob == null || !DOB_PATTERN.matcher(dob).matches()) {
            return false;
        }
        try {
            LocalDate birthDate = LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();
            return birthDate.isBefore(today.minusYears(18)); // Must be at least 18 years old
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public static boolean validateUserAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.length() <= 255;
    }
    
    public static boolean validateLoginAttempts() {
        loginAttempts++;
        if (loginAttempts >= MAX_ATTEMPTS) {
            System.out.println("Account locked due to multiple failed attempts.");
            return false;
        }
        return true;
    }
    
    public static void resetLoginAttempts() {
        loginAttempts = 0;
    }
}
