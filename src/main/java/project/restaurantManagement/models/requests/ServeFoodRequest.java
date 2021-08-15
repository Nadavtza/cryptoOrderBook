package project.restaurantManagement.models.requests;

import lombok.Data;
import project.restaurantManagement.models.entites.implemantation.Waiter;
import project.restaurantManagement.models.items.implemantation.Order;

import java.time.LocalDateTime;

@Data
public class ServeFoodRequest extends WaiterRequest{
    Order order;

    public ServeFoodRequest(Order order) {
        super();
        this.order = order;
    }

    @Override
    public void execute() {
        waiter.serveOrder(order);
    }
}
