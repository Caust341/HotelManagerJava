package view;

import model.entity.BookingEntity;
import model.entity.CompanyEntity;
import model.entity.CustomerEntity;

import java.util.List;

public class AdminUI {
    public void showMenu(String username) {
        System.out.println("\n\t=== ADMIN DASHBOARD ===");
        System.out.println("HELLO Admin " + username);
        System.out.println("1.MANAGE BOOKING");
        System.out.println("2.MANAGE CUSTOMERS");
        System.out.println("3.MANAGE COMPANY/ASSOCIATE");
        System.out.println("4.LOGOUT");
    }

    public void bookingManager() {
        System.out.println("\n\t=== BOOKING MANAGER ===");
        System.out.println("1. ADD BOOKING");
        System.out.println("2. UPDATE BOOKING");
        System.out.println("3. DELETE BOOKING");
        System.out.println("5.FIND BOOKING");
        System.out.println("6.SORT BOOKING");
        System.out.println("7.SHOW ALL BOOKING");
        System.out.println("8.EXIT");
    }

    public void addBookingTitle() {
        System.out.println("\n\t== ADD BOOKING ==");
    }


    public void addBookingCustomer() {
        System.out.println("1.ADD NEW CUSTOMER");
        System.out.println("2.FIND EXISTED CUSTOMER");
    }

    public void addBookingCompany() {
        System.out.println("1.ADD NEW COMPANY");
        System.out.println("2.FIND EXISTED COMPANY");
        System.out.println("3.NO COMPANY");
    }

    public void updateBookingTitle() {
        System.out.println("\n\t== UPDATE BOOKING ==");
    }

    public void updateBookingMenu() {
        System.out.println("\n\t== UPDATE BOOKING CATEGORY ==");
        System.out.println("1.UPDATE ROOM ID");
        System.out.println("2.UPDATE CHECK IN");
        System.out.println("3.UPDATE CHECK OUT");
        System.out.println("4.UPDATE BOOKING STATUS");
        System.out.println("5.UPDATE ALL");
        System.out.println("EXIT");
    }

    public void deleteBookingTitle() {
        System.out.println("\n\t== DELETE BOOKING ==");
    }


    public void findBookingMenu() {
        System.out.println("\n\t== FIND BOOKING ==");
    }


    public void sortRoomMenu() {
        System.out.println("\n\t== SORT BOOKING");
        System.out.println("1.Sort By ID");
        System.out.println("2.Sort By Channel");
        System.out.println("3.Sort By Status");
        System.out.println("4.Exit");
    }


    public void  customerManager() {
        System.out.println("\n\t=== CUSTOMERS MANAGER ===");
        System.out.println("1.ADD CUSTOMER");
        System.out.println("2.UPDATE CUSTOMER");
        System.out.println("3.FIND | SORT CUSTOMER");
        System.out.println("4.SHOW ALL CUSTOMERS");
        System.out.println("5.DELETE CUSTOMER");
        System.out.println("6.EXIT");
    }

    public void addCustomerTitle() {
        System.out.println("\n\t== ADD CUSTOMER ==");
    }

    public void updateCustomerTitle() {
        System.out.println("\n\t== UPDATE CUSTOMER ==");
    }

    public void updateCustomerCategory() {
        System.out.println("\n\t== UPDATE CATEGORY ==");
        System.out.println("1.Update Company ID");
        System.out.println("2.Update Customer Name");
        System.out.println("3.Update Customer Tier");
        System.out.println("4.Update All");
        System.out.println("5.Exit");
    }

    public void deleteCustomerTitle() {
        System.out.println("\n\t== DELETE CUSTOMER ==");
    }

    public void CustomerSubMenuUI() {
        System.out.println("\n\t== FIND AND SORT");
        System.out.println("1.Find Customer");
        System.out.println("2.Sort Customer");
        System.out.println("3.Exit");
    }

    public void findCustomerUI() {
        System.out.println("\n\t== FIND CUSTOMER ==");
        System.out.println("1. Find By ID");
        System.out.println("2. Find By Name");
        System.out.println("3. Find By Comp");
        System.out.println("4.Exit");
    }


    public void sortCustomerUI() {
        System.out.println("\n\t== SORT CUSTOMER ==");
        System.out.println("1.Sort By Customer ID");
        System.out.println("2.Sort By Company ID");
        System.out.println("3.Sort By Name");
        System.out.println("4.Sort By Tier");
        System.out.println("5.Exit");
    }

    public void companyManager() {
        System.out.println("\n\t=== COMPANY MANAGER ===");
        System.out.println("1.ADD COMPANY");
        System.out.println("2.UPDATE COMPANY");
        System.out.println("3.FIND | SORT COMPANY");
        System.out.println("4.SHOW ALL COMPANIES");
        System.out.println("5.DELETE COMPANY");
        System.out.println("6.EXIT");
    }

    public void addCompanyTitle() {
        System.out.println("\n\t== ADD COMPANY ==");
    }

    public void updateCompanyTitle() {
        System.out.println("\n\t== UPDATE COMPANY ==");
    }

    public void updateCompanyMenu() {
        System.out.println("\n\t== UPDATE CATEGORY ==");
        System.out.println("1. UPDATE BY NAME");
        System.out.println("2. UPDATE BY PRODUCTION");
        System.out.println("3. UPDATE BY CONTACT");
        System.out.println("4. UPDATE ALL");
        System.out.println("5. EXIT");
    }



    public void deleteCompanyTitle() {
        System.out.println("\n\t== DELETE COMPANY ==");
    }

    public void CompanySubMenu() {
        System.out.println("\n\t== FIND AND SORT COMPANY ==");
    }

    public void findCompanyUI() {
        System.out.println("\n\t == FIND COMPANY ==");
        System.out.println("1. FindByID");
        System.out.println("2. FindByName");
        System.out.println("3. Exit");
    }


    public void sortCompanyUI() {
        System.out.println("\n\t == SORT COMPANY ==");
        System.out.println("1.Sort by ID");
        System.out.println("2.Sort by Name");
        System.out.println("3.Sort by Production");
        System.out.println("4. Exit");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayBookingDetails(BookingEntity booking, List<String> guestDetails, Double totalPrice) {
        System.out.println("\n-------------------------------------------------------------");
        System.out.printf("Booking ID: %d | Room: %d | Channel: %s | Status: %s\n",
                booking.getBookingID(), booking.getRoomID(), booking.getBookingChannel(), booking.getBookingStatus());
        System.out.printf("Check-In: %s | Check-Out: %s | Price/Night: $%.2f\n",
                booking.getCheckIn(), booking.getCheckOut(), booking.getPricePerNight());

        System.out.println("\nGuests:");
        System.out.printf("%-15s %-20s %-10s\n", "Customer Name", "Company Name", "Is Primary");
        System.out.println("-------------------------------------------------------------");
        for (String guest : guestDetails) {
            System.out.println(guest);
        }

        System.out.println("-------------------------------------------------------------");
        System.out.printf("Total Calculated Price: $%.2f\n", totalPrice);
        System.out.println("-------------------------------------------------------------");
    }

    public void displayBookingSummary(BookingEntity b) {
        System.out.printf("ID: %d | Room: %d | Channel: %s | Status: %s | Total: $%.2f\n",
                b.getBookingID(), b.getRoomID(), b.getBookingChannel(), b.getBookingStatus(), b.getTotalPrice());
    }

    public void displayCustomerDetails(CustomerEntity c, String compName) {
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("Customer ID: %d | Name: %s | Contact: %s |Tier: %s | Stay Nights Count: %d | Company: %s\n",
                c.getCustID(), c.getCustName(), c.getContact() ,c.getCustTier(), c.getCount(), compName);
        System.out.println("-----------------------------------------------------------------------");
    }

    public void displayCompanyDetails(CompanyEntity comp, List<CustomerEntity> employees, Double totalSpent) {
        System.out.println("\n-------------------------------------------------------------");
        System.out.printf("Company ID: %d | Name: %s | Production Tier: %s\n",
                comp.getCompID(), comp.getCompName(), comp.getCompProd());
        System.out.println("-------------------------------------------------------------");

        System.out.println("Associated Customers:");
        if (employees.isEmpty()) {
            System.out.println("  (No associated customers)");
        } else {
            System.out.printf("  %-15s %-10s %-10s\n", "Name", "Tier", "Nights Count");
            for (CustomerEntity emp : employees) {
                System.out.printf("  %-15s %-10s %-10d\n", emp.getCustName(), emp.getCustTier(), emp.getCount());
            }
        }

        System.out.println("-------------------------------------------------------------");
        System.out.printf("Total Company Cost Spent: $%.2f\n", totalSpent);
        System.out.println("-------------------------------------------------------------");
    }

    public void showChannelUI() {
        System.out.println("""
                    Available Channels:
                    1. CORPORATE
                    2. OTA
                    3. TRANSIENT
                    4. WALK_IN""");
    }

    public void showStatusUI() {
        System.out.println("""
                    Available Booking Statuses:
                    1. CONFIRMED
                    2. CHECKED IN
                    3. CHECKED OUT
                    4. CANCELLED
                    5. NO SHOW""");
    }

    public void showCustomerTierUI() {
        System.out.println("""
                    Available Tiers:
                    1. PLATINUM
                    2. GOLD
                    3. SILVER
                    4. NEW""");
    }

    public void showCompanyProductionUI() {
        System.out.println("""
                    Available Productions:
                    1. NEWCOMER
                    2. ACTIVE
                    3. VALUED""");
    }

    public void displayCustomerListWithIndices(List<CustomerEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("[%d] ID: %d | Name: %s\n", i, list.get(i).getCustID(), list.get(i).getCustName());
        }
    }

    public void displayCompanyListWithIndices(List<CompanyEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("[%d] ID: %d | Name: %s\n", i, list.get(i).getCompID(), list.get(i).getCompName());
        }
    }

}
