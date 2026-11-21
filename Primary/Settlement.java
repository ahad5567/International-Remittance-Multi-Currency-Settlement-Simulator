package Remittance_Currency_Simulator.Primary;

public class Settlement {
    private String settlementId;
    private String transactionId;
    private double settlementAmount;
    private SettlementStatus status;

    public Settlement(String settlementId, String transactionId, double amount) {

        this.settlementId = settlementId;
        this.transactionId = transactionId;
        this.settlementAmount = amount;
        this.status = SettlementStatus.PENDING;
    }

    public boolean processSettlement() {

        if (settlementAmount <= 0) {
            failSettlement();
            return false;
        }

        status = SettlementStatus.PROCESSING;
        return true;
    }

    public void confirmSettlement() {
        status = SettlementStatus.SETTLED;
    }

    public void failSettlement() {
        status = SettlementStatus.FAILED;
    }

    public boolean reconcile(double debit, double credit) {

        return (Double.compare(debit, credit) == 0);
    }

    public String getSettlementId() {
        return settlementId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getSettlementAmount() {
        return settlementAmount;
    }

    public SettlementStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ("Settlement ID: " + settlementId + " | Amount: " + settlementAmount + " | Status: " + status);
    }
}
