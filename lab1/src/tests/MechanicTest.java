package tests;

import domain.Mechanic;
import domain.Specialization;

public class MechanicTest {
    public static void main(String[] args) {
        Mechanic m = new Mechanic("Петро", Specialization.ENGINE);
        System.out.println(m);

//        try {
//            new Mechanic("", Specialization.ENGINE);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Помилка: " + e.getMessage());
//        }
//
//        try {
//            new Mechanic("Петро",null);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Помилка: " + e.getMessage());
//        }
    }
}
