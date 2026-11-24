package StockTradingPlatform;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single BUY or SELL transaction.
 */
public class Transaction {
    public enum Type { BUY, SELL }

    private final Type type;
    private final String symbol;
    private final int quantity;
    private final double pricePerShare;
    private final LocalDateTime timestamp;

    public Transaction(Type type, String symbol, int quantity, double pricePerShare) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.timestamp = LocalDateTime.now();
    }

    public Type getType() { return type; }
    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getPricePerShare() { return pricePerShare; }
    public double getTotalValue() { return quantity * pricePerShare; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("%-5s %-6s x%-5d @ $%-9.2f Total: $%-11.2f %s",
                type, symbol, quantity, pricePerShare, getTotalValue(), timestamp.format(fmt));
    }
}