package StockTradingPlatform;
import java.util.ArrayList;
import java.util.Random;

/**
 * Simulates a stock market with daily price fluctuations.
 */
public class Market {
    private final ArrayList<Stock> stocks = new ArrayList<>();
    private final Random random = new Random();
    private int day = 1;

    public Market() {
        stocks.add(new Stock("AAPL",  "Apple Inc.",     185.50));
        stocks.add(new Stock("GOOGL", "Alphabet Inc.",  142.30));
        stocks.add(new Stock("MSFT",  "Microsoft Corp.",415.10));
        stocks.add(new Stock("AMZN",  "Amazon.com",     178.25));
        stocks.add(new Stock("TSLA",  "Tesla Inc.",     245.80));
        stocks.add(new Stock("NVDA",  "NVIDIA Corp.",   875.40));
    }

    public int getDay() { return day; }

    public Stock getStock(String symbol) {
        for (Stock s : stocks)
            if (s.getSymbol().equalsIgnoreCase(symbol)) return s;
        return null;
    }

    /** Displays all stocks with current price and daily change */
    public void displayMarketData() {
        System.out.println("\n=============== MARKET DATA — Day " + day + " ===============");
        System.out.printf("%-8s %-18s %-12s %-10s%n", "SYMBOL", "COMPANY", "PRICE ($)", "CHANGE");
        System.out.println("------------------------------------------------------");
        for (Stock s : stocks) {
            System.out.printf("%-8s %-18s %-12.2f %s%.2f%%%n",
                    s.getSymbol(), s.getCompanyName(), s.getPrice(),
                    s.getChangeArrow(), s.getChangePercent());
        }
        System.out.println("=======================================================");
    }

    /** Simulates one trading day: each stock moves randomly between -5% and +5% */
    public void simulateDay() {
        for (Stock s : stocks) {
            double changePercent = (random.nextDouble() - 0.5) * 10;
            s.updatePrice(s.getPrice() * (1 + changePercent / 100.0));
        }
        day++;
    }
}