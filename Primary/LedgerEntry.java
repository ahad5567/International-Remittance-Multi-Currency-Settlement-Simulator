package Remittance_Currency_Simulator.Primary;

import java.time.LocalDateTime;

public class LedgerEntry {
    private String entryId;
    private String accountId;
    private String transactionId;
    private String entryType;
    private double amount;
    private LocalDateTime entryDate;

    public LedgerEntry(String entryId, String accountId, String transactionId, String entryType, double amount) {

        this.entryId = entryId;
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.entryType = entryType;
        this.amount = amount;
        this.entryDate = LocalDateTime.now();
    }

    public LedgerEntry(String entryId, String accountId, String transactionId, String entryType, double amount, LocalDateTime entryDate) {

        this.entryId = entryId;
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.entryType = entryType;
        this.amount = amount;
        this.entryDate = entryDate;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getEntryType() {
        return entryType;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getEntryId() {
        return entryId;
    }

    @Override
    public String toString() {
        return (entryId + "," + accountId + "," + transactionId + "," + entryType + "," + amount + "," + entryDate);
    }
}
