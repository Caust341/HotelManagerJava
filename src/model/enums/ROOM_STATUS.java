package model.enums;

public enum ROOM_STATUS {
    VACANT("VACANT"),
    OCCUPIED("OCCUPIED"),
    OUT_OF_ORDER("OUT OF ORDER"),
    DIRTY("DIRTY"),
    CLEAN("CLEAN"),
    ;

    private final String status;

    ROOM_STATUS(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }



    public static ROOM_STATUS getStatus(int code) {
        return switch (code) {
            case 1 -> VACANT;
            case 2 -> OCCUPIED;
            case 3 -> OUT_OF_ORDER;
            case 4 -> DIRTY;
            case 5 -> CLEAN;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + code);
        };

    }
}
