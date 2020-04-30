package com.p3.archon.rdbms_extrator.core;

import com.p3.archon.rdbms_extrator.beans.ColumnBean;
import com.p3.archon.rdbms_extrator.beans.CompleteTableInfo;
import com.p3.archon.rdbms_extrator.beans.InputArgs;
import com.p3.archon.rdbms_extrator.beans.TableDetailsInfo;
import org.apache.log4j.Logger;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import java.util.*;

/**
 * Created by Suriyanarayanan K
 * on 06/03/20 12:14 AM.
 */
public class ParseJsonFileIntoBean {

    CompleteTableInfo completeTableInfo;
    TableDetailsInfo tableComp;
    List<ColumnBean> columnsList;
    List<String> tableList = new LinkedList<String>();
    Map<String, String> completedKeyColumnUUid;
    InputArgs inputBean;
    String mainTable;
    Set<String> checkedTablesList = new LinkedHashSet<>();
    String schemaName;
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());


    public ParseJsonFileIntoBean(InputArgs inputBean) {
        this.inputBean = inputBean;
        this.schemaName = inputBean.getSchemaName();

    }

    public CompleteTableInfo parseInputJsonIntoCompletetableDetails() {
        String currentTable = "";
        try {
            Set<String> checkedTable = new LinkedHashSet<>();
            completeTableInfo = new CompleteTableInfo();
            JSONObject obj = new JSONObject(inputBean.getInputJson());
            JSONObject jsontables = new JSONObject(obj.get("tables"));
            tableList.addAll(jsontables.keySet());
            completedKeyColumnUUid = new LinkedHashMap<>();

            for (String table : tableList) {
                currentTable = table;
                checkedTable.add(table);
                tableComp = new TableDetailsInfo();
                JSONObject tableDetails = obj.getJSONObject("tables").getJSONObject(table);
                tableComp.setSelected(Boolean.valueOf(tableDetails.getString("selected")));
                tableComp.setDeleteData(Boolean.valueOf(tableDetails.getString("deleteData")));
                tableComp.setWhereOrderClause(tableDetails.getString("whereOrderClause"));

                tableComp.setKeyColumns(new JSONArray(tableDetails.getString("KeyColumns").replace("\"", "")));
                if (tableDetails.getString("aliasTableName") != null
                        || !tableDetails.getString("aliasTableName").equals("null"))
                    tableComp.setAliasName(tableDetails.getString("aliasTableName"));
                else
                    tableComp.setAliasName(table);
                tableComp.setOriginalName(table);
                tableComp.setMainTable(tableDetails.getBoolean("mainTable"));
                if (tableComp.isMainTable()) {
                    mainTable = tableComp.getOriginalName();
                }
                JSONArray temp = tableDetails.getJSONArray("relatedTables");
                ArrayList<String> str = new ArrayList<String>();
                if (temp != null) {
                    for (int i = 0; i < temp.length(); i++)
                        str.add(temp.getString(i));
                }
                tableComp.setRelatedTables(str);
                TreeMap<String, String> tableJoinRelCond = new TreeMap<String, String>();
                JSONObject relationCond = new JSONObject(tableDetails.getString("TableJoinRelCond").trim());
                Iterator<String> keys = relationCond.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    tableJoinRelCond.put(key, relationCond.getString(key));
                }
                tableComp.setTableJoinRelCond(tableJoinRelCond);
                JSONArray colArray = new JSONArray(tableDetails.getString("column"));
                columnsList = new ArrayList<>();
                ColumnBean column = null;
                Map<String, String> columnType = new LinkedHashMap<>();
                for (int i = 0; i < colArray.length(); i++) {
                    JSONObject columnJson = colArray.getJSONObject(i);
                    column = new ColumnBean();
                    column.setOriginalColName(columnJson.getString("originalColName"));
                    column.setExpColName(columnJson.getString("expectedColName"));
                    column.setColType(columnJson.getString("colType"));
                    columnType.put(columnJson.getString("originalColName"), columnJson.getString("colType"));
                    columnsList.add(column);
                }

                tableComp.setColumns(columnsList);
                tableComp.setColumnType(columnType);
                completedKeyColumnUUid.putAll(getKeyColumnUUid(tableComp.getKeyColumns(), tableComp.getOriginalName()));
                tableComp.setWholeExecuteQuery(getWholeQueryFrammer(tableComp));
                tableComp.setTableJoinRelCond(getTableRelCond(tableComp, tableComp.getOriginalName(), columnType));
                tableComp.setQueryClause(tableDetails.getString("queryClause"));
                tableComp.setQueryforArchonKeyColumnId(getIdKeyColumnQuery(tableComp));
                completeTableInfo.getTableDetails().put(table, tableComp);
            }
            /* completeTableInfo.setRpx(obj.getString("rpx"));*/
            completeTableInfo.setAppName(obj.getString("appName"));
            completeTableInfo.setHolding(obj.getString("holding"));
            completeTableInfo.setMaintable(obj.getString("mainTable"));
            completeTableInfo.setCompletedKeyColumnUUid(completedKeyColumnUUid);

            checkedTablesList.add(mainTable);
            Set<String> listOfCondition = new LinkedHashSet<>();
            Set<String> maintableWholeFilterQuery = getTableWholeForMainTable("", completeTableInfo.getTableDetails().get(mainTable), listOfCondition);
            Set<String> checkedTableListWithSchema = new LinkedHashSet<>();
            for (String table : checkedTablesList) {
                checkedTableListWithSchema.add(schemaName + "." + table);
            }
            checkedTable.clear();
            completeTableInfo.setMainTableWholeFilterQuery(" from " +
                    String.join(",", checkedTableListWithSchema)
                    //+" where "
                    +(completeTableInfo.getTableDetails().get(mainTable).getWhereOrderClause().isEmpty()?" where ":" "+completeTableInfo.getTableDetails().get(mainTable).getWhereOrderClause()+" AND ")
                    + String.join(" AND ", maintableWholeFilterQuery));
        } catch (JSONException e) {
            LOGGER.error("While retriving the table :" + schemaName + "." + currentTable);
            LOGGER.error("Exception :" + e.getMessage());
        }

        return completeTableInfo;
    }

    /*private Set<String> getTableWholeForMainTable(String wholeFilterQueryString, TableDetailsInfo tableDetailsInfo, Set<String> listOfCondition) {
        for (Map.Entry<String, String> filterEntry : tableDetailsInfo.getTableJoinRelCond().entrySet()) {
            if (!checkedTablesList.contains(filterEntry.getKey())) {
                checkedTablesList.add(filterEntry.getKey());
                listOfCondition.add(filterEntry.getValue().replace("'", "").replace("= "," = "+schemaName+"."));
                //listOfCondition.add(filterEntry.getValue().replace("'", ""));
                String whereOrder = tableDetailsInfo.getWhereOrderClause().replaceFirst("where", "").replaceFirst("WHERE", "");
                String where = whereOrder.toLowerCase().contains("order by") ? whereOrder.substring(0, whereOrder.toLowerCase().indexOf("order by")) : whereOrder;
                if (!where.trim().isEmpty())
                    listOfCondition.add(where);
                listOfCondition.addAll(getTableWholeForMainTable(wholeFilterQueryString, completeTableInfo.getTableDetails().get(filterEntry.getKey()), listOfCondition));
            }
        }
        return listOfCondition;
    }*/
   private Set<String> getTableWholeForMainTable(String wholeFilterQueryString, TableDetailsInfo tableDetailsInfo,
                                                  Set<String> listOfCondition) {
        String whereOrder = tableDetailsInfo.getWhereOrderClause().replaceFirst("where", "").replaceFirst("WHERE", "");
        String where = whereOrder.toLowerCase().contains("order by")
                ? whereOrder.substring(0, whereOrder.toLowerCase().indexOf("order by"))
                : whereOrder;
        if (!where.trim().isEmpty())
            listOfCondition.add(where);
        for (Map.Entry<String, String> filterEntry : tableDetailsInfo.getTableJoinRelCond().entrySet()) {
            listOfCondition.add(filterEntry.getValue().replace("'", "").replace("= ", " = " + schemaName + "."));

        }
        for (String filterEntry : tableDetailsInfo.getRelatedTables()) {
            if (!checkedTablesList.contains(filterEntry)) {
                checkedTablesList.add(filterEntry);
//				listOfCondition.add(filterEntry.getValue().replace("'", "").replace("= ", " = " + schemaName + "."));
//				listOfCondition.add(filterEntry.getValue().replace("'", ""));

                listOfCondition.addAll(getTableWholeForMainTable(wholeFilterQueryString,
                        completeTableInfo.getTableDetails().get(filterEntry), listOfCondition));
            }
        }
        return listOfCondition;
    }

    private String getIdKeyColumnQuery(TableDetailsInfo tableComp) {
        StringBuffer keyColumnQuery = new StringBuffer();
        for (String keyColumn : tableComp.getKeyColumns()) {
            String tableColumnIdentifier = tableComp.getOriginalName() + "." + keyColumn;
            keyColumnQuery.append(",").append(inputBean.getSchemaName() + "." + tableColumnIdentifier).append(" as \"").append(completedKeyColumnUUid.get(tableColumnIdentifier)).append("\"");
        }
        return keyColumnQuery.length() > 0 ? keyColumnQuery.toString().substring(1) : keyColumnQuery.toString();
    }


    private Map<String, String> getKeyColumnUUid(List<String> keyColumnsList, String originalTableName) {
        Map<String, String> keyColumnUUid = new LinkedHashMap<>();
        for (String keyColumns : keyColumnsList)
            keyColumnUUid.put(originalTableName + "." + keyColumns, ("_ARCHON" + UUID.randomUUID().toString()).substring(0, 28) + "_");
        return keyColumnUUid;
    }

    private TreeMap<String, String> getTableRelCond(TableDetailsInfo tableComp, String originalTableName, Map<String, String> columnType) {
        TreeMap<String, String> tableRelatedJoinCond = new TreeMap<>();
        for (Map.Entry<String, Set<String>> relatedJoinCond : tableComp.getWholeExecuteQuery().entrySet()) {
            String filterQuery = tableComp.getTableJoinRelCond().get(relatedJoinCond.getKey());
            /*filterQuery = filterQuery.replace("= " + schemaName + ".", "=");*/
            for (String keyColumn : relatedJoinCond.getValue()) {
                filterQuery = filterQuery.replace("= " + schemaName + ".", "= ");
                String colType = columnType.get(keyColumn.trim());
                if (colType.equalsIgnoreCase("VARCHAR")
                        || colType.equalsIgnoreCase("TIMESTAMP")
                        || colType.equalsIgnoreCase("CHAR")) {
                    filterQuery = filterQuery.replace("\'" + originalTableName + "." + keyColumn.trim() + "\'", originalTableName + "." + keyColumn.trim()).replace(originalTableName + "." + keyColumn.trim(), "\'" + originalTableName + "." + keyColumn.trim() + "\'");
                } else if (colType.equalsIgnoreCase("INTEGER")) {
                    filterQuery = filterQuery.replace(originalTableName + "." + keyColumn.trim(), "" + originalTableName + "." + keyColumn.trim() + "");
                } else {
                    filterQuery = filterQuery.replace("\'" + originalTableName + "." + keyColumn.trim() + "\'", originalTableName + "." + keyColumn.trim()).replace(originalTableName + "." + keyColumn.trim(), "\'" + originalTableName + "." + keyColumn.trim() + "\'");
                }
            }
            tableRelatedJoinCond.put(relatedJoinCond.getKey(), filterQuery);
        }
        LOGGER.debug(tableComp.getOriginalName() + " tableRelatedJoinCond : " + tableRelatedJoinCond);
        return tableRelatedJoinCond;
    }

    private Map<String, Set<String>> getWholeQueryFrammer(TableDetailsInfo tableComp) {
        Map<String, Set<String>> wholeExecuteQuery = new LinkedHashMap<>();
        for (Map.Entry<String, String> tableJoinRelCond : tableComp.getTableJoinRelCond().entrySet()) {
            String relationTableName = tableJoinRelCond.getKey();
            Set<String> keyColumnList = getKeyColumnList(tableJoinRelCond.getValue());
            wholeExecuteQuery.put(relationTableName, keyColumnList);
        }
        return wholeExecuteQuery;
    }

    private Set<String> getKeyColumnList(String tableJoinRelCondValue) {

        String[] orFilterCondition = tableJoinRelCondValue.split("or");
        int orfilterCount = orFilterCondition.length - 1;
        int count = 0;
        String query = "";
        Set<String> keyColumn = new LinkedHashSet<>();
        for (String eachOrCondition : orFilterCondition) {
            eachOrCondition = eachOrCondition.replace("(", "").replace(")", "");
            String[] andFilterCondition = eachOrCondition.split("and");
            for (int i = 0; i < andFilterCondition.length; i++) {
                String condition = andFilterCondition[i];
                String[] keycolumn = condition.split("=")[1].split("\\.");
                keyColumn.add(keycolumn[keycolumn.length - 1]);
                if (i != andFilterCondition.length - 1) {
                    // query += " and ";
                }
            }
            if (count != orfilterCount) {

            }
            count++;
        }
        return keyColumn;
    }
}
