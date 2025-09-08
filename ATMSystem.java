import java.util.Scanner;

class BankAccount{
    private double balance;
    private String accountNumber;
    private String accountHolderName;
    
    public BankAccount(String accountNumber,String accountHolderName,double initialBalance){
        this.accountNumber=accountNumber;
        this.accountHolderName=accountHolderName;
        this.balance=initialBalance;
    }
    
    public double getBalance(){
        return balance;
    }
    
    public String getAccountNumber(){
        return accountNumber;
    }
    
    public String getAccountHolderName(){
        return accountHolderName;
    }
    
    public void deposit(double amount){
        if(amount>0){
            balance+=amount;
        }
    }
    
    public boolean withdraw(double amount){
        if(amount>0&&amount<=balance){
            balance-=amount;
            return true;
        }
        return false;
    }
    
    public boolean hasSufficientBalance(double amount){
        return balance>=amount;
    }
}

class ATMMachine{
    private BankAccount account;
    private Scanner scanner;
    private static final double MIN_WITHDRAWAL=10.0;
    private static final double MAX_WITHDRAWAL=10000.0;
    private static final double MIN_DEPOSIT=1.0;
    private static final double MAX_DEPOSIT=50000.0;
    
    public ATMMachine(BankAccount account){
        this.account=account;
        this.scanner=new Scanner(System.in);
    }
    
    public void displayMenu(){
        System.out.println("\n"+"=".repeat(40));
        System.out.println("         WELCOME TO ATM MACHINE");
        System.out.println("=".repeat(40));
        System.out.println("Account Holder: "+account.getAccountHolderName());
        System.out.println("Account Number: "+account.getAccountNumber());
        System.out.println("=".repeat(40));
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Deposit Money");
        System.out.println("4. Exit");
        System.out.println("=".repeat(40));
        System.out.print("Please select an option (1-4): ");
    }
    
    public void checkBalance(){
        System.out.println("\n"+"-".repeat(30));
        System.out.println("BALANCE INQUIRY");
        System.out.println("-".repeat(30));
        System.out.printf("Current Balance: $%.2f\n",account.getBalance());
        System.out.println("-".repeat(30));
    }
    
    public void withdraw(double amount){
        System.out.println("\n"+"-".repeat(30));
        System.out.println("WITHDRAWAL TRANSACTION");
        System.out.println("-".repeat(30));
        
        if(amount<MIN_WITHDRAWAL){
            System.out.println("Error: Minimum withdrawal amount is $"+MIN_WITHDRAWAL);
            return;
        }
        
        if(amount>MAX_WITHDRAWAL){
            System.out.println("Error: Maximum withdrawal amount is $"+MAX_WITHDRAWAL);
            return;
        }
        
        if(!account.hasSufficientBalance(amount)){
            System.out.println("Error: Insufficient balance!");
            System.out.printf("Current Balance: $%.2f\n",account.getBalance());
            return;
        }
        
        if(account.withdraw(amount)){
            System.out.println("Transaction Successful!");
            System.out.printf("Amount Withdrawn: $%.2f\n",amount);
            System.out.printf("Remaining Balance: $%.2f\n",account.getBalance());
        }else{
            System.out.println("Transaction Failed! Please try again.");
        }
        System.out.println("-".repeat(30));
    }
    
    public void deposit(double amount){
        System.out.println("\n"+"-".repeat(30));
        System.out.println("DEPOSIT TRANSACTION");
        System.out.println("-".repeat(30));
        
        if(amount<MIN_DEPOSIT){
            System.out.println("Error: Minimum deposit amount is $"+MIN_DEPOSIT);
            return;
        }
        
        if(amount>MAX_DEPOSIT){
            System.out.println("Error: Maximum deposit amount is $"+MAX_DEPOSIT);
            return;
        }
        
        double previousBalance=account.getBalance();
        account.deposit(amount);
        
        System.out.println("Transaction Successful!");
        System.out.printf("Amount Deposited: $%.2f\n",amount);
        System.out.printf("Previous Balance: $%.2f\n",previousBalance);
        System.out.printf("New Balance: $%.2f\n",account.getBalance());
        System.out.println("-".repeat(30));
    }
    
    private double getValidAmount(String transactionType){
        while(true){
            try{
                System.out.print("Enter amount to "+transactionType+": $");
                double amount=Double.parseDouble(scanner.nextLine());
                
                if(amount<=0){
                    System.out.println("Error: Amount must be positive!");
                    continue;
                }
                
                return amount;
            }catch(NumberFormatException e){
                System.out.println("Error: Please enter a valid number!");
            }
        }
    }
    
    private int getValidMenuChoice(){
        while(true){
            try{
                int choice=Integer.parseInt(scanner.nextLine());
                if(choice>=1&&choice<=4){
                    return choice;
                }
                System.out.print("Error: Please select a valid option (1-4): ");
            }catch(NumberFormatException e){
                System.out.print("Error: Please enter a valid number (1-4): ");
            }
        }
    }
    
    private boolean confirmTransaction(String transactionType,double amount){
        System.out.printf("\nConfirm %s of $%.2f? (Y/N): ",transactionType,amount);
        String confirmation=scanner.nextLine().trim().toUpperCase();
        return confirmation.equals("Y")||confirmation.equals("YES");
    }
    
    public void start(){
        System.out.println("Welcome to the ATM System!");
        
        while(true){
            displayMenu();
            int choice=getValidMenuChoice();
            
            switch(choice){
                case 1:
                    checkBalance();
                    break;
                    
                case 2:
                        double withdrawAmount=getValidAmount("withdraw");
                    if(confirmTransaction("withdrawal",withdrawAmount)){
                        withdraw(withdrawAmount);
                      }else{
                        System.out.println("Transaction cancelled.");
                    }
                 break;
                    
                case 3:
                    double depositAmount=getValidAmount("deposit");
                    if(confirmTransaction("deposit",depositAmount)){
                        deposit(depositAmount);
                    }else{
                        System.out.println("Transaction cancelled.");
                    }
                    break;
                    
                case 4:
                      System.out.println("\n"+"=".repeat(40));
                    System.out.println("Thank you for using our ATM service!");
                      System.out.println("Have a great day!");
                    System.out.println("=".repeat(40));
                    return;
            }
            
            System.out.println( "\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
}

public class ATMSystem{
    public static void main(String[] args){
        BankAccount userAccount=new BankAccount("ACC123456789","Yash Shakya",1500.00);
          ATMMachine atm=new ATMMachine(userAccount);
        atm.start();
    }
}
