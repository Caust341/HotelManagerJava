package repository;

import model.entity.CompanyEntity;
import model.enums.COMPANY_PRODUCTION;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepository implements IRepository<CompanyEntity>{
    private static final String filename = "database/company.csv";

    @Override
    public List<CompanyEntity> read() {
        List<CompanyEntity> clist = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return clist;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Long compId = Long.valueOf(data[0].trim());
                String compName = data[1].trim();
                COMPANY_PRODUCTION production = COMPANY_PRODUCTION.valueOf(data[2].trim());
                Double spent = Double.valueOf(data[3].trim());
                CompanyEntity company = new CompanyEntity(compId, compName, production, spent);
                clist.add(company);
            }
        } catch (IOException e) {
            System.err.println("Error reading company file: " + e.getMessage());
        }
        return  clist;
    }

    @Override
    public void write(List<CompanyEntity> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                StringBuilder line = new StringBuilder();
                for (CompanyEntity c : list) {
                    line.append(c.getCompID()).append(",")
                            .append(c.getCompName()).append(",")
                            .append(c.getCompProd()).append(",")
                            .append((c.getTotalSpent())).append("\n");
                }
                bw.write(line.toString());
            } catch (IOException e) {
                System.err.println("Error writing to company file: " + e.getMessage());
            }
    }
}
