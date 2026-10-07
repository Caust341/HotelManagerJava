package util.comparator;

import model.entity.BookingEntity;

import java.util.Comparator;

public enum BookingComparator {
    ID(Comparator.comparingLong(BookingEntity::getBookingID)),
    CHANNEL(Comparator.comparing(BookingEntity::getBookingChannel)),
    STATUS(Comparator.comparing(BookingEntity::getBookingStatus));

    private final Comparator<BookingEntity> comparator;

    BookingComparator(Comparator<BookingEntity> comparator) {
        this.comparator = comparator;
    }

    public Comparator<BookingEntity> getComparator() {
        return comparator;
    }

    public static BookingComparator getComparatorOption(int input) {
        return switch (input) {
            case 1 -> ID;
            case 2 -> CHANNEL;
            case 3 -> STATUS;
            default -> throw new IllegalArgumentException("ERROR: Invalid role code: " + input);
        };
    }
}
