package v1;

import matchingEngineUtils.*;
import java.util.List;

public  class Main {
    public static void main(String[] args) {

        OrderGenerator generator = new OrderGenerator();
        Long totalTime = 0L;
        int totalOrders = 0;
        for ( int i = 0 ; i < 25; i++){
            OrderBook book = prepareIteration(generator);
            Long start = System.nanoTime();
            List<Order.Match> results = runIteration(book);
            Long end = System.nanoTime();
            System.out.println(results.size() + " orders matched in " + (end - start)/1000000.0 + " ms.");
            totalTime += (end-start);
            totalOrders += results.size();
        }
        Double Time = totalTime/1000000000.0;
        Double speed = totalOrders/Time;
        System.out.println("-------------------- Results -----------------------");
        System.out.println("Processed a total of " + totalOrders + " orders in " + Time + "  s.");
        System.out.println("Speed : " + speed + " orders/s." );
    }

    private static List<Order.Match> runIteration(OrderBook orderBook){
        OrderProcessor processor = new OrderProcessor(orderBook);
        return processor.match();

    }

    private static OrderBook prepareIteration(OrderGenerator generator){
        OrderBook book = new OrderBook();
        for(int i = 0; i <= 100000; i++) {
            book.addToBook(generator.generateRandomOrder());
        }
        return book;
    }
}