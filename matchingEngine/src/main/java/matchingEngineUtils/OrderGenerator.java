package matchingEngineUtils;

import java.util.Random;


public class OrderGenerator{

    private static Random random;

    public OrderGenerator(){
        random = new Random();
    }

    private static Order.OrderType buyOrSell(){
        return random.nextInt(2) == 0 ? Order.OrderType.BUY : Order.OrderType.SELL;
    }

    public Order generateRandomOrder(){

        return new Order(buyOrSell(), "dummy", Order.Ticker.A, (random.nextDouble() * 1000), random.nextInt(10,10000) );
    }
}
