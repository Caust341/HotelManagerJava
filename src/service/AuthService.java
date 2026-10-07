package service;

import controller.ControllerFactory;
import controller.GeneralController;
import model.entity.UserEntity;

public class AuthService {
    private final UserService userService = UserService.getInstance();

    public void handleLogin(String username, String password) {
        UserEntity user = userService.authenticate(username, password);

        if (user != null) {
            System.out.println("Login successful");

            GeneralController controller = ControllerFactory.createController(user);
            controller.start();
        }
    }

}
