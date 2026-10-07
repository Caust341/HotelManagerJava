package service;

import error.ItemAlreadyExistException;
import error.ItemNotFoundException;
import model.entity.UserEntity;
import model.enums.USER_ROLE;
import repository.UserRepository;
import util.comparator.UserComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserService implements IFullService<UserEntity>{
    private  final UserRepository repo = new UserRepository();
    private final List<UserEntity> userList;
    private static UserService instance;

    private UserService() {
        userList = repo.read();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    @Override
    public void add(UserEntity entity) {
        if (userList.contains(entity)) {
            throw new ItemAlreadyExistException("User already existed");
        }
        userList.add(entity);
        repo.write(userList);
    }

    @Override
    public void delete(UserEntity entityOld) {
        userList.remove(entityOld);
        repo.write(userList);
    }

    @Override
    public List<UserEntity> showAll() {
        return userList;
    }

    @Override
    public UserEntity findById(Long input) {
        for (UserEntity u : userList) {
            if (u.getUserId() == input.longValue()) {
                return u;
            }
        }
        throw new ItemNotFoundException(": User " + input+ " does not exist.");
    }

    @Override
    public UserEntity findByIndex(Integer i, List<UserEntity> list) {
        return list.get(i);
    }

    public List<UserEntity> findByName(String input) {
        List<UserEntity> nameList = new ArrayList<>();
        for (UserEntity u : userList) {
            if (u.getUsername().contains(input)) {
                nameList.add(u);
            }
        }
        return nameList;
    }

    @Override
    public void update(UserEntity oldT, UserEntity newT) {
        int index = userList.indexOf(oldT);
        userList.set(index, newT);
        repo.write(userList);

    }

    public void updateRole(UserEntity user, USER_ROLE role) {
        user.setRole(role);
        repo.write(userList);
    }

    public void updateName(UserEntity user, String name) {
        user.setUsername(name);
        repo.write(userList);
    }

    public void updatePassword(UserEntity user, String password) {
        user.setPassword(password);
        repo.write(userList);
    }

    public List<UserEntity> sort(UserComparator comparator, boolean isReversed) {
        List<UserEntity> sorteduserlist = new ArrayList<>(showAll());
        Comparator<UserEntity> c = comparator.getComparator();

        if (isReversed) {
            c = c.reversed();
        }

        sorteduserlist.sort(c);
        return sorteduserlist;
    }

    public Long UserIdGenerator() {
        if (userList.isEmpty()) {
            return 5000000L;
        }
        return userList.getLast().getUserId() + 1;
    }

    public UserEntity authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        String trimmedUsername = username.trim();

        for (UserEntity user : userList) {
            if (user.getUsername() != null && user.getUsername().equalsIgnoreCase(trimmedUsername)) {
                if (user.getPassword() != null && user.getPassword().equals(password)) {
                    return user;
                }
                break;
            }
        }
        throw new IllegalArgumentException("Invalid username or password.");
    }

}
