package app;

import domain.Car;
import domain.Client;
import domain.CorporateClient;
import domain.Job;
import domain.Mechanic;
import domain.Order;
import domain.Specialization;
import service.CarService;

import java.math.BigDecimal;

public class CarServiceDemo {
    public static void main(String[] args) {
        CarService service = new CarService();

        System.out.println("=== 1. Реєстрація клієнта й автомобіля ===");
        Client client = new Client("Влад", "+380455623453");
        service.registerClient(client);
        Car car = new Car("WVWZZZ1JZXW000001", "Audi", "RS6", 2025, client);
        service.registerCar(car);
        System.out.println("Клієнт: " + client);
        System.out.println("Авто: " + car);

        Mechanic petro = new Mechanic("Петро", Specialization.ENGINE);
        Mechanic ivan = new Mechanic("Іван", Specialization.SUSPENSION);
        service.registerMechanic(petro);
        service.registerMechanic(ivan);

        System.out.println("\n=== 2. Успішне замовлення ===");
        Order order = service.createOrder(car);
        System.out.println("Створено, статус: " + order.getStatus());

        Job oil = new Job("Заміна масла", new BigDecimal("500"), new BigDecimal("200"));
        Job brakes = new Job("Заміна гальм", new BigDecimal("1000"), new BigDecimal("300"));
        order.addJob(oil);
        order.addJob(brakes);
        System.out.println("Додано 2 роботи");

        order.diagnose();
        System.out.println("Статус: " + order.getStatus());
        order.approve();
        System.out.println("Статус: " + order.getStatus());
        service.assignMechanic(order, petro);
        System.out.println("Призначено механіка: " + order.getMechanic());
        order.startWork();
        System.out.println("Статус: " + order.getStatus());
        order.completeJob(oil);
        order.completeJob(brakes);
        System.out.println("Роботи виконані: " + oil + "; " + brakes);
        order.complete();
        System.out.println("Статус: " + order.getStatus());

        System.out.println("\n=== 3. Підсумкова вартість ===");
        System.out.println("Разом: " + order.calculateTotal());

        System.out.println("\n=== 4. Погодити порожнє замовлення ===");
        Order empty = service.createOrder(car);
        empty.diagnose();
        try {
            empty.approve();
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n=== 5. Запустити без механіка ===");
        Order noMechanic = service.createOrder(car);
        noMechanic.addJob(new Job("Діагностика", new BigDecimal("100"), new BigDecimal("50")));
        noMechanic.diagnose();
        noMechanic.approve();
        try {
            noMechanic.startWork();
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n=== 6. Призначити зайнятого механіка ===");
        service.assignMechanic(noMechanic, petro);
        System.out.println("Петро призначений на замовлення, статус: " + noMechanic.getStatus());
        Order another = service.createOrder(car);
        another.addJob(new Job("Заміна свічок", new BigDecimal("200"), new BigDecimal("100")));
        another.diagnose();
        another.approve();
        try {
            service.assignMechanic(another, petro);
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n=== 7. Змінити завершене замовлення ===");
        try {
            order.addJob(new Job("Ще робота", new BigDecimal("10"), new BigDecimal("10")));
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n=== 8. Заборонений перехід статусів ===");
        Order fresh = service.createOrder(car);
        System.out.println("Статус: " + fresh.getStatus());
        try {
            fresh.startWork();   // CREATED -> IN_PROGRESS пропускає кілька етапів
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n=== 9. Дублікат VIN ===");
        Car duplicate = new Car("WVWZZZ1JZXW000001", "BMW", "M5", 2024, client);
        try {
            service.registerCar(duplicate);
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        System.out.println("\n=== 10. Корпоративний клієнт і знижки ===");
        CorporateClient corporate = new CorporateClient(
                "Олена", "+380671112233", "ТОВ АвтоТранс",
                new BigDecimal("10"), new BigDecimal("20"));
        service.registerClient(corporate);

        Car corporateCar = new Car("WVWZZZ1JZXW000002", "Mercedes", "Sprinter", 2024, corporate);
        service.registerCar(corporateCar);
        Order corporateOrder = service.createOrder(corporateCar);
        Job corporateJob = new Job("Сервісне обслуговування",
                new BigDecimal("1000"), new BigDecimal("500"));
        corporateOrder.addJob(corporateJob);
        corporateOrder.diagnose();
        corporateOrder.approve();
        service.assignMechanic(corporateOrder, ivan);
        corporateOrder.startWork();
        corporateOrder.completeJob(corporateJob);
        corporateOrder.complete();
        System.out.println("Корпоративний клієнт: " + corporate);
        System.out.println("Разом зі знижками: " + corporateOrder.calculateTotal());

        System.out.println("\n=== 11. Звичайний клієнт без знижок ===");
        Car ordinaryCar = new Car("WVWZZZ1JZXW000003", "Ford", "Focus", 2023, client);
        service.registerCar(ordinaryCar);
        Order ordinaryOrder = service.createOrder(ordinaryCar);
        ordinaryOrder.addJob(new Job("Сервісне обслуговування",
                new BigDecimal("1000"), new BigDecimal("500")));
        System.out.println("Разом: " + ordinaryOrder.calculateTotal());

        System.out.println("\n=== 12. Перевірка знижок ===");
        try {
            new CorporateClient("Ірина", "+380671112244", "ТОВ Перевізник",
                    new BigDecimal("150"), BigDecimal.ZERO);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
        try {
            new CorporateClient("Ірина", "+380671112244", "ТОВ Перевізник",
                    BigDecimal.ZERO, new BigDecimal("-5"));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}