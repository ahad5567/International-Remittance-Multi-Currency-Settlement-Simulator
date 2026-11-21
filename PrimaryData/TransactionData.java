package Remittance_Currency_Simulator.PrimaryData;

import Remittance_Currency_Simulator.Primary.Transaction;
import Remittance_Currency_Simulator.Primary.TransactionStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TransactionData extends FileData {

    private List<Transaction> transactions = new ArrayList<>();

    public TransactionData(String filePath) {
        super(filePath);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Transaction findTransactionById(String id) {

        for (Transaction t : transactions) {
            if (t.getTransactionId().equals(id)) {
                return t;
            }
        }

        return null;
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }


    @Override
    public void saveData() {
        try (BufferedWriter Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (Transaction t : transactions) {

                Writer.write(t.getTransactionId() + "," + t.getRemittanceId() + "," + t.getAmount() + "," + t.getTransferFee() + "," + t.getStatus());

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

        transactions.clear();

        try (BufferedReader Reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String data = Reader.readLine();
            while (data != null) {

                String[] p = data.split(",");

                if (p.length >= 5) {
                    Transaction t = new Transaction(p[0], p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]));
                    t.updateStatus(TransactionStatus.valueOf(p[4]));
                    transactions.add(t);
                }

                data = Reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
