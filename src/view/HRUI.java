package view;

import model.entity.UserEntity;

import java.util.List;

public class HRUI {
    public void showMenuUI(String username) {
        System.out.println("\n\t=== HR DASHBOARD ===");
        System.out.println("HELLO " + username);
        System.out.println("1.MANAGE USER");
        System.out.println("2. LOGOUT");

    }

    public void showUserManagerMenuUI() {
        System.out.println("\n\t=== USER MANAGEMENT ===");
        System.out.println("1.Add User");
        System.out.println("2.Update User");
        System.out.println("3.Delete User");
        System.out.println("4.Find/Sort User");
        System.out.println("5.Show User List");
        System.out.println("6.Exit");
    }

    public void showUserManagerSubMenuUI() {
        System.out.println("\n\t=== SORT/FIND ===");
        System.out.println("1.Find User");
        System.out.println("2.Sort User");
        System.out.println("3.Exit");

    }

    public void AddUserMenuUI() {
        System.out.println("\n\t== ADD USER ==");
    }

    public void UpdateMenuUi() {
        System.out.println("\n\t== UPDATE USER ==");
    }

    public void UpdateUserMenuUI() {
        System.out.println("\n\t== UPDATE CATEGORY ==");
        System.out.println("1.Update UserName");
        System.out.println("2.Update Password");
        System.out.println("3.Update Role");
        System.out.println("4.Update All");
        System.out.println("5.Exit");
    }

    public void findUserUI() {
        System.out.println("\n\t== USER SEARCH ==");
        System.out.println("1.Find By User ID");
        System.out.println("2.Find By User Name");
        System.out.println("3.Exit");
    }

    public void sortUserUI() {
        System.out.println("\n\t== SORT USER ==");
        System.out.println("1.Sort By Name");
        System.out.println("2.Sort By ID");
        System.out.println("3.Sort By Role");
        System.out.println("4.Exit");

    }

    public void LOGOUT() {
        System.out.println("Logging out!");
    }

    public void UserDeleteUI() {
        System.out.println("\n\t== DELETE USER ==");
    }

    public void printUserTable(List<UserEntity> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }


        // Table Header
        System.out.println("+------------+----------------------+--------------+-----------------+");
        System.out.printf("| %-10s | %-20s | %-12s | %-15s |%n", "User ID", "Username", "Role", "Password");
        System.out.println("+------------+----------------------+--------------+-----------------+");

        // Table Rows
        for (UserEntity user : users) {
            String idStr = (user.getUserId() != null) ? String.valueOf(user.getUserId()) : "N/A";
            String roleStr = user.getRole().getValue();

            String maskedPassword =  user.getPassword();

            System.out.printf("| %-10s | %-20s | %-12s | %-15s |%n",
                    idStr,
                    user.getUsername(),
                    roleStr,
                    maskedPassword
            );
        }

        // Table Footer
        System.out.println("+------------+----------------------+--------------+----------------------+");
        System.out.println("Total records: " + users.size());
    }
}
