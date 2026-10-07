package repository;

import model.entity.RoomEntity;
import model.enums.ROOM_STATUS;
import model.enums.ROOM_TIER;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository implements IRepository<RoomEntity>{
    private final static String filename = "database/room.csv";
    @Override
    public List<RoomEntity> read() {
        List<RoomEntity> rlist = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return rlist;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                Integer roomNumber = Integer.valueOf(data[0].trim());
                Short floor = Short.valueOf(data[1].trim());
                ROOM_TIER tier = ROOM_TIER.valueOf(data[2].trim());
                ROOM_STATUS status = ROOM_STATUS.valueOf(data[3].trim());
                RoomEntity r = new RoomEntity(roomNumber,floor,tier,status);
                rlist.add(r);
            }
        } catch (IOException e) {
            System.err.println("Error reading rooms file: " + e.getMessage());
        }
        return  rlist;
    }

    @Override
    public void write(List<RoomEntity> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder line = new StringBuilder();
            for (RoomEntity r : list) {
                line.append(r.getRoomNumber()).append(",")
                        .append(r.getFloor())
                        .append(r.getTier())
                        .append(r.getStatus()).append("\n");
            }
            bw.write(line.toString());
        } catch (IOException e) {
            System.err.println("Error writing to rooms file: " + e.getMessage());
        }
    }
}
