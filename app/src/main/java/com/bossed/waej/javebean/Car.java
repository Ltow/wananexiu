package com.bossed.waej.javebean;

public class Car {
    public Car(int type, String mileage, String policyName, String insureDate, String insure2Date, String dueDate, String carName, String remark, String vnCode, String cardNo) {
        this.type = type;
        this.mileage = mileage;
        this.policyName = policyName;
        this.insureDate = insureDate;
        this.insure2Date = insure2Date;
        this.dueDate = dueDate;
        this.carName = carName;
        this.remark = remark;
        this.vnCode = vnCode;
        this.cardNo = cardNo;
    }

    private String authTime;
    private Integer brandId;
    private String brandLogo;
    private String brandName;
    private Integer carId;
    private String carName;
    private String cardNo;
    private String createBy;
    private String createTime;
    private Integer customerId;
    private String customerName;
    private String customerPhone;
    private String displacement;
    private String driverImg;
    private String engineNo;
    private Integer id;
    private int isAuth;
    private int isDefault;
    private int isVip;
    private String maintenanceDate;
    private String maintenanceMileage;
    private String mileage;
    private String mjsid;
    private String oilNum;
    private String oilType;
    private String remark;
    private Integer seresId;
    private Integer shopId;
    private Integer tenantId;
    private String updateBy;
    private String updateTime;
    private String vnCode;
    private String year;
    private int type;
    private String jycarId;
    private String policyName;
    private String policy2Name;
    private String insureDate;
    private String insure2Date;
    private String dueDate;
    private int maintenanceMileageLast;
    private int maintenanceMileageNext;
    private int maintenanceMileageDay;
    private int maintenanceMileageCycle;
    private int advanceDue = 0;
    private int advanceInsure = 0;
    private int advanceMaintenance = 0;
    private int remindDue = 0;
    private int remindInsure = 0;
    private int remindMaintenance = 0;
    private int maintenanceType;

    public int getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(int maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public int getMaintenanceMileageCycle() {
        return maintenanceMileageCycle;
    }

    public void setMaintenanceMileageCycle(int maintenanceMileageCycle) {
        this.maintenanceMileageCycle = maintenanceMileageCycle;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicy2Name() {
        return policy2Name;
    }

    public void setPolicy2Name(String policy2Name) {
        this.policy2Name = policy2Name;
    }

    public String getInsureDate() {
        return insureDate;
    }

    public void setInsureDate(String insureDate) {
        this.insureDate = insureDate;
    }

    public String getInsure2Date() {
        return insure2Date;
    }

    public void setInsure2Date(String insure2Date) {
        this.insure2Date = insure2Date;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getMaintenanceMileageLast() {
        return maintenanceMileageLast;
    }

    public void setMaintenanceMileageLast(int maintenanceMileageLast) {
        this.maintenanceMileageLast = maintenanceMileageLast;
    }

    public int getMaintenanceMileageNext() {
        return maintenanceMileageNext;
    }

    public void setMaintenanceMileageNext(int maintenanceMileageNext) {
        this.maintenanceMileageNext = maintenanceMileageNext;
    }

    public int getMaintenanceMileageDay() {
        return maintenanceMileageDay;
    }

    public void setMaintenanceMileageDay(int maintenanceMileageDay) {
        this.maintenanceMileageDay = maintenanceMileageDay;
    }

    public int getAdvanceDue() {
        return advanceDue;
    }

    public void setAdvanceDue(int advanceDue) {
        this.advanceDue = advanceDue;
    }

    public int getAdvanceInsure() {
        return advanceInsure;
    }

    public void setAdvanceInsure(int advanceInsure) {
        this.advanceInsure = advanceInsure;
    }

    public int getAdvanceMaintenance() {
        return advanceMaintenance;
    }

    public void setAdvanceMaintenance(int advanceMaintenance) {
        this.advanceMaintenance = advanceMaintenance;
    }

    public int getRemindDue() {
        return remindDue;
    }

    public void setRemindDue(int remindDue) {
        this.remindDue = remindDue;
    }

    public int getRemindInsure() {
        return remindInsure;
    }

    public void setRemindInsure(int remindInsure) {
        this.remindInsure = remindInsure;
    }

    public int getRemindMaintenance() {
        return remindMaintenance;
    }

    public void setRemindMaintenance(int remindMaintenance) {
        this.remindMaintenance = remindMaintenance;
    }

    public String getJycarId() {
        return jycarId;
    }

    public void setJycarId(String jycarId) {
        this.jycarId = jycarId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }

    public String getAuthTime() {
        return authTime;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDriverImg(String driverImg) {
        this.driverImg = driverImg;
    }

    public String getDriverImg() {
        return driverImg;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceMileage(String maintenanceMileage) {
        this.maintenanceMileage = maintenanceMileage;
    }

    public String getMaintenanceMileage() {
        return maintenanceMileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMjsid(String mjsid) {
        this.mjsid = mjsid;
    }

    public String getMjsid() {
        return mjsid;
    }

    public void setOilNum(String oilNum) {
        this.oilNum = oilNum;
    }

    public String getOilNum() {
        return oilNum;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public String getOilType() {
        return oilType;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setSeresId(int seresId) {
        this.seresId = seresId;
    }

    public int getSeresId() {
        return seresId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

}
