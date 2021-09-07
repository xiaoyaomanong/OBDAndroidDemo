package com.example.obdandroid.ui.entity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/7 0007
 * 描述：
 */
public class ChargeMealEntity {


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
            private String rechargeSetMealSettingsId;
            private String rechargeSetMeaName;
            private int rechargeSetMeaType;
            private int rechargeSetMeaNum;
            private double rechargeSetMeaAmount;
            private String rechargeSetMeaExplain;
            private int effectiveDays;
            private int commodityType;
            private long pid;
            private SubsidiaryEntity subsidiary;
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

            public int getCommodityType() {
                return commodityType;
            }

            public void setCommodityType(int commodityType) {
                this.commodityType = commodityType;
            }

            public long getPid() {
                return pid;
            }

            public void setPid(long pid) {
                this.pid = pid;
            }

            public SubsidiaryEntity getSubsidiary() {
                return subsidiary;
            }

            public void setSubsidiary(SubsidiaryEntity subsidiary) {
                this.subsidiary = subsidiary;
            }

            public static class SubsidiaryEntity {
                private String rechargeSetMealSettingsId;
                private String rechargeSetMeaName;
                private int rechargeSetMeaType;
                private int rechargeSetMeaNum;
                private double rechargeSetMeaAmount;
                private String rechargeSetMeaExplain;
                private int effectiveDays;
                private int commodityType;
                private int pid;

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

                public int getCommodityType() {
                    return commodityType;
                }

                public void setCommodityType(int commodityType) {
                    this.commodityType = commodityType;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }
            }
        }
    }
}