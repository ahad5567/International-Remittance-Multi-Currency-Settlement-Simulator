package Remittance_Currency_Simulator.Payment;

public interface PaymentMethod {

    boolean processPayment(double amount);
    String getPaymentMethodName();
}
