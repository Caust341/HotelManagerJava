package repository;

import model.entity.CustomerEntity;
import model.enums.CUSTOMER_TIER;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements IRepository<CustomerEntity>{
    private final static String filename = "database/customer.csv";

    @Override
    public List<CustomerEntity> read() {
        List<CustomerEntity> culist = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return culist;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Long custId = Long.valueOf(data[0].trim());
                Long compId = Long.valueOf(data[1].trim());
                String custName = data[2].trim();
                String contact = data[3].trim();
                CUSTOMER_TIER custTier = CUSTOMER_TIER.valueOf(data[4].trim());
                Integer count = Integer.valueOf(data[5].trim());
                CustomerEntity cu = new CustomerEntity(custId, compId, custName, contact,custTier, count);
                culist.add(cu);
            }
        } catch (IOException e) {
            System.err.println("Error reading customer file: " + e.getMessage());
        }
        return  culist;
    }

    @Override
    public void write(List<CustomerEntity> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder line = new StringBuilder();
            for (CustomerEntity cu : list) {
                line.append(cu.getCustID()).append(",")
                        .append(cu.getCompID()).append(",")
                        .append(cu.getCustName()).append(",")
                        .append(cu.getContact()).append(",")
                        .append(cu.getCustTier()).append(",")
                        .append(cu.getCount()).append("\n");
            }
            bw.write(line.toString());
        } catch (IOException e) {
            System.err.println("Error writing to company production file: " + e.getMessage());
        }
    }
}
