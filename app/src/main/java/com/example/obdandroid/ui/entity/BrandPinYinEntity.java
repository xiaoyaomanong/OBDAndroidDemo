package com.example.obdandroid.ui.entity;


import com.example.obdandroid.utils.PinYin;
import com.example.obdandroid.utils.PinyinUtil;

import java.io.Serializable;

public class BrandPinYinEntity implements Comparable<BrandPinYinEntity> , Serializable {
    private String pinyin;
    private long automobileBrandId;
    private String name;
    private String logo;
    private int brandType;

    public BrandPinYinEntity(String name, long automobileBrandId, String logo, int brandType) {
        this.name = name;
        //一开始就转化好拼音
        setPinyin(PinYin.getPinYin(name));
        this.automobileBrandId = automobileBrandId;
        this.logo = logo;
        this.brandType = brandType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAutomobileBrandId() {
        return automobileBrandId;
    }

    public void setAutomobileBrandId(long automobileBrandId) {
        this.automobileBrandId = automobileBrandId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getBrandType() {
        return brandType;
    }

    public void setBrandType(int brandType) {
        this.brandType = brandType;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public int compareTo(BrandPinYinEntity o) {
        return getPinyin().compareTo(o.getPinyin());
    }
}
