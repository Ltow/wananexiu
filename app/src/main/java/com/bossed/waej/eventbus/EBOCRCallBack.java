package com.bossed.waej.eventbus;

public class EBOCRCallBack {

    private String chepai;
    private String vin;
    private String address;
    private String engineNo;
    private boolean isDriving;
    private int ocrType;

    public EBOCRCallBack(String chepai, String vin, String address, String engineNo, boolean isDriving, int ocrType) {
        this.chepai = chepai;
        this.vin = vin;
        this.address = address;
        this.isDriving = isDriving;
        this.engineNo = engineNo;
        this.ocrType = ocrType;
    }

    public int getOcrType() {
        return ocrType;
    }

    public void setOcrType(int ocrType) {
        this.ocrType = ocrType;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public boolean isDriving() {
        return isDriving;
    }

    public void setDriving(boolean driving) {
        isDriving = driving;
    }

    public String getChepai() {
        return chepai;
    }

    public void setChepai(String chepai) {
        this.chepai = chepai;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
