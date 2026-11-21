package Remittance_Currency_Simulator.Primary;

public class BankAccount {
    private String accountId;
    private String accountNumber;
    private String accountHolderId;
    private double balance;
    private Currency currency;

    public BankAccount(String accountId, String accountNumber,
                       String accountHolderId,
                       double balance, Currency currency) {

        if (balance < 0) {
            throw new IllegalArgumentException("Invalid balance");
        } else {
            this.accountId = accountId;
            this.accountNumber = accountNumber;
            this.accountHolderId = accountHolderId;
            this.balance = balance;
            this.currency = currency;
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public boolean hasSufficientBalance(double amount) {
        return balance >= amount;
    }

    public void deposit(double amount) {
        validateAmount(amount);
        balance += amount;
    }

    public boolean withdraw(double amount) {
        validateAmount(amount);
        if (hasSufficientBalance(amount)) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderId() {
        return accountHolderId;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + balance + " " + currency.getCurrencyCode();
    }
}
