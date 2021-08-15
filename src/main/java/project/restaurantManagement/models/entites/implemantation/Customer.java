package project.restaurantManagement.models.entites.implemantation;

import lombok.Data;
import project.restaurantManagement.models.entites.interfaces.ICustomer;
import project.restaurantManagement.models.items.implemantation.Bill;
import project.restaurantManagement.models.items.implemantation.Dish;
import project.restaurantManagement.models.items.implemantation.Order;
import project.restaurantManagement.models.items.implemantation.Table;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

@Data
public class Customer extends RestaurantEntity implements ICustomer {
    private Table table;
    private Order order;
    private Bill bill;

    public Customer(String id, String name, Restaurant restaurant) {
        super(id, name, restaurant);
    }

    @Override
    public void run() {
        enterRestaurant();
        orderFood();
        waitForFood();
        eat();
        orderBill();
        pay();
        leaveRestaurant();
    }

    public void waitForFood() {
        System.out.println(String.format("Customer [ %s ] waiting for the order \n", name));

        synchronized (order){
           try {
               order.wait();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

    @Override
    public void enterRestaurant(){
        System.out.println(String.format("Customer [ %s ] enter the restaurant \n", name));

        restaurant.enter(this);
    }

    @Override
    public void orderFood() {
        int numberOfDishes = getRandomIntInRange(1, 5);
        List<Dish> dishes = restaurant.getMenu().getDishes();

        HashMap<Dish, Integer> selectDishesMap = selectDishes(dishes, numberOfDishes);

        order = new Order(table.getTableNumber(), "", selectDishesMap, numberOfDishes);

        System.out.println(String.format("Customer [ %s ] making an order : [ %s ] \n", name, order));

        restaurant.order(this.getOrder());
    }

    @Override
    public void eat() {
        System.out.println(String.format("Customer %s eating \n", name));

        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Customer %s finish to eat \n", name));
    }

    @Override
    public void orderBill() {
        System.out.println(String.format("Customer [ %s ] order bill \n", name));
        restaurant.orderBill(this);
    }

    @Override
    public void pay() {
        System.out.println(String.format("Customer [ %s ] asked to pay \n" , name));

        bill.setReceivedValue(bill.getTotalCost() + 10);
        restaurant.pay(this);
    }

    @Override
    public void leaveRestaurant(){
        System.out.println(String.format("Customer [ %s ] leaving the restaurant \n" , name));
        restaurant.leave(this);
    }

    private int getRandomIntInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private HashMap<Dish, Integer> selectDishes(List<Dish> dishes, int numberOfDishes) {
        HashMap<Dish,Integer> dishMap = new HashMap<>();

        for(int i = 0; i< numberOfDishes; i++){
            Dish selectedDish = dishes.get(getRandomIntInRange(0, dishes.size()));
            dishMap.put(selectedDish, dishMap.getOrDefault(selectedDish,0) +1);
        }
        return dishMap;
    }
}
