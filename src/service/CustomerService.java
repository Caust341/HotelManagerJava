package service;

import error.ItemAlreadyExistException;
import error.ItemNotFoundException;
import model.entity.CustomerEntity;
import model.enums.CUSTOMER_TIER;
import repository.CustomerRepository;
import util.comparator.CustomerComparator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CustomerService implements IFullService<CustomerEntity> {
    private final CustomerRepository repo = new CustomerRepository();
    private final List<CustomerEntity> custList;
    private static CustomerService instance;

    private CustomerService() {
        custList = repo.read();
    }

    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    @Override
    public void add(CustomerEntity entity) {
        if (custList.contains(entity)) {
            throw new ItemAlreadyExistException(" Customer already exists.");
        }
        custList.add(entity);
        repo.write(custList);
    }

    @Override
    public void delete(CustomerEntity entityOld) {
        custList.remove(entityOld);
        repo.write(custList);
    }

    public void deleteByCompany(long compId) {
        boolean modified = false;
        for (CustomerEntity customer : custList) {
            if (customer.getCompID() != null && customer.getCompID() == compId) {
                customer.setCompID(0L);
                modified = true;
            }
        }
        if (modified) {
            repo.write(custList); // Fixed: missing repository write
        }
    }

    public void deleteByBooking(long bookingId) {
        // Intermediary hook if customer cleanup by booking ID is required via BookingGuestService
        List<model.entity.BookingGuestEntity> guests = BookingGuestService.getInstance().findAllCustomerOfBooking(bookingId);
        for (model.entity.BookingGuestEntity bg : guests) {
            CustomerEntity cust = findById(bg.custID());
            delete(cust);
        }
    }

    @Override
    public List<CustomerEntity> showAll() {
        return custList;
    }

    @Override
    public CustomerEntity findById(Long input) {
        if (input == null) throw new IllegalArgumentException("Customer ID cannot be null.");
        for (CustomerEntity c : custList) {
            if (Objects.equals(c.getCustID(), input)) {
                return c;
            }
        }
        throw new ItemNotFoundException(" Customer ID " + input + " does not exist.");
    }

    public List<CustomerEntity> findByComp(Long input) {
        List<CustomerEntity> searchedList = new ArrayList<>();
        if (input == null) return searchedList;

        for (CustomerEntity c : custList) {
            if (c.getCompID() == input.longValue()) {
                searchedList.add(c);
            }
        }
        return searchedList;
    }

    public List<CustomerEntity> findByName(String input) {
        List<CustomerEntity> searchedList = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) return searchedList;

        for (CustomerEntity c : custList) {
            if (c.getCustName().contains(input)) {
                searchedList.add(c);
            }
        }
        return searchedList;
    }

    @Override
    public CustomerEntity findByIndex(Integer i, List<CustomerEntity> list) {
        return list.get(i);
    }

    @Override
    public void update(CustomerEntity oldT, CustomerEntity newT) {
        int index = custList.indexOf(oldT);
        if (index != -1) {
            custList.set(index, newT);
            repo.write(custList);
        }
    }

    public void updateComp(CustomerEntity oldT, Long compId) {
        oldT.setCompID(compId);
        repo.write(custList);
    }

    public void updateName(CustomerEntity oldT, String name) {
        oldT.setCustName(name);
        repo.write(custList);
    }

    public void updateTier(CustomerEntity oldT, CUSTOMER_TIER tier) {
        oldT.setCustTier(tier);
        repo.write(custList);
    }

    public void updateContact(CustomerEntity oldT, String contact) {
        oldT.setContact(contact);
        repo.write(custList);
    }

    public List<CustomerEntity> sort(CustomerComparator comparator, boolean isReversed) {
        List<CustomerEntity> sortedList = new ArrayList<>(showAll());
        Comparator<CustomerEntity> custComparator = comparator.getComparator();

        if (isReversed) {
            custComparator = custComparator.reversed();
        }
        sortedList.sort(custComparator);
        return sortedList;
    }

    public Long CustomerIdGenerator() {
        if (custList.isEmpty()) {
            return 3000000L;
        }
        return custList.getLast().getCustID() + 1;
    }

    public void processLoyaltyPoints(CustomerEntity customer, LocalDate checkin, LocalDate checkout) {
        long nightsStayed = ChronoUnit.DAYS.between(checkin, checkout);
        int currentPoints = customer.getCount() + (int) nightsStayed;

        while (currentPoints >= 10) {
            if (customer.getCustTier() == CUSTOMER_TIER.PLATINUM) {
                break;
            }
            upgradeCustomerTier(customer);
            currentPoints -= 10;
        }
        customer.setCount(currentPoints);
        repo.write(custList);
    }

    private void upgradeCustomerTier(CustomerEntity customer) {
        CUSTOMER_TIER currentTier = customer.getCustTier();
        if (currentTier == CUSTOMER_TIER.NEW) {
            customer.setCustTier(CUSTOMER_TIER.SILVER);
        } else if (currentTier == CUSTOMER_TIER.SILVER) {
            customer.setCustTier(CUSTOMER_TIER.GOLD);
        } else if (currentTier == CUSTOMER_TIER.GOLD) {
            customer.setCustTier(CUSTOMER_TIER.PLATINUM);
        }
    }
}