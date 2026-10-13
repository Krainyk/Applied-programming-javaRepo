package service;

import domain.Car;
import domain.Client;
import domain.Mechanic;
import domain.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarService {
    private final List<Client> clients = new ArrayList<>();
    private final Map<String, Car> carsByVin = new HashMap<>();
    private final List<Mechanic> mechanics = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    public void registerClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Клієнт не може бути null");
        }
        clients.add(client);
    }

    public void registerCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Автомобіль не може бути null");
        }
        if (carsByVin.containsKey(car.getVinCode())) {
            throw new IllegalStateException("Автомобіль з VIN " + car.getVinCode() + " уже зареєстрований");
        }
        if (!clients.contains(car.getOwner())) {
            throw new IllegalStateException("Власник автомобіля не зареєстрований в автосервісі");
        }
        carsByVin.put(car.getVinCode(), car);
    }

    public void registerMechanic(Mechanic mechanic) {
        if (mechanic == null) {
            throw new IllegalArgumentException("Механік не може бути null");
        }
        mechanics.add(mechanic);
    }

    public Order createOrder(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Автомобіль не може бути null");
        }
        if (carsByVin.get(car.getVinCode()) != car) {
            throw new IllegalStateException("Автомобіль не зареєстрований в автосервісі");
        }
        Order order = new Order(car);
        orders.add(order);
        return order;
    }

    public void assignMechanic(Order order, Mechanic mechanic) {
        if (order == null || mechanic == null) {
            throw new IllegalArgumentException("Замовлення і механік не можуть бути null");
        }
        if (!mechanics.contains(mechanic)) {
            throw new IllegalStateException("Механік не зареєстрований в автосервісі");
        }
        for (Order other : orders) {
            if (other != order && other.isActive() && other.getMechanic() == mechanic) {
                throw new IllegalStateException("Механік " + mechanic + " уже зайнятий в іншому активному замовленні");
            }
        }
        order.assignMechanic(mechanic);
    }
}