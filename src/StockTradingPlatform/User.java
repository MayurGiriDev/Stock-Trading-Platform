package StockTradingPlatform;
/**
 * Represents a user of the trading platform.
 */
public class User {
    private final String name;
    private final Portfolio portfolio;

    public User(String name, double initialCash) {
        this.name = name;
        this.portfolio = new Portfolio(initialCash);
    }

    /** Used when loading a saved portfolio from file */
    public User(String name, Portfolio portfolio) {
        this.name = name;
        this.portfolio = portfolio;
    }

    public String getName() { return name; }
    public Portfolio getPortfolio() { return portfolio; }
}