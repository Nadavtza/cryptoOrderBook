package project.restaurantManagement.models.entites.implemantation;

import lombok.Data;
import project.restaurantManagement.models.entites.interfaces.IChef;
import project.restaurantManagement.models.items.implemantation.Dish;
import project.restaurantManagement.models.items.implemantation.Order;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

@Data
public class Chef extends RestaurantEntity implements IChef {
    AtomicBoolean isWorking = new AtomicBoolean(true);
    Order order;

    public Chef(String id, String name, Restaurant restaurant) {
        super(id, name, restaurant);
    }

    @Override
    public void run() {
        while (isWorking.get()){
            assign();
            prepareOrder();
        }
    }

    @Override
    public void assign() {
        System.out.println(String.format("Chef [ %s ] assigning \n", name));
        restaurant.assignChef(this);
    }

    @Override
    public void prepareOrder(){
        if(order == null)
            return;

        System.out.println(String.format("Chef [ %s ] starting the order for table [ %d ] \n", name, order.getTableNumber()));

        cookOrder();

        System.out.println(String.format("Chef [ %s ] completed the order for table [ %d ] \n", name, order.getTableNumber()));

        restaurant.alertWaiter(order);
        order = null;
    }

    private void cookOrder() {
        for(Map.Entry<Dish,Integer> entry : order.getDishMap().entrySet()){
            int dishCounter = entry.getValue();

            for(int i = 0 ; i<dishCounter; i++){
                cookDish(entry.getKey());
            }
        }
    }

    private void cookDish(Dish dish) {
        System.out.println(String.format("Chef [ %s ] preparing dish [ %s ] , dish preparation time %d \n", name, dish.getName(), dish.getPreparationTime()));

        try {
            sleep(dish.getPreparationTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Chef [ %s ] prepared dish [ %s ] , dish is ready \n", name, dish.getName()));
    }

    @Override
    public void stopWorking(){
        isWorking.set(false);
    }
}
