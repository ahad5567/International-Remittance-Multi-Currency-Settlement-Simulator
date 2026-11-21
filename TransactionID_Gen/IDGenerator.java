package Remittance_Currency_Simulator.TransactionID_Gen;

import java.util.UUID;

public class IDGenerator {

    private static String generate(String prefix) {

        return prefix + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateUserId() {
        return generate("USR");
    }

    public static String generateTransactionId() {
        return generate("TXN");
    }

    public static String generateSettlementId() {
        return generate("STL");
    }

    public static String generateRemittanceId() {
        return generate("REM");
    }

    public static String generateLedgerEntryId() {
        return generate("LED");
    }

    public static String generateAccountId() {
        return generate("ACC");
    }

    public static String generateAccountNumber() {
        return String.valueOf(1000000000L + (long) (Math.random() * 8999999999L));
    }
}
