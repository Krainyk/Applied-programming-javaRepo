package tests;

import domain.Client;
import domain.Car;
import domain.Job;
import domain.Mechanic;
import domain.Specialization;
import domain.Order;

import java.math.BigDecimal;

public class OrderTest {
    public static void main(String[] args) {
        Client owner = new Client("Влад", "+380455623453");
        Car car = new Car("WVWZZZ1JZXW000001", "Audi", "RS6", 2025, owner);
        Order order = new Order(car);

        System.out.println(order.getStatus());

        order.diagnose();
        System.out.println(order.getStatus());


        try {
            order.approve();
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        try {
            order.diagnose();
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        order.addJob(new Job("Заміна масла", new BigDecimal("700"), new BigDecimal("300")));
        order.approve();
        System.out.println("Після схвалення: " + order.getStatus());

        Mechanic petro = new Mechanic("Петро", Specialization.ENGINE);
        order.assignMechanic(petro);
        System.out.println("Механік призначений: " +  order.getMechanic());


//        try {
//            order.startWork();
//        } catch (IllegalStateException e) {
//            System.out.println("Помилка: " + e.getMessage());
//        }

        order.assignMechanic(new Mechanic("Влад", Specialization.ENGINE));
        System.out.println("Механік призначений: " +   order.getMechanic());
        order.startWork();
        System.out.println("Статус: "  + order.getStatus());


        Job oil = new Job("Заміна масла", new BigDecimal("700"), new BigDecimal("300"));
        order.addJob(oil);
        order.completeJob(oil);
        System.out.println(oil);

        Job stranger = new Job("Чужа робота", new BigDecimal("700"), new BigDecimal("300"));
        try {
            order.completeJob(stranger);
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        Order second = new Order(car);
        second.cancel();
        System.out.println("Статус: " + second.getStatus());

        try {
            second.cancel();                       // вдруге не можна
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        try {
            second.addJob(new Job("Тест", new BigDecimal("1"), new BigDecimal("1")));
        } catch (IllegalStateException e) {
            System.out.println("Помилка: " + e.getMessage());   // скасоване не змінюється
        }
    }
}
