package project.restaurantManagement.models.entites.implemantation;

import project.restaurantManagement.models.entites.interfaces.IWaiter;
import project.restaurantManagement.models.items.implemantation.Bill;
import project.restaurantManagement.models.items.implemantation.Order;

import java.util.concurrent.atomic.AtomicBoolean;

public class Waiter extends RestaurantEntity implements IWaiter {
    AtomicBoolean isWorking = new AtomicBoolean(true);

    public Waiter(String id, String name, Restaurant restaurant) {
        super(id, name, restaurant);
    }

    @Override
    public void run() {
        while (isWorking.get()) {
            assign();
        }
    }

    @Override
    public void assign() {
        System.out.println(String.format("Waiter [ %s ] assigning \n", name));
        restaurant.assignWaiter(this);
    }

    @Override
    public void placeOrder(Order order) {
        System.out.println(String.format("Waiter [ %s ] placing order of table [ %d ] \n", name, order.getTableNumber()));
        restaurant.alertChef(order);
    }

    @Override
    public void serveOrder(Order order) {
        System.out.println(String.format("Waiter [ %s ] served the order to table [ %d ] \n", name, order.getTableNumber()));

        synchronized (order){
            order.notify();
        }
    }

    @Override
    public void serveBill(Customer customer){
        customer.setBill(new Bill(customer.getTable().getTableNumber(), customer.getOrder().getCost(), 0));
        System.out.println(String.format("Waiter [ %s ] served the bill to customer [ %s ], Bill Info [%s ] \n", name, customer.getName(), customer.getBill()));
    }

    @Override
    public void collectPayment(Bill bill) {
        System.out.println(String.format("Waiter [ %s ] collect the bill from [ table ] %d , Received [ %d ] \n", name, bill.getTableNumber(), bill.getReceivedValue()));

        if(bill.getReceivedValue() < bill.getTotalCost()){
            System.out.println("Warning: missing money!");
        }

        restaurant.getIncomes().addAndGet(bill.getReceivedValue());
        System.out.println(String.format("Updated restaurant incomes [ %d ] \n", restaurant.getIncomes().get()));
    }

    @Override
    public void stopWorking(){
        isWorking.set(false);
    }
}
