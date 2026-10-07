package controller;

import model.entity.UserEntity;

public class ControllerFactory {
    public static GeneralController createController(UserEntity user) {
        if (user == null || user.getRole() == null) {
            throw new IllegalArgumentException("Cannot create controller for null user");
        }
        return switch (user.getRole()) {
            case ADMIN -> new AdminController(user);
            case HR -> new HRController(user);
            case MANAGER -> new ManagerController(user);
            default -> throw new IllegalArgumentException("Unsupported role!");
        };
    }

}
