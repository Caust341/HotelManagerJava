package service;

import error.ItemAlreadyExistException;
import error.ItemNotFoundException;
import model.entity.BookingEntity;
import model.entity.BookingGuestEntity;
import model.entity.CompanyEntity;
import model.entity.CustomerEntity;
import model.enums.BOOKING_STATUS;
import model.enums.COMPANY_PRODUCTION;
import repository.CompanyRepository;
import util.comparator.CompanyComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;




public class CompanyService implements IFullService<CompanyEntity>{
    private  final CompanyRepository repo = new CompanyRepository();
    private final List<CompanyEntity> compList;
    private static CompanyService instance;
    private static final BookingGuestService bgService = BookingGuestService.getInstance();
    private static final CustomerService customerService = CustomerService.getInstance();
    private static final BookingService bookingService = BookingService.getInstance();

    private CompanyService() {
        compList = repo.read();
    }

    public static CompanyService getInstance() {
        if (instance == null) {
            instance = new CompanyService();
        }
        return instance;
    }

    @Override
    public void add(CompanyEntity entity) {
        if (compList.contains(entity) ) {
            throw new ItemAlreadyExistException(" Company Already existed");
        }
        compList.add(entity);
        repo.write(compList);
    }

    @Override
    public void delete(CompanyEntity entityOld) {
        compList.remove(entityOld);
        repo.write(compList);
    }

    @Override
    public List<CompanyEntity> showAll() {
        return compList;
    }

    @Override
    public CompanyEntity findById(Long input) {
        if (input == null) throw new IllegalArgumentException("CompanyID cannot be null");
        for (CompanyEntity c : compList) {
            if ((c.getCompID() != null && c.getCompID().equals(input))) {
                return c;
            }
        }
        throw new ItemNotFoundException(" Company ID " + input + " does not exist");
    }

    public List<CompanyEntity> findByName(String input) {
        List<CompanyEntity> searchedlist = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) {
            return searchedlist;
        }

        for (CompanyEntity c : compList) {
            if (c.getCompName() != null && c.getCompName().toLowerCase().contains(input.toLowerCase())) {
                searchedlist.add(c);
            }
        }
        return searchedlist;
    }

    @Override
    public CompanyEntity findByIndex(Integer i, List<CompanyEntity> list) {
        if (list != null && i != null && i >= 0 && i < list.size()) {
            return list.get(i);
        }
        return null;
    }

    @Override
    public void update(CompanyEntity oldT, CompanyEntity newT) {
        int index = compList.indexOf(oldT);
        compList.set(index, newT);
        repo.write(compList);
    }

    public void updateName(CompanyEntity oldT, String name) {
        oldT.setCompName(name);
        repo.write(compList);
    }

    public void updateProduction(CompanyEntity oldT, COMPANY_PRODUCTION production) {
        oldT.setCompProd(production);
        repo.write(compList);
    }


    public List<CompanyEntity> sort(CompanyComparator comparator, boolean isReverse) {
        List<CompanyEntity> sortedList = new ArrayList<>(showAll());
        Comparator<CompanyEntity> c = comparator.getComparator();

        if (isReverse) {
            c = c.reversed();
        }
        sortedList.sort(c);
        return  sortedList;
    }

    public Long CompIdGenerator() {
        if (compList.isEmpty()) {
            return 2000000L;
        }
        return compList.getLast().getCompID() + 1;
    }


    public Double calculateCompanyTotalCost(Long compId) {
        if (compId == 0) {
            return 0.00;
        }
        double totalSpent = 0.0;

        List<BookingGuestEntity> allGuests = bgService.showAll();


        for (BookingGuestEntity guest : allGuests) {
                CustomerEntity customer = customerService.findById(guest.custID());
                if (customer.getCompID() != null && customer.getCompID().equals(compId)) {
                    BookingEntity booking = bookingService.findById(guest.bookingID());
                    if (booking.getBookingStatus() == BOOKING_STATUS.CHECKED_OUT) {
                        totalSpent += booking.getTotalPrice();
                    }
                }

        }

        return totalSpent;
    }

    public void processCompanyProductionUpgrade(CompanyEntity company) {
        Double newlyCalculatedSpent = calculateCompanyTotalCost(company.getCompID());
        company.setTotalSpent(newlyCalculatedSpent);

        COMPANY_PRODUCTION currentTier = company.getCompProd();
        if (newlyCalculatedSpent >= 10000.0 && currentTier == COMPANY_PRODUCTION.NEWCOMER) {
            company.setCompProd(COMPANY_PRODUCTION.ACTIVE);
            System.out.println("Company upgraded to ACTIVE!");
        }

        if (newlyCalculatedSpent >= 50000.0 && company.getCompProd() == COMPANY_PRODUCTION.ACTIVE) {
            company.setCompProd(COMPANY_PRODUCTION.VALUED);
            System.out.println("Company upgraded to VALUED!");
        }
        update(company, company);
    }


}
