package model.entity;

import model.enums.BOOKING_CHANNEL;
import model.enums.BOOKING_STATUS;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class BookingEntity {
    private final Long bookingID;       // PK (Nullable prior to DB insertion)
    private Integer roomID;          // FK
    private final Long userId;          // FK
    private final Double pricePerNight;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private final BOOKING_CHANNEL bookingChannel;
    private BOOKING_STATUS bookingStatus;



    // Full Constructor (Existing records loaded from Database)
    public BookingEntity(Long bookingID, Integer roomID, Long userId, Double pricePerNight,
                         LocalDate checkIn, LocalDate checkOut, BOOKING_CHANNEL bookingChannel,
                         BOOKING_STATUS bookingStatus) {
        if (bookingID != null && bookingID < 0) {
            throw new IllegalArgumentException("ERROR: Booking ID cannot be negative.");
        }
        if (roomID == null || roomID < 0) {
            throw new IllegalArgumentException("ERROR: Room ID cannot be negative or null.");
        }
        if (userId == null || userId < 0) {
            throw new IllegalArgumentException("ERROR: User ID cannot be negative or null.");
        }
        if (pricePerNight == null || pricePerNight < 0) {
            throw new IllegalArgumentException("ERROR: Price per night cannot be negative or null.");
        }
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("ERROR: Check-in and Check-out dates cannot be null.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("ERROR: Check-out date must be strictly after Check-in date.");
        }
        if (bookingChannel == null) {
            throw new IllegalArgumentException("ERROR: Booking channel cannot be null.");
        }
        if (bookingStatus == null) {
            throw new IllegalArgumentException("ERROR: Booking status cannot be null.");
        }

        this.bookingID = bookingID;
        this.roomID = roomID;
        this.userId = userId;
        this.pricePerNight = pricePerNight;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.bookingChannel = bookingChannel;
        this.bookingStatus = bookingStatus;
    }







    // --- GETTERS & SETTERS ---

    public Long getBookingID() { // Returned wrapper type to avoid NPE unboxing
        return bookingID;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        if (roomID == null || roomID < 0) {
            throw new IllegalArgumentException("ERROR: Room ID cannot be negative or null.");
        }
        this.roomID = roomID;
    }

    public Long getUserId() {
        return userId;
    }



    public Double getPricePerNight() {
        return pricePerNight;
    }



    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        if (checkIn == null) {
            throw new IllegalArgumentException("ERROR: Check-in date cannot be null.");
        }
        if (this.checkOut != null && !this.checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("ERROR: Check-in date must be before Check-out date.");
        }
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        if (checkOut == null) {
            throw new IllegalArgumentException("ERROR: Check-out date cannot be null.");
        }
        if (this.checkIn != null && !checkOut.isAfter(this.checkIn)) {
            throw new IllegalArgumentException("ERROR: Check-out date must be strictly after Check-in date.");
        }
        this.checkOut = checkOut;
    }

    public BOOKING_CHANNEL getBookingChannel() {
        return bookingChannel;
    }


    public BOOKING_STATUS getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BOOKING_STATUS bookingStatus) {
        if (bookingStatus == null) {
            throw new IllegalArgumentException("ERROR: Booking status cannot be null.");
        }


        this.bookingStatus = bookingStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity entity = (BookingEntity) o;
        return Objects.equals(roomID, entity.roomID) &&
                Objects.equals(userId, entity.userId) &&
                Objects.equals(pricePerNight, entity.pricePerNight) &&
                Objects.equals(checkIn, entity.checkIn) &&
                Objects.equals(checkOut, entity.checkOut) &&
                Objects.equals(bookingChannel, entity.bookingChannel) &&
                Objects.equals(bookingStatus, entity.bookingStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, userId, pricePerNight, checkIn, checkOut, bookingChannel, bookingStatus);
    }

    public double getTotalPrice() {
        int days = Math.toIntExact(ChronoUnit.DAYS.between(checkIn, checkOut));
        return this.pricePerNight * (days);
    }
}