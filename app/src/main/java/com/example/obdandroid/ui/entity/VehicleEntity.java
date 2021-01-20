package com.example.obdandroid.ui.entity;

import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/1/9 0009
 * 描述：
 */
public class VehicleEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"total":1,"list":[{"vehicleId":1346369014047444993,"appUserId":1346351606125563906,"automobileBrandId":1347111731436064772,"automobileBrandName":"广汽本田","modelId":1347433223176720384,"modelName":"雅阁","fuelType":1347374483257823232,"fuelTypeName":"92号汽油","vehicleName":"广汽本田-雅阁","transmissionType":1347445641806221312,"transmissionTypeName":"自动","vehicleStatus":1,"vehicleStatusName":"未绑定","currentMileage":1500,"vehiclePurchaseDate":null,"yearManufacture":"2018-08-08","engineDisplacement":"2.0L","grossVehicleWeight":"1619","tankCapacity":"48","fualCost":"","licensePlateNumber":null,"engineNumber":null,"lastMaintenanceDate":null,"lastMaintenanceMileage":null,"maintenanceInterval":null,"maintenanceMileageInterval":null}],"pageNum":1,"pageSize":1,"size":1,"startRow":0,"endRow":0,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
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
         * total : 1
         * list : [{"vehicleId":1346369014047444993,"appUserId":1346351606125563906,"automobileBrandId":1347111731436064772,"automobileBrandName":"广汽本田","modelId":1347433223176720384,"modelName":"雅阁","fuelType":1347374483257823232,"fuelTypeName":"92号汽油","vehicleName":"广汽本田-雅阁","transmissionType":1347445641806221312,"transmissionTypeName":"自动","vehicleStatus":1,"vehicleStatusName":"未绑定","currentMileage":1500,"vehiclePurchaseDate":null,"yearManufacture":"2018-08-08","engineDisplacement":"2.0L","grossVehicleWeight":"1619","tankCapacity":"48","fualCost":"","licensePlateNumber":null,"engineNumber":null,"lastMaintenanceDate":null,"lastMaintenanceMileage":null,"maintenanceInterval":null,"maintenanceMileageInterval":null}]
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

        public static class ListEntity {
            /**
             * vehicleId : 1346369014047444993
             * appUserId : 1346351606125563906
             * automobileBrandId : 1347111731436064772
             * automobileBrandName : 广汽本田
             * modelId : 1347433223176720384
             * modelName : 雅阁
             * fuelType : 1347374483257823232
             * fuelTypeName : 92号汽油
             * vehicleName : 广汽本田-雅阁
             * transmissionType : 1347445641806221312
             * transmissionTypeName : 自动
             * vehicleStatus : 1
             * vehicleStatusName : 未绑定
             * currentMileage : 1500.0
             * vehiclePurchaseDate : null
             * yearManufacture : 2018-08-08
             * engineDisplacement : 2.0L
             * grossVehicleWeight : 1619
             * tankCapacity : 48
             * fualCost :
             * licensePlateNumber : null
             * engineNumber : null
             * lastMaintenanceDate : null
             * lastMaintenanceMileage : null
             * maintenanceInterval : null
             * maintenanceMileageInterval : null
             */

            private long vehicleId;
            private long appUserId;
            private long automobileBrandId;
            private String automobileBrandName;
            private long modelId;
            private String modelName;
            private long fuelType;
            private String logo;
            private String bluetoothDeviceNumber;
            private String fuelTypeName;
            private String vehicleName;
            private long transmissionType;
            private String transmissionTypeName;
            private int vehicleStatus;
            private String vehicleStatusName;
            private double currentMileage;
            private String vehiclePurchaseDate;
            private String yearManufacture;
            private String engineDisplacement;
            private String grossVehicleWeight;
            private String tankCapacity;
            private String fualCost;
            private String licensePlateNumber;
            private String engineNumber;
            private String lastMaintenanceDate;
            private String lastMaintenanceMileage;
            private long maintenanceInterval;
            private String maintenanceMileageInterval;
            private boolean isSelected; //自定义列表是否选中的标识

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public String getBluetoothDeviceNumber() {
                return bluetoothDeviceNumber;
            }

            public void setBluetoothDeviceNumber(String bluetoothDeviceNumber) {
                this.bluetoothDeviceNumber = bluetoothDeviceNumber;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public long getVehicleId() {
                return vehicleId;
            }

            public void setVehicleId(long vehicleId) {
                this.vehicleId = vehicleId;
            }

            public long getAppUserId() {
                return appUserId;
            }

            public void setAppUserId(long appUserId) {
                this.appUserId = appUserId;
            }

            public long getAutomobileBrandId() {
                return automobileBrandId;
            }

            public void setAutomobileBrandId(long automobileBrandId) {
                this.automobileBrandId = automobileBrandId;
            }

            public String getAutomobileBrandName() {
                return automobileBrandName;
            }

            public void setAutomobileBrandName(String automobileBrandName) {
                this.automobileBrandName = automobileBrandName;
            }

            public long getModelId() {
                return modelId;
            }

            public void setModelId(long modelId) {
                this.modelId = modelId;
            }

            public String getModelName() {
                return modelName;
            }

            public void setModelName(String modelName) {
                this.modelName = modelName;
            }

            public long getFuelType() {
                return fuelType;
            }

            public void setFuelType(long fuelType) {
                this.fuelType = fuelType;
            }

            public String getFuelTypeName() {
                return fuelTypeName;
            }

            public void setFuelTypeName(String fuelTypeName) {
                this.fuelTypeName = fuelTypeName;
            }

            public String getVehicleName() {
                return vehicleName;
            }

            public void setVehicleName(String vehicleName) {
                this.vehicleName = vehicleName;
            }

            public long getTransmissionType() {
                return transmissionType;
            }

            public void setTransmissionType(long transmissionType) {
                this.transmissionType = transmissionType;
            }

            public String getTransmissionTypeName() {
                return transmissionTypeName;
            }

            public void setTransmissionTypeName(String transmissionTypeName) {
                this.transmissionTypeName = transmissionTypeName;
            }

            public int getVehicleStatus() {
                return vehicleStatus;
            }

            public void setVehicleStatus(int vehicleStatus) {
                this.vehicleStatus = vehicleStatus;
            }

            public String getVehicleStatusName() {
                return vehicleStatusName;
            }

            public void setVehicleStatusName(String vehicleStatusName) {
                this.vehicleStatusName = vehicleStatusName;
            }

            public double getCurrentMileage() {
                return currentMileage;
            }

            public void setCurrentMileage(double currentMileage) {
                this.currentMileage = currentMileage;
            }

            public String getVehiclePurchaseDate() {
                return vehiclePurchaseDate;
            }

            public void setVehiclePurchaseDate(String vehiclePurchaseDate) {
                this.vehiclePurchaseDate = vehiclePurchaseDate;
            }

            public String getYearManufacture() {
                return yearManufacture;
            }

            public void setYearManufacture(String yearManufacture) {
                this.yearManufacture = yearManufacture;
            }

            public String getEngineDisplacement() {
                return engineDisplacement;
            }

            public void setEngineDisplacement(String engineDisplacement) {
                this.engineDisplacement = engineDisplacement;
            }

            public String getGrossVehicleWeight() {
                return grossVehicleWeight;
            }

            public void setGrossVehicleWeight(String grossVehicleWeight) {
                this.grossVehicleWeight = grossVehicleWeight;
            }

            public String getTankCapacity() {
                return tankCapacity;
            }

            public void setTankCapacity(String tankCapacity) {
                this.tankCapacity = tankCapacity;
            }

            public String getFualCost() {
                return fualCost;
            }

            public void setFualCost(String fualCost) {
                this.fualCost = fualCost;
            }

            public String getLicensePlateNumber() {
                return licensePlateNumber;
            }

            public void setLicensePlateNumber(String licensePlateNumber) {
                this.licensePlateNumber = licensePlateNumber;
            }

            public String getEngineNumber() {
                return engineNumber;
            }

            public void setEngineNumber(String engineNumber) {
                this.engineNumber = engineNumber;
            }

            public String getLastMaintenanceDate() {
                return lastMaintenanceDate;
            }

            public void setLastMaintenanceDate(String lastMaintenanceDate) {
                this.lastMaintenanceDate = lastMaintenanceDate;
            }

            public String getLastMaintenanceMileage() {
                return lastMaintenanceMileage;
            }

            public void setLastMaintenanceMileage(String lastMaintenanceMileage) {
                this.lastMaintenanceMileage = lastMaintenanceMileage;
            }

            public long getMaintenanceInterval() {
                return maintenanceInterval;
            }

            public void setMaintenanceInterval(long maintenanceInterval) {
                this.maintenanceInterval = maintenanceInterval;
            }

            public String getMaintenanceMileageInterval() {
                return maintenanceMileageInterval;
            }

            public void setMaintenanceMileageInterval(String maintenanceMileageInterval) {
                this.maintenanceMileageInterval = maintenanceMileageInterval;
            }
        }
    }
}