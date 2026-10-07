package model.entity;

import model.enums.COMPANY_PRODUCTION;

import java.util.Objects;

public class CompanyEntity {
    private Long compID;          // PK (Nullable prior to DB persistence / auto-generation)
    private String compName;
    private COMPANY_PRODUCTION compProd;
    private Double spent;

    public CompanyEntity() {
    }

    // Full Constructor (Existing records loaded from Database)
    public CompanyEntity(Long compID, String compName, COMPANY_PRODUCTION compProd, Double spent) {
        if (compID != null && compID < 0) {
            throw new IllegalArgumentException(" Company ID cannot be negative.");
        }
        if (compName == null || compName.trim().isEmpty()) {
            throw new IllegalArgumentException(" Company name cannot be null or empty.");
        }
        if (compProd == null) {
            throw new IllegalArgumentException(" Company production tier cannot be null.");
        }

        this.compID = compID;
        this.compName = compName;
        this.compProd = compProd;
        this.spent = spent;
    }



    public Long getCompID() {
        return compID;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        if (compName == null || compName.trim().isEmpty()) {
            throw new IllegalArgumentException(" Company name cannot be null or empty.");
        }
        this.compName = compName;
    }

    public COMPANY_PRODUCTION getCompProd() {
        return compProd;
    }

    public void setCompProd(COMPANY_PRODUCTION compProd) {
        if (compProd == null) {
            throw new IllegalArgumentException(" Company production tier cannot be null.");
        }
        this.compProd = compProd;
    }

    public Double getTotalSpent() {
        return spent;
    }

    public void setTotalSpent(Double spent) {
        this.spent = spent;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity company = (CompanyEntity) o;
        return Objects.equals(compName, company.compName) && compProd == company.compProd
                && Objects.equals(spent, company.spent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compName, compProd, spent);
    }
}