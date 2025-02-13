package com.example;

import java.util.Scanner;

public class Users {
    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String mobileNumber;
    private String userAddress;
    private String userDOB;
    private static final String BOLD = "\033[1m";
    public static final String RESET = "\033[0m"; // Reset to normal
    private static final String GREEN = "\033[32m"; // Green color
    private static final String RED = "\033[31m";   // Red color

    // ✅ Constructor for new user registration
    public Users(String userId, String firstName, String lastName, String password, String mobileNumber, String userAddress, String userDOB) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.userAddress = userAddress;
        this.userDOB = userDOB;
    }

    // ✅ Method for user authentication (Login)
    public static Users authenticateUser() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(BOLD + "\n┌────────────────────────────────────────────┐");
        System.out.println(BOLD + "│          GOLDEN FEDERAL CREDIT UNION       │");
        System.out.println(BOLD + "└────────────────────────────────────────────┘" + RESET);
        
        System.out.print("\nAre you an existing user? (Y/N): ");
        char isExistingUser = scanner.next().charAt(0);
        scanner.nextLine();  // Consume newline

        if (isExistingUser == 'Y' || isExistingUser == 'y') {
            return loginUser(scanner);
        } else {
            return registerUser(scanner);
        }
    }

    // ✅ Login an existing user (Boxed UI)
    private static Users loginUser(Scanner scanner) {
        int attempts = 3;

        while (attempts > 0) {
            System.out.println(BOLD + "\n┌────────────────── User Login ──────────────────┐" + RESET);
            System.out.print("│ Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("│ Enter Password: ");
            String password = scanner.nextLine();
            System.out.println("└───────────────────────────────────────────────┘");

            Users user = UserDatabase.findUserById(userId);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println(GREEN + "\nLogin Successful. Welcome, " + user.getFirstName() + "!" + RESET);
                return user;
            } else {
                attempts--;
                System.out.println(RED + "\nIncorrect credentials. Attempts left: " + attempts + RESET);
            }
        }

        System.out.println("Too many failed attempts. Exiting...");
        System.exit(0);
        return null;  // This line will never execute, but is required to prevent compilation errors
    }

    // ✅ Register a new user with Unique UserID & Mobile Number Check
    private static Users registerUser(Scanner scanner) {
        System.out.print("\nWould you like to register as a new user? (Y/N): ");
        char register = scanner.next().charAt(0);
        scanner.nextLine();  // Consume newline

        if (register == 'Y' || register == 'y') {
            String userId, mobileNumber;

            // ✅ Ensure Unique UserID
            do {
                System.out.println("\n┌────────────────── User Registration ──────────────────┐");
                System.out.print("\nEnter User ID (6-12 alphanumeric characters): ");
                userId = scanner.nextLine();
                if (!UserInputValidation.validateUserId(userId)) {
                    System.out.println("Invalid User ID! Must be 6-12 alphanumeric characters.");
                    continue;
                }
                if (UserDatabase.findUserById(userId) != null) {
                    System.out.println("User ID already exists! Try a different one.");
                } else {
                    break;
                }
            } while (true);

            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            // ✅ Ensure Unique Mobile Number
            do {
                System.out.print("Enter Mobile Number: ");
                mobileNumber = scanner.nextLine();
                if (!UserInputValidation.validateMobileNumber(mobileNumber)) {
                    System.out.println("Invalid Mobile Number! Must be 10 digits.");
                    continue;
                }
                if (UserDatabase.isMobileNumberExists(mobileNumber)) {
                    System.out.println("Mobile Number is already registered! Try a different one.");
                } else {
                    break;
                }
            } while (true);

            System.out.print("Enter Address: ");
            String address = scanner.nextLine();

            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            String dob = scanner.nextLine();

            Users newUser = new Users(userId, firstName, lastName, password, mobileNumber, address, dob);
            UserDatabase.addUser(newUser);
            System.out.println("\nRegistration successful! Your User ID is: " + newUser.getUserId());
            return newUser;
        }
        else if (register == 'N' || register == 'n') {
            System.out.println("Thank you for choosing our bank!");
            System.exit(0);
        }
        else {
            System.out.println("Invalid choice. Please try again.");
            registerUser(scanner);
        }

        return null;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPassword() { return password; }
    public String getMobileNumber() { return mobileNumber; }
    public String getUserAddress() { return userAddress; }
    public String getUserDOB() { return userDOB; }
}
