package service;

import error.ItemNotFoundException;
import model.entity.CompanyProductionEntity;
import model.enums.COMPANY_PRODUCTION;
import repository.CompanyProductionRepository;

import java.util.List;

public class CompanyProductionService {
    private  final CompanyProductionRepository repo = new CompanyProductionRepository();
    private final List<CompanyProductionEntity> cpList;
    private static CompanyProductionService instance;

    private CompanyProductionService() {
        cpList = repo.read();
    }

    public static CompanyProductionService getInstance() {
        if (instance == null) {
            instance = new CompanyProductionService();
        }
        return instance;
    }

    public List<CompanyProductionEntity> showAll() {
        return cpList;
    }

    public CompanyProductionEntity findByProduction(COMPANY_PRODUCTION input) {
        for (CompanyProductionEntity e : cpList) {
            if (e.getProduction() == input) {
                return e;
            }
        }
        throw new ItemNotFoundException(" Production " + input + " does not exist");
    }


    public void updateByDiscount(CompanyProductionEntity oldT, Double discount) {
        oldT.setDiscount(discount);
        repo.write(cpList);
    }
}
