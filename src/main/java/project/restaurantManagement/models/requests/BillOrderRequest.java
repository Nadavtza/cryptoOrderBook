package project.restaurantManagement.models.requests;

import lombok.Data;
import project.restaurantManagement.models.entites.implemantation.Customer;
import project.restaurantManagement.models.entites.implemantation.Waiter;

@Data
public class BillOrderRequest extends WaiterRequest {
    Customer customer;

    public BillOrderRequest(Customer customer) {
        super();
        this.customer = customer;
    }

    @Override
    public void execute() {
        waiter.serveBill(customer);
    }
}
