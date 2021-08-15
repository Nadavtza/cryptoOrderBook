package project.restaurantManagement.models.entites.interfaces;

import project.restaurantManagement.models.entites.implemantation.Customer;
import project.restaurantManagement.models.items.implemantation.Bill;
import project.restaurantManagement.models.items.implemantation.Order;

public interface IWaiter {
    void assign();

    void placeOrder(Order order);

    void serveOrder(Order order);

    void serveBill(Customer customer);

    void collectPayment(Bill bill);

    void stopWorking();
}
