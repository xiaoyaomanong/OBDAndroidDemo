package com.example.obdandroid.ui.entity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/26 0026
 * 描述：
 */
public class FaultCodeDetailsEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : [{"faultCodeId":null,"faultCode":"B0123","scopeOfApplication":"该OBD故障码适用于所有汽车制造商","chineseMeaning":"一号安全带开关电路电压过高","englishMeaning":null,"belongingSystem":null,"causeOfFailure":null}]
     */

    private boolean success;
    private String code;
    private String message;
    private List<DataEntity> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * faultCodeId : null
         * faultCode : B0123
         * scopeOfApplication : 该OBD故障码适用于所有汽车制造商
         * chineseMeaning : 一号安全带开关电路电压过高
         * englishMeaning : null
         * belongingSystem : null
         * causeOfFailure : null
         */

        private Object faultCodeId;
        private String faultCode;
        private String scopeOfApplication;
        private String chineseMeaning;
        private String englishMeaning;
        private String belongingSystem;
        private String causeOfFailure;

        public Object getFaultCodeId() {
            return faultCodeId;
        }

        public void setFaultCodeId(Object faultCodeId) {
            this.faultCodeId = faultCodeId;
        }

        public String getFaultCode() {
            return faultCode;
        }

        public void setFaultCode(String faultCode) {
            this.faultCode = faultCode;
        }

        public String getScopeOfApplication() {
            return scopeOfApplication;
        }

        public void setScopeOfApplication(String scopeOfApplication) {
            this.scopeOfApplication = scopeOfApplication;
        }

        public String getChineseMeaning() {
            return chineseMeaning;
        }

        public void setChineseMeaning(String chineseMeaning) {
            this.chineseMeaning = chineseMeaning;
        }

        public String getEnglishMeaning() {
            return englishMeaning;
        }

        public void setEnglishMeaning(String englishMeaning) {
            this.englishMeaning = englishMeaning;
        }

        public String getBelongingSystem() {
            return belongingSystem;
        }

        public void setBelongingSystem(String belongingSystem) {
            this.belongingSystem = belongingSystem;
        }

        public String getCauseOfFailure() {
            return causeOfFailure;
        }

        public void setCauseOfFailure(String causeOfFailure) {
            this.causeOfFailure = causeOfFailure;
        }
    }
}