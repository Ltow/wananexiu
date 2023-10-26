package com.bossed.waej.javebean;

import java.util.List;

public class ItemBean {
    private String attrName;
    private int cateId;
    private String cateName;
    private double costPrice;
    private double grossProfitMoney;
    private int id;
    //    private List<InquiryList> inquiryList;
    private List<ItemDispatchBean> itemDispatchList;
    private double itemMoney;
    private String itemName;
    private double madeFee;
    private double madeFeeRate;
    private double madeMoney;
    private double madeRate;
    private double netProfitMoney;
    private double num;
    private String oem;
    private Integer orderId;
    private String orderSn;
    private String remark;
    private double serviceFee;
    private Integer shopId;
    private List<SupplyPriceBean> supplyPriceList;
    private Integer tenantId;
    private int type = 0;
    private double unitPrice;
    private boolean isSelect;
    private String shop4sPrice;
    private String shop4sServiceFee;
    private String shop4sNum;
    private boolean isShow4s = false;
    private boolean isShowOE = false;
    private String jyitemId;

    public boolean isShowOE() {
        return isShowOE;
    }

    public void setShowOE(boolean showOE) {
        isShowOE = showOE;
    }

    public String getShop4sNum() {
        return shop4sNum;
    }

    public void setShop4sNum(String shop4sNum) {
        this.shop4sNum = shop4sNum;
    }

    public String getShop4sPrice() {
        return shop4sPrice;
    }

    public void setShop4sPrice(String shop4sPrice) {
        this.shop4sPrice = shop4sPrice;
    }

    public String getShop4sServiceFee() {
        return shop4sServiceFee;
    }

    public void setShop4sServiceFee(String shop4sServiceFee) {
        this.shop4sServiceFee = shop4sServiceFee;
    }

    public String getJyitemId() {
        return jyitemId;
    }

    public void setJyitemId(String jyitemId) {
        this.jyitemId = jyitemId;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public boolean isShow4s() {
        return isShow4s;
    }

    public void setShow4s(boolean show4s) {
        isShow4s = show4s;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setGrossProfitMoney(double grossProfitMoney) {
        this.grossProfitMoney = grossProfitMoney;
    }

    public double getGrossProfitMoney() {
        return grossProfitMoney;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

//    public void setInquiryList(List<InquiryList> inquiryList) {
//        this.inquiryList = inquiryList;
//    }
//
//    public List<InquiryList> getInquiryList() {
//        return inquiryList;
//    }

    public void setItemDispatchList(List<ItemDispatchBean> itemDispatchList) {
        this.itemDispatchList = itemDispatchList;
    }

    public List<ItemDispatchBean> getItemDispatchList() {
        return itemDispatchList;
    }

    public void setItemMoney(double itemMoney) {
        this.itemMoney = itemMoney;
    }

    public double getItemMoney() {
        return itemMoney;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setMadeFee(double madeFee) {
        this.madeFee = madeFee;
    }

    public double getMadeFee() {
        return madeFee;
    }

    public void setMadeFeeRate(double madeFeeRate) {
        this.madeFeeRate = madeFeeRate;
    }

    public double getMadeFeeRate() {
        return madeFeeRate;
    }

    public void setMadeMoney(double madeMoney) {
        this.madeMoney = madeMoney;
    }

    public double getMadeMoney() {
        return madeMoney;
    }

    public void setMadeRate(int madeRate) {
        this.madeRate = madeRate;
    }

    public double getMadeRate() {
        return madeRate;
    }

    public void setNetProfitMoney(int netProfitMoney) {
        this.netProfitMoney = netProfitMoney;
    }

    public double getNetProfitMoney() {
        return netProfitMoney;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public double getNum() {
        return num;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getOem() {
        return oem;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setSupplyPriceList(List<SupplyPriceBean> supplyPriceList) {
        this.supplyPriceList = supplyPriceList;
    }

    public List<SupplyPriceBean> getSupplyPriceList() {
        return supplyPriceList;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }


}
