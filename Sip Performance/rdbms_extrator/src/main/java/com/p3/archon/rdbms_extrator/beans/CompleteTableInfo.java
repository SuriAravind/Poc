package com.p3.archon.rdbms_extrator.beans;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@Data
public class CompleteTableInfo {
    public TreeMap<String, TableDetailsInfo> tableDetails = new TreeMap<String, TableDetailsInfo>();
    public String rpx = "100";
    private Map<String, String> completedKeyColumnUUid;
    public String appName;
    public String holding;
    public String maintable;
    public String mainTableWholeFilterQuery;

}
