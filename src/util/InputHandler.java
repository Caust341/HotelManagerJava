package util;

import java.time.LocalDate;
import java.util.Scanner;

public class InputHandler {
    private static final Scanner sc = new Scanner(System.in);

    public String getString(String message) {
        System.out.println(message);
        return sc.nextLine().trim();
    }

    public Integer getInteger(String message) {
        while (true) {
            try {
                System.out.println(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid input");
            } catch (NullPointerException e) {
                System.out.println("ERROR: empty value");
            }
        }
    }

    public Double getDouble(String message) {
        while (true) {
            try {
                System.out.println(message);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid Input");
            } catch (NullPointerException e) {
                System.out.println("ERROR: empty value");
            }
        }
    }

    public LocalDate getDate(String message) {
        while (true) {
            try {
                System.out.println(message + " (PROMPT: YYYY-MM-DD):");
                return LocalDate.parse(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Wrong Input");
            } catch (NullPointerException e) {
                System.out.println("Empty value");
            }
        }
    }

    public Long getLong(String s) {
        while (true) {
            try {
                System.out.println(s);
                return Long.parseLong(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid input");
            } catch (NullPointerException e) {
                System.out.println("ERROR: empty value");
            }
        }
    }
}
