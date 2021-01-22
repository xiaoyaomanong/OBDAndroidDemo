package com.example.obdandroid.ui.entity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/7 0007
 * 描述：
 */
public class ChargeMealEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : [{"rechargeSetMealSettingsId":1346338492057051138,"rechargeSetMeaName":"10次19.9","rechargeSetMeaType":1,"rechargeSetMeaNum":10,"rechargeSetMeaAmount":20,"bindingDeviceNum":1,"rechargeSetMeaExplain":"测试","effectiveDays":null},{"rechargeSetMealSettingsId":1346422331801161730,"rechargeSetMeaName":"1次","rechargeSetMeaType":1,"rechargeSetMeaNum":1,"rechargeSetMeaAmount":7,"bindingDeviceNum":1,"rechargeSetMeaExplain":"","effectiveDays":null}]
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
         * rechargeSetMealSettingsId : 1346338492057051138
         * rechargeSetMeaName : 10次19.9
         * rechargeSetMeaType : 1
         * rechargeSetMeaNum : 10
         * rechargeSetMeaAmount : 20
         * bindingDeviceNum : 1
         * rechargeSetMeaExplain : 测试
         * effectiveDays : null
         */

        private long rechargeSetMealSettingsId;
        private String rechargeSetMeaName;
        private int rechargeSetMeaType;
        private int rechargeSetMeaNum;
        private int rechargeSetMeaAmount;
        private int bindingDeviceNum;
        private String rechargeSetMeaExplain;
        private int effectiveDays;
        private boolean checked = false;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public long getRechargeSetMealSettingsId() {
            return rechargeSetMealSettingsId;
        }

        public void setRechargeSetMealSettingsId(long rechargeSetMealSettingsId) {
            this.rechargeSetMealSettingsId = rechargeSetMealSettingsId;
        }

        public String getRechargeSetMeaName() {
            return rechargeSetMeaName;
        }

        public void setRechargeSetMeaName(String rechargeSetMeaName) {
            this.rechargeSetMeaName = rechargeSetMeaName;
        }

        public int getRechargeSetMeaType() {
            return rechargeSetMeaType;
        }

        public void setRechargeSetMeaType(int rechargeSetMeaType) {
            this.rechargeSetMeaType = rechargeSetMeaType;
        }

        public int getRechargeSetMeaNum() {
            return rechargeSetMeaNum;
        }

        public void setRechargeSetMeaNum(int rechargeSetMeaNum) {
            this.rechargeSetMeaNum = rechargeSetMeaNum;
        }

        public int getRechargeSetMeaAmount() {
            return rechargeSetMeaAmount;
        }

        public void setRechargeSetMeaAmount(int rechargeSetMeaAmount) {
            this.rechargeSetMeaAmount = rechargeSetMeaAmount;
        }

        public int getBindingDeviceNum() {
            return bindingDeviceNum;
        }

        public void setBindingDeviceNum(int bindingDeviceNum) {
            this.bindingDeviceNum = bindingDeviceNum;
        }

        public String getRechargeSetMeaExplain() {
            return rechargeSetMeaExplain;
        }

        public void setRechargeSetMeaExplain(String rechargeSetMeaExplain) {
            this.rechargeSetMeaExplain = rechargeSetMeaExplain;
        }

        public int getEffectiveDays() {
            return effectiveDays;
        }

        public void setEffectiveDays(int effectiveDays) {
            this.effectiveDays = effectiveDays;
        }
    }
}