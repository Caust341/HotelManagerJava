package model.enums;

public enum COMPANY_PRODUCTION {
    NONE("NONE"),
    NEWCOMER("NEWCOMER"),
    ACTIVE("ACTIVE"),
    VALUED("VALUED"),
    ;

    private final String prod;

    COMPANY_PRODUCTION(String prod) {
        this.prod = prod;
    }

    public String getValue() {
        return prod;
    }

    public static COMPANY_PRODUCTION getProduction(int code) {
        return switch (code) {
            case 1 -> NEWCOMER;
            case 2 -> ACTIVE;
            case 3 -> VALUED;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + code);
        };
    }
}
