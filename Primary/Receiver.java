package Remittance_Currency_Simulator.Primary;

import java.util.ArrayList;
import java.util.List;

public class Receiver extends User {
    private List<BankAccount> accounts;
    private List<Remittance> receivedRemittances;

    public Receiver(String userID, String name, String email, String phone, String password) {
        super(userID, name, email, phone, password);
        accounts = new ArrayList<>();
        receivedRemittances = new ArrayList<>();
    }

    public void addBankAccount(BankAccount account) {
        accounts.add(account);
    }

    public void addReceivedRemittance(Remittance remittance) {
        receivedRemittances.add(remittance);
    }

    public void viewReceivedMoney() {
        for (Remittance r : receivedRemittances) {
            System.out.println(r);
        }
    }

    public List<BankAccount> getAccounts() {
        return new ArrayList<>(accounts);
    }

    @Override
    public void showDashboard() {
        System.out.println("===== RECEIVER DASHBOARD =====");
        System.out.println("1. View Accounts");
        System.out.println("2. View Received Money");
        System.out.println("3. Transaction History");
        System.out.println("4. Logout");
    }
}
