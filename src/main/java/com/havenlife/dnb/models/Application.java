package com.havenlife.dnb.models;

import java.time.LocalDate;

public class Application {
    private Integer id;
    private String applicationId;
    private Integer userId;
    private Integer coverageAmount;
    private Integer coverageYear;
    private boolean isSmoker;
    private boolean isSubmitted;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String healthRate;
    private String profession;
    private boolean isCitizen;
    private Integer salary;
    private boolean hasLicense;
    private boolean hasCancer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(Integer coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public Integer getCoverageYear() {
        return coverageYear;
    }

    public void setCoverageYear(Integer coverageYear) {
        this.coverageYear = coverageYear;
    }

    public boolean isSmoker() {
        return isSmoker;
    }

    public void setSmoker(boolean smoker) {
        isSmoker = smoker;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getHealthRate() {
        return healthRate;
    }

    public void setHealthRate(String healthRate) {
        this.healthRate = healthRate;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public boolean isCitizen() {
        return isCitizen;
    }

    public void setCitizen(boolean citizen) {
        isCitizen = citizen;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public boolean isHasLicense() {
        return hasLicense;
    }

    public void setHasLicense(boolean hasLicense) {
        this.hasLicense = hasLicense;
    }

    public boolean isHasCancer() {
        return hasCancer;
    }

    public void setHasCancer(boolean hasCancer) {
        this.hasCancer = hasCancer;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", applicationId='" + applicationId + '\'' +
                ", userId=" + userId +
                ", coverageAmount=" + coverageAmount +
                ", coverageYear=" + coverageYear +
                ", isSmoker=" + isSmoker +
                ", isSubmitted=" + isSubmitted +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
