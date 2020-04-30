package com.p3.archon.vsamextrator.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class CopybookBean {

    private Map<String, Map<String, ColumnBean>> copybookDetails = new LinkedHashMap<>();
    private Map<String, ColumnBean> columnDetails = new LinkedHashMap<>();

    public Map<String, Map<String, ColumnBean>> getCopybookDetails() {
        return copybookDetails;
    }

    public void setCopybookDetails(Map<String, Map<String, ColumnBean>> copybookDetails) {
        this.copybookDetails = copybookDetails;
    }

    public Map<String, ColumnBean> getColumnDetails() {
        return columnDetails;
    }

    public void setColumnDetails(Map<String, ColumnBean> columnDetails) {
        this.columnDetails = columnDetails;
    }
}
