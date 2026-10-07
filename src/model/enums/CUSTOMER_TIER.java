package model.enums;

public enum CUSTOMER_TIER {
    PLATINUM("PLATINUM"),
    GOLD("GOLD"),
    SILVER("SILVER"),
    NEW("NEW"),
    ;

    private final String tier;

    private CUSTOMER_TIER(String tier) {
        this.tier = tier;
    }

    public String getValue() {
        return tier;
    }


    public static CUSTOMER_TIER getTier(int code) {
        return switch (code) {
            case 1 -> PLATINUM;
            case 2 -> GOLD;
            case 3 -> SILVER;
            case 4 -> NEW;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + code);
        };
    }
}
