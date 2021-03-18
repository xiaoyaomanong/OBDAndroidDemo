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
     * data : {"total":6,"list":[{"rechargeSetMealSettingsId":1346338492057051138,"rechargeSetMeaName":"10次19.9","rechargeSetMeaType":1,"rechargeSetMeaNum":10,"rechargeSetMeaAmount":19.9,"rechargeSetMeaExplain":"测试","effectiveDays":10},{"rechargeSetMealSettingsId":1346422331801161730,"rechargeSetMeaName":"1次","rechargeSetMeaType":1,"rechargeSetMeaNum":1,"rechargeSetMeaAmount":7,"rechargeSetMeaExplain":"","effectiveDays":10},{"rechargeSetMealSettingsId":1348551694983557122,"rechargeSetMeaName":"包月","rechargeSetMeaType":2,"rechargeSetMeaNum":10,"rechargeSetMeaAmount":1,"rechargeSetMeaExplain":"包月","effectiveDays":30},{"rechargeSetMealSettingsId":1348551906632331266,"rechargeSetMeaName":"季度","rechargeSetMeaType":2,"rechargeSetMeaNum":0,"rechargeSetMeaAmount":90,"rechargeSetMeaExplain":"","effectiveDays":120},{"rechargeSetMealSettingsId":1348552074882641921,"rechargeSetMeaName":"半年","rechargeSetMeaType":2,"rechargeSetMeaNum":0,"rechargeSetMeaAmount":179,"rechargeSetMeaExplain":"","effectiveDays":180},{"rechargeSetMealSettingsId":1348552218990538753,"rechargeSetMeaName":"包年","rechargeSetMeaType":2,"rechargeSetMeaNum":0,"rechargeSetMeaAmount":360,"rechargeSetMeaExplain":"","effectiveDays":365}],"pageNum":1,"pageSize":6,"size":6,"startRow":0,"endRow":5,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
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

    public static class DataEntity {
        /**
         * total : 6
         * list : [{"rechargeSetMealSettingsId":1346338492057051138,"rechargeSetMeaName":"10次19.9","rechargeSetMeaType":1,"rechargeSetMeaNum":10,"rechargeSetMeaAmount":19.9,"rechargeSetMeaExplain":"测试","effectiveDays":10},{"rechargeSetMealSettingsId":1346422331801161730,"rechargeSetMeaName":"1次","rechargeSetMeaType":1,"rechargeSetMeaNum":1,"rechargeSetMeaAmount":7,"rechargeSetMeaExplain":"","effectiveDays":10},{"rechargeSetMealSettingsId":1348551694983557122,"rechargeSetMeaName":"包月","rechargeSetMeaType":2,"rechargeSetMeaNum":10,"rechargeSetMeaAmount":1,"rechargeSetMeaExplain":"包月","effectiveDays":30},{"rechargeSetMealSettingsId":1348551906632331266,"rechargeSetMeaName":"季度","rechargeSetMeaType":2,"rechargeSetMeaNum":0,"rechargeSetMeaAmount":90,"rechargeSetMeaExplain":"","effectiveDays":120},{"rechargeSetMealSettingsId":1348552074882641921,"rechargeSetMeaName":"半年","rechargeSetMeaType":2,"rechargeSetMeaNum":0,"rechargeSetMeaAmount":179,"rechargeSetMeaExplain":"","effectiveDays":180},{"rechargeSetMealSettingsId":1348552218990538753,"rechargeSetMeaName":"包年","rechargeSetMeaType":2,"rechargeSetMeaNum":0,"rechargeSetMeaAmount":360,"rechargeSetMeaExplain":"","effectiveDays":365}]
         * pageNum : 1
         * pageSize : 6
         * size : 6
         * startRow : 0
         * endRow : 5
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

        public static class ListEntity {
            /**
             * rechargeSetMealSettingsId : 1346338492057051138
             * rechargeSetMeaName : 10次19.9
             * rechargeSetMeaType : 1
             * rechargeSetMeaNum : 10
             * rechargeSetMeaAmount : 19.9
             * rechargeSetMeaExplain : 测试
             * effectiveDays : 10
             */

            private String rechargeSetMealSettingsId;
            private String rechargeSetMeaName;
            private int rechargeSetMeaType;
            private int rechargeSetMeaNum;
            private double rechargeSetMeaAmount;
            private String rechargeSetMeaExplain;
            private int effectiveDays;
            private boolean isChecked;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getRechargeSetMealSettingsId() {
                return rechargeSetMealSettingsId;
            }

            public void setRechargeSetMealSettingsId(String rechargeSetMealSettingsId) {
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

            public double getRechargeSetMeaAmount() {
                return rechargeSetMeaAmount;
            }

            public void setRechargeSetMeaAmount(double rechargeSetMeaAmount) {
                this.rechargeSetMeaAmount = rechargeSetMeaAmount;
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
}