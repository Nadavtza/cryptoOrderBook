package project.restaurantManagement.models.requests;

import lombok.Data;
import project.restaurantManagement.models.entites.implemantation.Waiter;

import java.time.LocalDateTime;

@Data
public abstract class WaiterRequest implements Comparable<WaiterRequest>{
    protected LocalDateTime date;
    protected Waiter waiter;

    public WaiterRequest(){
        this.date = LocalDateTime.now();
    }

    public void executeRequest(){
        execute();
    }

    protected abstract void execute();

    public int compareTo(WaiterRequest waiterRequest){
        return -1 * this.getDate().compareTo(waiterRequest.getDate());
    }
}
