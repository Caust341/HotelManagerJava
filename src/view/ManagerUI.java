package view;

import model.entity.CompanyProductionEntity;
import model.entity.RoomEntity;
import model.entity.RoomTierEntity;

import java.util.List;

public class ManagerUI {
    public void showMenuUI(String username) {
        System.out.println("\n\t=== MANAGER DASHBOARD ===");
        System.out.println("HELLO MANAGER " + username);
        System.out.println("1.MANAGE ROOM");
        System.out.println("2.MANAGE COMPANY PRODUCTION");
        System.out.println("3.MANAGE ROOM TIER");
        System.out.println("4.LOGOUT");
    }

    public void showMenuRoomUI() {
        System.out.println("\n\t=== ROOM MANAGER ===");
        System.out.println("1. UPDATE ROOM");
        System.out.println("2. FIND | SORT ROOM");
        System.out.println("3. SHOW ROOM LIST");
        System.out.println("4. EXIT");
    }

    public void showCompanyProductionUI() {
        System.out.println("\n\t=== COMPANY PRODUCTION MANAGER ===");
        System.out.println("1. FIND PRODUCTION");
        System.out.println("2. UPDATE DISCOUNT");
        System.out.println("3. SHOW PRODUCTION LIST");
    }

    public void showRoomTierUI() {
        System.out.println("\n\t=== ROOM TIER MANAGER ===");
        System.out.println("1. FIND TIER");
        System.out.println("2. UPDATE PRICE");
        System.out.println("3. SHOW TIER LIST");
    }

    public void UpdateRoomUI() {
        System.out.println("\n\t== UPDATE ROOM ==");
    }

    public void showMenuRoomUpdateUI() {
        System.out.println("\n\t== UPDATE CATEGORY ==");
        System.out.println("1.Update by Tier");
        System.out.println("2.Update by Status");
        System.out.println("3.Exit");
    }

    public void findRoomMenuUI() {
        System.out.println("\n\t== FIND ROOM MENU ==");
    }

    public void RoomSubMenuUI() {
        System.out.println("\n\t==SORT AND FIND==");
        System.out.println("1.Find room");
        System.out.println("2.Sort room");
        System.out.println("3. Exit");
    }

    public void sortRoomMenuUI() {
        System.out.println("\n\t==SORT ROOM MENU ==");
        System.out.println("1.Sort by Number");
        System.out.println("2.Sort by Floor");
        System.out.println("3.Sort by Status");
        System.out.println("4.Sort by Tier");
        System.out.println("5.Exit");
    }

    public void printUserTable(List<RoomEntity> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("No Rooms Found");
            return;
        }

        System.out.println("+------------+-----+-----------------+-----------------+");
        System.out.printf("| %-10s | %-5s | %-15s | %-15s |%n", "RoomNo", "Floor", "Tier", "Status");
        System.out.println("+------------+-----+-----------------+-----------------+");

        for (RoomEntity room : rooms) {
            String roomNumber = String.valueOf(room.getRoomNumber());
            String floor = String.valueOf(room.getFloor());
            String tier = room.getTier().getValue();
            String status = room.getStatus().getValue();

            System.out.printf("| %-10s | %-5s | %-15s | %-15s |%n",
                    roomNumber,
                    floor,
                    tier,
                    status
            );
        }

        System.out.println("+------------+-----+-----------------+-----------------+");
        System.out.println("Total rooms: " +rooms.size());
    }

    public void printCompanyProductionTable(List<CompanyProductionEntity> productions) {
        if (productions == null || productions.isEmpty()) {
            System.out.println("No Company Productions Found");
            return;
        }

        System.out.println("+-----------------+-----------------+");
        System.out.printf("| %-15s | %-15s |%n", "Production", "Discount");
        System.out.println("+-----------------+-----------------+");

        for (CompanyProductionEntity prod : productions) {
            System.out.printf("| %-15s | %-15.2f |%n",
                    prod.getProduction().getValue(),
                    prod.getDiscount()
            );
        }
        System.out.println("+-----------------+-----------------+");
        System.out.println("Total Productions: " + productions.size());
    }

    public void printRoomTierTable(List<RoomTierEntity> tiers) {
        if (tiers == null || tiers.isEmpty()) {
            System.out.println("No Room Tiers Found");
            return;
        }

        System.out.println("+-----------------+-----------------+-----------------+");
        System.out.printf("| %-15s | %-15s | %-15s |%n", "Tier", "Base Price", "Max Capacity");
        System.out.println("+-----------------+-----------------+-----------------+");

        for (RoomTierEntity tier : tiers) {
            System.out.printf("| %-15s | %-15.2f | %-15d |%n",
                    tier.getRoomTier().getValue(),
                    tier.getBasePrice(),
                    tier.getMaxCap()
            );
        }
        System.out.println("+-----------------+-----------------+-----------------+");
        System.out.println("Total Tiers: " + tiers.size());
    }

    public void LOGOUT() {
        System.out.println("Logging out!");
    }


}
