package Remittance_Currency_Simulator.Payment;

public class BankTransfer implements PaymentMethod{
    @Override
    public boolean processPayment(double amount) {
        if(amount<=0){
            return false;
        }
        else {
            System.out.println("Bank transfer processed: "+amount);
            return true;
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Bank Transfer";
    }
}
