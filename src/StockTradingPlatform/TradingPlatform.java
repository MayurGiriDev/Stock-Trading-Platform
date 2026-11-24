package StockTradingPlatform;
import java.util.Scanner;

/**
 * TASK 2 — Stock Trading Platform (CodeAlpha Java Internship)
 * Console-based simulated stock market with buy/sell operations,
 * portfolio tracking, performance history, and file persistence.
 */
public class TradingPlatform {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Market market = new Market();
    private static User user;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    STOCK TRADING PLATFORM — CodeAlpha");
        System.out.println("=================================================");

        Portfolio saved = FileHandler.load();
        if (saved != null) {
            user = new User("Trader", saved);
            System.out.println("✔ Saved portfolio loaded from portfolio_data.txt");
        } else {
            user = new User("Trader", 10000.00);
            System.out.println("✔ New account created with $10,000.00 virtual cash.");
        }
        recordSnapshot();

        while (true) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> market.displayMarketData();
                case 2 -> buyStock();
                case 3 -> sellStock();
                case 4 -> user.getPortfolio().display(market);
                case 5 -> user.getPortfolio().displayTransactions();
                case 6 -> user.getPortfolio().displayPerformance();
                case 7 -> nextTradingDay();
                case 8 -> {
                    FileHandler.save(user.getPortfolio());
                    System.out.println("✔ Portfolio saved. Goodbye! 👋");
                    return;
                }
                default -> System.out.println("⚠ Invalid choice! Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n---------------- MENU ----------------");
        System.out.println("1. View Market Data");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. Transaction History");
        System.out.println("6. Portfolio Performance Over Time");
        System.out.println("7. Next Trading Day (simulate market)");
        System.out.println("8. Save & Exit");
        System.out.printf("Cash: $%,.2f | Day %d%n", user.getPortfolio().getCash(), market.getDay());
    }

    private static void buyStock() {
        market.displayMarketData();
        System.out.print("Enter stock symbol to BUY: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        Stock stock = market.getStock(symbol);
        if (stock == null) { System.out.println("⚠ Unknown symbol!"); return; }

        int qty = readInt("Enter quantity: ");
        if (qty <= 0) { System.out.println("⚠ Quantity must be positive!"); return; }

        if (user.getPortfolio().buy(stock, qty)) {
            System.out.printf("✔ Bought %d %s @ $%.2f — Total: $%,.2f%n",
                    qty, symbol, stock.getPrice(), stock.getPrice() * qty);
            recordSnapshot();
        } else {
            int affordable = (int) (user.getPortfolio().getCash() / stock.getPrice());
            System.out.printf("⚠ Not enough cash! You can afford max %d shares.%n", affordable);
        }
    }

    private static void sellStock() {
        user.getPortfolio().display(market);
        System.out.print("Enter stock symbol to SELL: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        Stock stock = market.getStock(symbol);
        if (stock == null) { System.out.println("⚠ Unknown symbol!"); return; }

        int owned = user.getPortfolio().getHoldings().getOrDefault(symbol, 0);
        if (owned == 0) { System.out.println("⚠ You don't own any " + symbol + " shares!"); return; }

        int qty = readInt("Enter quantity (you own " + owned + "): ");
        if (qty <= 0) { System.out.println("⚠ Quantity must be positive!"); return; }

        if (user.getPortfolio().sell(stock, qty)) {
            System.out.printf("✔ Sold %d %s @ $%.2f — Proceeds: $%,.2f%n",
                    qty, symbol, stock.getPrice(), stock.getPrice() * qty);
            recordSnapshot();
        } else {
            System.out.println("⚠ Invalid quantity! You own only " + owned + " shares.");
        }
    }

    private static void nextTradingDay() {
        market.simulateDay();
        System.out.println("\n📈 Market simulated — welcome to Day " + market.getDay() + "!");
        market.displayMarketData();
        recordSnapshot();
    }

    private static void recordSnapshot() {
        user.getPortfolio().recordSnapshot(market.getDay(),
                user.getPortfolio().getTotalValue(market));
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠ Please enter a valid number.");
            }
        }
    }
}