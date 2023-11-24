package com.havenlife.dnb.models;

import java.security.PrivateKey;
import java.time.LocalDate;

public class Policy {
    private Integer id;
    private LocalDate createdDate;
    private LocalDate updatedDated;
    private String policyNumber;
    private Integer applicationId;
    private Double premiumAmount;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDated() {
        return updatedDated;
    }

    public void setUpdatedDated(LocalDate updatedDated) {
        this.updatedDated = updatedDated;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(Double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
