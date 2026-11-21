package Remittance_Currency_Simulator.Calculation;

import Remittance_Currency_Simulator.Primary.Settlement;
import Remittance_Currency_Simulator.Primary.Transaction;

import java.util.List;

public class ReportGenerator {
    private List<Transaction> transactions;
    private  List<Settlement> settlements;

    public ReportGenerator(List<Transaction> transactions, List<Settlement> settlements){
        this.transactions = transactions;
        this.settlements = settlements;
    }

    public void generateTransactionReport(){
        System.out.println("===== TRANSACTION REPORT =====");

        for(Transaction t : transactions){
            System.out.println(t);
        }
        System.out.println("Total transaction: "+transactions.size());
    }

    public void generateSettlementReport(){
        System.out.println("===== SETTLEMENT REPORT =====");

        for(Settlement s : settlements){
            System.out.println(s);
        }
        System.out.println("Total settlements: "+settlements.size());
    }

    public void generateFeeReport(){
        System.out.println("===== FEE REPORT =====");

        for(Transaction t : transactions){
            System.out.println(t.getTransactionId()+" : "+t.getTransferFee());
        }
    }

    public void generateCurrencyWiseReport() {
        System.out.println("Currency-wise report will be generated here.");
    }
}
