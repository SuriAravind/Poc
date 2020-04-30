package com.p3.archon.analysis_core.processor;


import com.p3.archon.analysis_core.beans.ConnectionParameter;
import com.p3.archon.analysis_core.constants.QueriesAge;
import com.p3.archon.analysis_core.response.TableResponseAge;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AgeProcess {
    private String outputFolderLocation;

    public AgeProcess(String outputFolderLocation) {
        this.outputFolderLocation = outputFolderLocation;
    }

//    public Map<String, TableResponseAge> startAgeProcess(ConnectionParameter params, Map<String, TableResponseAge> tableMap, Connection connection, String metaOutputFileLocation) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, ParseException {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date criteriaDate = sdf.parse(params.getAgeByDate());
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(metaOutputFileLocation + File.separator + "MetaFiles.csv"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.trim().equals(""))
//                    continue;
//                String[] splitLine = line.split(",");
//                String currentTable = splitLine[0];  //0
//                ResultSet rs = null;
////                String countQuery = queriesAge.getCountRecordsQuery();
////                try(PreparedStatement stmt = connection.prepareStatement(countQuery,
////                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
////                rs = stmt.executeQuery();
////                    while(rs.next()) {
////                        tableMap.get(splitLine[0]).setTotalRowCount(rs.getLong(1));
////                    }
////                }
//                tableMap.get(currentTable).setTotalRowCount(Long.parseLong(splitLine[4]));
//
//                QueriesAge queriesAge = new QueriesAge(currentTable, params);
//
//                String tableSizeQuery = queriesAge.getTableSizeQuery();
//                try(PreparedStatement stmt = connection.prepareStatement(tableSizeQuery,
//                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
//                    rs = stmt.executeQuery();
//                    while(rs.next()) {
//                        tableMap.get(rs.getString(1)).setTableSize(rs.getDouble(2));
//                    }
//                }
//
//                if(splitLine[6].equalsIgnoreCase("true")) {
//                    QueriesAge queriesDateQualifyCount = new QueriesAge(currentTable, params, splitLine[1], new java.sql.Date(criteriaDate.getTime()));
//                    String afterDateQuery = queriesDateQualifyCount.getTableDateAfterDateQuery();
//                    try(PreparedStatement stmt = connection.prepareStatement(afterDateQuery,
//                            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
//                        rs = stmt.executeQuery();
//                        while(rs.next()) {
//                            tableMap.get(splitLine[0]).setQualifiedRowCount(rs.getLong(1));
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return tableMap;
//    }

    public Map<String, TableResponseAge> startAgeProcess(ConnectionParameter params, Connection connection, String ageRequiredPath) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date criteriaDate = sdf.parse(params.getAgeByDate());

        Map<String, TableResponseAge> tableMap = new HashMap<String, TableResponseAge>();

        File file = new File(ageRequiredPath);
        if (!file.exists()) {
//            System.out.println("Path Error");
            throw new Exception("Invalid File Path");
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ageRequiredPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                String[] splitLine = line.split(",");
                String currentTable = splitLine[0];  //0
                String dateColumn = splitLine[1];

                String key = currentTable + "|" + dateColumn;

                if (!tableMap.containsKey(key)) {
                    TableResponseAge tableResponseAge = new TableResponseAge(0L, 0L);
                    tableMap.put(key, tableResponseAge);
                }

                ResultSet rs;

                QueriesAge queriesAge = new QueriesAge(currentTable, params, dateColumn, new java.sql.Date(criteriaDate.getTime()));

                String countQuery = queriesAge.getCountRecordsQuery();
                try {
//                    System.out.println(countQuery);
                    PreparedStatement stmt = connection.prepareStatement(countQuery,
                            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        tableMap.get(key).setTotalRowCount(rs.getLong(1));
                    }
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                String tableSizeQuery = queriesAge.getTableSizeQuery();
//                System.out.println(">" + tableSizeQuery);
//                try(PreparedStatement stmt = connection.prepareStatement(tableSizeQuery,
//                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
//                    rs = stmt.executeQuery();
//                    while(rs.next()) {
//                        System.out.println(rs.getString(1) + ":" + rs.getString(2));
//                        if(tableMap.containsKey(rs.getString(1))) {
//                            tableMap.get(rs.getString(1)).setTableSize(rs.getDouble(2));
//                        }
//                    }
//                }

                String afterDateQuery = queriesAge.getTableDateAfterDateQuery();
                try {
                    PreparedStatement stmt = connection.prepareStatement(afterDateQuery,
                            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        tableMap.get(key).setQualifiedRowCount(rs.getLong(1));
                    }
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableMap;
    }

    public void writeAgeResults(Map<String, TableResponseAge> responseMap) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputFolderLocation + File.separator + "AgeReport.csv")));

        pw.write("Table_Name,Column_Name,Total_Count,Qualified_Count" + "\n");
        for (Map.Entry<String, TableResponseAge> entry : responseMap.entrySet()) {
            String[] tableColName = entry.getKey().split("\\|");  // key as "tableName|colName"
            String toSave = tableColName[0] + "," + tableColName[1] + ",";
            TableResponseAge response = entry.getValue();
//            for(String category : response.getCategories()) {
//                toSave += category + "|";
//            }
//            toSave = toSave.substring(0, toSave.length()-1) + ",";
//            toSave += response.getTableSize() + "," + response.getTotalRowCount() + ","
//                    + response.getQualifiedRowCount() + "\n";
            toSave += response.getTotalRowCount() + "," + response.getQualifiedRowCount() + "\n";
            pw.write(toSave);
        }

        pw.close();
    }
}
