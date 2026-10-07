package util.comparator;

import model.entity.RoomEntity;

import java.util.Comparator;

public enum RoomComparator {
    NUMBER(Comparator.comparingInt(RoomEntity::getRoomNumber)),
    FLOOR(Comparator.comparing(RoomEntity::getFloor)),
    STATUS(Comparator.comparing(RoomEntity::getStatus)),
    TIER(Comparator.comparing(RoomEntity::getTier));

    private final Comparator<RoomEntity> comparator;

    RoomComparator(Comparator<RoomEntity> comparator) {
        this.comparator = comparator;
    }

    public Comparator<RoomEntity> getComparator() {
        return comparator;
    }

    public static RoomComparator getComparatorOption(int input) {
        return switch (input) {
            case 1 -> NUMBER;
            case 2 -> FLOOR;
            case 3 -> STATUS;
            case 4 -> TIER;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + input);
        };
    }
}
