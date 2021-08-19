package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/3/22
 * 描述：
 */
public class DefaultCodeEntity {
    private boolean success;
    private String code;
    private String message;
    private Data data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public static class Data {

        private String faultCodeId;
        private String faultCode;
        private String scopeOfApplication;
        private String chineseMeaning;
        private String englishMeaning;
        private String belongingSystem;
        private String causeOfFailure;
        private boolean display;

        public void setFaultCodeId(String faultCodeId) {
            this.faultCodeId = faultCodeId;
        }

        public String getFaultCodeId() {
            return faultCodeId;
        }

        public void setFaultCode(String faultCode) {
            this.faultCode = faultCode;
        }

        public String getFaultCode() {
            return faultCode;
        }

        public void setScopeOfApplication(String scopeOfApplication) {
            this.scopeOfApplication = scopeOfApplication;
        }

        public String getScopeOfApplication() {
            return scopeOfApplication;
        }

        public void setChineseMeaning(String chineseMeaning) {
            this.chineseMeaning = chineseMeaning;
        }

        public String getChineseMeaning() {
            return chineseMeaning;
        }

        public void setEnglishMeaning(String englishMeaning) {
            this.englishMeaning = englishMeaning;
        }

        public String getEnglishMeaning() {
            return englishMeaning;
        }

        public void setBelongingSystem(String belongingSystem) {
            this.belongingSystem = belongingSystem;
        }

        public String getBelongingSystem() {
            return belongingSystem;
        }

        public void setCauseOfFailure(String causeOfFailure) {
            this.causeOfFailure = causeOfFailure;
        }

        public String getCauseOfFailure() {
            return causeOfFailure;
        }

        public boolean isDisplay() {
            return display;
        }

        public void setDisplay(boolean display) {
            this.display = display;
        }
    }
}