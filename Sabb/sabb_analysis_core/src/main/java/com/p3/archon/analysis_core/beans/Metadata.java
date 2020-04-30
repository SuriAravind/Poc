package com.p3.archon.analysis_core.beans;

public class Metadata {
    public String tableName;
    public Column column;
    public int distinctRowCount;
    public int totalRowCount;
    public String schemaName;
    public String dbName;
    public int recNum;
    public boolean probablePrimary;
    public boolean isDateColumn;

    public Metadata(String tableName, Column column, int distinctRowCount, int totalRowCount, String schemaName, String dbName, int recNum, boolean probablePrimary, boolean isDateColumn) {
        this.tableName = tableName;
        this.column = column;
        this.distinctRowCount = distinctRowCount;
        this.totalRowCount = totalRowCount;
        this.schemaName = schemaName;
        this.dbName = dbName;
        this.recNum = recNum;
        this.probablePrimary = probablePrimary;
        this.isDateColumn = isDateColumn;
    }

    public Metadata() {

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public int getDistinctRowCount() {
        return distinctRowCount;
    }

    public void setDistinctRowCount(int distinctRowCount) {
        this.distinctRowCount = distinctRowCount;
    }

    public int getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getRecNum() {
        return recNum;
    }

    public void setRecNum(int recNum) {
        this.recNum = recNum;
    }

    public boolean isProbablePrimary() {
        return probablePrimary;
    }

    public void setProbablePrimary(boolean probablePrimary) {
        this.probablePrimary = probablePrimary;
    }

    public boolean isDateColumn() {
        return isDateColumn;
    }

    public void setDateColumn(boolean dateColumn) {
        isDateColumn = dateColumn;
    }
}
