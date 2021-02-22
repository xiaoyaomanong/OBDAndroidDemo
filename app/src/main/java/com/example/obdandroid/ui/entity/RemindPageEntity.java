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
     * data : {"total":6,"list":[{"remindId":null,"appUserId":null,"remindType":1,"content":"APP更新通知","title":"APP更新通知","isRead":0},{"remindId":null,"appUserId":1348930017264144385,"remindType":2,"content":"机油保养，机油使用一段时间会变脏、粘度和耐热性会变差，一定程度后无法起到对发动机的保护左右","title":"全车检测自动生成体检报告","isRead":0},{"remindId":null,"appUserId":null,"remindType":1,"content":"阿萨德阿萨德","title":"啊实打实多","isRead":0},{"remindId":null,"appUserId":null,"remindType":1,"content":" 阿萨德阿萨德","title":"阿萨锁定asd","isRead":0},{"remindId":null,"appUserId":null,"remindType":1,"content":"啊实打实多","title":"阿萨锁定a","isRead":0},{"remindId":null,"appUserId":1348930017264144385,"remindType":2,"content":"{\"content\":\"通过检测,无故障码,您的车辆很健康!\",\"createTime\":\"2021-02-07\",\"details\":\"\"}","title":"车辆检测报告","isRead":0}],"pageNum":1,"pageSize":6,"size":6,"startRow":0,"endRow":5,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
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
         * list : [{"remindId":null,"appUserId":null,"remindType":1,"content":"APP更新通知","title":"APP更新通知","isRead":0},{"remindId":null,"appUserId":1348930017264144385,"remindType":2,"content":"机油保养，机油使用一段时间会变脏、粘度和耐热性会变差，一定程度后无法起到对发动机的保护左右","title":"全车检测自动生成体检报告","isRead":0},{"remindId":null,"appUserId":null,"remindType":1,"content":"阿萨德阿萨德","title":"啊实打实多","isRead":0},{"remindId":null,"appUserId":null,"remindType":1,"content":" 阿萨德阿萨德","title":"阿萨锁定asd","isRead":0},{"remindId":null,"appUserId":null,"remindType":1,"content":"啊实打实多","title":"阿萨锁定a","isRead":0},{"remindId":null,"appUserId":1348930017264144385,"remindType":2,"content":"{\"content\":\"通过检测,无故障码,您的车辆很健康!\",\"createTime\":\"2021-02-07\",\"details\":\"\"}","title":"车辆检测报告","isRead":0}]
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
             * remindId : null
             * appUserId : null
             * remindType : 1
             * content : APP更新通知
             * title : APP更新通知
             * isRead : 0
             */

            private Object remindId;
            private Object appUserId;
            private int remindType;
            private String content;
            private String title;
            private int isRead;

            public Object getRemindId() {
                return remindId;
            }

            public void setRemindId(Object remindId) {
                this.remindId = remindId;
            }

            public Object getAppUserId() {
                return appUserId;
            }

            public void setAppUserId(Object appUserId) {
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