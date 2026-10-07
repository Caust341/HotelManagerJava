package util.comparator;

import model.entity.CustomerEntity;

import java.util.Comparator;

public enum CustomerComparator {
    CUSTID(Comparator.comparingLong(CustomerEntity::getCustID)),
    COMPID(Comparator.comparingLong(CustomerEntity::getCompID)),
    NAME(Comparator.comparing(CustomerEntity::getCustName)),
    TIER(Comparator.comparing(CustomerEntity::getCustTier)),
    ;

    private final Comparator<CustomerEntity> comparator;
    CustomerComparator(Comparator<CustomerEntity> c) {
        this.comparator = c;
    }

    public Comparator<CustomerEntity> getComparator() {
        return comparator;
    }

    public CustomerComparator getComparatorOption(int input) {
        return switch (input) {
            case 1 -> CUSTID;
            case 2 -> COMPID;
            case 3 -> NAME;
            case 4 -> TIER;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + input);
        };
    }
}
