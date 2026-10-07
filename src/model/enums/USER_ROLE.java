package model.enums;

public enum USER_ROLE {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    RECEPTION("RECEPTION"),
    HR("HR"),
    DEV("DEVELOPER");
    private final String role;

    private USER_ROLE(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }

    public static USER_ROLE getRole(int code) {

                return switch (code) {
                    case 1 -> ADMIN;
                    case 2 -> MANAGER;
                    case 3 -> HR;
                    default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + code);
                };


    }
}
