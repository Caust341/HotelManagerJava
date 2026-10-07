package model.enums;

public enum BOOKING_STATUS {
    CONFIRMED("CONFIRMED"),
    CHECKED_IN("CHECKED IN"),
    CHECKED_OUT("CHECKED OUT"),
    CANCELLED("CANCELLED"),
    NO_SHOW("NO SHOW"),
    ;

    private final String status;

    private BOOKING_STATUS(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }

    public static BOOKING_STATUS getStatus(int code) {
        return switch (code) {
            case 1 -> CONFIRMED;
            case 2 -> CHECKED_IN;
            case 3 -> CHECKED_OUT;
            case 4 -> CANCELLED;
            case 5 -> NO_SHOW;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + code);
        };
    }
}
