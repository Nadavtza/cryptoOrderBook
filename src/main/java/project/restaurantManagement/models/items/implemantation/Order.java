package project.restaurantManagement.models.items.implemantation;

import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@ToString
@Data
public class Order {
    int tableNumber;
    String comments;
    HashMap<Dish,Integer> dishMap;
    long cost;
    int numberOfDishes;

    public Order(int tableNumber, String comments, HashMap<Dish,Integer> dishMap, int numberOfDishes){
        this.tableNumber = tableNumber;
        this.comments = comments;
        this.dishMap = dishMap;
        this.numberOfDishes = numberOfDishes;

        this.cost =  calculateCost(dishMap);
    }

    private long calculateCost(HashMap<Dish, Integer> selectDishesMap) {
        long cost = 0 ;

        if(CollectionUtils.isEmpty(selectDishesMap))
            return cost;

        for(Map.Entry<Dish, Integer> entry : selectDishesMap.entrySet()){
            cost += entry.getKey().getPrice() * entry.getValue();
        }

        return cost;
    }
}
