import java.util.Random;  
  
class BankAccount {  
    private int balance;  
  
    public BankAccount(int initialBalance) {  
        this.balance = initialBalance;  
    }  
  
    // Synchronized method to ensure thread safety  
    public synchronized void deposit(int amount) {  
        balance += amount;  
        System.out.println(Thread.currentThread().getName() + " deposited " + amount + ". New balance: " + balance);  
    }  
  
    // Synchronized method to ensure thread safety  
    public synchronized void withdraw(int amount) {  
        if (amount <= balance) {  
            balance -= amount;  
            System.out.println(Thread.currentThread().getName() + " withdrew " + amount + ". New balance: " + balance);  
        } else {  
            System.out.println(Thread.currentThread().getName() + " attempted to withdraw " + amount + " but insufficient funds.");  
        }  
    }  
  
    public int getBalance() {  
        return balance;  
    }  
}  
  
class User implements Runnable {  
    private BankAccount account;  
    private Random random;  
  
    public User(BankAccount account) {  
        this.account = account;  
        this.random = new Random();  
    }  
  
    @Override  
    public void run() {  
        for (int i = 0; i < 5; i++) {  
            // Randomly choose to deposit or withdraw  
            int amount = random.nextInt(100) + 1; // Amount between 1 and 100  
  
            if (random.nextBoolean()) {  
                account.deposit(amount);  
            } else {  
                account.withdraw(amount);  
            }  
  
            try {  
                Thread.sleep(random.nextInt(1000)); // Sleep for 0-999 ms  
            } catch (InterruptedException e) {  
                Thread.currentThread().interrupt();  
            }  
        }  
    }  
}  
  
public class BankSimulation {  
    public static void main(String[] args) {  
        BankAccount account = new BankAccount(500); // Initial balance of 500  
  
        // Create and start multiple user threads  
        Thread user1 = new Thread(new User(account), "User 1");  
        Thread user2 = new Thread(new User(account), "User 2");  
        Thread user3 = new Thread(new User(account), "User 3");  
  
        user1.start();  
        user2.start();  
        user3.start();  
  
        // Wait for all threads to finish  
        try {  
            user1.join();  
            user2.join();  
            user3.join();  
        } catch (InterruptedException e) {  
            Thread.currentThread().interrupt();  
        }  
  
        System.out.println("Final account balance: " + account.getBalance());  
    }  
}  