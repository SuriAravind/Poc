package com.p3.archon;

import java.util.List;

public class ExceltoDBBean {
    private List<String> primaryKeyList;
    private List<String> indexList;
    private List<String> dataType;
    private List<String> columnName;

    public List<String> getPrimaryKeyList() {
        return primaryKeyList;
    }

    public void setPrimaryKeyList(List<String> primaryKeyList) {
        this.primaryKeyList = primaryKeyList;
    }

    public List<String> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<String> indexList) {
        this.indexList = indexList;
    }

    public List<String> getDataType() {
        return dataType;
    }

    public void setDataType(List<String> dataType) {
        this.dataType = dataType;
    }

    public List<String> getColumnName() {
        return columnName;
    }

    public void setColumnName(List<String> columnName) {
        this.columnName = columnName;
    }
}
