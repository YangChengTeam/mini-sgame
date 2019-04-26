package com.yc.sgame.model.bean;

public class InitInfo {
    private UserInfo userInfo;

    private ProductInfo init_img;

    public ProductInfo getInit_img() {
        return init_img;
    }

    public void setInit_img(ProductInfo init_img) {
        this.init_img = init_img;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    private String notice;

    private String app_status;

    public String getApp_status() {
        return app_status;
    }

    public void setApp_status(String app_status) {
        this.app_status = app_status;
    }

    private int app_id;

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    private String loan_status;

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
    }
}
