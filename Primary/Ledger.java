package Remittance_Currency_Simulator.Primary;

import java.util.ArrayList;
import java.util.List;

public class Ledger {
    private String ledgerId;
    private List<LedgerEntry> entries;

    public Ledger(String ledgerId) {
        this.ledgerId = ledgerId;
        this.entries = new ArrayList<>();
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public void recordDebit(String entryId, String accountId, String transactionId, double amount) {

        validateAmount(amount);
        entries.add(new LedgerEntry(entryId, accountId, transactionId, "DEBIT", amount));
    }

    public void recordCredit(String entryId, String accountId, String transactionId, double amount) {

        validateAmount(amount);
        entries.add(new LedgerEntry(entryId, accountId, transactionId, "CREDIT", amount));
    }

    public double getBalance(String accountId) {

        double balance = 0;

        for (LedgerEntry entry : entries) {

            if (entry.getAccountId().equals(accountId)) {

                if (entry.getEntryType().equals("CREDIT")) {
                    balance += entry.getAmount();
                } else {
                    balance -= entry.getAmount();
                }
            }
        }

        return balance;
    }

    public boolean reconcileBalance() {

        double totalDebit = 0;
        double totalCredit = 0;

        for (LedgerEntry entry : entries) {

            if (entry.getEntryType().equals("DEBIT")) {
                totalDebit += entry.getAmount();
            } else {
                totalCredit += entry.getAmount();
            }
        }

        return (Double.compare(totalDebit, totalCredit) == 0);
    }


    public List<LedgerEntry> getEntries() {
        return (new ArrayList<>(entries));
    }
}
