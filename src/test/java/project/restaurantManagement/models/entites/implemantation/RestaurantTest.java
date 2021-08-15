package project.restaurantManagement.models.entites.implemantation;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class RestaurantTest {
    private Restaurant restaurant;

    @Before
    public void setup(){
        restaurant = new Restaurant(5, 1, 10, 4);
    }

    @Test
    public void restaurant_test(){
        restaurant.open();

        int numberOfCustomers = 30;
        List<Thread> customers = new LinkedList<>();

        for(int i =0 ; i< numberOfCustomers ; i++){
            customers.add(new Thread(new Customer("customerID" + i, "customerName" + i, restaurant)));
        }

        for (Thread customer : customers){
            customer.start();
        }

        try {
            for (Thread customer : customers){
                    customer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        restaurant.close();
    }
}
