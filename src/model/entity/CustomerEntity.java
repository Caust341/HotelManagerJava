package model.entity;

import model.enums.CUSTOMER_TIER;

import java.util.Objects;

public class CustomerEntity {
    private Long custID;        // PK (Nullable prior to DB persistence / auto-generation)
    private Long compID;        // FK (Nullable for individual guests without corporate affiliation)
    private String custName;
    private String contact;
    private CUSTOMER_TIER custTier;
    private Integer count;

    public CustomerEntity() {
    }

    // Full Constructor (Existing records loaded from Database)
    public CustomerEntity(Long custID, Long compID, String custName, String contact,CUSTOMER_TIER custTier, Integer count) {
        if (custID != null && custID < 0) {
            throw new IllegalArgumentException(" Customer ID cannot be negative.");
        }
        if (compID != null && compID < 0) {
            throw new IllegalArgumentException(" Company ID cannot be negative.");
        }
        if (custName == null || custName.trim().isEmpty()) {
            throw new IllegalArgumentException(" Customer name cannot be null or empty.");
        }
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer contact cannot be null or empty");
        }
        if (custTier == null) {
            throw new IllegalArgumentException("Customer tier cannot be null.");
        }

        this.custID = custID;
        this.compID = compID;
        this.custName = custName;
        this.contact = contact;
        this.custTier = custTier;
        this.count = count;
    }



    public Long getCustID() {
        return custID;
    }



    public Long getCompID() {
        return compID;
    }

    public void setCompID(Long compID) {
        if (compID != null && compID < 0) {
            throw new IllegalArgumentException("Company ID cannot be negative.");
        }
        this.compID = compID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        if (custName == null || custName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        this.custName = custName;
    }

    public CUSTOMER_TIER getCustTier() {
        return custTier;
    }

    public void setCustTier(CUSTOMER_TIER custTier) {
        if (custTier == null) {
            throw new IllegalArgumentException("Customer tier cannot be null.");
        }
        this.custTier = custTier;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        if (contact == null || contact.isEmpty()) {
            this.contact = contact;
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity entity = (CustomerEntity) o;
        return Objects.equals(compID, entity.compID) && Objects.equals(custName, entity.custName) && custTier == entity.custTier && Objects.equals(count, entity.count) &&
                Objects.equals(contact, entity.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compID, custName, contact,custTier, count);
    }
}