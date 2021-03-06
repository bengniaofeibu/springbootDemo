package com.applet.entity.UserInfo;

import com.applet.entity.BaseEntity.BaseResponseEntity;

public class UserInfoResponse extends BaseResponseEntity{

    private String adminId;

    private String phone;

    private Integer status;

    private Integer borrowBicycle;

    private Integer borrowBicycleDate;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBorrowBicycle() {
        return borrowBicycle;
    }

    public void setBorrowBicycle(Integer borrowBicycle) {
        this.borrowBicycle = borrowBicycle;
    }

    public Integer getBorrowBicycleDate() {
        return borrowBicycleDate;
    }

    public void setBorrowBicycleDate(Integer borrowBicycleDate) {
        this.borrowBicycleDate = borrowBicycleDate;
    }
}
