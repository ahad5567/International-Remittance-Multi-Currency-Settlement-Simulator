package Remittance_Currency_Simulator.Primary;

import Remittance_Currency_Simulator.Calculation.FeeCalculator;
import Remittance_Currency_Simulator.Payment.PaymentMethod;

public class Remittance {
    private String remittanceId;
    private Sender sender;
    private Receiver receiver;
    private BankAccount sourceAccount;
    private BankAccount destinationAccount;
    private double sendAmount;
    private double receiveAmount;
    private double transferFee;
    private ExchangeRate exchangeRate;
    private PaymentMethod paymentMethod;
    private Transaction transaction;
    private FeeCalculator feeCalculator;

    public Remittance(String id, Sender sender, Receiver receiver, BankAccount source, BankAccount destination, double amount, ExchangeRate rate, PaymentMethod payment, FeeCalculator calculator) {

        this.remittanceId = id;
        this.sender = sender;
        this.receiver = receiver;
        this.sourceAccount = source;
        this.destinationAccount = destination;
        this.sendAmount = amount;
        this.exchangeRate = rate;
        this.paymentMethod = payment;
        this.feeCalculator = calculator;
    }

    public boolean validateTransfer() {

        if (sendAmount <= 0) {
            return false;
        }

        if (sourceAccount == null || destinationAccount == null) {
            return false;
        }

        if (!sourceAccount.getCurrency().getCurrencyCode()
                .equals(exchangeRate.getFromCurrency().getCurrencyCode())) {
            return false;
        }

        if (!destinationAccount.getCurrency().getCurrencyCode().equals(exchangeRate.getToCurrency().getCurrencyCode())) {
            return false;
        }

        if (!sourceAccount.hasSufficientBalance(sendAmount)) {
            return false;
        }

        return true;
    }

    public double calculateReceiveAmount() {

        transferFee = feeCalculator.calculateFee(sendAmount);

        double netAmount = sendAmount - transferFee;

        if (netAmount <= 0) {
            throw new IllegalArgumentException("Fee exceeds transfer amount");
        } else {
            receiveAmount = exchangeRate.convert(netAmount);
        }

        return receiveAmount;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public boolean initiateTransfer() {

        if (!validateTransfer()) {
            return false;
        }

        calculateReceiveAmount();

        if (!paymentMethod.processPayment(sendAmount)) {
            return false;
        }

        return true;
    }

    public void cancelTransfer() {

        if (transaction != null && transaction.getStatus() == TransactionStatus.PENDING) {

            transaction.updateStatus(TransactionStatus.CANCELLED);
        }
    }

    public void generateReceipt() {

        System.out.println("===== REMITTANCE RECEIPT =====");
        System.out.println("Remittance ID: " + remittanceId);
        System.out.println("Sender: " + sender.getName());
        System.out.println("Receiver: " + receiver.getName());
        System.out.println("Sent: " + sendAmount);
        System.out.println("Fee: " + transferFee);
        System.out.println("Received: " + receiveAmount);
    }

    public String getRemittanceId() {
        return remittanceId;
    }

    public Sender getSender() {
        return sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }

    public BankAccount getDestinationAccount() {
        return destinationAccount;
    }

    public double getSendAmount() {
        return sendAmount;
    }

    public double getReceiveAmount() {
        return receiveAmount;
    }

    public double getTransferFee() {
        return transferFee;
    }

    @Override
    public String toString() {
        return "Remittance ID: " + remittanceId + " | Amount: " + sendAmount + " | Received: " + receiveAmount;
    }

}
