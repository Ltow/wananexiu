package com.bossed.waej.javebean;

import java.util.List;

public class ChePaiBack {
    /**
     * PlateNum : 1
     * recogInfo : {"requestId":"123","beginTime":"2020-05-20 11:24:36","endTime":"2020-05-20 11:24:38","time":1950,"responseId":"2111134"}
     * totalMs : 3062
     * ErrorCode : 0
     * Plate : [{"车牌颜色":"蓝色","车牌号":"渝B8R561"}]
     */
    private int PlateNum;
    private RecogInfoEntity recogInfo;
    private int totalMs;
    private String ErrorCode;
    private List<PlateEntity> Plate;

    public void setPlateNum(int PlateNum) {
        this.PlateNum = PlateNum;
    }

    public void setRecogInfo(RecogInfoEntity recogInfo) {
        this.recogInfo = recogInfo;
    }

    public void setTotalMs(int totalMs) {
        this.totalMs = totalMs;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public void setPlate(List<PlateEntity> Plate) {
        this.Plate = Plate;
    }

    public int getPlateNum() {
        return PlateNum;
    }

    public RecogInfoEntity getRecogInfo() {
        return recogInfo;
    }

    public int getTotalMs() {
        return totalMs;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public List<PlateEntity> getPlate() {
        return Plate;
    }

    public static class RecogInfoEntity {
        /**
         * requestId : 123
         * beginTime : 2020-05-20 11:24:36
         * endTime : 2020-05-20 11:24:38
         * time : 1950
         * responseId : 2111134
         */
        private String requestId;
        private String beginTime;
        private String endTime;
        private int time;
        private String responseId;

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void setResponseId(String responseId) {
            this.responseId = responseId;
        }

        public String getRequestId() {
            return requestId;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public int getTime() {
            return time;
        }

        public String getResponseId() {
            return responseId;
        }
    }

    public static class PlateEntity {
        /**
         * 车牌颜色 : 蓝色
         * 车牌号 : 渝B8R561
         */
        private String 车牌颜色;
        private String 车牌号;

        public void set车牌颜色(String 车牌颜色) {
            this.车牌颜色 = 车牌颜色;
        }

        public void set车牌号(String 车牌号) {
            this.车牌号 = 车牌号;
        }

        public String get车牌颜色() {
            return 车牌颜色;
        }

        public String get车牌号() {
            return 车牌号;
        }
    }
}
