package util.comparator;

import model.entity.CompanyEntity;

import java.util.Comparator;

public enum CompanyComparator {
    ID(Comparator.comparingLong(CompanyEntity::getCompID)),
    NAME(Comparator.comparing(CompanyEntity::getCompName)),
    PRODUCTION(Comparator.comparing((CompanyEntity::getCompProd)));

    private final Comparator<CompanyEntity> comparator;

    CompanyComparator(Comparator<CompanyEntity> comp) {
        this.comparator = comp;
    }

    public Comparator<CompanyEntity> getComparator() {
        return comparator;
    }

    public static CompanyComparator getComparatorOption(int input) {
        return switch (input) {
            case 1 -> ID;
            case 2 -> NAME;
            case 3 -> PRODUCTION;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + input);
        };
    }
}
