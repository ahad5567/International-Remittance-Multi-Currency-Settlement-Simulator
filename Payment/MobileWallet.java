package Remittance_Currency_Simulator.Payment;

public class MobileWallet implements PaymentMethod{
    @Override
    public boolean processPayment(double amount) {
        if(amount<=0){
            return false;
        }
        else {
            System.out.println("Mobile wallet processed: "+amount);
            return true;
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Mobile Wallet";
    }
}
