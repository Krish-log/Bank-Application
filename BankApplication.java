import java.io.*;
import java.util.*;

class Accounts extends Users {
                    
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
    
        // Save updated balance to JSON
        Accounts.saveAccountsToJson();
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
        // ✅ Step 3: Authenticate user
        Users.User user = new Users.User();
        System.out.println("✅ Step 3: User logged in with ID: " + user.getUserId());

        // ✅ Step 1: Load users first
        System.out.println("\n🔹 Step 1: Loading users from Users.json...");
        Users.User.userList = Users.User.loadUsersFromJson();
        System.out.println("✅ Users loaded: " + Users.User.userList);

        // ✅ Step 2: Load accounts BEFORE authentication
        System.out.println("\n🔹 Step 2: Loading accounts from Accounts.json...");
        Accounts.accountMap = Accounts.loadAccountsFromJson();
        System.out.println("✅ Accounts loaded: " + Accounts.accountMap.size());

        // ✅ Debug: Print loaded accounts
        for (Map.Entry<String, Accounts.Account> entry : Accounts.accountMap.entrySet()) {
            System.out.println("   - UserID: " + entry.getKey() + " → Account Number: " + entry.getValue().getAccNumber());
        }


        // ✅ Step 4: Lookup user's account
        System.out.println("\n🔹 Step 4: Searching for an account for UserID: " + user.getUserId());
        Accounts.Account account = Accounts.findAccountByUser(user);

        if (account == null) {
            System.out.println("❌ No existing account found for User: " + user.getUserId());
            account = new Accounts.Account(user, new Random().nextInt(90000) + 10000, 0.0);
            Accounts.accountMap.put(user.getUserId(), account);
            Accounts.saveAccountsToJson();
        } else {
            System.out.println("✅ Account found: " + account.getAccNumber());
        }

        // ✅ Step 5: Proceed with banking operations
        BankApp bankApp = new BankApp(account);
        bankApp.performBankingOperations();
    }
}
