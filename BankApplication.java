import java.io.*;
import java.util.*;

class Users {
    public static class User {
        private String userId;
        private String firstName;
        private String lastName;
        private String password;
        private long mobileNumber;
        private String userAddress;
        private String userDOB;

        private static final String USERS_JSON_FILE = "Users.json";
        private static List<User> userList = loadUsersFromJson();
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
            //Console console = System.console();
            
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
                    System.out.println("Enter UserID:");
                    System.out.flush();
                    String enteredUserId = sc.nextLine().trim();

                    System.out.println("Enter Password:");
                    System.out.flush();
                    String enteredPassword = sc.nextLine();
                    
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
                Accounts accountsInstance = new Accounts();
                Accounts.Account account = accountsInstance.findAccountByUser(this);

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

        private static List<User> loadUsersFromJson() {
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


class Accounts extends Users {
    private static final String ACCOUNTS_JSON_FILE = "Accounts.json";
    private static List<Account> accountList = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public class Account {
        private int accNumber;
        protected User user;
        private double initialAmount = 0;
    
        public int getAccNumber() {
            return accNumber;
        }

        public double getBalance() {
            return initialAmount;
        }

        public void setBalance(double balance) {
            this.initialAmount = balance;
        }

        static {
            if (accountList == null) {
                accountList = loadAccountsFromJson();
            }
        }

        public Account(User user) {
            Random random = new Random();
            this.user = user;
            

            System.out.println("Would you like to create an account (Y / N)? : ");
            char isAcc = sc.next().charAt(0);

            if (isAcc == 'Y' || isAcc == 'y') {
                accNumber = random.nextInt(90000) + 10000;
                accountList.add(this);
                saveAccountsToJson();

                System.out.println("Account Number: " + accNumber);
                System.out.println("Account created successfully!");
                displayAccountDetails();
            } else {
                System.out.println("Account creation canceled.");
                System.out.println("Thank you for choosing our bank!");
                System.exit(0);
            }
        }

        public void displayAccountDetails() {
            System.out.println("\nACCOUNT DETAILS:");
            System.out.println("--------------\n");
            System.out.println("Account Number: " + accNumber);
            System.out.println("User ID: " + user.getUserId());
            System.out.println("Name: " + user.getUserFirstName() + " " + user.getUserLastName());
            System.out.println("Initial Amount: " + initialAmount);
        }
    }

    private static void saveAccountsToJson() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_JSON_FILE))) {
            writer.write("[\n");
            for (int i = 0; i < accountList.size(); i++) {
                Account acc = accountList.get(i);
                writer.write("  {\n");
                writer.write("    \"accNumber\": " + acc.accNumber + ",\n");
                writer.write("    \"userId\": \"" + acc.user.getUserId() + "\",\n");  // Explicitly store userId as a string
                writer.write("    \"initialAmount\": " + acc.initialAmount + "\n");
                writer.write("  }" + (i < accountList.size() - 1 ? "," : "") + "\n");
            }
            writer.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static List<Account> loadAccountsFromJson() {
        List<Account> accounts = new ArrayList<>();
        File file = new File(ACCOUNTS_JSON_FILE);
    
        if (!file.exists()) {
            return accounts; // Return empty list if file doesn't exist
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
    
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line.trim());
            }
    
            String json = jsonContent.toString();
            if (!json.startsWith("[") || !json.endsWith("]")) {
                return accounts;
            }
    
            json = json.substring(1, json.length() - 1).trim();
            String[] accountEntries = json.split("},\\s*\\{");
    
            for (String accountEntry : accountEntries) {
                accountEntry = accountEntry.trim();
                if (accountEntry.isEmpty()) continue;
    
                accountEntry = "{" + accountEntry + "}";
    
                String accNumberStr = extractJsonValue(accountEntry, "accNumber");
                String userId = extractJsonValue(accountEntry, "userId");
                String initialAmountStr = extractJsonValue(accountEntry, "initialAmount");
    
                int accNumber = Integer.parseInt(accNumberStr);
                double initialAmount = Double.parseDouble(initialAmountStr);
    
                User user = Users.User.findUserById(userId);
                if (user == null) {
                    continue;
                }
    
                Accounts accountsInstance = new Accounts();
                Account account = accountsInstance.new Account(user);
                account.accNumber = accNumber;
                account.initialAmount = initialAmount;
                accounts.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return accounts;
    }   
        
        /**
         * Extracts a value from a JSON string.
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
    
            // Extract and return the value, trimming unnecessary characters
            return json.substring(startIndex, endIndex).replace("\"", "").trim();
        }
    

    public Account findAccountByUser(User user) {
    
        for (Account account : accountList) {    
            if (account.user.getUserId().equals(user.getUserId())) {
                return account;
            }
        }
    
        System.out.println("No account found for the user!");
        return null;
    }
    
}

class BankApp {
    private Accounts.Account account;
    private Scanner sc = new Scanner(System.in);
    private List<String> transactionHistory = new ArrayList<>();

    public BankApp(Accounts.Account account) {
        this.account = account;
    }

    public void deposit() {
        System.out.print("Enter the amount to deposit: ");
        double amount = sc.nextDouble();
        if (amount > 0) {
            double newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);
            recordTransaction("Credit", amount);
            System.out.println("Successfully deposited: " + amount);
            showBalance();
        } else {
            System.out.println("Deposit amount must be greater than zero.");
        }
    }

    public void withdraw() {
        System.out.print("Enter the amount to withdraw: ");
        double amount = sc.nextDouble();
        if (amount > 0 && amount <= account.getBalance()) {
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
            recordTransaction("Debit", amount);
            System.out.println("Successfully withdrew: " + amount);
            showBalance();
        } else if (amount > account.getBalance()) {
            System.out.println("Insufficient balance for withdrawal.");
        } else {
            System.out.println("Withdrawal amount must be greater than zero.");
        }
    }

    public void showBalance() {
        System.out.println("Current balance: " + account.getBalance());
    }

    public void recordTransaction(String transactionType, double amount) {
        String transaction = String.format(
                "Account ID: %d, User ID: %s, Type: %s, Amount: %.2f, Balance: %.2f",
                account.getAccNumber(),
                account.user.getUserId(),
                transactionType,
                amount,
                account.getBalance()
        );
        transactionHistory.add(transaction);
    }

    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\nTRANSACTION HISTORY:");
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    public void performBankingOperations() {
        System.out.println("***************************************************");
        System.out.println("Would you like to perform any transaction (Y / N)? : ");
            char performTransaction = sc.next().charAt(0);

            if (performTransaction == 'Y' || performTransaction == 'y') {
                boolean exit = false;
                try (Scanner sc = new Scanner(System.in)) {
                    this.sc = sc;
                    while (!exit) {
                        System.out.println("\nChoose an operation:");
                        System.out.println("1. Deposit");
                        System.out.println("2. Withdraw");
                        System.out.println("3. Show Balance");
                        System.out.println("4. Show Transaction History");
                        System.out.println("5. Exit");
                        System.out.print("Enter your choice: ");
        
                        int choice = sc.nextInt();
                        switch (choice) {
                            case 1: deposit(); break;
                            case 2: withdraw(); break;
                            case 3: showBalance(); break;
                            case 4: showTransactionHistory(); break;
                            case 5: exit = true; System.out.println("Thank you!"); System.exit(0);
                            default: System.out.println("Invalid choice."); 
                        }
                    }
                }
            } else {
                System.out.println("Thank you for choosing our bank!"); System.exit(0);
            }
        boolean exit = false;
        while (!exit) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Show Balance");
            System.out.println("4. Show Transaction History");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: deposit(); break;
                case 2: withdraw(); break;
                case 3: showBalance(); break;
                case 4: showTransactionHistory(); break;
                case 5: exit = true; System.out.println("Thank you!"); System.exit(0);
                default: System.out.println("Invalid choice."); 
            }
        }
    }
}
public class BankApplication {
    public static void main(String[] args) {
        Users.User user = new Users.User();
        Accounts accounts = new Accounts();
        Accounts.Account account = accounts.findAccountByUser(user);
        if (account == null) {
            account = accounts.new Account(user);
        }

        BankApp bankApp = new BankApp(account);
        bankApp.performBankingOperations();
    }
}
