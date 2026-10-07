package controller;

import error.ItemAlreadyExistException;
import error.ItemNotFoundException;
import model.entity.*;
import model.enums.BOOKING_CHANNEL;
import model.enums.BOOKING_STATUS;
import model.enums.COMPANY_PRODUCTION;
import model.enums.CUSTOMER_TIER;
import model.enums.ROOM_STATUS;
import model.enums.ROOM_TIER;
import service.*;
import util.InputHandler;
import util.comparator.BookingComparator;
import util.comparator.CompanyComparator;
import util.comparator.CustomerComparator;
import util.validator.BookingValidator;
import util.validator.CompanyValidator;
import util.validator.CustomerValidator;
import util.validator.ValidationResult;
import view.AdminUI;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AdminController extends GeneralController {
    private static final InputHandler input = new InputHandler();
    private static final BookingService bookingService = BookingService.getInstance();
    private static final CustomerService customerService = CustomerService.getInstance();
    private static final CompanyService companyService = CompanyService.getInstance();
    private static final BookingGuestService bookingGuestService = BookingGuestService.getInstance();
    private static final RoomService roomService = RoomService.getInstance();

    private final AdminUI view = new AdminUI();

    public AdminController(UserEntity user) {
        super(user);
    }

    @Override
    public void start() {
        boolean isRunning = true;
        while (isRunning) {
            view.showMenu(user.getUsername());
            int choice = input.getInteger("Please enter one of the options: ");
            switch (choice) {
                case 1 -> BookingManager();
                case 2 -> CustomerManager();
                case 3 -> CompanyManager();
                case 4 -> {
                    view.displayMessage("Logging out...");
                    isRunning = false;
                }
                default -> System.out.println("Invalid Input");
            }
        }
    }


    private void BookingManager() {
        boolean isRunning = true;
        while (isRunning) {
            view.bookingManager();
            int choice = input.getInteger("Select one of the options: ");
            try {
                switch (choice) {
                    case 1 -> addBooking();
                    case 2 -> updateBooking();
                    case 3 -> deleteBooking();
                    case 5 -> findBooking();
                    case 6 -> sortBooking();
                    case 7 -> showAllBookings();
                    case 8 -> isRunning = false;
                    default -> System.out.println("ERROR: Invalid Option");
                }
            } catch (ItemNotFoundException | ItemAlreadyExistException | IllegalArgumentException |
                     DateTimeParseException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void addBooking() {
        view.addBookingTitle();

        CustomerEntity customer = selectOrCreateCustomerForBooking();
        if (customer == null) {
            view.displayMessage("Booking creation cancelled: No valid customer selected.");
            return;
        }

        BOOKING_CHANNEL channel = getValidateChannel();

        int roomNumber = selectAvailableRoomNumber();
        RoomEntity room = roomService.findByNumber(roomNumber);

        if (customer.getCustTier() == CUSTOMER_TIER.GOLD || customer.getCustTier() == CUSTOMER_TIER.PLATINUM) {
            roomNumber = upgradeRoomTierByOne(room);
            view.displayMessage("Customer Tier " + customer.getCustTier() + " eligible for automatic 1-Tier Room Upgrade! Assigned Room Number: " + roomNumber);
        }

        LocalDate checkIn = readValidCheckInDate();
        LocalDate checkOut = readValidCheckOutDate(checkIn);


        Double pricePerNight = bookingService.calculatePrice(customer.getCustID(), roomNumber, channel);

        // Create Booking Entity
        Long bookingId = bookingService.BookingIdGenerator();
        BookingEntity booking = new BookingEntity(
                bookingId,
                roomNumber,
                user.getUserId(),
                pricePerNight,
                checkIn,
                checkOut,
                channel,
                BOOKING_STATUS.CONFIRMED
        );

        bookingService.add(booking);

        // Link Primary Guest
        BookingGuestEntity guest = new BookingGuestEntity(customer.getCustID(), bookingId);
        bookingGuestService.add(guest);

        view.displayMessage("Booking " + bookingId + " successfully created!");
    }

    private CustomerEntity selectOrCreateCustomerForBooking() {
        view.addBookingCustomer();
        int option = input.getInteger("Select option: ");
        if (option == 1) {
            String name = readValidCustomerName();
            CompanyEntity comp = selectOrCreateCompany();
            Long compId = (comp != null) ? comp.getCompID() : 0L;

            String contact = readValidCustomerContact();
            Long custId = customerService.CustomerIdGenerator();
            CustomerEntity newCust = new CustomerEntity(custId, compId, name, contact,CUSTOMER_TIER.NEW, 0);
            customerService.add(newCust);
            view.displayMessage("New customer created with ID: " + custId);
            return newCust;
        } else if (option == 2) {
            String nameInput = input.getString("Enter Customer Name to search: ");
            List<CustomerEntity> list = customerService.findByName(nameInput);
            if (list.isEmpty()) {
                view.displayMessage("No customer found with name: " + nameInput);
                return null;
            }
            view.displayCustomerListWithIndices(list);
            int index = input.getInteger("Select customer index (0 to " + (list.size() - 1) + "): ");
            return customerService.findByIndex(index, list);
        }
        return null;
    }

    private CompanyEntity selectOrCreateCompany() {
        view.addBookingCompany();

        int option = input.getInteger("Select option: ");
        switch (option) {
            case 1 -> {
                String compName = readValidCompanyName();
                Long compId = companyService.CompIdGenerator();
                CompanyEntity newComp = new CompanyEntity(compId, compName, COMPANY_PRODUCTION.NEWCOMER, 0.0);
                companyService.add(newComp);
                view.displayMessage("New company created with ID: " + compId);
                return newComp;
            }
            case 2 -> {
                String compNameInput = input.getString("Enter Company Name to search: ");
                List<CompanyEntity> compList = companyService.findByName(compNameInput);
                if (compList.isEmpty()) {
                    view.displayMessage("No company found with name: " + compNameInput);
                    return null;
                }
                view.displayCompanyListWithIndices(compList);
                int index = input.getInteger("Select company index (0 to " + (compList.size() - 1) + "): ");
                return companyService.findByIndex(index, compList);
            }
            case 3 -> {
                view.displayMessage("No company assigned to this customer.");
                return null;
            }
            default -> {
                view.displayMessage("Invalid choice. Defaulting to No Company.");
                return null;
            }
        }
    }

    private int selectAvailableRoomNumber() {
        while (true) {
            int roomNo = input.getInteger("Enter Room Number: ");
            try {
                RoomEntity room = roomService.findByNumber(roomNo);
                if (room.getStatus() == ROOM_STATUS.OCCUPIED ||
                        room.getStatus() == ROOM_STATUS.DIRTY ||
                        room.getStatus() == ROOM_STATUS.OUT_OF_ORDER) {
                    view.displayMessage("Room " + roomNo + " is currently " + room.getStatus() + ". Please choose an available room.");
                } else {
                    return roomNo;
                }
            } catch (ItemNotFoundException e) {
                view.displayMessage("Room " + roomNo + " does not exist. Please try again.");
            }
        }
    }

    private int upgradeRoomTierByOne(RoomEntity currentRoom) {
        try {
            int nextTierOrdinal = currentRoom.getTier().ordinal() + 1;
            ROOM_TIER[] tiers = ROOM_TIER.values();
            if (nextTierOrdinal < tiers.length) {
                ROOM_TIER targetTier = tiers[nextTierOrdinal];
                List<RoomEntity> availableRooms = roomService.findByTier(targetTier);
                for (RoomEntity r : availableRooms) {
                    if (r.getStatus() == ROOM_STATUS.VACANT || r.getStatus() == ROOM_STATUS.CLEAN) {
                        return r.getRoomNumber();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return currentRoom.getRoomNumber();
    }

    private void updateBooking() {
        view.updateBookingTitle();
        Long bookingId = input.getLong("Enter Booking ID to update: ");

        BookingEntity booking = bookingService.findById(bookingId);
        view.updateBookingMenu();
        int choice = input.getInteger("Select option: ");

        switch (choice) {
            case 1 -> {
                int newRoomNo = selectAvailableRoomNumber();
                bookingService.updateRoomID(booking, newRoomNo);
                view.displayMessage("Room ID updated successfully.");
            }
            case 2 -> {
                LocalDate checkIn = readValidCheckInDate();
                bookingService.updateCheckIn(booking, checkIn);
                view.displayMessage("Check-In date updated successfully.");
            }
            case 3 -> {
                LocalDate checkOut = readValidCheckOutDate(booking.getCheckIn());
                bookingService.updateCheckOut(booking, checkOut);
                view.displayMessage("Check-Out date updated successfully.");
            }
            case 4 -> {
                BOOKING_STATUS status = getValidateBookingStatus();
                bookingService.updateBookingStatus(booking, status);

                if (status == BOOKING_STATUS.CHECKED_OUT) {
                    processCustomerCheckOutUpgrades(booking);
                }
                view.displayMessage("Booking status updated to " + status);
            }
            case 5 -> {
                int newRoomNo = selectAvailableRoomNumber();
                LocalDate checkIn = readValidCheckInDate();
                LocalDate checkOut = readValidCheckOutDate(checkIn);
                BOOKING_STATUS status = getValidateBookingStatus();

                booking.setRoomID(newRoomNo);
                booking.setCheckIn(checkIn);
                booking.setCheckOut(checkOut);
                booking.setBookingStatus(status);

                bookingService.update(booking, booking);

                if (status == BOOKING_STATUS.CHECKED_OUT) {
                    processCustomerCheckOutUpgrades(booking);
                }
                view.displayMessage("All booking fields updated successfully.");
            }
            default -> view.displayMessage("Exiting update menu.");
        }
    }

    private void processCustomerCheckOutUpgrades(BookingEntity booking) {
        List<BookingGuestEntity> guests = bookingGuestService.findAllCustomerOfBooking(booking.getBookingID());
        for (BookingGuestEntity bg : guests) {
            CustomerEntity cust = customerService.findById(bg.custID());
            customerService.processLoyaltyPoints(cust, booking.getCheckIn(), booking.getCheckOut());

            if (cust.getCompID() != null && cust.getCompID() > 0L) {
                CompanyEntity company = companyService.findById(cust.getCompID());
                companyService.processCompanyProductionUpgrade(company);
            }
        }
    }

    private void deleteBooking() {
        view.deleteBookingTitle();
        Long bookingId = input.getLong("Enter Booking ID to delete: ");
        BookingEntity booking = bookingService.findById(bookingId);

        bookingGuestService.deleteByBooking(bookingId);
        bookingService.delete(booking);

        view.displayMessage("Booking " + bookingId + " and associated guest links deleted successfully.");
    }

    private void findBooking() {
        view.findBookingMenu();
        Long bookingId = input.getLong("Enter Booking ID: ");
        BookingEntity booking = bookingService.findById(bookingId);
        List<BookingGuestEntity> guests = bookingGuestService.findAllCustomerOfBooking(bookingId);

        List<String> guestDetails = new ArrayList<>();
        for (BookingGuestEntity bg : guests) {
            CustomerEntity cust = customerService.findById(bg.custID());
            String compName = "None";
            if (cust.getCompID() != null && cust.getCompID() > 0L) {
                try {
                    compName = companyService.findById(cust.getCompID()).getCompName();
                } catch (ItemNotFoundException ignored) {}
            }
            guestDetails.add(String.format("%-15s %-20s", cust.getCustName(), compName));
        }

        view.displayBookingDetails(booking, guestDetails, booking.getTotalPrice());
    }

    private void sortBooking() {
        view.sortRoomMenu();
        int choice = input.getInteger("Select sort criteria: ");
        if (choice < 1 || choice > 3) return;

        int orderChoice = input.getInteger("Sort Direction (Press 1 for Descending): ");
        boolean isReversed = (orderChoice == 1);

        BookingComparator compOption = BookingComparator.getComparatorOption(choice);
        List<BookingEntity> sorted = bookingService.sort(compOption, isReversed);

        view.displayMessage("\n=== SORTED BOOKINGS ===");
        for (BookingEntity b : sorted) {
            view.displayBookingSummary(b);
        }
    }

    private void showAllBookings() {
        List<BookingEntity> list = bookingService.showAll();
        if (list.isEmpty()) {
            view.displayMessage("No bookings found.");
            return;
        }
        view.displayMessage("\n=== ALL BOOKINGS ===");
        for (BookingEntity b : list) {
            view.displayBookingSummary(b);
        }
    }

    // ==========================================
    // 2. CUSTOMER MANAGEMENT
    // ==========================================

    private void CustomerManager() {
        boolean isRunning = true;
        while (isRunning) {
            view.customerManager();
            int choice = input.getInteger("Select one of the options: ");
            try {
                switch (choice) {
                    case 1 -> addCustomer();
                    case 2 -> updateCustomer();
                    case 3 -> findOrSortCustomerSubMenu();
                    case 4 -> showAllCustomers();
                    case 5 -> deleteCustomer();
                    case 6 -> isRunning = false;
                    default -> System.out.println("ERROR: Invalid Option");
                }
            } catch (ItemNotFoundException | ItemAlreadyExistException | IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void addCustomer() {
        view.addCustomerTitle();
        String name = readValidCustomerName();

        CompanyEntity comp = selectOrCreateCompany();
        long compId = (comp != null) ? comp.getCompID() : 0L;
        String contact = readValidCustomerContact();
        Long custId = customerService.CustomerIdGenerator();
        CustomerEntity cust = new CustomerEntity(custId, compId, name, contact,CUSTOMER_TIER.NEW, 0);
        customerService.add(cust);

        String companyInfo = (compId > 0L) ? "Company ID: " + compId : "Individual Guest (No Company)";
        view.displayMessage("Customer created successfully with ID: " + custId + " [" + companyInfo + "]");
    }

    private void updateCustomer() {
        view.updateCustomerTitle();
        Long custId = input.getLong("Enter Customer ID to update: ");
        CustomerEntity cust = customerService.findById(custId);

        view.updateCustomerCategory();
        int choice = input.getInteger("Select option: ");

        switch (choice) {
            case 1 -> {
                System.out.println("Enter 0 if the customer has no company association.");
                Long compId = input.getLong("Enter new Company ID: ");
                if (compId > 0L) {
                    companyService.findById(compId); // Validates company existence before linking
                }
                customerService.updateComp(cust, compId);
                view.displayMessage("Company association updated successfully.");
            }
            case 2 -> {
                String name = readValidCustomerName();
                customerService.updateName(cust, name);
                view.displayMessage("Customer name updated.");
            }
            case 3 -> {
                CUSTOMER_TIER tier = getValidateCustomerTier();
                customerService.updateTier(cust, tier);
                view.displayMessage("Customer tier updated.");
            }
            case 5 -> {
                Long compId = input.getLong("Enter new Company ID: ");
                String name = readValidCustomerName();
                String contact = readValidCustomerContact();
                CUSTOMER_TIER tier = getValidateCustomerTier();


                CustomerEntity updatedCust = new CustomerEntity(cust.getCustID(), compId, name, contact, tier, cust.getCount());

                customerService.update(cust, updatedCust);
                view.displayMessage("Customer details updated.");
            }
            case 4 -> {
                String contact = readValidCustomerContact();
                customerService.updateContact(cust, contact);
                view.displayMessage("Customer Contact updated.");
            }
            default -> view.displayMessage("Exiting update menu.");
        }
    }

    private void findOrSortCustomerSubMenu() {
        view.CustomerSubMenuUI();
        int choice = input.getInteger("Select option: ");
        if (choice == 1) {
            findCustomer();
        } else if (choice == 2) {
            sortCustomer();
        }
    }

    private void findCustomer() {
        view.findCustomerUI();
        int choice = input.getInteger("Select option: ");
        switch (choice) {
            case 1 -> {
                Long id = input.getLong("Enter Customer ID: ");
                CustomerEntity c = customerService.findById(id);
                displayDetailedCustomer(c);
            }
            case 2 -> {
                String name = input.getString("Enter Customer Name: ");
                List<CustomerEntity> list = customerService.findByName(name);
                if (list.isEmpty()) {
                    view.displayMessage("No customer found matching name: " + name);
                    return;
                }
                view.displayCustomerListWithIndices(list);
                int idx = input.getInteger("Select index to view details: ");
                CustomerEntity c = customerService.findByIndex(idx, list);
                displayDetailedCustomer(c);
            }
            case 3 -> {
                Long compId = input.getLong("Enter Company ID: ");
                List<CustomerEntity> list = customerService.findByComp(compId);
                if (list.isEmpty()) {
                    view.displayMessage("No customers found associated with Company ID: " + compId);
                    return;
                }
                for (CustomerEntity c : list) {
                    displayDetailedCustomer(c);
                }
            }
            default -> view.displayMessage("Exiting find menu.");
        }
    }

    private void sortCustomer() {
        view.sortCustomerUI();
        int choice = input.getInteger("Select sort criteria: ");
        if (choice < 1 || choice > 4) return;

        int orderChoice = input.getInteger("Sort Direction (Press 1 for Descending): ");
        boolean isReversed = (orderChoice == 1);

        CustomerComparator option = CustomerComparator.CUSTID.getComparatorOption(choice);
        List<CustomerEntity> sorted = customerService.sort(option, isReversed);

        view.displayMessage("\n=== SORTED CUSTOMERS ===");
        for (CustomerEntity c : sorted) {
            displayDetailedCustomer(c);
        }
    }

    private void showAllCustomers() {
        List<CustomerEntity> list = customerService.showAll();
        if (list.isEmpty()) {
            view.displayMessage("No customers found.");
            return;
        }
        view.displayMessage("\n=== ALL CUSTOMERS ===");
        for (CustomerEntity c : list) {
            displayDetailedCustomer(c);
        }
    }

    private void deleteCustomer() {
        view.deleteCustomerTitle();
        Long custId = input.getLong("Enter Customer ID to delete: ");
        CustomerEntity cust = customerService.findById(custId);

        List<BookingGuestEntity> guestLinks = bookingGuestService.findAllBookingFromCustomer(custId);
        for (BookingGuestEntity bg : guestLinks) {
            bookingGuestService.delete(bg);
            try {
                BookingEntity booking = bookingService.findById(bg.bookingID());
                bookingService.delete(booking);
            } catch (ItemNotFoundException ignored) {}
        }

        customerService.delete(cust);
        view.displayMessage("Customer " + custId + " and associated bookings removed successfully.");
    }

    private void displayDetailedCustomer(CustomerEntity c) {
        String compName = "None";
        if (c.getCompID() != null && c.getCompID() > 0L) {
            try {
                compName = companyService.findById(c.getCompID()).getCompName();
            } catch (ItemNotFoundException ignored) {}
        }
        view.displayCustomerDetails(c, compName);
    }

    // ==========================================
    // 3. COMPANY MANAGEMENT
    // ==========================================

    private void CompanyManager() {
        boolean isRunning = true;
        while (isRunning) {
            view.companyManager();
            int choice = input.getInteger("Select one of the options: ");
            try {
                switch (choice) {
                    case 1 -> addCompany();
                    case 2 -> updateCompany();
                    case 3 -> findOrSortCompanySubMenu();
                    case 4 -> showAllCompanies();
                    case 5 -> deleteCompany();
                    case 6 -> isRunning = false;
                    default -> System.out.println("ERROR: Invalid Option");
                }
            } catch (ItemNotFoundException | ItemAlreadyExistException | IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void addCompany() {
        view.addCompanyTitle();
        String name = readValidCompanyName();
        Long compId = companyService.CompIdGenerator();
        CompanyEntity company = new CompanyEntity(compId, name, COMPANY_PRODUCTION.NEWCOMER, 0.0);
        companyService.add(company);
        view.displayMessage("Company created successfully with ID: " + compId);
    }

    private void updateCompany() {
        view.updateCompanyTitle();
        Long compId = input.getLong("Enter Company ID to update: ");
        CompanyEntity company = companyService.findById(compId);

        view.updateCompanyMenu();
        int choice = input.getInteger("Select option: ");

        switch (choice) {
            case 1 -> {
                String name = readValidCompanyName();
                companyService.updateName(company, name);
                view.displayMessage("Company name updated.");
            }
            case 2 -> {
                COMPANY_PRODUCTION prod = getValidateCompanyProduction();
                companyService.updateProduction(company, prod);
                view.displayMessage("Company production tier updated.");
            }
            case 3 -> {
                String name = readValidCompanyName();
                COMPANY_PRODUCTION prod = getValidateCompanyProduction();
                company.setCompName(name);
                company.setCompProd(prod);
                companyService.update(company, company);
                view.displayMessage("Company updated.");
            }
            default -> view.displayMessage("Exiting update menu.");
        }
    }

    private void findOrSortCompanySubMenu() {
        view.CompanySubMenu();
        int choice = input.getInteger("Select option: ");
        if (choice == 1) {
            findCompany();
        } else if (choice == 2) {
            sortCompany();
        }
    }

    private void findCompany() {
        view.findCompanyUI();
        int choice = input.getInteger("Select option: ");
        switch (choice) {
            case 1 -> {
                Long id = input.getLong("Enter Company ID: ");
                CompanyEntity company = companyService.findById(id);
                displayDetailedCompany(company);
            }
            case 2 -> {
                String name = input.getString("Enter Company Name: ");
                List<CompanyEntity> list = companyService.findByName(name);
                if (list.isEmpty()) {
                    view.displayMessage("No company found matching name: " + name);
                    return;
                }
                view.displayCompanyListWithIndices(list);
                int idx = input.getInteger("Select index to view details: ");
                CompanyEntity company = companyService.findByIndex(idx, list);
                displayDetailedCompany(company);
            }
            default -> view.displayMessage("Exiting find menu.");
        }
    }

    private void sortCompany() {
        view.sortCompanyUI();
        int choice = input.getInteger("Select sort criteria: ");
        if (choice < 1 || choice > 3) return;

        int orderChoice = input.getInteger("Sort Direction (Press 1 for Descending): ");
        boolean isReversed = (orderChoice == 1);

        CompanyComparator option = CompanyComparator.getComparatorOption(choice);
        List<CompanyEntity> sorted = companyService.sort(option, isReversed);

        view.displayMessage("\n=== SORTED COMPANIES ===");
        for (CompanyEntity c : sorted) {
            displayDetailedCompany(c);
        }
    }

    private void showAllCompanies() {
        List<CompanyEntity> list = companyService.showAll();
        if (list.isEmpty()) {
            view.displayMessage("No companies found.");
            return;
        }
        view.displayMessage("\n=== ALL COMPANIES ===");
        for (CompanyEntity c : list) {
            displayDetailedCompany(c);
        }
    }

    private void deleteCompany() {
        view.deleteCompanyTitle();
        Long compId = input.getLong("Enter Company ID to delete: ");
        CompanyEntity company = companyService.findById(compId);

        customerService.deleteByCompany(compId);
        companyService.delete(company);

        view.displayMessage("Company " + compId + " removed and employee associations cleared.");
    }

    private void displayDetailedCompany(CompanyEntity company) {
        List<CustomerEntity> employees = customerService.findByComp(company.getCompID());
        Double totalSpent = companyService.calculateCompanyTotalCost(company.getCompID());
        view.displayCompanyDetails(company, employees, totalSpent);
    }

    // ==========================================
    // ENUM VALIDATOR & HELPER UTILITIES
    // ==========================================

    private BOOKING_CHANNEL getValidateChannel() {
        while (true) {
            view.showChannelUI();
            int code = input.getInteger("Select Channel Code:");
            try {
                return BOOKING_CHANNEL.getChannel(code);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private BOOKING_STATUS getValidateBookingStatus() {
        while (true) {
            view.showStatusUI();
            int code = input.getInteger("Select Status Code:");
            try {
                return BOOKING_STATUS.getStatus(code);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private CUSTOMER_TIER getValidateCustomerTier() {
        while (true) {
            view.showCustomerTierUI();
            int code = input.getInteger("Select Tier Code:");
            try {
                return CUSTOMER_TIER.getTier(code);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private COMPANY_PRODUCTION getValidateCompanyProduction() {
        while (true) {
            view.showCompanyProductionUI();
            int code = input.getInteger("Select Production Code:");
            try {
                return COMPANY_PRODUCTION.getProduction(code);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private String readValidCustomerName() {
        while (true) {
            String name = input.getString("Enter Customer Name (Capitalized Words): ");
            ValidationResult res = CustomerValidator.isCustomerNameValid(name);
            if (res.isValid()) {
                return name;
            }
            System.out.println("Validation Error: " + res.getErrorMessage());
        }
    }

    private String readValidCustomerContact() {
        while (true) {
            String contact = input.getString("Enter Customer Contact (Phone Number): ");
            ValidationResult res = CustomerValidator.isCustomerContactValid(contact);
            if (res.isValid()) {
                return contact;
            }
            System.out.println("Validation Error: " + res.getErrorMessage());
        }
    }

    private String readValidCompanyName() {
        while (true) {
            String name = input.getString("Enter Company Name: ");
            ValidationResult res = CompanyValidator.isCompanyName(name);
            if (res.isValid()) {
                return name;
            }
            System.out.println("Validation Error: " + res.getErrorMessage());
        }
    }

    private LocalDate readValidCheckInDate() {
        while (true) {
            LocalDate date = input.getDate("Enter Check-In Date");
            ValidationResult res = BookingValidator.IsCheckInValid(date);
            if (res.isValid()) {
                return date;
            }
            System.out.println("Validation Error: " + res.getErrorMessage());
        }
    }

    private LocalDate readValidCheckOutDate(LocalDate checkIn) {
        while (true) {
            LocalDate checkOut = input.getDate("Enter Check-Out Date");
            if (checkOut.isAfter(checkIn)) {
                return checkOut;
            }
            System.out.println("Check-Out date must be after Check-In date (" + checkIn + ")");
        }
    }


}