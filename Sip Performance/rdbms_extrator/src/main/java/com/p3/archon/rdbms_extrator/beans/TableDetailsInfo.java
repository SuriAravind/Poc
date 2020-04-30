package com.p3.archon.rdbms_extrator.beans;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Data
public class TableDetailsInfo {

    private boolean selected;
    private boolean deleteData;
    private String aliasName;
    private String originalName;
    private String queryClause;
    private String whereOrderClause;
    private boolean mainTable;
    private List<ColumnBean> columns;

    private ArrayList<String> relatedTables;
    private String relationsJSON;
    //private String TableJoinCond;
    private TreeMap<String, String> TableJoinRelCond;
    private List<String> keyColumns;

    private Map<String, Set<String>> wholeExecuteQuery;
    private Map<String, String> columnType;
    private String queryforArchonKeyColumnId;

}
