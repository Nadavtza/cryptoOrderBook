package project.restaurantManagement.models.entites.implemantation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class RestaurantEntity implements Runnable{

    protected String id;
    protected String name;
    protected Restaurant restaurant;

    public RestaurantEntity(String id, String name, Restaurant restaurant){
        this.id = id;
        this.name = name;
        this.restaurant = restaurant;
    }

    public abstract void run();
}
