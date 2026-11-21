package Remittance_Currency_Simulator.Primary;

public class ExchangeRate {
    private Currency fromCurrency;
    private Currency toCurrency;
    private double rate;

    public ExchangeRate(Currency fromCurrency, Currency toCurrency, double rate) {
        if (rate <= 0) {
            throw new IllegalArgumentException("Invalid rate");
        } else {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.rate = rate;
        }
    }

    public double convert(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid rate");
        } else {
            return Math.round((amount * rate) * 100.0) / 100.0;
        }
    }

    public void updateRate(double newRate) {
        if (newRate <= 0) {
            throw new IllegalArgumentException("Invalid rate");
        } else {
            this.rate = newRate;
        }
    }

    public double getRate() {
        return rate;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }
}
