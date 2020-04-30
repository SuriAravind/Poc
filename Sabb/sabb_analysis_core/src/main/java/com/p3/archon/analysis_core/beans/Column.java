package com.p3.archon.analysis_core.beans;

public class Column {
    public String colName;
    public String colType;
    public boolean probablePrimary;

    public Column() {

    }

    public Column(String colName, String colType, boolean probablePrimary) {
        this.colName = colName;
        this.colType = colType;
        this.probablePrimary = probablePrimary;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public boolean isProbablePrimary() {
        return probablePrimary;
    }

    public void setProbablePrimary(boolean probablePrimary) {
        this.probablePrimary = probablePrimary;
    }
}
