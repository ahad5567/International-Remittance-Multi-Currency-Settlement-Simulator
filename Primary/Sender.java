package Remittance_Currency_Simulator.Primary;

import java.util.ArrayList;
import java.util.List;

public class Sender extends User{

    private List<BankAccount> accounts;
    private List<Remittance> remittanceHistory;


    public Sender(String userID, String name, String email, String phone, String password) {
        super(userID, name, email, phone, password);
        accounts = new ArrayList<>();
        remittanceHistory = new ArrayList<>();
    }

    public void addBankAccount(BankAccount account){
        accounts.add(account);
    }

    public List<BankAccount> getAccounts(){
        return new ArrayList<>(accounts);
    }

    public void addRemittance(Remittance remittance){
        remittanceHistory.add(remittance);
    }

    public List<Remittance> getRemittanceHistory(){
        return  new ArrayList<>(remittanceHistory);
    }

    public void viewAccounts(){
        for(BankAccount account : accounts){
            System.out.println(account);
        }
    }

    public void viewTransactionHistory(){
        for(Remittance remittance : remittanceHistory){
            System.out.println(remittance);
        }
    }

    @Override
    public void showDashboard() {
        System.out.println("===== SENDER DASHBOARD =====");
        System.out.println("1. View Accounts");
        System.out.println("2. Send Remittance");
        System.out.println("3. Transaction History");
        System.out.println("4. Logout");
    }
}
