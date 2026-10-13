package tests;

import domain.Client;
import domain.Car;

public class CarTest {
    public static void main(String[] args) {
        Client owner = new Client("Влад", "+380455623453");
        Car car = new Car("WVWZZZ1JZXW000001", "Audi", "RS6", 2025, owner);
        System.out.println(car);

        try {
            new Car("WVWZZZ1JZXW000001", "Audi", "", 2025, owner);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}
