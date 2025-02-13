package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Accounts {
    private static final String ACCOUNTS_JSON_FILE = "data/Accounts.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Map<String, Account> accountMap = new HashMap<>();

    // ✅ Load accounts at startup
    static {
        accountMap = loadAccountsFromJson();
        if (accountMap == null) {
            accountMap = new HashMap<>();  // Fallback to prevent NullPointerException
        }
    }

    public static class Account {
        private int accNumber;
        public Users user;
        private double balance;

        public Users getUser() {
            return user;
        }

        public static Map<String, Account> getAccountMap() {
            return accountMap;
        }

        public Account(Users user, int accNumber, double balance) {
            this.user = user;
            this.accNumber = accNumber;
            this.balance = balance;
        }

        public int getAccNumber() {
            return accNumber;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getUserID() {
            return user.getUserId();
        }      

        public void displayAccountDetails() {
            System.out.println("┌───────────────────────────────────────┐");
            System.out.printf("│ %-35s │\n", "\t\tACCOUNT DETAILS");
            System.out.println("├───────────────────────────────────────┤");
            System.out.printf("│ %-16s : %-14s │\n", "Account Number", accNumber);
            System.out.printf("│ %-16s : %-14s │\n", "User ID", user.getUserId());
            System.out.printf("│ %-16s : %-14s │\n", "Name", user.getFirstName() + " " + user.getLastName());
            System.out.printf("│ %-16s : %-14.2f │\n", "Balance", balance);
            System.out.println("└───────────────────────────────────────┘");
        }
        
    }

    public static void createAccount(Users user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nWould you like to create an account (Y/N)? : ");
        char choice = scanner.next().charAt(0);

        if (choice == 'Y' || choice == 'y') {
            int accNumber = new Random().nextInt(90000) + 10000;
            Account account = new Account(user, accNumber, 0.0);
            Account.getAccountMap().put(user.getUserId(), account);
            saveAccountsToJson();

            System.out.println("Account created successfully!");
            account.displayAccountDetails();
        } else {
            System.out.println("Account creation canceled. Thank you!");
            System.exit(0);
        }
    }

    public static void saveAccountsToJson() {
        try {
            String json = gson.toJson(accountMap);
            Files.write(Paths.get(ACCOUNTS_JSON_FILE), json.getBytes());
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    public static Map<String, Account> loadAccountsFromJson() {
        try {
            if (!Files.exists(Paths.get(ACCOUNTS_JSON_FILE))) {
                return new HashMap<>();
            }
            String json = new String(Files.readAllBytes(Paths.get(ACCOUNTS_JSON_FILE)));
            return gson.fromJson(json, new TypeToken<Map<String, Account>>() {}.getType());
        } catch (IOException e) {
            System.err.println("\nError loading accounts: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static Account findAccountByUser(Users user) {
        if (accountMap == null) {  // Prevent NullPointerException
            System.out.println("\nERROR: accountMap is not initialized! Loading accounts...");
            accountMap = loadAccountsFromJson();
            if (accountMap == null) {
                accountMap = new HashMap<>(); // Ensure it's never null
            }
        }

        System.out.println("\nSearching for account of UserID: " + user.getUserId());
        return accountMap.get(user.getUserId()); // Lookup user in the accounts map
    }
}
