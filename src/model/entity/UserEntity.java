package model.entity;

import model.enums.USER_ROLE;
import java.util.Objects;

public class UserEntity {
    private Long userId;
    private String username;
    private String password;
    private USER_ROLE role;

    public UserEntity() {
    }

    // Full Constructor (Existing records loaded from Database)
    public UserEntity(Long userId, String username, String password, USER_ROLE role) {
        if (userId != null && userId < 0) {
            throw new IllegalArgumentException(" User ID cannot be negative.");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException(" Username cannot be null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(" Password cannot be null or empty.");
        }
        if (role == null) {
            throw new IllegalArgumentException(" User role cannot be null.");
        }

        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor for NEW Users prior to DB persistence (Auto-generated ID)
    public UserEntity(String username, String password, USER_ROLE role) {
        this(null, username, password, role);
    }

    public UserEntity(String username, String password) {
        this(null, username, password, null);
    }
    // --- GETTERS & SETTERS ---

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if (userId != null && userId < 0) {
            throw new IllegalArgumentException(" User ID cannot be negative.");
        }
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException(" Username cannot be null or empty.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(" Password cannot be null or empty.");
        }
        this.password = password;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        if (role == null) {
            throw new IllegalArgumentException(" User role cannot be null.");
        }
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

}
