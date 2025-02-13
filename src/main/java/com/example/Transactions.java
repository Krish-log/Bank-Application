package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.*;

class Transactions {
    protected Accounts.Account account;
    private Scanner sc;
    private static final String TRANSACTIONS_FILE = "data/Transactions.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<Transaction> transactions; // ✅ Declare transactions globally
    public static final String RESET = "\033[0m"; // Reset to normal

    // ✅ Load transactions from JSON on startup
    static {
        transactions = loadTransactions();
        if (transactions == null) {
            transactions = new ArrayList<>(); // ✅ Prevent NullPointerException
        }
    }

    public Transactions(Accounts.Account account, Scanner scanner) {
        this.account = account;
        this.sc = scanner;
    }

    public void performBankingOperations() {
        System.out.println("\n***************************************************");
        System.out.print("Would you like to perform any transaction (Y / N)? : ");
        System.out.println("\n***************************************************\n");
        char performTransaction = sc.next().charAt(0);

        if (performTransaction == 'Y' || performTransaction == 'y') {
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
                    case 1: 
                        deposit();
                        break;
                    case 2: 
                        withdraw();
                        break;
                    case 3: 
                        showBalance();
                        break;
                    case 4: 
                        showTransactionHistory();
                        break;
                    case 5: 
                        exit = true;
                        System.out.println("Thank you!");
                        System.exit(0);
                        break;
                    default: 
                        System.out.println("Invalid choice. Please select a valid option");
                        performBankingOperations();
                        break;
                }
            }
        } else {
            System.out.println("Thank you for choosing our bank!");
            System.exit(0);
        }
    }

    public void deposit() {
        System.out.print("Enter the amount to deposit: ");
        double amount = sc.nextDouble();
        if (amount > 0) {
            double newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);
            recordTransaction("Credit", amount);
            System.out.println("✅ Successfully deposited: " + amount);
            showBalance();
        } else {
            System.out.println("❌ Deposit amount must be greater than zero.");
        }
    }

    public void withdraw() {
        System.out.print("Enter the amount to withdraw: ");
        double amount = sc.nextDouble();
        if (amount > 0 && amount <= account.getBalance()) {
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
            recordTransaction("Debit", amount );
            System.out.println("✅ Successfully withdrew: " + amount);
            showBalance();
        } else if (amount > account.getBalance()) {
            System.out.println("❌ Insufficient balance for withdrawal.");
        } else {
            System.out.println("❌ Withdrawal amount must be greater than zero.");
        }
    }

    public void showBalance() {
        System.out.println("💰 Current balance: " + account.getBalance());
    }

    // ✅ Transaction class for structured storage
    static class Transaction {
        private final String date;
        private final String userId;
        private final int accountId;
        private final String type;
        private final double amount;
        private final double balance;

        public Transaction(String userId, int accountId, String type, double amount, double balance) {
            this.date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            this.userId = userId;
            this.accountId = accountId;
            this.type = type;
            this.amount = amount;
            this.balance = balance;
        }
    }

    // ✅ Save transactions to JSON file
    public void recordTransaction(String transactionType, double amount) {
        if (transactions == null) { // ✅ Ensure transactions list is initialized
            transactions = new ArrayList<>();
        }

        // ✅ Create new transaction
        Transaction transaction = new Transaction(account.getUser().getUserId(), account.getAccNumber(), transactionType, amount, account.getBalance());
        transactions.add(transaction);

        // ✅ Ensure directory exists before saving
        File file = new File(TRANSACTIONS_FILE);
        file.getParentFile().mkdirs(); // ✅ Creates the "data" directory if it doesn't exist

        // ✅ Save transactions to file
        try (Writer writer = new FileWriter(TRANSACTIONS_FILE)) {
            gson.toJson(transactions, writer);
        } catch (IOException e) {
            System.err.println("❌ Error saving transactions: " + e.getMessage());
        }

        // ✅ Save updated balance to JSON
        Accounts.saveAccountsToJson();
    }

    // ✅ Load transactions from JSON file
    public static List<Transaction> loadTransactions() {
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<List<Transaction>>() {}.getType());
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ✅ Show only transactions for the logged-in user
    public void showTransactionHistory() {
        List<Transaction> allTransactions = loadTransactions();
        List<Transaction> userTransactions = new ArrayList<>();
    
        // ✅ Filter transactions for the logged-in user
        for (Transaction transaction : allTransactions) {
            if (transaction.userId.equals(account.getUser().getUserId())) {
                userTransactions.add(transaction);
            }
        }
    
        if (userTransactions.isEmpty()) {
            System.out.println("\nNo transactions found for User: " + account.getUser().getUserId());
        } else {
            System.out.println("\n\033[1mTRANSACTION HISTORY:\033[0m"); // Bold heading
    
            // ✅ Print Table Header
            System.out.println("+---------------------+----------+-----------+--------+----------+----------+");
            System.out.printf("| %-19s | %-8s | %-9s | %-6s | %-8s | %-8s |\n", 
                              "Date", "UserID", "AccountID", "Type", "Amount", "Balance");
            System.out.println("+---------------------+----------+-----------+--------+----------+----------+");
    
            // ✅ Print Transactions with Color Formatting
            for (Transaction transaction : userTransactions) {
                String colorCode = transaction.type.equalsIgnoreCase("Credit") ? "\033[32m" : "\033[31m"; // Green for Credit, Red for Debit
                String resetColor = "\033[0m"; // Reset color after printing
    
                System.out.printf("| %-19s | %-8s | %-9d | %-6s | %s%-8.2f%s | %-8.2f |\n",
                        transaction.date, transaction.userId, transaction.accountId, 
                        transaction.type, colorCode, transaction.amount, resetColor, transaction.balance);
            }
    
            // ✅ Print Table Footer
            System.out.println("+---------------------+----------+-----------+--------+----------+----------+");
        }
    }    
}
