package model.entity;

import model.enums.COMPANY_PRODUCTION;

public class CompanyProductionEntity {
    private final COMPANY_PRODUCTION production; // Primary Key
    private Double discount;

    public CompanyProductionEntity(COMPANY_PRODUCTION production, Double discount) {
        if (production == null) {
            throw new IllegalArgumentException(" Company production tier cannot be null.");
        }
        this.production = production;
        setDiscount(discount);
    }


    public COMPANY_PRODUCTION getProduction() {
        return production;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        if (discount == null || discount < 0.0 || discount > 1.0) {
            throw new IllegalArgumentException(" Discount cannot be null, negative, or greater than 1.0 (100%).");
        }
        this.discount = discount;
    }
}
