package com.example.obdandroid.ui.entity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/2/22 0022
 * 描述：
 */
public class RemindPageEntity {


    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"total":2,"list":[{"remindId":null,"appUserId":"1364419027981832194","remindType":2,"content":"{\"content\":\"通过检测,无故障码,您的车辆很健康!\",\"createTime\":\"2021-03-25\",\"details\":\"1374918713771560962\",\"platformType\":1}","title":"全车检测自动生成体检报告","isRead":0},{"remindId":null,"appUserId":"1364419027981832194","remindType":2,"content":"{\"content\":\"通过检测,无故障码,您的车辆很健康!\",\"createTime\":\"2021-03-25\",\"details\":\"1374919262860480513\",\"platformType\":1}","title":"全车检测自动生成体检报告","isRead":0}],"pageNum":1,"pageSize":10,"size":2,"startRow":1,"endRow":2,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
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
         * total : 2
         * list : [{"remindId":null,"appUserId":"1364419027981832194","remindType":2,"content":"{\"content\":\"通过检测,无故障码,您的车辆很健康!\",\"createTime\":\"2021-03-25\",\"details\":\"1374918713771560962\",\"platformType\":1}","title":"全车检测自动生成体检报告","isRead":0},{"remindId":null,"appUserId":"1364419027981832194","remindType":2,"content":"{\"content\":\"通过检测,无故障码,您的车辆很健康!\",\"createTime\":\"2021-03-25\",\"details\":\"1374919262860480513\",\"platformType\":1}","title":"全车检测自动生成体检报告","isRead":0}]
         * pageNum : 1
         * pageSize : 10
         * size : 2
         * startRow : 1
         * endRow : 2
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
        private List<ListEntity> list;
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
        private List<Integer> navigatepageNums;
        private int navigateFirstPage;
        private int navigateLastPage;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
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

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
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

        public static class ListEntity {
            /**
             * remindId : null
             * appUserId : 1364419027981832194
             * remindType : 2
             * content : {"content":"通过检测,无故障码,您的车辆很健康!","createTime":"2021-03-25","details":"1374918713771560962","platformType":1}
             * title : 全车检测自动生成体检报告
             * isRead : 0
             */

            private long remindId;
            private String appUserId;
            private int remindType;
            private String content;
            private String title;
            private int isRead;

            public long getRemindId() {
                return remindId;
            }

            public void setRemindId(long remindId) {
                this.remindId = remindId;
            }

            public String getAppUserId() {
                return appUserId;
            }

            public void setAppUserId(String appUserId) {
                this.appUserId = appUserId;
            }

            public int getRemindType() {
                return remindType;
            }

            public void setRemindType(int remindType) {
                this.remindType = remindType;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getIsRead() {
                return isRead;
            }

            public void setIsRead(int isRead) {
                this.isRead = isRead;
            }
        }
    }
}