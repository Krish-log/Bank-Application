import java.io.*;
import java.util.*;

public class Users {
    public static class User {
        private String userId;
        private String firstName;
        private String lastName;
        private String password;
        private long mobileNumber;
        private String userAddress;
        private String userDOB;

        private static final String USERS_JSON_FILE = "Users.json";
        public static List<User> userList = loadUsersFromJson();
        private Scanner sc;

        public User(String userId, String firstName, String lastName, String password, String userAddress, long mobileNumber, String userDOB) {
            this.userId = userId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.userAddress = userAddress;
            this.mobileNumber = mobileNumber;
            this.userDOB = userDOB;
        }
        

        public User() {
            Random random = new Random();
            sc = new Scanner(System.in);
            Console console = System.console();
            
            System.out.println("\t\t\tWELCOME TO OUR BANK");
            System.out.println("==============================================\n");
            
            System.out.println("Are you an existing user? (Y/N): ");
            char isExistingUser = sc.next().charAt(0);
            sc.nextLine();  // Consume the leftover newline!

            
            if (isExistingUser == 'Y' || isExistingUser == 'y') {
                int attempts = 3;
                boolean isAuthenticated = false;
                User existingUser = null;
            
                while (attempts > 0 && !isAuthenticated) {
                    System.out.println("\nPlease provide credentials to login.");
                    
                    // Prompt for UserID and Password
                    System.out.print("Enter UserID:");
                    System.out.flush();
                    String enteredUserId = sc.nextLine().trim();

                    System.out.print("Enter Password:");
                    System.out.flush();
                    String enteredPassword = new String(console.readPassword());
                    
                    // Attempt to authenticate
                    existingUser = findUserById(enteredUserId);
                    if (existingUser != null && existingUser.password.equals(enteredPassword)) {
                        isAuthenticated = true;
                        break;
                    } else {
                        attempts--;
                        System.out.println("Incorrect Credentials. Attempts left: " + attempts);
                    }
                }                
            
                // If authentication fails after 3 attempts, exit
                if (!isAuthenticated) {
                    System.out.println("Too many failed attempts. Exiting...");
                    System.exit(0);
                }
            
                // ✅ Successful login
                System.out.println("Welcome back, " + existingUser.getUserFirstName() + " " + existingUser.getUserLastName() + "!");
            
                // Copy user details to current user object
                this.userId = existingUser.getUserId();
                this.firstName = existingUser.getUserFirstName();
                this.lastName = existingUser.getUserLastName();
                this.password = existingUser.getPassword();
                this.mobileNumber = existingUser.getUserPhoneNumber();
                this.userAddress = existingUser.getUserAddress();
                this.userDOB = existingUser.getUserDOB();
            
                // 🔍 Check if user has an account
                Accounts.Account account = Accounts.findAccountByUser(this);

                System.out.println(account);
            
                if (account != null) {
                    account.displayAccountDetails();
                } else {
                    System.out.println("No account found for the user.");
                    System.out.println("************************************\n");
                }
            } else {
                registerNewUser(random);
            }            
        }                

        private void registerNewUser(Random random) {
            System.out.print("Would you like to register? (Y/N): ");
            char isNewUser = sc.next().charAt(0);
            sc.nextLine();

            if (isNewUser == 'Y' || isNewUser == 'y') {
                do {
                    System.out.print("Enter UserID (6-12 alphanumeric characters): ");
                    this.userId = sc.nextLine();
                } while (!UserInputValidation.isValidUserID(this.userId));

                do {
                    System.out.print("Enter First Name: ");
                    this.firstName = sc.nextLine();
                } while (!UserInputValidation.isValidName(this.firstName));

                do {
                    System.out.print("Enter Last Name: ");
                    this.lastName = sc.nextLine();
                } while (!UserInputValidation.isValidName(this.lastName));

                do {
                    System.out.print("Enter Password (Min 8 chars, 1 uppercase, 1 digit, 1 special char): ");
                    this.password = sc.nextLine();
                } while (!UserInputValidation.isValidPassword(this.password));

                do {
                    System.out.print("Enter Phone Number (10 digits): ");
                    String phoneNumber = sc.nextLine();
                    if (UserInputValidation.isValidPhoneNumber(phoneNumber)) {
                        this.mobileNumber = Long.parseLong(phoneNumber);
                        break;
                    }
                } while (true);

                do {
                    System.out.print("Enter Address (Min 10 characters): ");
                    this.userAddress = sc.nextLine();
                } while (!UserInputValidation.isValidAddress(this.userAddress));

                do {
                    System.out.print("Enter Date of Birth (YYYY-MM-DD, Must be 18+): ");
                    this.userDOB = sc.nextLine();
                } while (!UserInputValidation.isValidDateOfBirth(this.userDOB));
                
                userList.add(this);
                saveUsersToJson();

                System.out.println("+++++++++++++++ \nUser ID created successfully!");
                displayUserDetails(this);
            } else {
                System.out.println("Thank you for choosing our bank!");
                System.exit(0);
            }
        }

        public static User findUserById(String userId) {
            for (User user : userList) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }        

        public void displayUserDetails(User user) {
            System.out.println("\nUSER DETAILS:");
            System.out.println("--------------");
            System.out.println("UserID: " + getUserId());
            System.out.println("First Name: " + getUserFirstName());
            System.out.println("Last Name: " + getUserLastName());
            System.out.println("Password: " + getPassword()); // Masking password
            System.out.println("Phone Number: " + getUserPhoneNumber());
            System.out.println("Address: " + getUserAddress());
            System.out.println("Date of Birth: " + getUserDOB());
            System.out.println("--------------\n");
        }

        //getter methods for user details
        public String getUserId() {
            return this.userId;
        }

        public String getUserFirstName() {
            return this.firstName;
        }

        public String getUserLastName() {
            return this.lastName;
        }

        public long getUserPhoneNumber() {
            return this.mobileNumber;
        }
        
        public String getUserAddress() {
            return this.userAddress;
        }

        public String getPassword() {
            return (this.password != null) ? "*".repeat(this.password.length()) : "N/A";
        }

        public String getUserDOB() {
            return this.userDOB;
        }

        private static void saveUsersToJson() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_JSON_FILE))) {
                writer.write("{\n"); // Start JSON object
        
                for (int i = 0; i < userList.size(); i++) {
                    User user = userList.get(i);
                    writer.write("  \"" + user.userId + "\": {\n"); // UserID as key
                    writer.write("    \"First Name\": \"" + user.firstName + "\",\n");
                    writer.write("    \"Last Name\": \"" + user.lastName + "\",\n");
                    writer.write("    \"Date of Birth\": \"" + user.userDOB + "\",\n");
                    writer.write("    \"Password\": \"" + user.password + "\",\n");
                    writer.write("    \"Mobile Number\": \"" + user.mobileNumber + "\",\n");
                    writer.write("    \"Address\": \"" + user.userAddress + "\"\n");
                    writer.write("  }" + (i < userList.size() - 1 ? "," : "") + "\n");
                }
        
                writer.write("}\n"); // End JSON object
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static List<User> loadUsersFromJson() {
            List<User> users = new ArrayList<>();
            File file = new File(USERS_JSON_FILE);
        
            if (!file.exists()) {
                return users;
            }
        
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;
        
                // Read the entire JSON file into a string
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line.trim());
                }
        
                String json = jsonContent.toString();
        
                // Ensure JSON is correctly formatted
                if (!json.startsWith("{") || !json.endsWith("}")) {
                    return users;
                }
        
                json = json.substring(1, json.length() - 1).trim(); // Remove outer {}
        
                String[] userEntries = json.split("},\\s*\"");
        
                for (String userEntry : userEntries) {
                    userEntry = userEntry.trim();
                    if (userEntry.isEmpty()) continue;
        
                    // Extract userId
                    int colonIndex = userEntry.indexOf(":");
                    if (colonIndex == -1) continue;
        
                    String userId = userEntry.substring(0, colonIndex).replace("\"", "").trim();
                    String userJson = userEntry.substring(colonIndex + 1).trim();
        
                    // Remove extra braces
                    if (userJson.startsWith("{")) userJson = userJson.substring(1);
                    if (userJson.endsWith("}")) userJson = userJson.substring(0, userJson.length() - 1);
        
                    // Extract details
                    String firstName = extractJsonValue(userJson, "First Name");
                    String lastName = extractJsonValue(userJson, "Last Name");
                    String userDOB = extractJsonValue(userJson, "Date of Birth");
                    String password = extractJsonValue(userJson, "Password");
                    String mobileStr = extractJsonValue(userJson, "Mobile Number");
                    String userAddress = extractJsonValue(userJson, "Address");
        
                    long mobileNumber = mobileStr.isEmpty() ? 0 : Long.parseLong(mobileStr);
        
                    User user = new User(userId, firstName, lastName, password, userAddress, mobileNumber, userDOB);
                    users.add(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return users;
        }

        /**
         * Extracts a value from a manually parsed JSON string.
         */
        private static String extractJsonValue(String json, String key) {
            String searchKey = "\"" + key + "\":";
            int startIndex = json.indexOf(searchKey);
            
            if (startIndex == -1) {
                return ""; // Key not found
            }
        
            startIndex += searchKey.length(); // Move index past the key
        
            // Find the first non-space and non-quote character
            while (startIndex < json.length() && (json.charAt(startIndex) == ' ' || json.charAt(startIndex) == '\"')) {
                startIndex++;
            }
        
            // Identify the end of the value
            int endIndex = startIndex;
            while (endIndex < json.length() && json.charAt(endIndex) != ',' && json.charAt(endIndex) != '}') {
                endIndex++;
            }
        
            // Trim unnecessary characters
            return json.substring(startIndex, endIndex).replace("\"", "").trim();
        }                               
    }
}