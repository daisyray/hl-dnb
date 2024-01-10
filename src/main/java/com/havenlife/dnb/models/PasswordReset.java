package com.havenlife.dnb.models;

import java.sql.Date;

public class PasswordReset {
    private int id;
    private Date createdDate;
    private int registerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getRegisterId() {
        return registerId;
    }

    public void setRegisterId(int registerId) {
        this.registerId = registerId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            PasswordReset otherObJ = (PasswordReset) obj;
            if (this.id == otherObJ.getId() &&
                this.registerId == otherObJ.getRegisterId() &&
                this.createdDate.equals(otherObJ.getCreatedDate())) {
                return true;
            }
        }
        return false;
    }
}
