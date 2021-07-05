package orderbook.orderbookServer.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import orderbook.orderbookServer.models.Exchange;
import orderbook.orderbookServer.models.Order;
import orderbook.orderbookServer.models.OrderBook;
import orderbook.orderbookServer.models.OrderBookSnapshot;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ExchangeManagerService {

    private Map<String, Exchange> exchangeMap = new HashMap<>();

    @PostConstruct
    private void init(){
        List<Order> orders = readCsvFile();

        for(Order order : orders){
            Exchange exchange = exchangeMap.get(order.getExchange());

            if(exchange == null){
                exchange = new Exchange(order.getExchange());
                exchangeMap.put(order.getExchange(), exchange);
            }

            if(exchange.getOrderBook(order.getSymbol()) == null){
                exchange.addOrderBook(order.getSymbol(), new OrderBook(order.getSymbol()));
            }

            processOrder(order.getExchange(), order.getSymbol(), order);
        }
    }

    private List<Order> readCsvFile() {
        List<Order> orders = new ArrayList<>();
        List<String[]> allData = new ArrayList<>();

        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("./src/main/resources/orderbook_data.csv");

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            allData = csvReader.readAll();

            // print Data
            for (String[] row : allData) {
                orders.add(new Order(Instant.ofEpochMilli( Long.parseLong( row[0] ) ), row[1], row[2], row[3], Double.valueOf(row[4]), Double.valueOf(row[5]), row[6], row[7]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public OrderBookSnapshot getSnapshot(String exchangeName, String symbol){
        Exchange exchange = exchangeMap.get(exchangeName);

        if(exchange != null){
            OrderBook orderBook = exchange.getOrderBook(symbol);
            if(orderBook != null){
                return new OrderBookSnapshot(orderBook.getSymbol(), orderBook.getBidMap(), orderBook.getAskMap());
            }
        }
        return null;
    }

    public double processOrder(String exchangeName, String symbol, Order order){
        if(Strings.isEmpty(exchangeName) || order == null){
            return 0;
        }

        Exchange exchange = exchangeMap.get(exchangeName);

        if(exchange == null){
            return 0;
        }

        return exchange.processOrder(symbol, order);
    }
}
