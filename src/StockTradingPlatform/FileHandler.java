    package StockTradingPlatform;
import java.io.*;

/**
 * Saves / loads portfolio data using simple file I/O (CSV-like format).
 */
public class FileHandler {
    private static final String FILE_NAME = "portfolio_data.txt";

    public static void save(Portfolio p) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println(p.getCash());
            for (var entry : p.getHoldings().entrySet()) {
                writer.println("HOLDING," + entry.getKey() + "," + entry.getValue());
            }
            for (Transaction t : p.getTransactions()) {
                writer.println("TX," + t.getType() + "," + t.getSymbol() + ","
                        + t.getQuantity() + "," + t.getPricePerShare());
            }
        } catch (IOException e) {
            System.out.println("⚠ Save failed: " + e.getMessage());
        }
    }

    /** Returns a loaded Portfolio, or null if no save file exists. */
    public static Portfolio load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Portfolio p = new Portfolio(Double.parseDouble(reader.readLine()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("HOLDING")) {
                    p.getHoldings().put(parts[1], Integer.parseInt(parts[2]));
                } else if (parts[0].equals("TX")) {
                    p.addTransaction(new Transaction(
                            Transaction.Type.valueOf(parts[1]), parts[2],
                            Integer.parseInt(parts[3]), Double.parseDouble(parts[4])));
                }
            }
            return p;
        } catch (Exception e) {
            System.out.println("⚠ Load failed: " + e.getMessage());
            return null;
        }
    }
}