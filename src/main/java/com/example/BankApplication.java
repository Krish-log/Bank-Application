package com.example;

import java.util.*;

public class BankApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ✅ Step 1: Load users from database
        UserDatabase.loadUsers();

        // ✅ Step 2: Authenticate User (Login or Register)
        Users user = Users.authenticateUser();

        if (user == null) {
            System.out.println("Authentication failed. Exiting...");
            System.exit(0);
        }

        // ✅ Step 3: Load accounts BEFORE checking for user accounts
        Accounts.accountMap = Accounts.loadAccountsFromJson();

        // ✅ Step 4: Lookup user's account
        Accounts.Account account = Accounts.findAccountByUser(user);

        if (account == null) {
            System.out.println("No existing account found for User: " + user.getUserId());

            // ✅ Use createAccount() from Accounts.java
            Accounts.createAccount(user);

            // ✅ Load the newly created account
            account = Accounts.findAccountByUser(user);

            if (account == null) {
                System.out.println("Account creation failed! Exiting...");
                System.exit(0);
            }
        } else {
            System.out.println("Account found: " + account.getAccNumber());
        }

        // ✅ Step 5: Proceed with banking operations
        Transactions bankApp = new Transactions(account, scanner);
        bankApp.performBankingOperations();

        scanner.close();
    }
}
