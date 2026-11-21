package Remittance_Currency_Simulator.PrimaryData;

import Remittance_Currency_Simulator.Primary.Currency;
import Remittance_Currency_Simulator.Primary.ExchangeRate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CurrencyData extends FileData {

    private List<Currency> currencies = new ArrayList<>();
    private List<ExchangeRate> exchangeRates = new ArrayList<>();

    public CurrencyData(String filePath) {
        super(filePath);
    }

    public void addCurrency(Currency currency) {
        currencies.add(currency);
    }

    public Currency findCurrency(String code) {

        for (Currency currency : currencies) {
            if (currency.getCurrencyCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }

        return null;
    }

    public void addExchangeRate(ExchangeRate rate) {
        exchangeRates.add(rate);
    }

    public ExchangeRate findExchangeRate(String from, String to) {

        for (ExchangeRate rate : exchangeRates) {

            if (rate.getFromCurrency().getCurrencyCode().equalsIgnoreCase(from) && rate.getToCurrency().getCurrencyCode().equalsIgnoreCase(to)) {

                return rate;
            }
        }

        return null;
    }

    public List<Currency> getCurrencies() {
        return new ArrayList<>(currencies);
    }

    public List<ExchangeRate> getExchangeRates() {
        return new ArrayList<>(exchangeRates);
    }

    @Override
    public void saveData() {
        try (BufferedWriter Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            for (Currency c : currencies) {
                Writer.write("CURRENCY," + c.getCurrencyCode() + "," + c.getCurrencyName() + "," + c.getSymbol());
                Writer.newLine();
            }

            for (ExchangeRate r : exchangeRates) {
                Writer.write("RATE," + r.getFromCurrency().getCurrencyCode() + "," + r.getToCurrency().getCurrencyCode() + "," + r.getRate());
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

        currencies.clear();
        exchangeRates.clear();

        List<String[]> rateLines = new ArrayList<>();

        try (BufferedReader Reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String data = Reader.readLine();
            while (data != null) {

                String[] p = data.split(",");

                if (p.length >= 4 && p[0].equals("CURRENCY")) {
                    currencies.add(new Currency(p[1], p[2], p[3]));
                } else if (p.length >= 4 && p[0].equals("RATE")) {
                    rateLines.add(p);
                }

                data = Reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // rates need the currencies to exist first
        for (String[] p : rateLines) {
            Currency from = findCurrency(p[1]);
            Currency to = findCurrency(p[2]);

            if (from != null && to != null) {
                exchangeRates.add(new ExchangeRate(from, to, Double.parseDouble(p[3])));
            }
        }
    }
}
