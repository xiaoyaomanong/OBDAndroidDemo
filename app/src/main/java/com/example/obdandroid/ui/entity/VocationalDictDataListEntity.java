package com.example.obdandroid.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/11 0011
 * 描述：
 */
public class VocationalDictDataListEntity implements Serializable{

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : [{"vocationalDictDataId":1347373511274008600,"typeId":1347372304501444600,"value":"90号汽油","code":"90_gasoline","sort":1,"remark":null,"status":0},{"vocationalDictDataId":1347374483257823200,"typeId":1347372304501444600,"value":"92号汽油","code":"92_gasoline","sort":2,"remark":null,"status":0},{"vocationalDictDataId":1347374754528628700,"typeId":1347372304501444600,"value":"93号汽油","code":"93_gasoline","sort":3,"remark":null,"status":0},{"vocationalDictDataId":1347374875731431400,"typeId":1347372304501444600,"value":"95号汽油","code":"95_gasoline","sort":4,"remark":null,"status":0},{"vocationalDictDataId":1347375015959597000,"typeId":1347372304501444600,"value":"97号汽油","code":"97_gasoline","sort":5,"remark":null,"status":0},{"vocationalDictDataId":1347375165029355500,"typeId":1347372304501444600,"value":"98号汽油","code":"98_gasoline","sort":6,"remark":null,"status":0},{"vocationalDictDataId":1347375474220863500,"typeId":1347372304501444600,"value":"0号柴油","code":"0_diesel","sort":7,"remark":null,"status":0},{"vocationalDictDataId":1347444497876914200,"typeId":1347444300920787000,"value":"手动","code":"manual","sort":1,"remark":null,"status":0},{"vocationalDictDataId":1347445641806221300,"typeId":1347444300920787000,"value":"自动","code":"automatic","sort":2,"remark":null,"status":0},{"vocationalDictDataId":1347445885444952000,"typeId":1347444300920787000,"value":"半自动","code":"semi_automatic","sort":3,"remark":null,"status":0},{"vocationalDictDataId":1347445885444952000,"typeId":1347444300920787000,"value":"手自一体","code":"hand_in_hand","sort":4,"remark":null,"status":0},{"vocationalDictDataId":1347445885444952000,"typeId":1347444300920787000,"value":"无极变速","code":"stepless_speed_change","sort":5,"remark":null,"status":0},{"vocationalDictDataId":1347445885444952000,"typeId":1347444300920787000,"value":"无极/手动一体式","code":"stepless_manual_integrated","sort":6,"remark":null,"status":0}]
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

    public static class DataEntity implements Serializable {
        /**
         * vocationalDictDataId : 1347373511274008600
         * typeId : 1347372304501444600
         * value : 90号汽油
         * code : 90_gasoline
         * sort : 1
         * remark : null
         * status : 0
         */

        private long vocationalDictDataId;
        private long typeId;
        private String value;
        private String code;
        private int sort;
        private String remark;
        private int status;

        public long getVocationalDictDataId() {
            return vocationalDictDataId;
        }

        public void setVocationalDictDataId(long vocationalDictDataId) {
            this.vocationalDictDataId = vocationalDictDataId;
        }

        public long getTypeId() {
            return typeId;
        }

        public void setTypeId(long typeId) {
            this.typeId = typeId;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}