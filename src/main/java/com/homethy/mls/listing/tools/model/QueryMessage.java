package com.homethy.mls.listing.tools.model;


/**
 * Created by jia on 17-7-6.
 * 查询的参数，还有些之后可以拓展
 */
public class QueryMessage {
    private String query;
    private String resource;
    private String sclass;
    private int size;

    public QueryMessage() {

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    @Override
    public String toString() {
        return "query='" + query + '\'' +
                ", resource='" + resource + '\'' +
                ", sclass='" + sclass + '\'' +
                ", 本次查询到" + size +"条结果";
    }
}
