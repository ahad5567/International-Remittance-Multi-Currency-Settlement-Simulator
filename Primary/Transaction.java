package Remittance_Currency_Simulator.Primary;

import java.time.LocalDateTime;

public class Transaction {
    private String transcationId;
    private String remittanceId;
    private LocalDateTime transacntionDate;
    private double amount;
    private double transferFee;
    private TransactionStatus status;
    private String failureReason;

    public Transaction(String transactionId, String remittanceId, double amount, double transferFee) {

        this.transcationId = transactionId;
        this.remittanceId = remittanceId;
        this.amount = amount;
        this.transferFee = transferFee;
        this.transacntionDate = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
        this.failureReason = "";
    }

    public void updateStatus(TransactionStatus status) {
        this.status = status;
    }

    public void markCompleted() {
        this.status = TransactionStatus.COMPLETED;
        this.failureReason = "";
    }

    public void markFailed(String reason) {
        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
    }

    public String getTransactionId() {
        return transcationId;
    }

    public String getRemittanceId() {
        return remittanceId;
    }

    public double getAmount() {
        return amount;
    }

    public double getTransferFee() {
        return transferFee;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getTransactionDate() {
        return transacntionDate;
    }

    @Override
    public String toString() {
        return ("Transaction ID: " + transcationId + " | Amount: " + amount + " | Fee: " + transferFee + " | Status: " + status);
    }
}
