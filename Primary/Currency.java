package Remittance_Currency_Simulator.Primary;

public class Currency {
    private String currencyCode;
    private String currencyName;
    private String symbol;

    public Currency(String code, String name, String symbol) {
        this.currencyCode = code;
        this.currencyName = name;
        this.symbol = symbol;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return (currencyCode + " - " + currencyName);
    }
}
