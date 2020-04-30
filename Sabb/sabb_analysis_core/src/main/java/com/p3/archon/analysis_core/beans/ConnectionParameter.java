package com.p3.archon.analysis_core.beans;


import com.p3.archon.analysis_core.enums.Type;

import java.util.List;


public class ConnectionParameter {

    private String appName;
    private Type type;
    private String hostName;
    private Integer port;
    private String dbName;
    private String schema;
    private String userName;
    private String password;
    //    private String metadataFileLocation;
    private String ageByDate;
    private String outputLocation;
    private String categories;

    public ConnectionParameter() {

    }

    public ConnectionParameter(String appName, Type type, String hostName, Integer port, String dbName, List<String> schema, String userName, String password, String ageByDate, String outputLocation, String categories) {
        this.appName = appName;
        this.type = type;
        this.hostName = hostName;
        this.port = port;
        this.dbName = dbName;

        this.userName = userName;
        this.password = password;
        this.ageByDate = ageByDate;
        this.outputLocation = outputLocation;
        this.categories = categories;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAgeByDate() {
        return ageByDate;
    }

    public void setAgeByDate(String ageByDate) {
        this.ageByDate = ageByDate;
    }

    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
