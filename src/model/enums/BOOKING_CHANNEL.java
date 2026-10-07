package model.enums;

public enum BOOKING_CHANNEL {
    CORPORATE("CORPORATE"),
    OTA("OTA"),
    TRANSIENT("TRANSIENT"),
    WALK_IN("WALK_IN"),
    ;

    private final String channel;

    private BOOKING_CHANNEL(String channel) {
        this.channel = channel;
    }

    public String getValue() {
        return channel;
    }

    public static BOOKING_CHANNEL getChannel(int code) {
        return switch (code) {
            case 1 -> CORPORATE;
            case 2 -> OTA;
            case 3 -> TRANSIENT;
            case 4 -> WALK_IN;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + code);
        };
    }
}
