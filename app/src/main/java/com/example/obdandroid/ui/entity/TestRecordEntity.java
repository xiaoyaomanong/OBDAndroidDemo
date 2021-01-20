package com.example.obdandroid.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/20 0020
 * 描述：
 */
public class TestRecordEntity implements Serializable{

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"total":1,"list":[{"testRecordId":1351724591225245697,"vehicleId":null,"detectionTime":"2021-01-20 10:53:51","testData":"[{\"name\": \"车速\", \"value\": \"6km/h\"}]","appUserId":1349165978564694017}],"pageNum":1,"pageSize":1,"size":1,"startRow":0,"endRow":0,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
     */

    private boolean success;
    private String code;
    private String message;
    private DataEntity data;

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

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity implements Serializable{
        /**
         * total : 1
         * list : [{"testRecordId":1351724591225245697,"vehicleId":null,"detectionTime":"2021-01-20 10:53:51","testData":"[{\"name\": \"车速\", \"value\": \"6km/h\"}]","appUserId":1349165978564694017}]
         * pageNum : 1
         * pageSize : 1
         * size : 1
         * startRow : 0
         * endRow : 0
         * pages : 1
         * prePage : 0
         * nextPage : 0
         * isFirstPage : true
         * isLastPage : true
         * hasPreviousPage : false
         * hasNextPage : false
         * navigatePages : 8
         * navigatepageNums : [1]
         * navigateFirstPage : 1
         * navigateLastPage : 1
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int pages;
        private int prePage;
        private int nextPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private int navigateFirstPage;
        private int navigateLastPage;
        private List<ListEntity> list;
        private List<Integer> navigatepageNums;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNavigateFirstPage() {
            return navigateFirstPage;
        }

        public void setNavigateFirstPage(int navigateFirstPage) {
            this.navigateFirstPage = navigateFirstPage;
        }

        public int getNavigateLastPage() {
            return navigateLastPage;
        }

        public void setNavigateLastPage(int navigateLastPage) {
            this.navigateLastPage = navigateLastPage;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListEntity implements Serializable{
            /**
             * testRecordId : 1351724591225245697
             * vehicleId : null
             * detectionTime : 2021-01-20 10:53:51
             * testData : [{"name": "车速", "value": "6km/h"}]
             * appUserId : 1349165978564694017
             */

            private long testRecordId;
            private Object vehicleId;
            private String detectionTime;
            private String testData;
            private long appUserId;

            public long getTestRecordId() {
                return testRecordId;
            }

            public void setTestRecordId(long testRecordId) {
                this.testRecordId = testRecordId;
            }

            public Object getVehicleId() {
                return vehicleId;
            }

            public void setVehicleId(Object vehicleId) {
                this.vehicleId = vehicleId;
            }

            public String getDetectionTime() {
                return detectionTime;
            }

            public void setDetectionTime(String detectionTime) {
                this.detectionTime = detectionTime;
            }

            public String getTestData() {
                return testData;
            }

            public void setTestData(String testData) {
                this.testData = testData;
            }

            public long getAppUserId() {
                return appUserId;
            }

            public void setAppUserId(long appUserId) {
                this.appUserId = appUserId;
            }
        }
    }
}