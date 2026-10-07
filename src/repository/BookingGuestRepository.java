package repository;

import model.entity.BookingGuestEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookingGuestRepository implements IRepository<BookingGuestEntity>{

    private static final String filename = "database/bookingguest.csv";

    @Override
    public List<BookingGuestEntity> read() {
        List<BookingGuestEntity> bglist = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return bglist;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Long custId = Long.valueOf(data[0].trim());
                Long bookingId = Long.valueOf(data[1].trim());
                BookingGuestEntity bg = new BookingGuestEntity(custId, bookingId);
                bglist.add(bg);
            }

        } catch (IOException e) {
            System.err.println("Error reading booking guest file: " + e.getMessage());
        }
        return bglist;
    }

        @Override
    public void write(List<BookingGuestEntity> list) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder bl = new StringBuilder();

            for (BookingGuestEntity bg : list) {
                bl.append(bg.custID()).append(",")
                        .append(bg.bookingID()).append("\n");

            }
            bw.write(bl.toString());
        } catch (IOException e) {
            System.err.println("Error writing to booking file: " + e.getMessage());
        }

    }
}
