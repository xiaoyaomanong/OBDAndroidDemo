package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/1/5 0005
 * 描述：
 */
public class RegisterParam {

    /**
     * headPortrait : string
     * mobile : string
     * nickname : string
     * password : string
     * registrationPlatform : string
     * verificationCode : string
     */

    private String headPortrait;
    private String mobile;
    private String nickname;
    private String password;
    private String registrationPlatform;
    private String verificationCode;

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationPlatform() {
        return registrationPlatform;
    }

    public void setRegistrationPlatform(String registrationPlatform) {
        this.registrationPlatform = registrationPlatform;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}