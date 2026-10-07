package controller;

import model.entity.UserEntity;

public abstract class GeneralController {
    protected final UserEntity user;

    public GeneralController(UserEntity user) {
        this.user = user;
    }

    public abstract void start();

    public UserEntity getUser() {
        return user;
    }
}
