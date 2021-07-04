package orderbook.orderbookServer.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.TreeMap;

@Data
@AllArgsConstructor
public class OrderBookSnapshot {
    private String symbol;
    private TreeMap<Double, Order> bidMap;
    private TreeMap<Double, Order> askMap;
}
