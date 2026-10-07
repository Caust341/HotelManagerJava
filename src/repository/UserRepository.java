package repository;

import model.entity.UserEntity;
import model.enums.USER_ROLE;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<UserEntity> {

    // 1. Corrected filename to user.csv
    private static final String FILENAME = "database/user.csv";

    @Override
    public List<UserEntity> read() {
        List<UserEntity> userList = new ArrayList<>();
        File file = new File(FILENAME);

        if (!file.exists()) {
            return userList;
        }


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 4) continue;

                Long userId = Long.valueOf(data[0].trim());
                String username = data[1].trim();
                String password = data[2].trim();
                USER_ROLE role = USER_ROLE.valueOf(data[3].trim());

                userList.add(new UserEntity(userId, username, password, role));
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
        return userList;
    }

    @Override
    public void write(List<UserEntity> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
                StringBuilder line = new StringBuilder();
                for (UserEntity u : list) {
                    line.append(u.getUserId()).append(",")
                            .append(u.getUsername()).append(",")
                            .append(u.getPassword()).append(",")
                            .append(u.getRole()).append("\n");

                }
                bw.write(line.toString());

        } catch (IOException e) {
            System.err.println("Error writing to users file: " + e.getMessage());
        }
    }
}