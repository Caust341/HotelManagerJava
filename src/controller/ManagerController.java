package controller;

import error.ItemAlreadyExistException;
import error.ItemNotFoundException;
import model.entity.CompanyProductionEntity;
import model.entity.RoomEntity;
import model.entity.RoomTierEntity;
import model.entity.UserEntity;
import model.enums.COMPANY_PRODUCTION;
import model.enums.ROOM_STATUS;
import model.enums.ROOM_TIER;
import service.CompanyProductionService;
import service.RoomService;
import service.RoomTierService;
import util.InputHandler;
import util.comparator.RoomComparator;
import view.ManagerUI;

import java.util.Collections;
import java.util.List;

public class ManagerController extends GeneralController{
    private static final InputHandler input = new InputHandler();
    private static final RoomService service = RoomService.getInstance();
    private static final RoomTierService tierService = RoomTierService.getInstance(); //[cite: 6]
    private static final CompanyProductionService cpService = CompanyProductionService.getInstance();
    private final ManagerUI view = new ManagerUI();



    public ManagerController(UserEntity user) {
        super(user);
    }

    @Override
    public void start() {
        boolean isRunning = true;
        while (isRunning) {
            view.showMenuUI(user.getUsername());
            int choice = input.getInteger("Please enter one of the options: ");
            switch (choice) {
                case 1:
                    RoomManager();
                    break;
                case 2:
                    CompanyProductionManager();
                    break;
                case 3:
                    RoomTierManager();
                    break;
                case 4:
                    view.LOGOUT();
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }



    private void RoomManager() {
        boolean isRunning = true;
        while(isRunning) {
            view.showMenuRoomUI();
            int choice = input.getInteger("Select one of the options: ");
            try {
                switch (choice) {
                    case 1 -> updateRoom();
                    case 2 -> findOrSortRoom();
                    case 3 -> view.printUserTable(service.showAll());
                    case 4 -> isRunning = false;
                    default -> System.out.println("ERROR Invalid Option");
                }
            } catch (ItemNotFoundException | ItemAlreadyExistException | IllegalArgumentException e) {
                System.out.println("ERROR " + e.getMessage());
            }
        }
    }

    private void updateRoom() {
        view.UpdateRoomUI();

        Integer targetNumber = input.getInteger("Enter Room Number to Update");
        RoomEntity targetRoom = service.findByNumber(targetNumber);

        view.showMenuRoomUpdateUI();
        int choice = input.getInteger("Select update option: ");

        switch (choice) {
            case 1 -> {
                ROOM_TIER newTier = getValidateTier();
                service.updateTier(targetRoom, newTier);
                System.out.println("Tier updated Successfully");
            }

            case 2 -> {
                ROOM_STATUS newStatus = getValidateStatus();
                service.updateStatus(targetRoom, newStatus);
                System.out.println("Status updated Successfully");
            }

            case 3 -> System.out.println("Returning to Room Management...");
            default -> System.out.println("ERROR Invalid choice.");
        }
    }

    private void findOrSortRoom() {
        boolean isRunningSub = true;
        while (isRunningSub) {
            view.RoomSubMenuUI();
            int choice = input.getInteger("Please select option: ");

            switch (choice) {
                case 1 -> processFindRoom();
                case 2 -> processSortRoom();
                case 3 -> isRunningSub = false;
                default -> System.out.println("ERROR: Invalid Option");
            }

        }
    }

    private void processSortRoom() {
        view.sortRoomMenuUI();
        int choice = input.getInteger("Select field to sort by");
        if (choice == 5) return;

        try {
            RoomComparator comparatorOption = RoomComparator.getComparatorOption(choice);
            int orderChoice = input.getInteger("Sort Direction: (Press 1 if you want to descend.)");
            boolean isReversed = (orderChoice == 1);

            List<RoomEntity> sortedRooms = service.sort(comparatorOption, isReversed);
            view.printUserTable(sortedRooms);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void processFindRoom() {
        view.findRoomMenuUI();
        Integer number = input.getInteger("Enter Room Number: ");
        try {
            RoomEntity foundRoom = service.findByNumber(number);
            view.printUserTable(Collections.singletonList(foundRoom));
        } catch (ItemNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void RoomTierManager() {
        boolean isRunning = true;
        while (isRunning) {
            view.showRoomTierUI(); //[cite: 9]
            System.out.println("4. EXIT"); // Added because it was missing in ManagerUI[cite: 9]

            int choice = input.getInteger("Select one of the options: ");
            try {
                switch (choice) {
                    case 1 -> findRoomTier();
                    case 2 -> updateRoomTierPrice();
                    case 3 -> view.printRoomTierTable(tierService.showAll()); //[cite: 6]
                    case 4 -> isRunning = false;
                    default -> System.out.println("ERROR: Invalid Option");
                }
            } catch (ItemNotFoundException | ItemAlreadyExistException |IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void findRoomTier() {
        ROOM_TIER tier = getValidateTier(); //[cite: 4]
        RoomTierEntity foundTier = tierService.findByTier(tier); //[cite: 6]
        view.printRoomTierTable(Collections.singletonList(foundTier));
    }

    private void updateRoomTierPrice() {
        ROOM_TIER tier = getValidateTier();
        RoomTierEntity targetTier = tierService.findByTier(tier);
        Double newPrice = input.getDouble("Enter new base price: ");
        tierService.updatePrice(targetTier, newPrice); //[cite: 6]
        System.out.println(" Base Price updated successfully");
    }

    // ==========================================
    // COMPANY PRODUCTION MANAGEMENT
    // ==========================================

    private void CompanyProductionManager() {
        boolean isRunning = true;
        while (isRunning) {
            view.showCompanyProductionUI(); //[cite: 9]
            System.out.println("4. EXIT"); // Added because it was missing in ManagerUI[cite: 9]

            int choice = input.getInteger("Select one of the options: ");
            try {
                switch (choice) {
                    case 1 -> findCompanyProduction();
                    case 2 -> updateCompanyProductionDiscount();
                    case 3 -> view.printCompanyProductionTable(cpService.showAll()); //[cite: 7]
                    case 4 -> isRunning = false;
                    default -> System.out.println("ERROR: Invalid Option");
                }
            } catch (ItemNotFoundException | ItemAlreadyExistException |IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void findCompanyProduction() {
        COMPANY_PRODUCTION production = getValidateProduction(); //[cite: 5]
        CompanyProductionEntity foundProduction = cpService.findByProduction(production); //[cite: 7]
        view.printCompanyProductionTable(Collections.singletonList(foundProduction));
    }

    private void updateCompanyProductionDiscount() {
        COMPANY_PRODUCTION production = getValidateProduction();
        CompanyProductionEntity targetProduction = cpService.findByProduction(production);
        Double newDiscount = input.getDouble("Enter new discount amount (0.0 to 1.0): ");
        cpService.updateByDiscount(targetProduction, newDiscount); //[cite: 7]
        System.out.println(" Discount updated successfully");
    }


    private ROOM_TIER getValidateTier() {
        while (true) {
            System.out.println("""
                    Available Tiers:\s
                    1.STANDARD\s
                    2.DELUXE\s
                    3.SUITE""");
            int tierCode = input.getInteger("Select Tier Code:");
            try {
                return ROOM_TIER.getTier(tierCode);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR " + e.getMessage() );
            }
        }
    }

    private ROOM_STATUS getValidateStatus() {
        while (true) {
            System.out.println("""
                    Available Tiers:\s
                    1.VACANT\s
                    2.OCCUPIED\s
                    3.OUT OF ORDER
                    4.DIRTY
                    5.CLEAN""");
            int code = input.getInteger("Select Status Code:");
            try {
                return ROOM_STATUS.getStatus(code);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR " + e.getMessage() );
            }
        }

    }

    private COMPANY_PRODUCTION getValidateProduction() {
        while (true) {
            System.out.println("""
                    Available Productions:\s
                    1.NEWCOMER\s
                    2.ACTIVE\s
                    3.VALUED"""); //[cite: 5]
            int code = input.getInteger("Select Production Code:");
            try {
                return COMPANY_PRODUCTION.getProduction(code); //[cite: 5]
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

}
