package repository;

import model.entity.BookingEntity;
import model.enums.BOOKING_CHANNEL;
import model.enums.BOOKING_STATUS;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository implements IRepository<BookingEntity>{
    private static final String filename = "database/booking.csv";
    @Override
    public List<BookingEntity> read() {
        List<BookingEntity> blist = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return blist;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] data = line.split(",");
                Long bookingID = Long.valueOf(data[0].trim());
                Integer roomID = Integer.valueOf(data[1].trim());
                Long userID = Long.valueOf(data[2].trim());
                Double pricePerNight = Double.valueOf(data[3].trim());
                LocalDate checkin = LocalDate.parse(data[4].trim());
                LocalDate checkout = LocalDate.parse(data[5].trim());
                BOOKING_CHANNEL channel = BOOKING_CHANNEL.valueOf(data[6].trim());
                BOOKING_STATUS status = BOOKING_STATUS.valueOf(data[7].trim());
                BookingEntity b = new BookingEntity(bookingID, roomID, userID,
                        pricePerNight, checkin, checkout, channel, status);
                blist.add(b);
            }

        } catch (IOException e) {
            System.err.println("Error reading booking file: " + e.getMessage());
        }
        return blist;
    }

    @Override
    public void write(List<BookingEntity> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder bl = new StringBuilder();

            for (BookingEntity b : list) {
                bl.append(b.getBookingID()).append(",")
                        .append(b.getRoomID()).append(",")
                        .append(b.getUserId()).append(",")
                        .append(b.getPricePerNight()).append(",")
                        .append(b.getCheckIn()).append(",")
                        .append(b.getCheckOut()).append(",")
                        .append(b.getBookingChannel()).append(",")
                        .append(b.getBookingStatus()).append("\n");
            }
            bw.write(bl.toString());
        } catch (IOException e) {
            System.err.println("Error writing to booking file: " + e.getMessage());
        }
    }
}
