import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Accounts extends Users {
    private static final String ACCOUNTS_JSON_FILE = "Accounts.json";
    public static Map<String, Account> accountMap = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    public static class Account {
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

        public String getUserID() {
            return user.getUserId();
        }

        // static {
        //     System.out.println("🔹 Initializing Accounts...");
        //     accountMap = loadAccountsFromJson();
        
        //     if (accountMap.isEmpty()) {
        //         System.out.println("⚠️ No accounts loaded from JSON.");
        //     } else {
        //         System.out.println("✅ " + accountMap.size() + " accounts loaded successfully.");
        //     }
        // }
        

        public Account(User user, int accNumber, double balance) {
            Random random = new Random();
            this.user = user;
            this.accNumber = accNumber;
            this.initialAmount = balance;
            
            System.out.println("Would you like to create an account (Y / N)? : ");
            Scanner sc = new Scanner(System.in);
            char isAcc = sc.next().charAt(0);

            if (isAcc == 'Y' || isAcc == 'y') {
                accNumber = random.nextInt(90000) + 10000;
                accountMap.put(user.getUserId(), this);
                Accounts.saveAccountsToJson();

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

    public static void saveAccountsToJson() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_JSON_FILE))) {
            writer.write("{\n"); // Start JSON object
    
            int count = 0;
            int size = accountMap.size();
    
            for (Map.Entry<String, Account> entry : accountMap.entrySet()) {
                Account acc = entry.getValue();
                writer.write("  \"" + acc.getUserID() + "\": {\n"); // Use UserID as key
                writer.write("    \"Account Number\": " + acc.accNumber + ",\n");
                writer.write("    \"Balance\": " + acc.getBalance() + "\n");
                writer.write("  }" + (++count < size ? "," : "") + "\n");
            }
    
            writer.write("}\n"); // End JSON object
        } catch (IOException e) {
            e.printStackTrace();
        }
    }               
    
    public static Map<String, Account> loadAccountsFromJson() {
        Map<String, Account> accounts = new HashMap<>();
        File file = new File(ACCOUNTS_JSON_FILE);
    
        if (!file.exists()) {
            System.out.println("⚠️ Accounts.json file not found. Creating a new one...");
            return accounts;
        }
    
        System.out.println("🔹 Loading accounts from JSON...");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
    
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line.trim());
            }
    
            String json = jsonContent.toString();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                System.out.println("❌ Invalid JSON format!");
                return accounts;
            }
    
            json = json.substring(1, json.length() - 1).trim(); // Remove outer {}
    
            String[] accountEntries = json.split("},\\s*\"");
    
            for (String accountEntry : accountEntries) {
                accountEntry = accountEntry.trim();
                if (accountEntry.isEmpty()) continue;
    
                int colonIndex = accountEntry.indexOf(":");
                if (colonIndex == -1) continue;
    
                String userId = accountEntry.substring(0, colonIndex).replace("\"", "").trim();
                String accountJson = accountEntry.substring(colonIndex + 1).trim();
    
                if (accountJson.startsWith("{")) accountJson = accountJson.substring(1);
                if (accountJson.endsWith("}")) accountJson = accountJson.substring(0, accountJson.length() - 1);
    
                String accNumberStr = extractJsonValue(accountJson, "Account Number");
                String balanceStr = extractJsonValue(accountJson, "Balance");
    
                int accNumber = Integer.parseInt(accNumberStr);
                double balance = Double.parseDouble(balanceStr);
    
                // 🔗 Link User ID from Users.json
                User user = Users.User.findUserById(userId);
                if (user == null) {
                    System.out.println("⚠️ User not found for Account ID: " + accNumber);
                    continue;
                }
    
                // ✅ Create an account and associate it with the user
                Account account = new Account(user, accNumber, balance); // ✅ Update constructor
                accounts.put(userId, account);
    
                // ✅ Debug: Print each loaded account
                System.out.println("✅ Loaded Account: " + userId + " → Account Number: " + accNumber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        System.out.println("✅ Total Accounts Loaded: " + accounts.size());
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
    

        public static Account findAccountByUser(User user) {
        System.out.println("🔹 Searching for account of UserID: " + user.getUserId());
        System.out.println("🔹 Current Accounts in Memory: " + accountMap.keySet());

        if (accountMap.isEmpty()) {
            System.out.println("⚠️ ERROR: accountMap is empty. Something went wrong while loading accounts!");
        }

        Account account = accountMap.get(user.getUserId()); // 🔍 Direct lookup
        if (account != null) {
            System.out.println("✅ Account found for UserID: " + user.getUserId() + " → Account Number: " + account.getAccNumber());
            return account;
        }

        System.out.println("❌ No account found for UserID: " + user.getUserId());
        return null;
    }
}
