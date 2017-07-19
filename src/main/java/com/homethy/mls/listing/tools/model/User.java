package com.homethy.mls.listing.tools.model;

/**
 * Created by jia on 17-7-6.
 * 登录所需的一些参数，可继续增加
 */
public class User {

    private String loginUrl;
    private String username;
    private String password;
    private String userAgent;
    private String userAgentPWD;
    private String retsVersion;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgentPWD() {
        return userAgentPWD;
    }

    public void setUserAgentPWD(String userAgentPWD) {
        this.userAgentPWD = userAgentPWD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getRetsVersion() {
        return retsVersion;
    }

    public void setRetsVersion(String retsVersion) {
        this.retsVersion = retsVersion;
    }

    @Override
    public String toString() {
        return "User{" +
                "loginUrl='" + loginUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", userAgentPWD='" + userAgentPWD + '\'' +
                ", retsVersion='" + retsVersion + '\'' +
                '}';
    }
}
