package Remittance_Currency_Simulator.Primary;

import Remittance_Currency_Simulator.Calculation.ReportGenerator;

import java.util.List;

public class Admin extends User {

    public Admin(String userID, String name, String email, String phone, String password) {
        super(userID, name, email, phone, password);
    }

    public void manageUser() {
        System.out.println("User management selected.");
    }

    public void updateExchangeRate(ExchangeRate exchangeRate, double newRate) {
        exchangeRate.updateRate(newRate);
    }

    public void viewAllTransaction(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public void generateReports(ReportGenerator generator) {
        generator.generateTransactionReport();
        generator.generateSettlementReport();
    }

    @Override
    public void showDashboard() {
        System.out.println("===== ADMIN DASHBOARD =====");
        System.out.println("1. Manage Users");
        System.out.println("2. Update Exchange Rate");
        System.out.println("3. View Transactions");
        System.out.println("4. Generate Reports");
        System.out.println("5. Logout");
    }
}
