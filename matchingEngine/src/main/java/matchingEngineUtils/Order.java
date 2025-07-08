package matchingEngineUtils;

import java.util.UUID;



public class Order {

    public enum OrderType{
        BUY,
        SELL

    }
     public enum Ticker {
        A, B, C, D, E
     }

    private final UUID orderId;
    private final String userId;
    private final OrderType type;
    private final double price;
    private final long timestamp;
    private final Ticker ticker;
    private int quantity;
    private boolean cancelled;
    private boolean filled;

    public Order(
            OrderType type,
            String userId,
            Ticker ticker,
            double price,
            int quantity
    ) {
        this.orderId = UUID.randomUUID();
        this.userId = userId;
        this.type = type;
        this.ticker = ticker;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = System.nanoTime();
        this.cancelled = false;
        this.filled = false;
    }

    public UUID getId() {
        return orderId;
    }

    public OrderType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public String getUserId() {
        return userId;
    }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    @Override
    public String toString() {
        return String.format(
                "Order{orderId=%s, userId=%s, type=%s, ticker=%s, price=%.2f, quantity=%d, timestamp=%d, cancelled=%s, filled=%s}",
                orderId,
                userId,
                type,
                ticker,
                price,
                quantity,
                timestamp,
                cancelled,
                filled
        );
    }

}