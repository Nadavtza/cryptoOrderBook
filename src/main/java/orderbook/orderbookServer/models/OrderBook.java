package orderbook.orderbookServer.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.TreeMap;

@Data
@RequiredArgsConstructor
public class OrderBook {
    private static final String BUY = "BUY";
    private static final String SELL = "SELL";
    private static final String CANCEL = "Cancel";

    @NonNull private String symbol;
    private TreeMap<Double, Order> askMap = new TreeMap<>();
    private TreeMap<Double, Order> bidMap = new TreeMap<>(Collections.reverseOrder());

    public double processOrder(Order order){
        if(order.getSide().equalsIgnoreCase(BUY)){
            return handleBuyOrder(order);
        }
        else{
            return handleSellOrder(order);
        }
    }

    private double handleBuyOrder(Order buyOrder){
        if(buyOrder.getStatus().equalsIgnoreCase(CANCEL)){
            return cancel(bidMap, buyOrder);
        }

        while(!askMap.isEmpty() && buyOrder.getQuantity() > 0){
            if(buyOrder.getPrice() < askMap.firstEntry().getKey()){
                break;
            }

            calculate(buyOrder, askMap);
        }

        return handleLeftoverQuantity(buyOrder, bidMap);
    }

    private double handleSellOrder(Order sellOrder){
        if(sellOrder.getStatus().equalsIgnoreCase(CANCEL)){
            return cancel(askMap, sellOrder);
        }

        while(!bidMap.isEmpty() && sellOrder.getQuantity() > 0){
            if(sellOrder.getPrice() > bidMap.firstEntry().getKey()){
                break;
            }

            calculate(sellOrder, bidMap);
        }

        return handleLeftoverQuantity(sellOrder, askMap);
    }

    private void calculate(Order orderToProcess, TreeMap<Double, Order> orderMap) {
        Order pollOrder = orderMap.pollFirstEntry().getValue();
        double pollOrderQuantity = pollOrder.getQuantity();
        double orderToProcessQuantity = orderToProcess.getQuantity();

        orderToProcess.setQuantity(Math.max(orderToProcessQuantity - pollOrderQuantity, 0));
        pollOrder.setQuantity(Math.max(pollOrderQuantity - orderToProcessQuantity, 0));

        // Handle case buy quantity left
        if(pollOrder.getQuantity() > 0){
            orderMap.putIfAbsent(pollOrder.getPrice(), pollOrder);
        }
    }

    private double handleLeftoverQuantity(Order order, TreeMap<Double, Order> orderSideMap) {
        if(order.getQuantity() > 0){
            Order updatedOrder = orderSideMap.get(order.getPrice());

            if(updatedOrder == null){
                orderSideMap.putIfAbsent(order.getPrice(), order);
            }
            else{
                order.addQuantity(updatedOrder.getQuantity());
                orderSideMap.replace(order.getPrice(), order);
            }
        }

        if(askMap.isEmpty()){
            return 0;
        }

        Order leftAfterOrder= askMap.firstEntry().getValue();
        double leftQuantity = 0;

        if(leftAfterOrder.getPrice() == order.getPrice()){
            leftQuantity = leftAfterOrder.getQuantity();
        }

        return leftQuantity;
    }

    private double cancel(TreeMap<Double, Order> map, Order order){
        Order orderToCancel = map.get(order.getPrice());

        if(orderToCancel == null || orderToCancel.getQuantity() < order.getQuantity()){
            return 0;
        }

        if(orderToCancel.getQuantity() ==  order.getQuantity()){
            map.remove(order.getPrice());
            return 0;
        }
        else {
            orderToCancel.subQuantity(order.getQuantity());
            return orderToCancel.getQuantity();
        }
    }
}
