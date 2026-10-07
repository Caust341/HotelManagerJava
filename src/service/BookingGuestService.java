package service;

import error.ItemAlreadyExistException;
import model.entity.BookingGuestEntity;
import repository.BookingGuestRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BookingGuestService {
    private final BookingGuestRepository repo = new BookingGuestRepository();
    private final List<BookingGuestEntity> list;
    private static BookingGuestService instance;

    private BookingGuestService() {
        list = repo.read();
    }

    public static BookingGuestService getInstance() {
        if (instance == null) {
            instance = new BookingGuestService();
        }
        return instance;
    }

    public void add(BookingGuestEntity entity) {
        if (list.contains(entity)) {
            throw new ItemAlreadyExistException("Booking guest already exists.");
        }
        list.add(entity);
        repo.write(list);
    }

    public void delete(BookingGuestEntity entityOld) {
        list.remove(entityOld);
        repo.write(list);
    }

    public List<BookingGuestEntity> showAll() {
        return list;
    }

    public List<BookingGuestEntity> findAllCustomerOfBooking(Long input) {
        List<BookingGuestEntity> searched = new ArrayList<>();
        if (input == null) return searched;

        for (BookingGuestEntity bg : list) {
            if (Objects.equals(bg.bookingID(), input)) {
                searched.add(bg);
            }
        }
        Collections.sort(searched);
        return searched;
    }

    public List<BookingGuestEntity> findAllBookingFromCustomer(Long input) {
        List<BookingGuestEntity> searched = new ArrayList<>();
        if (input == null) return searched;

        for (BookingGuestEntity bg : list) {
            if (Objects.equals(bg.custID(), input)) { // Fixed: checked getCustID instead of getBookingID
                searched.add(bg);
            }
        }
        return searched;
    }


    public void deleteByBooking(long bookingId) {
        list.removeIf(bg -> bg.bookingID() != null && bg.bookingID() == bookingId);
        repo.write(list);
    }
}