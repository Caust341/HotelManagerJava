package repository;

import model.entity.RoomTierEntity;
import model.enums.ROOM_TIER;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTierRepository implements IRepository<RoomTierEntity>{
    private static final String filename = "database/roomtier.csv";

    @Override
    public List<RoomTierEntity> read() {
        List<RoomTierEntity> rtlist = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return rtlist;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                ROOM_TIER tier = ROOM_TIER.valueOf(data[0].trim());
                Double basePrice = Double.valueOf(data[1].trim());
                Integer maxCap = Integer.valueOf(data[2].trim());
                RoomTierEntity ct = new RoomTierEntity(tier, basePrice, maxCap);
                rtlist.add(ct);
            }
        } catch (IOException e) {
            System.err.println("Error reading room tiers file: " + e.getMessage());
        }
        return  rtlist;
    }

    @Override
    public void write(List<RoomTierEntity> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder line = new StringBuilder();
            for (RoomTierEntity rt : list) {
                line.append(rt.getRoomTier()).append(",")
                        .append(rt.getBasePrice()).append(",")
                        .append(rt.getMaxCap()).append("\n");
            }
            bw.write(line.toString());
        } catch (IOException e) {
            System.err.println("Error writing to room tiers file: " + e.getMessage());
        }
    }
}
