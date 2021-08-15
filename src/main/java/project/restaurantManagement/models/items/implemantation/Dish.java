package project.restaurantManagement.models.items.implemantation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class Dish {
    String name;
    long price;
    int preparationTime; // Change type TODO
    String type;
}
