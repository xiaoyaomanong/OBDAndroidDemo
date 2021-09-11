package com.example.obdandroid.ui.entity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/22 0022
 * 描述：
 */
public class RechargeRecordEntity {


    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"total":14,"list":[{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:34:00.000+0000","rechargeStatusName":"已取消","rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:33:56.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:33:28.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:32:17.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:31:49.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:30:13.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:29:07.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:27:14.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:23:43.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:18:06.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"}],"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"pages":2,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2],"navigateFirstPage":1,"navigateLastPage":2}
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
         * total : 14
         * list : [{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:34:00.000+0000","rechargeStatusName":"已取消","rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:33:56.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:33:28.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:32:17.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:31:49.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:30:13.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:29:07.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:27:14.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:23:43.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"},{"rechargeSetMeaName":"10次19.9","rechargeTime":"2021-03-19T09:18:06.000+0000","rechargeStatusName":null,"rechargetAmount":0.01,"paymentChannelsName":"微信"}]
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * pages : 2
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2]
         * navigateFirstPage : 1
         * navigateLastPage : 2
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
             * rechargeSetMeaName : 10次19.9
             * rechargeTime : 2021-03-19T09:34:00.000+0000
             * rechargeStatusName : 已取消
             * rechargetAmount : 0.01
             * paymentChannelsName : 微信
             */

            private String rechargeSetMeaName;
            private String rechargeTime;
            private String rechargeStatusName;
            private double rechargetAmount;
            private String paymentChannelsName;
            private String address ;// 地址
            private String  contacts; // 联系人
            private String courierNumber; // 快递单号
            private String expressName; // 快递名称
            private String telephone ;// 电话
            private int commodityType;//商品类型1 实物 2 虚拟

            public int getCommodityType() {
                return commodityType;
            }

            public void setCommodityType(int commodityType) {
                this.commodityType = commodityType;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getContacts() {
                return contacts;
            }

            public void setContacts(String contacts) {
                this.contacts = contacts;
            }

            public String getCourierNumber() {
                return courierNumber;
            }

            public void setCourierNumber(String courierNumber) {
                this.courierNumber = courierNumber;
            }

            public String getExpressName() {
                return expressName;
            }

            public void setExpressName(String expressName) {
                this.expressName = expressName;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getRechargeSetMeaName() {
                return rechargeSetMeaName;
            }

            public void setRechargeSetMeaName(String rechargeSetMeaName) {
                this.rechargeSetMeaName = rechargeSetMeaName;
            }

            public String getRechargeTime() {
                return rechargeTime;
            }

            public void setRechargeTime(String rechargeTime) {
                this.rechargeTime = rechargeTime;
            }

            public String getRechargeStatusName() {
                return rechargeStatusName;
            }

            public void setRechargeStatusName(String rechargeStatusName) {
                this.rechargeStatusName = rechargeStatusName;
            }

            public double getRechargetAmount() {
                return rechargetAmount;
            }

            public void setRechargetAmount(double rechargetAmount) {
                this.rechargetAmount = rechargetAmount;
            }

            public String getPaymentChannelsName() {
                return paymentChannelsName;
            }

            public void setPaymentChannelsName(String paymentChannelsName) {
                this.paymentChannelsName = paymentChannelsName;
            }
        }
    }
}