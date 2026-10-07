package model.entity;

/**
 * @param custID    PK (Composite Part 1)
 * @param bookingID PK (Composite Part 2)
 */
public record BookingGuestEntity(Long custID, Long bookingID) implements Comparable<BookingGuestEntity> {
    public BookingGuestEntity {
        if (custID == null || custID < 0) {
            throw new IllegalArgumentException(" Customer ID cannot be null or negative.");
        }
        if (bookingID == null || bookingID < 0) {
            throw new IllegalArgumentException(" Booking ID cannot be null or negative.");
        }
    }

    // --- GETTERS & SETTERS ---


    @Override
    public int compareTo(BookingGuestEntity o) {
        if (o == null) {
            throw new NullPointerException("Cannot compare with null");
        }
        return Long.compare(this.custID(), o.custID());
    }
}