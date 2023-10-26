package com.bossed.waej.javebean;

public class VinBean {
    /**
     * recogInfo : {"requestId":null,"beginTime":"2020-09-24 15:59:05","endTime":"2020-09-24 15:59:06","time":1123,"responseId":"3022256"}
     * VIN : LFWSRXNH6AAD38754
     * ErrorCode : 0
     */
    private RecogInfoEntity recogInfo;
    private String VIN;
    private String ErrorCode;

    public void setRecogInfo(RecogInfoEntity recogInfo) {
        this.recogInfo = recogInfo;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public RecogInfoEntity getRecogInfo() {
        return recogInfo;
    }

    public String getVIN() {
        return VIN;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public static class RecogInfoEntity {
        /**
         * requestId : null
         * beginTime : 2020-09-24 15:59:05
         * endTime : 2020-09-24 15:59:06
         * time : 1123
         * responseId : 3022256
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
}
