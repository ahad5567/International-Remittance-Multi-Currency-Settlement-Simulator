package Remittance_Currency_Simulator.MainExecution;

import Remittance_Currency_Simulator.Primary.*;
import Remittance_Currency_Simulator.Calculation.*;
import Remittance_Currency_Simulator.Payment.*;
import Remittance_Currency_Simulator.PrimaryData.*;
import Remittance_Currency_Simulator.TransactionID_Gen.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    private static Scanner scanner = new Scanner(System.in);

    private static CurrencyData currencyData;
    private static UserData userData;
    private static AccountData accountData;
    private static LedgerData ledgerData;
    private static TransactionData transactionData;

    private static List<Settlement> sessionSettlements = new ArrayList<>();




    private static User login() {

        String email = readLine("Email: ");
        String password = readLine("Password: ");

        User user = userData.findUserByEmail(email);


        if (user == null || !user.validateLogin(PasswordHasher.hash(password))) {
            System.out.println("Invalid email or password.");
            return null;
        }

        System.out.println("Welcome, " + user.getName() + "!");
        return user;
    }

    private static void register() {

        System.out.println("Register as: 1. Sender   2. Receiver");
        int role = readInt("Choose: ");

        if (role != 1 && role != 2) {
            System.out.println("Invalid role.");
            return;
        }

        String name = readLine("Name: ");
        String email = readLine("Email: ");

        if (!email.contains("@") || email.length() < 5) {
            System.out.println("Invalid email.");
            return;
        }

        if (userData.findUserByEmail(email) != null) {
            System.out.println("This email is already registered.");
            return;
        }

        String phone = readLine("Phone: ");
        String password = readLine("Password (min 4 characters): ");

        if (password.length() < 4) {
            System.out.println("Password too short.");
            return;
        }

        System.out.println("Available currencies:");
        for (Currency c : currencyData.getCurrencies()) {
            System.out.println("  " + c);
        }

        Currency currency = currencyData.findCurrency(readLine("Account currency code: "));

        if (currency == null) {
            System.out.println("Unknown currency.");
            return;
        }

        double balance = 0;

        if (role == 1) {
            balance = readDouble("Opening balance: ");
            if (balance < 0) {
                System.out.println("Invalid balance.");
                return;
            }
        }

        String hashed = PasswordHasher.hash(password);
        String userId = IDGenerator.generateUserId();

        BankAccount account = new BankAccount(IDGenerator.generateAccountId(), IDGenerator.generateAccountNumber(), userId, balance, currency);

        if (role == 1) {
            Sender sender = new Sender(userId, name, email, phone, hashed);
            sender.addBankAccount(account);
            userData.addUser(sender);
        } else {
            Receiver receiver = new Receiver(userId, name, email, phone, hashed);
            receiver.addBankAccount(account);
            userData.addUser(receiver);
        }

        accountData.addAccount(account);
        saveAll();

        System.out.println("Registration successful! Your account number: " + account.getAccountNumber());
    }



    private static void runDashboard(User user) {

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println();
            user.showDashboard();
            int choice = readInt("Choose: ");

            if (user instanceof Sender) {
                Sender sender = (Sender) user;

                if (choice == 1) {
                    sender.viewAccounts();
                } else if (choice == 2) {
                    sendRemittance(sender);
                } else if (choice == 3) {
                    showLedgerHistory(sender.getAccounts(), false);
                } else if (choice == 4) {
                    loggedIn = false;
                } else {
                    System.out.println("Invalid choice.");
                }

            } else if (user instanceof Receiver) {
                Receiver receiver = (Receiver) user;

                if (choice == 1) {
                    for (BankAccount account : receiver.getAccounts()) {
                        System.out.println(account);
                    }
                } else if (choice == 2) {
                    showLedgerHistory(receiver.getAccounts(), true);
                } else if (choice == 3) {
                    showLedgerHistory(receiver.getAccounts(), false);
                } else if (choice == 4) {
                    loggedIn = false;
                } else {
                    System.out.println("Invalid choice.");
                }

            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;

                if (choice == 1) {
                    admin.manageUser();
                    for (User u : userData.getUsers()) {
                        System.out.println(u.getUserId() + " | " + u.getClass().getSimpleName() + " | " + u.getName() + " | " + u.getEmail() + " | " + u.getPhone());
                    }
                } else if (choice == 2) {
                    updateExchangeRate(admin);
                } else if (choice == 3) {
                    admin.viewAllTransaction(transactionData.getAllTransactions());
                } else if (choice == 4) {
                    admin.generateReports(new ReportGenerator(transactionData.getAllTransactions(), sessionSettlements));
                } else if (choice == 5) {
                    loggedIn = false;
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }

    private static void updateExchangeRate(Admin admin) {

        List<ExchangeRate> rates = currencyData.getExchangeRates();

        for (ExchangeRate r : rates) {
            System.out.println(r.getFromCurrency().getCurrencyCode() + " -> " + r.getToCurrency().getCurrencyCode() + " : " + r.getRate());
        }

        String from = readLine("From currency code: ");
        String to = readLine("To currency code: ");

        ExchangeRate rate = currencyData.findExchangeRate(from, to);

        if (rate == null) {
            System.out.println("Exchange rate not found.");
            return;
        }

        double newRate = readDouble("New rate: ");

        try {
            admin.updateExchangeRate(rate, newRate);
            saveAll();
            System.out.println("Exchange rate updated.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rate.");
        }
    }


    private static void showLedgerHistory(List<BankAccount> accounts, boolean creditsOnly) {

        boolean found = false;

        for (LedgerEntry entry : ledgerData.getEntries()) {

            for (BankAccount account : accounts) {

                if (entry.getAccountId().equals(account.getAccountId())) {

                    if (creditsOnly && !entry.getEntryType().equals("CREDIT")) {
                        continue;
                    }

                    System.out.println(entry.getEntryDate().toLocalDate() + " " + entry.getEntryDate().toLocalTime().withNano(0) + " | " + entry.getTransactionId() + " | " + entry.getEntryType() + " | " + entry.getAmount() + " " + account.getCurrency().getCurrencyCode());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No records found.");
        }
    }



    private static void sendRemittance(Sender sender) {

        List<BankAccount> myAccounts = sender.getAccounts();

        if (myAccounts.isEmpty()) {
            System.out.println("You have no bank account.");
            return;
        }

        BankAccount senderAccount = myAccounts.get(0);

        if (myAccounts.size() > 1) {
            for (int i = 0; i < myAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + myAccounts.get(i));
            }
            int index = readInt("Select your account: ") - 1;

            if (index < 0 || index >= myAccounts.size()) {
                System.out.println("Invalid account.");
                return;
            }
            senderAccount = myAccounts.get(index);
        }

        String receiverEmail = readLine("Receiver email: ");
        User found = userData.findUserByEmail(receiverEmail);

        if (!(found instanceof Receiver)) {
            System.out.println("Receiver not found.");
            return;
        }

        Receiver receiver = (Receiver) found;


        BankAccount receiverAccount = null;
        ExchangeRate rate = null;

        for (BankAccount account : receiver.getAccounts()) {
            ExchangeRate r = currencyData.findExchangeRate(senderAccount.getCurrency().getCurrencyCode(), account.getCurrency().getCurrencyCode());

            if (r != null) {
                receiverAccount = account;
                rate = r;
                break;
            }
        }

        if (receiverAccount == null) {
            System.out.println("No exchange rate available between your account and the receiver's account.");
            return;
        }

        double amount = readDouble("Amount to send (" + senderAccount.getCurrency().getCurrencyCode() + "): ");

        System.out.println("Payment method: 1. Bank Transfer   2. Mobile Wallet");
        int method = readInt("Choose: ");

        PaymentMethod payment = (method == 2) ? new MobileWallet() : new BankTransfer();

        FeeCalculator calculator = new FeeCalculator(5, 1, 0);

        Remittance remittance = new Remittance(IDGenerator.generateRemittanceId(), sender, receiver, senderAccount, receiverAccount, amount, rate, payment, calculator);


        if (!remittance.validateTransfer()) {
            System.out.println("Invalid Remittance! (check the amount and your balance)");
            return;
        }


        double receiveAmount;

        try {
            receiveAmount = remittance.calculateReceiveAmount();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }


        Transaction transaction = new Transaction(IDGenerator.generateTransactionId(), remittance.getRemittanceId(), remittance.getSendAmount(), remittance.getTransferFee());

        remittance.setTransaction(transaction);
        transactionData.addTransaction(transaction);


        if (!payment.processPayment(remittance.getSendAmount())) {
            transaction.markFailed("Payment processing failed");
            System.out.println("Payment Failed!");
            saveAll();
            return;
        }

        if (!senderAccount.withdraw(remittance.getSendAmount())) {

            transaction.markFailed("Insufficient Balance");
            System.out.println("Insufficient Balance!");
            saveAll();
            return;
        }


        Settlement settlement = new Settlement(IDGenerator.generateSettlementId(), transaction.getTransactionId(), receiveAmount);
        sessionSettlements.add(settlement);


        if (settlement.processSettlement()) {


            receiverAccount.deposit(receiveAmount);

            settlement.confirmSettlement();
            transaction.markCompleted();


            Ledger ledger = new Ledger("LEDGER-01");

            ledger.recordDebit(IDGenerator.generateLedgerEntryId(), senderAccount.getAccountId(), transaction.getTransactionId(), remittance.getSendAmount());

            ledger.recordCredit(IDGenerator.generateLedgerEntryId(), receiverAccount.getAccountId(), transaction.getTransactionId(), receiveAmount);


            for (LedgerEntry entry : ledger.getEntries()) {
                ledgerData.addEntry(entry);
            }

            sender.addRemittance(remittance);
            receiver.addReceivedRemittance(remittance);


            remittance.generateReceipt();

            System.out.println();
            System.out.println("Transaction Status: " + transaction.getStatus());
            System.out.println("Settlement Status: " + settlement.getStatus());
            System.out.println("Sender Balance: " + senderAccount.getBalance());
            System.out.println("Receiver Balance: " + receiverAccount.getBalance());
            System.out.println("Ledger Entries: " + ledger.getEntries().size());

        } else {


            senderAccount.deposit(remittance.getSendAmount());

            settlement.failSettlement();
            transaction.markFailed("Settlement Failed");

            System.out.println("Settlement Failed!");
            System.out.println("Sender Refunded.");
        }


        saveAll();
    }


    private static void linkAccountsToUsers() {

        for (BankAccount account : accountData.getAccounts()) {

            User owner = userData.findUserById(account.getAccountHolderId());

            if (owner instanceof Sender) {
                ((Sender) owner).addBankAccount(account);
            } else if (owner instanceof Receiver) {
                ((Receiver) owner).addBankAccount(account);
            }
        }
    }


    private static void seedDefaultDataIfEmpty() {

        boolean changed = false;

        if (currencyData.getCurrencies().isEmpty()) {
            Currency usd = new Currency("USD", "US Dollar", "$");
            Currency bdt = new Currency("BDT", "Bangladeshi Taka", "৳");

            currencyData.addCurrency(usd);
            currencyData.addCurrency(bdt);
            currencyData.addExchangeRate(new ExchangeRate(usd, bdt, 122));
            currencyData.addExchangeRate(new ExchangeRate(bdt, usd, 0.0082));
            changed = true;
        }

        if (userData.getUsers().isEmpty()) {
            Currency usd = currencyData.findCurrency("USD");
            Currency bdt = currencyData.findCurrency("BDT");

            Admin admin = new Admin(IDGenerator.generateUserId(), "Admin", "admin@gmail.com", "01800000000", PasswordHasher.hash("admin123"));

            Sender sender = new Sender(IDGenerator.generateUserId(), "John", "john@gmail.com", "123456789", PasswordHasher.hash("1234"));
            Receiver receiver = new Receiver(IDGenerator.generateUserId(), "Rahim", "rahim@gmail.com", "01700000000", PasswordHasher.hash("1234"));

            BankAccount senderAccount = new BankAccount(IDGenerator.generateAccountId(), "US-1001", sender.getUserId(), 5000, usd);
            BankAccount receiverAccount = new BankAccount(IDGenerator.generateAccountId(), "BD-2001", receiver.getUserId(), 0, bdt);

            sender.addBankAccount(senderAccount);
            receiver.addBankAccount(receiverAccount);

            userData.addUser(admin);
            userData.addUser(sender);
            userData.addUser(receiver);
            accountData.addAccount(senderAccount);
            accountData.addAccount(receiverAccount);
            changed = true;
        }

        if (changed) {
            saveAll();
        }
    }

    private static void saveAll() {
        currencyData.saveData();
        userData.saveData();
        accountData.saveData();
        ledgerData.saveData();
        transactionData.saveData();
    }



    private static String readLine(String prompt) {
        System.out.print(prompt);

        if (!scanner.hasNextLine()) {
            System.out.println();
            System.exit(0);
        }


        return scanner.nextLine().trim().replace(",", " ");
    }

    private static int readInt(String prompt) {
        try {
            return Integer.parseInt(readLine(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDouble(String prompt) {
        try {
            return Double.parseDouble(readLine(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    public static void main(String[] args) {

        currencyData = new CurrencyData( "E:\\EWU course\\1st year\\3rd semester\\CSE110 - Md Sabbir Hossain\\Presentation\\Project\\src\\Remittance_Currency_Simulator\\DataStore\\currencies.txt");
        userData = new UserData( "E:\\EWU course\\1st year\\3rd semester\\CSE110 - Md Sabbir Hossain\\Presentation\\Project\\src\\Remittance_Currency_Simulator\\DataStore\\users.txt");
        accountData = new AccountData( "E:\\EWU course\\1st year\\3rd semester\\CSE110 - Md Sabbir Hossain\\Presentation\\Project\\src\\Remittance_Currency_Simulator\\DataStore\\accounts.txt", currencyData);
        ledgerData = new LedgerData("E:\\EWU course\\1st year\\3rd semester\\CSE110 - Md Sabbir Hossain\\Presentation\\Project\\src\\Remittance_Currency_Simulator\\DataStore\\ledger.txt");
        transactionData = new TransactionData( "E:\\EWU course\\1st year\\3rd semester\\CSE110 - Md Sabbir Hossain\\Presentation\\Project\\src\\Remittance_Currency_Simulator\\DataStore\\transactions.txt");


        currencyData.loadData();
        userData.loadData();
        accountData.loadData();
        ledgerData.loadData();
        transactionData.loadData();

        linkAccountsToUsers();
        seedDefaultDataIfEmpty();

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== REMITTANCE CURRENCY SIMULATOR =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            int choice = readInt("Choose: ");

            if (choice == 1) {
                User user = login();
                if (user != null) {
                    runDashboard(user);
                }
            } else if (choice == 2) {
                register();
            } else if (choice == 3) {
                running = false;
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
