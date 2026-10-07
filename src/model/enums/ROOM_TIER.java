package model.enums;

public enum ROOM_TIER {
    STANDARD("STANDARD"),
    DELUXE("DELUXE"),
    SUITE("SUITE"),
    ;

    private final String tier;

    private ROOM_TIER(String tier) {
        this.tier = tier;
    }

    public String getValue() {
        return tier;
    }

    public static ROOM_TIER getTier(int code) {
        return switch (code) {
            case 1 -> STANDARD;
            case 2 -> DELUXE;
            case 3 -> SUITE;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + code);
        };

    }
}
