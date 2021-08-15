package project.restaurantManagement.models.requests;

import lombok.Data;
import project.restaurantManagement.models.entites.implemantation.Waiter;
import project.restaurantManagement.models.items.implemantation.Order;

@Data
public class FoodOrderRequest extends WaiterRequest {
    Order order;

    public FoodOrderRequest(Order order) {
        super();
        this.order = order;
    }

    @Override
    public void execute() {
        waiter.placeOrder(order);
    }
}
