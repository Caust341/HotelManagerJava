package controller;

import error.ItemAlreadyExistException;
import error.ItemNotFoundException;
import model.entity.UserEntity;
import model.enums.USER_ROLE;
import service.UserService;
import util.InputHandler;
import util.comparator.UserComparator;
import util.validator.UserValidator;
import util.validator.ValidationResult;
import view.HRUI;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class HRController extends GeneralController {
    private static final InputHandler input = new InputHandler();
    private static final UserService service = UserService.getInstance();
    private final HRUI view = new HRUI();

    public HRController(UserEntity user) {
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
                    UserManager();
                    break;
                case 2:
                    view.LOGOUT();
                    isRunning = false;
                    break;
                default:
                    System.out.println("ERROR Invalid Input");
            }
        }
    }

    public void UserManager() {
        boolean isRunning = true;
        while (isRunning) {
            view.showUserManagerMenuUI();
            int choice = input.getInteger("Please select one of the options: ");
            try {
                switch (choice) {
                    case 1 -> addUser();
                    case 2 -> updateUser();
                    case 3 -> deleteUser();
                    case 4 -> findOrSortUser();
                    case 5 -> view.printUserTable(service.showAll());
                    case 6 -> isRunning = false;
                    default -> System.out.println("ERROR Invalid Option");
                }
            } catch (ItemNotFoundException | ItemAlreadyExistException | IllegalArgumentException e) {
                System.out.println("ERROR " + e.getMessage());
            }
        }
    }

    private void addUser() {
        view.AddUserMenuUI();

        String username = getValidatedString("Enter Username: ", UserValidator::isUserNameValid);
        String password = getValidatedString("Enter Password: ", UserValidator::isPasswordValid);
        USER_ROLE role = getValidatedRole();

        Long generatedId = service.UserIdGenerator();
        UserEntity newUser = new UserEntity(generatedId, username, password, role);

        service.add(newUser);
        System.out.println(" User created successfully with ID: " + generatedId);
    }

    private void updateUser() {
        view.UpdateMenuUi();

        Long targetId = Long.valueOf(input.getInteger("Enter User ID to update: "));
        UserEntity targetUser = service.findById(targetId);

        view.UpdateUserMenuUI();
        int choice = input.getInteger("Select update option: ");

        switch (choice) {
            case 1 -> {
                String newName = getValidatedString("Enter new Username: ", UserValidator::isUserNameValid);
                service.updateName(targetUser, newName);
                System.out.println(" Username updated successfully.");
            }
            case 2 -> {
                String newPassword = getValidatedString("Enter new Password: ", UserValidator::isPasswordValid);
                service.updatePassword(targetUser, newPassword);
                System.out.println(" Password updated successfully.");
            }
            case 3 -> {
                USER_ROLE newRole = getValidatedRole();
                service.updateRole(targetUser, newRole);
                System.out.println(" Role updated successfully.");
            }
            case 4 -> {
                String newName = getValidatedString("Enter new Username: ", UserValidator::isUserNameValid);
                String newPassword = getValidatedString("Enter new Password: ", UserValidator::isPasswordValid);
                USER_ROLE newRole = getValidatedRole();

                UserEntity updatedUser = new UserEntity(targetUser.getUserId(), newName, newPassword, newRole);
                service.update(targetUser, updatedUser);
                System.out.println(" All user fields updated successfully.");
            }
            case 5 -> System.out.println("Returning to User Management...");
            default -> System.out.println("ERROR Invalid choice.");
        }
    }

    private void deleteUser() {
        view.UserDeleteUI();

        Long targetId = Long.valueOf(input.getInteger("Enter User ID to delete: "));
        UserEntity targetUser = service.findById(targetId);

        if (targetUser.getUserId().equals(user.getUserId())) {
            System.out.println("ERROR You cannot delete your own active session account!");
            return;
        }

        service.delete(targetUser);
        System.out.println(" User ID " + targetId + " deleted successfully.");
    }

    private void findOrSortUser() {
        boolean inSubMenu = true;
        while (inSubMenu) {
            view.showUserManagerSubMenuUI();
            int choice = input.getInteger("Please select option: ");

            switch (choice) {
                case 1 -> processFindUser();
                case 2 -> processSortUser();
                case 3 -> inSubMenu = false;
                default -> System.out.println("ERROR Invalid Option");
            }
        }
    }

    private void processFindUser() {
        view.findUserUI();
        int choice = input.getInteger("Select search option: ");

        switch (choice) {
            case 1 -> {
                Long id = Long.valueOf(input.getInteger("Enter User ID: "));
                try {
                    UserEntity foundUser = service.findById(id);
                    view.printUserTable(Collections.singletonList(foundUser));
                } catch (ItemNotFoundException e) {
                    System.out.println("ERROR " + e.getMessage());
                }
            }
            case 2 -> {
                String searchName = input.getString("Enter Username keyword to search: ");
                List<UserEntity> results = service.findByName(searchName);
                view.printUserTable(results);
            }
            case 3 -> System.out.println("Exiting Search...");
            default -> System.out.println("ERROR Invalid Choice");
        }
    }

    private void processSortUser() {
        view.sortUserUI();
        int choice = input.getInteger("Select field to sort by: ");
        if (choice == 4) return;

        try {
            UserComparator comparatorOption = UserComparator.getComparatorOption(choice);
            int orderChoice = input.getInteger("Sort Direction: (Press 1 if you want to descend.): ");
            boolean isReversed = (orderChoice == 1);

            List<UserEntity> sortedUsers = service.sort(comparatorOption, isReversed);
            view.printUserTable(sortedUsers);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }


    private String getValidatedString(String message, Function<String, ValidationResult> validator) {
        while (true) {
            String value = input.getString(message);
            ValidationResult result = validator.apply(value);

            if (result.isValid()) {
                return value;
            }
            System.out.println("ERROR Validation Error: " + result.getErrorMessage());
        }
    }

    private USER_ROLE getValidatedRole() {
        while (true) {
            System.out.println("Available Roles: 1: ADMIN | 2: MANAGER | 3: HR");
            int roleCode = input.getInteger("Select Role Code: ");
            try {
                return USER_ROLE.getRole(roleCode);
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR " + e.getMessage());
            }
        }
    }
}