// Version 2 - Banking Application (Casual Style)
import java.io.*;
import java.util.*;

// Exception classes for validation
class LowBalanceError extends Exception {
    public LowBalanceError(String msg) {
        super(msg);
    }
}

class NotEnoughMoneyError extends Exception {
    public NotEnoughMoneyError(String msg) {
        super(msg);
    }
}

class InvalidIDError extends Exception {
    public InvalidIDError(String msg) {
        super(msg);
    }
}

class InvalidAmountError extends Exception {
    public InvalidAmountError(String msg) {
        super(msg);
    }
}

// Account holder details
class AccountHolder implements Serializable {
    private int id;
    private String name;
    private double balance;
    
    public AccountHolder(int id, String name, double balance) throws InvalidIDError, 
                                                                      LowBalanceError, 
                                                                      InvalidAmountError {
        if (id < 1 || id > 20) {
            throw new InvalidIDError("ID should be from 1 to 20 only!");
        }

        if (balance < 1000) {
            throw new LowBalanceError("You need at least Rs. 1000 to open an account");
        }

        if (balance < 0) {
            throw new InvalidAmountError("Balance can't be negative!");
        }
        
        this.id = id;
        this.name = name;
        this.balance = balance;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void addMoney(double amt) throws InvalidAmountError {
        if (amt < 0) {
            throw new InvalidAmountError("Can't add negative amount!");
        }
        balance += amt;
    }
    
    public void takeMoney(double amt) throws NotEnoughMoneyError, InvalidAmountError {
        if (amt < 0) {
            throw new InvalidAmountError("Can't withdraw negative amount!");
        }
        
        if (amt > balance) {
            throw new NotEnoughMoneyError("Not enough money! You have only Rs. " + balance);
        }
        
        balance -= amt;
    }
    
    public String toString() {
        return "ID: " + id + " || Name: " + name + " || Balance: Rs. " + balance;
    }
}

// Main program
public class Version2_BankingSystem {
    static final String DATA_FILE = "bank_accounts_v2.dat";
    static ArrayList<AccountHolder> accounts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        loadData();
        
        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("What would you like to do? ");
            int option = sc.nextInt();
            sc.nextLine(); // clear buffer
            
            try {
                switch (option) {
                    case 1:
                        openNewAccount();
                        break;
                    case 2:
                        addMoneyToAccount();
                        break;
                    case 3:
                        takeMoneyFromAccount();
                        break;
                    case 4:
                        showAllAccounts();
                        break;
                    case 5:
                        findAccount();
                        break;
                    case 6:
                        saveData();
                        System.out.println("Thanks for banking with us!");
                        running = false;
                        break;
                    default:
                        System.out.println("Wrong choice! Try again.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
            
            System.out.println();
        }
    }
    
    static void showMenu() {
        System.out.println("\n*** BANK MENU ***");
        System.out.println("1. Open New Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Show All Accounts");
        System.out.println("5. Find Account");
        System.out.println("6. Exit & Save");
        System.out.println("*****************");
    }
    
    static void openNewAccount() throws InvalidIDError, LowBalanceError, InvalidAmountError {
        System.out.println("\n>> Opening New Account");
        System.out.print("Enter ID (1-20): ");
        int id = sc.nextInt();
        sc.nextLine();
        
        // make sure ID isn't already taken
        for (AccountHolder acc : accounts) {
            if (acc.getId() == id) {
                System.out.println("This ID is already taken!");
                return;
            }
        }
        
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        
        System.out.print("Enter Initial Amount (min 1000): ");
        double amt = sc.nextDouble();
        
        AccountHolder newAcc = new AccountHolder(id, name, amt);
        accounts.add(newAcc);
        
        System.out.println("SUCCESS! Account created.");
        System.out.println(newAcc);
    }
    
    static void addMoneyToAccount() throws InvalidAmountError {
        System.out.println("\n>> Deposit Money");
        System.out.print("Enter Account ID: ");
        int id = sc.nextInt();
        
        AccountHolder acc = searchById(id);
        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }
        
        System.out.print("How much to deposit? Rs. ");
        double amt = sc.nextDouble();
        
        acc.addMoney(amt);
        System.out.println("Money added successfully!");
        System.out.println(acc);
    }
    
    static void takeMoneyFromAccount() throws NotEnoughMoneyError, InvalidAmountError {
        System.out.println("\n>> Withdraw Money");
        System.out.print("Enter Account ID: ");
        int id = sc.nextInt();
        
        AccountHolder acc = searchById(id);
        if (acc == null) {
            System.out.println("Account not found!");
            return;
        }
        
        System.out.print("How much to withdraw? Rs. ");
        double amt = sc.nextDouble();
        
        acc.takeMoney(amt);
        System.out.println("Withdrawal complete!");
        System.out.println(acc);
    }
    
    static void showAllAccounts() {
        System.out.println("\n>> All Accounts");
        if (accounts.isEmpty()) {
            System.out.println("No accounts yet!");
            return;
        }
        
        for (AccountHolder acc : accounts) {
            System.out.println(acc);
        }
    }
    
    static void findAccount() {
        System.out.println("\n>> Find Account");
        System.out.print("Enter Account ID: ");
        int id = sc.nextInt();
        
        AccountHolder acc = searchById(id);
        if (acc == null) {
            System.out.println("No account with this ID!");
        } else {
            System.out.println(acc);
        }
    }
    
    static AccountHolder searchById(int id) {
        for (AccountHolder acc : accounts) {
            if (acc.getId() == id) {
                return acc;
            }
        }
        return null;
    }
    
    static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(accounts);
            System.out.println("All data saved!");
        } catch (IOException e) {
            System.out.println("Couldn't save data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    static void loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            return;
        }
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            accounts = (ArrayList<AccountHolder>) in.readObject();
            System.out.println("Previous data loaded!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data available.");
        }
    }
}
