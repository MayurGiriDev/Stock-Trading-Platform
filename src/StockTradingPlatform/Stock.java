package StockTradingPlatform;

/**
 * Represents a single stock in the market.
 */
public class Stock {
    private final String symbol;
    private final String companyName;
    private double price;
    private double previousPrice;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.previousPrice = price;
    }

    public String getSymbol() { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getPrice() { return price; }

    public void updatePrice(double newPrice) {
        previousPrice = price;
        price = Math.max(1.0, Math.round(newPrice * 100.0) / 100.0); // never below $1
    }

    /** % change since the last price update */
    public double getChangePercent() {
        if (previousPrice == 0) return 0;
        return (price - previousPrice) / previousPrice * 100.0;
    }

    public String getChangeArrow() {
        double c = getChangePercent();
        return c > 0.01 ? "▲" : (c < -0.01 ? "▼" : "▬");
    }
}