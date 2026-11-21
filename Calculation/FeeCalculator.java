package Remittance_Currency_Simulator.Calculation;

public class FeeCalculator {
    private double fixedFee;
    private double percentaeFee;
    private double fxSpread;

    public FeeCalculator(double fixedFee, double percentaeFee, double fxSpread){
        this.fixedFee = fixedFee;
        this.percentaeFee = percentaeFee;
        this.fxSpread = fxSpread;
    }

    public double calculateFee(double amount){
        double percentage = (Math.round(amount * percentaeFee) / 100.0);
        double sum = fixedFee + percentage;
        return sum;
    }

    public double calculateSpread(double amount){
        double fxFee=  Math.round(amount * fxSpread) / 100.0;
        return fxFee;
    }

    public double calculateTotalCost(double amount){
        double totalCost = calculateFee(amount) + calculateSpread(amount);
        return totalCost;
    }
}
