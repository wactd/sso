package com.dongly.constant;

/**
 * Created by tiger on 2017/7/19.
 */
public enum  EnumSSO {

    SSO_TOKEN("SSO_TOKEN", "Cookie名称"),
    SSO_REDIRECT_URL("http://sso.dongly.com:8080?redirectUrl=", "重定向登录界面"),


    ;

    private String desc;
    private String value;

    EnumSSO(String value, String desc) {
        this.desc = desc;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
