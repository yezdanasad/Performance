package v1;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

public  class OrderProcessor {

    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;
    private static List<Match> matchesFound;

    public OrderProcessor (OrderBook orderBook){
        this.buyOrders = orderBook.getBuyOrders();
        this.sellOrders = orderBook.getSellOrders();
    }

    public List<Match> match() {

        List <Match> matchesFound = new ArrayList<Match>();

        while (matchPossible()) {
            processMatches(buyOrders.peek(), sellOrders.peek());
        }

        return matchesFound;
    }

    private void processMatches(Order buy, Order sell){
        int quantity = Math.min(buy.getQuantity(), sell.getQuantity());
        double price = sell.getPrice();

        buy.reduceQuantity(quantity);
        sell.reduceQuantity(quantity);

        matchesFound.add(new Match(buy.getId(), sell.getId(), quantity, price, System.nanoTime()));

        boolean sellFilled = sell.getQuantity() == 0;
        boolean buyFilled = buy.getQuantity() == 0;

        // Remove completed orders
        if (buyFilled) buyOrders.poll();
        if (sellFilled) sellOrders.poll();

    }

    private boolean matchPossible() {
        return (
                !buyOrders.isEmpty() &&
                        !sellOrders.isEmpty() &&
                        buyOrders.peek().getPrice() >= sellOrders.peek().getPrice()
        );
    }


}