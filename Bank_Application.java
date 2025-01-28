package practice;

import java.util.*;

class Users {
    public class User {
        Scanner sc = new Scanner(System.in);

        private int userId;
        private String accHolderName;
        private long userPhoneNumber;

        public User() {
            Random random = new Random();

            System.out.println("\t\t\tWELCOME TO OUR BANK");
            System.out.println("==============================================\n");
            System.out.println("Do you want to register as a user (Y / N): ");
            char isUser = sc.next().charAt(0);

            if (isUser == 'Y' || isUser == 'y') {
                System.out.println("Please provide the following details:");

                userId = random.nextInt(9000) + 1000;

                System.out.print("Enter your name: ");
                sc.nextLine();
                accHolderName = sc.nextLine();

                System.out.print("Enter your 10-digit phone number: ");
                userPhoneNumber = sc.nextLong();

                System.out.println("+++++++++++++++ \nUser ID created successfully!");
                displayUserDetails();
            } else {
                System.out.println("User ID creation cancelled. Thank you for visiting our bank!!!");
                System.exit(0);
            }
        }

        public void displayUserDetails() {
            System.out.println("\nUSER DETAILS:");
            System.out.println("--------------");
            System.out.println("User ID: " + userId);
            System.out.println("Name: " + accHolderName);
            System.out.println("Phone: " + userPhoneNumber);
            System.out.println("--------------\n");
        }

        public int getUserId() {
            return userId;
        }

        public String getAccHolderName() {
            return accHolderName;
        }

        public long getUserPhoneNumber() {
            return userPhoneNumber;
        }
    }
}

class Accounts extends Users {
    private HashMap<Integer, User> accountMap = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    public class Account {
        private int accNumber;
        protected User user;
        private double initialAmount = 0;

        public Account(User user) {
            Random random = new Random();
            this.user = user;

            System.out.println("Would you like to create an account (Y / N)? : ");
            char isAcc = sc.next().charAt(0);

            if (isAcc == 'Y' || isAcc == 'y') {
                accNumber = random.nextInt(90000) + 10000;
                accountMap.put(accNumber, user);

                System.out.println("Account Number: " + accNumber);
                System.out.println("Account created successfully!");

                displayAccountDetails(accNumber);
            } else {
                System.out.println("Account creation canceled.");
            }
        }

        public void displayAccountDetails(int accNumber) {
            User associatedUser = accountMap.get(accNumber);
            if (associatedUser != null) {
                System.out.println("\nACCOUNT DETAILS:");
                System.out.println("--------------\nAccount Number: " + accNumber);
                System.out.println("User ID: " + associatedUser.getUserId());
                System.out.println("Name: " + associatedUser.getAccHolderName());
                System.out.println("Phone: " + associatedUser.getUserPhoneNumber());
                System.out.println("Initial Amount: " + initialAmount);
            } else {
                System.out.println("No account found for the given account number.");
            }
        }
        public double getBalance() {
            return initialAmount;
        }

        public void setBalance(double balance) {
            this.initialAmount = balance;
        }

        public int getAccNumber() {
            return accNumber;
        }
    }

    public HashMap<Integer, User> getAccountMap() {
        return accountMap;
    }

    public static void main(String[] args) {
        Accounts accounts = new Accounts();
        User user = accounts.new User();
        Account account = accounts.new Account(user);

        BankApp bankApp = new BankApp(account);
        bankApp.performBankingOperations();
    }
}

class BankApp {
    private Accounts.Account account;
    private Scanner sc = new Scanner(System.in);
    private List<String> transactionHistory = new ArrayList<>(); // To store transaction details

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

    // Function to record transactions
    public void recordTransaction(String transactionType, double amount) {
        String transaction = String.format(
                "Account ID: %d, User ID: %d, Type: %s, Amount: %.2f, Balance: %.2f",
                account.getAccNumber(),
                account.user.getUserId(),
                transactionType,
                amount,
                account.getBalance()
        );
        transactionHistory.add(transaction);
    }

    // Function to display transaction history
    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\nTRANSACTION HISTORY:");
            System.out.println("---------------------");
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    public void performBankingOperations() {
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
                    System.out.println("Thank you for using our banking services!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
