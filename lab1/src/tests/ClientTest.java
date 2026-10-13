package tests;

import domain.Client;

public class ClientTest {
    public static void main(String[] args) {
        Client c = new Client("Максим", "+380453423745");
        System.out.println(c);

        try {
            new Client("", "123");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}
