package controller;

import service.AuthService;
import util.InputHandler;
import view.LoginUI;

public class LoginController {
    private final LoginUI view = new LoginUI();
    private final AuthService authService = new AuthService();
    private final InputHandler input = new InputHandler();

    public void start() {
        boolean running = true;
        while (running) {
            try {
                view.showMenu();
                int choice = input.getInteger("Please Enter one of the options: ");

                switch (choice) {
                    case 1 -> {
                        String username = input.getString("Enter username: ");
                        String password = input.getString("Enter password: ");
                        authService.handleLogin(username, password);
                    }
                    case 2 -> {
                        System.out.println("Exiting system. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch(IllegalArgumentException e) {
                System.out.println("ERROR:" + e.getMessage());
            }
        }
    }
}