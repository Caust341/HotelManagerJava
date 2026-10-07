package repository;

import model.entity.CompanyProductionEntity;
import model.enums.COMPANY_PRODUCTION;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyProductionRepository implements IRepository<CompanyProductionEntity>{
    private static final String filename = "database/companyproduction.csv";
    @Override
    public List<CompanyProductionEntity> read() {
        List<CompanyProductionEntity> cplist = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            return cplist;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                COMPANY_PRODUCTION production = COMPANY_PRODUCTION.valueOf(data[0].trim());
                Double discount = Double.valueOf(data[1].trim());
                CompanyProductionEntity comp_prod = new CompanyProductionEntity(production, discount);
                cplist.add(comp_prod);
            }
        } catch (IOException e) {
            System.err.println("Error reading company production file: " + e.getMessage());
        }
        return  cplist;
    }

    @Override
    public void write(List<CompanyProductionEntity> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder line = new StringBuilder();
            for (CompanyProductionEntity cp : list) {
                line.append(cp.getProduction()).append(",")
                        .append(cp.getDiscount()).append("\n");
            }
            bw.write(line.toString());
        } catch (IOException e) {
            System.err.println("Error writing to company production file: " + e.getMessage());
        }
    }
}
