package util.comparator;

import model.entity.UserEntity;

import java.util.Comparator;

public enum UserComparator {
    ID(Comparator.comparingLong(UserEntity::getUserId)),
    NAME(Comparator.comparing(UserEntity::getUsername)),
    ROLE(Comparator.comparing(UserEntity::getRole));

    private final Comparator<UserEntity> comparator;

    UserComparator(Comparator<UserEntity> comparator) {
        this.comparator = comparator;
    }

    public Comparator<UserEntity> getComparator() {
        return comparator;
    }


    public static UserComparator getComparatorOption(int input) {
        return switch (input) {
            case 1 -> NAME;
            case 2 -> ID;
            case 3 -> ROLE;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + input);
        };
    }
}
