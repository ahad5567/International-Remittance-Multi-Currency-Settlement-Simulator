package Remittance_Currency_Simulator.PrimaryData;

import Remittance_Currency_Simulator.Primary.BankAccount;
import Remittance_Currency_Simulator.Primary.Currency;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountData extends FileData {

    private List<BankAccount> accounts = new ArrayList<>();
    private CurrencyData currencyData;

    public AccountData(String filePath, CurrencyData currencyData) {
        super(filePath);
        this.currencyData = currencyData;
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public List<BankAccount> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public BankAccount findAccountById(String id) {
        for (BankAccount a : accounts) {
            if (a.getAccountId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void saveData() {
        try (BufferedWriter Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (BankAccount a : accounts) {
                Writer.write(a.getAccountId() + "," + a.getAccountNumber() + "," + a.getAccountHolderId() + "," + String.format(Locale.US, "%.2f", a.getBalance()) + "," + a.getCurrency().getCurrencyCode());
                Writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }

        accounts.clear();

        try (BufferedReader Reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String data = Reader.readLine();
            while (data != null) {

                String[] p = data.split(",");

                if (p.length >= 5) {
                    Currency currency = currencyData.findCurrency(p[4]);

                    if (currency != null) {
                        accounts.add(new BankAccount(p[0], p[1], p[2], Double.parseDouble(p[3]), currency));
                    }
                }

                data = Reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
