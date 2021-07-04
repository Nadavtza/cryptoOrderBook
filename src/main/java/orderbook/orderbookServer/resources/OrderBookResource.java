package orderbook.orderbookServer.resources;

import orderbook.orderbookServer.models.Order;
import orderbook.orderbookServer.services.ExchangeManagerService;
import orderbook.orderbookServer.models.OrderBookSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orderbook")
public class OrderBookResource {

    @Autowired
    private ExchangeManagerService exchangeManagerService;

    @RequestMapping(value = "/fetch", params = { "exchange", "symbol" }, method = RequestMethod.GET)
    public OrderBookSnapshot getOrderBookSnapshot(@RequestParam("exchange") String exchange, @RequestParam("symbol") String symbol) {
        OrderBookSnapshot orderBookSnapshot = exchangeManagerService.getSnapshot(exchange,symbol);

        if(orderBookSnapshot == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange or Symbol not found");

        return orderBookSnapshot;
    }

    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public double update(@RequestBody Order order){
        return exchangeManagerService.processOrder(order.getExchange(), order.getSymbol(), order);
    }
}
