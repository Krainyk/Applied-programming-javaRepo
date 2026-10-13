package tests;

import domain.Job;

import java.math.BigDecimal;

public class JobTest {
    public static void main(String[] args) {
        Job j = new Job("Заміна масла: ", new BigDecimal("400"),  new BigDecimal("500"));
        System.out.println(j);
        j.markCompleted();
        System.out.println(j);

        try {
            new Job("Тест", new BigDecimal("-1"), new BigDecimal("0"));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}
