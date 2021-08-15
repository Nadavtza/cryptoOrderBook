package project.restaurantManagement.models.items.implemantation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Bill {
    int tableNumber;
    long totalCost;
    long receivedValue;
}
