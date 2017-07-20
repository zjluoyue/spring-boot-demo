package com.homethy.mls.listing.tools.model;

/**
 * Created by jia on 17-7-6.
 * 登录所需的一些参数，可继续增加
 */
public class User {

    private String id;
    private String login_url;
    private String username;
    private String password;
    private String user_agent;
    private String ua_pwd;
    private String rets_version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
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

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getUa_pwd() {
        return ua_pwd;
    }

    public void setUa_pwd(String ua_pwd) {
        this.ua_pwd = ua_pwd;
    }

    public String getRets_version() {
        return rets_version;
    }

    public void setRets_version(String rets_version) {
        this.rets_version = rets_version;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login_url='" + login_url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", user_agent='" + user_agent + '\'' +
                ", ua_pwd='" + ua_pwd + '\'' +
                ", rets_version='" + rets_version + '\'' +
                '}';
    }
}
