package project.restaurantManagement.models.items.implemantation;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Table {
    int tableNumber;
    int numberOfSeats;
    int numberOfCustomers;

    public void clearTable(){
        numberOfCustomers = 0;
    }
}
