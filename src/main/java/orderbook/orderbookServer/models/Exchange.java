package orderbook.orderbookServer.models;

import lombok.*;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Exchange {
    @NonNull private String exchangeName ;
    private Map<String, OrderBook> orderBookMap = new HashMap<>();

    public void addOrderBook(String orderBookName, OrderBook orderBook){
        orderBookMap.putIfAbsent(orderBookName, orderBook);
    }

    public void removeOrderBook(String orderBookName){
        orderBookMap.remove(orderBookName);
    }

    public double processOrder(String symbol, Order order){
        if(Strings.isEmpty(symbol) || order == null){
            return 0;
        }

        OrderBook orderBook = orderBookMap.get(symbol);

        if(orderBook == null){
            return 0;
        }

        return orderBook.processOrder(order);
    }

    public OrderBook getOrderBook(String symbol) {
        return orderBookMap.get(symbol);
    }
}
