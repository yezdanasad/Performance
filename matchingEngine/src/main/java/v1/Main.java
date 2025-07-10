package v1;

import matchingEngineUtils.*;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public  class Main {
    public static void main(String[] args) throws IOException {


        OrderGenerator generator = new OrderGenerator();
        AtomicBoolean running = new AtomicBoolean(true);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        OrderProcessor processor = new OrderProcessor(new OrderBook());

        try(SingleChronicleQueue queue = SingleChronicleQueueBuilder.builder().path("test-queue").build();){

            executor.submit(() -> {
                try (ExcerptAppender appender = queue.createAppender();
                ){
                    System.out.println("Enqueing orders...");
                    while(true){
                        Order order = generator.generateRandomOrder();
                        appender.writeDocument(w -> w.write("order").object(order));
                    }
                }
            });

            executor.submit(() -> {

                AtomicInteger numMatches = new AtomicInteger(0);

                try (ExcerptTailer tailer = queue.createTailer()) {
                    long durationNanos = 60_000_000_000L;
                    long start = System.nanoTime();

                    while (System.nanoTime() - start <= durationNanos) {
                        tailer.readDocument(wire -> {
                            Order order = wire.read("order").object(Order.class);
                            processor.addOrder(order);
                            List<Order.Match> results = processor.match();
                            numMatches.addAndGet(results.size());
                        });
                    }
                }
                System.out.println("Speed : " + (numMatches.get() / 60) + " orders /s");

            });

            executor.shutdown();
            executor.awaitTermination(65, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


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