package project.restaurantManagement.models.requests;

import lombok.Data;
import project.restaurantManagement.models.entites.implemantation.Customer;
import project.restaurantManagement.models.entites.implemantation.Waiter;

@Data
public class CollectBillRequest extends WaiterRequest {
    Customer customer;

    public CollectBillRequest(Customer customer) {
        super();
        this.customer = customer;
    }

    @Override
    public void execute() {
        waiter.collectPayment(customer.getBill());
    }
}
