package com.example.obdandroid.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/11 0011
 * 描述：
 */
public class CarModelEntity implements Serializable {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : [{"carModelId":1347433223176720400,"automobileBrandId":1347111731436064800,"name":"雅阁"},{"carModelId":1347474420536578000,"automobileBrandId":1347111731436064800,"name":"皓影"}]
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

    public static class DataEntity implements Serializable{
        /**
         * carModelId : 1347433223176720400
         * automobileBrandId : 1347111731436064800
         * name : 雅阁
         */

        private long carModelId;
        private long automobileBrandId;
        private String name;

        public long getCarModelId() {
            return carModelId;
        }

        public void setCarModelId(long carModelId) {
            this.carModelId = carModelId;
        }

        public long getAutomobileBrandId() {
            return automobileBrandId;
        }

        public void setAutomobileBrandId(long automobileBrandId) {
            this.automobileBrandId = automobileBrandId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}