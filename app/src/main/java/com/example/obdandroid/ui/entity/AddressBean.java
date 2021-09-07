package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/9/2 0002
 * 描述：
 */
public class AddressBean {
    private String address;
    private String houseNumber;
    private String contractName;
    private String phone;

    public AddressBean(String address, String houseNumber, String contractName, String phone) {
        this.address = address;
        this.houseNumber = houseNumber;
        this.contractName = contractName;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}