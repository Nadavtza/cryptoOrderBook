package orderbook.orderbookServer.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private String timestamp;
    private String exchange;
    private String symbol;
    private String side;
    private double price;
    private double quantity;
    private String status;
    private String type;

    public void addQuantity(double quantity){
        this.quantity = this.quantity + quantity;
    }

    public void subQuantity(double quantity) {
        this.quantity = this.quantity - quantity;
    }
}
