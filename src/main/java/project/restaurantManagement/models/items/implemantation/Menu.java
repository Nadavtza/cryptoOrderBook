package project.restaurantManagement.models.items.implemantation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Data
public class Menu {
    List<Dish> dishes;

    public void addDish(Dish dish){
        dishes.add(dish);
    }

    public void presentMenu(){
        System.out.println("* ------ Menu ------ * \n");

        Optional.ofNullable(dishes)
                .orElse(new LinkedList<>())
                .forEach((dish) -> System.out.println(dish.toString()));

        System.out.println("\n * ------ Menu End------ * \n");
    }
}
