package service;

import error.ItemNotFoundException;
import error.OverloadException;
import model.entity.*;
import model.enums.BOOKING_CHANNEL;
import model.enums.BOOKING_STATUS;
import model.enums.ROOM_STATUS;
import repository.BookingRepository;
import util.comparator.BookingComparator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BookingService implements IFullService<BookingEntity> {
    private final BookingRepository repo = new BookingRepository();
    private final List<BookingEntity> bookingList;
    private static BookingService instance;

    private BookingService() {
        bookingList = repo.read();
    }

    public static BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
        }
        return instance;
    }

    @Override
    public void add(BookingEntity entity) {

        checkCapacity(entity);

        bookingList.add(entity);
        repo.write(bookingList);
    }

    @Override
    public void delete(BookingEntity entityOld) {
        bookingList.remove(entityOld);
        repo.write(bookingList);
    }

    @Override
    public List<BookingEntity> showAll() {
        return bookingList;
    }

    @Override
    public BookingEntity findById(Long input) {
        if (input == null) throw new IllegalArgumentException("Booking ID cannot be null.");
        for (BookingEntity b : bookingList) {
            if (Objects.equals(b.getBookingID(), input)) {
                return b;
            }
        }
        throw new ItemNotFoundException("Booking ID " + input + " does not exist.");
    }

    @Override
    public BookingEntity findByIndex(Integer i, List<BookingEntity> list) {
        return list.get(i);
    }

    @Override
    public void update(BookingEntity oldT, BookingEntity newT) {
        checkCapacity(newT);

        int index = bookingList.indexOf(oldT);
        if (index == -1) {
            throw new ItemNotFoundException("Booking to update was not found.");
        }
        bookingList.set(index, newT);
        repo.write(bookingList);
    }

    public void updateRoomID(BookingEntity b, Integer roomNumber) {
        RoomEntity room = RoomService.getInstance().findByNumber(roomNumber);
        if (room.getStatus() == ROOM_STATUS.OCCUPIED || room.getStatus() == ROOM_STATUS.DIRTY
                || room.getStatus() == ROOM_STATUS.OUT_OF_ORDER) {
            throw new IllegalArgumentException("Room is not available to use due to having " + room.getStatus() + " status");
        }
        b.setRoomID(roomNumber);
        repo.write(bookingList);
    }

    public void updateCheckIn(BookingEntity b, LocalDate checkin) {
        b.setCheckIn(checkin);
        repo.write(bookingList);
    }

    public void updateCheckOut(BookingEntity b, LocalDate checkout) {
        b.setCheckOut(checkout);
        repo.write(bookingList);
    }

    public void updateBookingStatus(BookingEntity b, BOOKING_STATUS status) {
        b.setBookingStatus(status);
        repo.write(bookingList);
    }

    public List<BookingEntity> sort(BookingComparator comparator, boolean isReversed) {
        List<BookingEntity> sortedList = new ArrayList<>(showAll());
        Comparator<BookingEntity> c = comparator.getComparator();

        if (isReversed) {
            c = c.reversed();
        }

        sortedList.sort(c);
        return sortedList;
    }

    public Long BookingIdGenerator() {
        if (bookingList.isEmpty()) {
            return 1000000L;
        }
        return bookingList.getLast().getBookingID() + 1;
    }

    private void checkCapacity(BookingEntity entity) {
        if (entity == null || entity.getRoomID() == null) return ;

        Integer roomID = entity.getRoomID();

        long activeCount = bookingList.stream()
                .filter(e -> roomID.equals(e.getRoomID()))
                .filter(e -> !e.equals(entity))
                .filter(e -> e.getBookingStatus() != BOOKING_STATUS.CANCELLED
                        && e.getBookingStatus() != BOOKING_STATUS.CHECKED_OUT)
                .count();

        RoomEntity room = RoomService.getInstance().findByNumber(roomID);
        RoomTierEntity roomTier = RoomTierService.getInstance().findByTier(room.getTier());
        try {
            roomTier.checkMax(activeCount);
        } catch (OverloadException e) {
            room.setStatus(ROOM_STATUS.OCCUPIED);
        }
    }


    public Double calculatePrice(Long custId, Integer roomID, BOOKING_CHANNEL channel) {
        RoomEntity room = RoomService.getInstance().findByNumber(roomID);
        RoomTierEntity tier = RoomTierService.getInstance().findByTier(room.getTier());

        if (channel == BOOKING_CHANNEL.WALK_IN || channel == BOOKING_CHANNEL.TRANSIENT) {
            return tier.getBasePrice();
        }

        CustomerEntity cust = CustomerService.getInstance().findById(custId);
        if (cust.getCompID() == null || cust.getCompID() == 0L) {
            return tier.getBasePrice();
        }

        CompanyEntity comp = CompanyService.getInstance().findById(cust.getCompID());
        CompanyProductionEntity compProd = CompanyProductionService.getInstance().findByProduction(comp.getCompProd());

        return tier.getBasePrice() * compProd.getDiscount();
    }
}