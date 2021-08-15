package project.restaurantManagement.models.entites.implemantation;

import lombok.Data;
import project.restaurantManagement.configurations.Metadata;
import project.restaurantManagement.models.entites.interfaces.IChef;
import project.restaurantManagement.models.entites.interfaces.IWaiter;
import project.restaurantManagement.models.items.implemantation.Dish;
import project.restaurantManagement.models.items.implemantation.Menu;
import project.restaurantManagement.models.items.implemantation.Order;
import project.restaurantManagement.models.items.implemantation.Table;
import project.restaurantManagement.models.requests.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Restaurant {
    private Menu menu;
    private List<Thread> workers = new LinkedList<>();
    private List<IChef> chefs = new LinkedList<>();
    private List<IWaiter> waiters = new LinkedList<>();

    private BlockingQueue<Order> chefPool = new LinkedBlockingDeque<>();
    private BlockingQueue<WaiterRequest> waitersPool = new LinkedBlockingDeque<>();
//    private PriorityBlockingQueue<WaiterRequest> waitersPool = new PriorityBlockingQueue<>();


    private BlockingQueue<Table> availableTables = new LinkedBlockingDeque<>();

    private AtomicLong incomes;
    private int numberOfChefs;
    private int numberOfWaiters;
    private int numberOfTables;
    private int numberOfSeatsForTable;

    public Restaurant(int numberOfChefs, int numberOfWaiters, int numberOfTables, int numberOfCSeatsForTable) {
        incomes = new AtomicLong();
        this.numberOfChefs = numberOfChefs;
        this.numberOfWaiters = numberOfWaiters;
        this.numberOfTables = numberOfTables;
        this.numberOfSeatsForTable = numberOfCSeatsForTable;

        init();
    }

    public void enter(Customer customer) {
        try {
            Table table = availableTables.take();
            customer.setTable(table);
            System.out.println(String.format("customer [ %s ] set in table [ %d ] \n", customer.getName(), table.getTableNumber()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void order(Order order) {
        try {
            FoodOrderRequest foodOrderRequest = new FoodOrderRequest(order);

            synchronized (waitersPool){
                synchronized (foodOrderRequest){
                    waitersPool.put(foodOrderRequest);
                    foodOrderRequest.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void orderBill(Customer customer) {
        try {
            BillOrderRequest billOrderRequest = new BillOrderRequest(customer);

            synchronized (waitersPool){
                synchronized (billOrderRequest){
                    waitersPool.put(billOrderRequest);
                    billOrderRequest.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pay(Customer customer) {
        try {
            CollectBillRequest collectBillRequest = new CollectBillRequest(customer);

            synchronized (waitersPool){
                synchronized (collectBillRequest){
                    waitersPool.put(collectBillRequest);
                    collectBillRequest.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leave(Customer customer) {
        Table table = customer.getTable();
        table.clearTable();

        try {
            availableTables.put(table);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void assignWaiter(Waiter waiter) {
        try {
            WaiterRequest waiterRequest = waitersPool.take();

           synchronized (waiterRequest){
               waiterRequest.setWaiter(waiter);
               waiterRequest.executeRequest();
               waiterRequest.notifyAll();
           }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void alertChef(Order order) {
        try {
            chefPool.put(order);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void assignChef(Chef chef) {
        try {
            Order order = chefPool.take();
            chef.setOrder(order);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void alertWaiter(Order order) {
        ServeFoodRequest serveFoodRequest  = new ServeFoodRequest(order);
        try {
            waitersPool.put(serveFoodRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Restaurant data

    public void open() {
        for (Thread worker : workers) {
            worker.start();
        }
    }

    public void close() {
        chefs.stream().forEach(IChef::stopWorking);
        waiters.stream().forEach(IWaiter::stopWorking);

        for (Thread worker : workers) {
            worker.interrupt();

            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        createMenu();
        initTables();
        initWorkers();
    }

    public void initCocks() {
        for (int i = 1; i <= numberOfChefs; ++i) {
            Chef chef = new Chef("chefID" + i, "chefName" + i, this);
            chefs.add(chef);
            workers.add(new Thread(chef));
        }
    }

    public void initWaiters() {
        for (int i = 1; i <= numberOfWaiters; ++i) {
            Waiter waiter = new Waiter("waiterID" + i, "waiterName" + i, this);
            waiters.add(waiter);
            workers.add(new Thread(waiter));
        }
    }

    public void initWorkers() {
        workers = new LinkedList<>();
        initCocks();
        initWaiters();
    }

    private void initTables() {
        for (int i = 1; i <= numberOfTables; i++) {
            Table table = new Table(i, numberOfSeatsForTable, 0);
            availableTables.add(table);
        }
    }
    public void addDish(Dish newDish) {
        if (menu == null) {
            menu = new Menu(new LinkedList<>());
        }

        menu.addDish(newDish);
    }

    private void createMenu() {
        addDish(new Dish("fish", 102, 5, Metadata.DishTypes.MAIN_COURSE));
        addDish(new Dish("pasta", 67, 6, Metadata.DishTypes.MAIN_COURSE));
        addDish(new Dish("salad", 51, 3, Metadata.DishTypes.APPETIZER));
        addDish(new Dish("pizza", 65, 7, Metadata.DishTypes.MAIN_COURSE));
        addDish(new Dish("coke", 12, 1, Metadata.DishTypes.DRINK));
        addDish(new Dish("sprite", 12, 1, Metadata.DishTypes.DRINK));
        addDish(new Dish("wine", 31, 1, Metadata.DishTypes.DRINK));
        addDish(new Dish("beer", 29, 1, Metadata.DishTypes.DRINK));
        addDish(new Dish("cake", 34, 2, Metadata.DishTypes.DESSERT));
        addDish(new Dish("ice cream", 41, 3, Metadata.DishTypes.DESSERT));
        addDish(new Dish("brownies", 27, 3, Metadata.DishTypes.DESSERT));

        menu.presentMenu();
    }
}
