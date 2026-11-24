package StockTradingPlatform;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Holds the user's cash, stock holdings, transaction history,
 * and a day-by-day value history for performance tracking.
 */
public class Portfolio {
    private double cash;
    private final HashMap<String, Integer> holdings = new HashMap<>();
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private final ArrayList<double[]> valueHistory = new ArrayList<>(); // {day, value}

    public Portfolio(double initialCash) {
        this.cash = initialCash;
    }

    // ---------- trading ----------
    public boolean buy(Stock stock, int qty) {
        double cost = stock.getPrice() * qty;
        if (cost > cash) return false;
        cash -= cost;
        holdings.merge(stock.getSymbol(), qty, Integer::sum);
        transactions.add(new Transaction(Transaction.Type.BUY,
                stock.getSymbol(), qty, stock.getPrice()));
        return true;
    }

    public boolean sell(Stock stock, int qty) {
        int owned = holdings.getOrDefault(stock.getSymbol(), 0);
        if (qty <= 0 || qty > owned) return false;
        cash += stock.getPrice() * qty;
        if (owned == qty) holdings.remove(stock.getSymbol());
        else holdings.put(stock.getSymbol(), owned - qty);
        transactions.add(new Transaction(Transaction.Type.SELL,
                stock.getSymbol(), qty, stock.getPrice()));
        return true;
    }

    // ---------- valuation ----------
    public double getHoldingsValue(Market market) {
        double total = 0;
        for (var entry : holdings.entrySet()) {
            Stock s = market.getStock(entry.getKey());
            if (s != null) total += s.getPrice() * entry.getValue();
        }
        return total;
    }

    public double getTotalValue(Market market) {
        return cash + getHoldingsValue(market);
    }

    public void recordSnapshot(int day, double value) {
        valueHistory.add(new double[]{day, value});
    }

    // ---------- display ----------
    public void display(Market market) {
        System.out.println("\n================= PORTFOLIO =================");
        System.out.printf("Cash Balance : $%,.2f%n", cash);
        if (holdings.isEmpty()) {
            System.out.println("Holdings    : (no stocks owned)");
        } else {
            System.out.printf("%-8s %-10s %-12s %-12s%n", "SYMBOL", "QTY", "PRICE ($)", "VALUE ($)");
            System.out.println("---------------------------------------------");
            for (var entry : holdings.entrySet()) {
                Stock s = market.getStock(entry.getKey());
                System.out.printf("%-8s %-10d %-12.2f %-12.2f%n",
                        entry.getKey(), entry.getValue(), s.getPrice(),
                        s.getPrice() * entry.getValue());
            }
            System.out.printf("Holdings Value : $%,.2f%n", getHoldingsValue(market));
        }
        System.out.printf("TOTAL VALUE    : $%,.2f%n", getTotalValue(market));
        System.out.println("=============================================");
    }

    public void displayTransactions() {
        System.out.println("\n=============== TRANSACTION HISTORY ===============");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : transactions) System.out.println(t);
        }
        System.out.println("===================================================");
    }

    public void displayPerformance() {
        System.out.println("\n========= PORTFOLIO PERFORMANCE OVER TIME =========");
        if (valueHistory.isEmpty()) {
            System.out.println("No history recorded yet. Trade or simulate days first!");
            System.out.println("====================================================");
            return;
        }
        double first = valueHistory.get(0)[1];
        System.out.printf("%-8s %-16s %-12s%n", "DAY", "VALUE ($)", "CHANGE");
        System.out.println("----------------------------------------------------");
        for (int i = 0; i < valueHistory.size(); i++) {
            double day = valueHistory.get(i)[0];
            double value = valueHistory.get(i)[1];
            String change;
            if (i == 0) {
                change = "— start";
            } else {
                double prev = valueHistory.get(i - 1)[1];
                double pct = (value - prev) / prev * 100.0;
                change = String.format("%s%.2f%%", pct >= 0 ? "▲ +" : "▼ ", pct);
            }
            System.out.printf("%-8.0f %,-16.2f %-12s%n", day, value, change);
        }
        double last = valueHistory.get(valueHistory.size() - 1)[1];
        double overall = (last - first) / first * 100.0;
        System.out.println("----------------------------------------------------");
        System.out.printf("Overall return: %s%.2f%%%n", overall >= 0 ? "+" : "", overall);
        System.out.println("====================================================");
    }

    // ---------- getters / persistence helpers ----------
    public double getCash() { return cash; }
    public HashMap<String, Integer> getHoldings() { return holdings; }
    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void addTransaction(Transaction t) { transactions.add(t); }
}
