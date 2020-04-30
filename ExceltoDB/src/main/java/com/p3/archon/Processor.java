package com.p3.archon;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Processor {

    String srcFilePath;

    //connection
    Connection connection;
    Statement statement;
    //PreparedStatement preparedStatement;

    //
    String createQuery = "";
    String insertQuery = "";

    //
    String schemaName = "";
    String tableName = "";
    String insertPrefix = "";

    //constants
    public final static String QUOTE = "`";

    public Processor(String srcFilePath, String schemaName, String username, String password) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        this.connection = DriverManager.getConnection
                ("jdbc:mysql://localhost/?user=" + username + "&password=" + password);
        this.statement = connection.createStatement();
        statement.executeUpdate("DROP SCHEMA IF EXISTS "+ schemaName);
        int result = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + schemaName);
        //System.out.println("Result  :" + result);
        if (result != 1) {
            throw new Exception("Database not created");
        }
        this.srcFilePath = srcFilePath;
        this.schemaName = schemaName;
    }


    public void start() {

        FileInputStream fis = null;
        int count = 0;
        try {
            fis = new FileInputStream(srcFilePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            int numberofSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberofSheets; i++) {
                ExceltoDBBean exceltoDBBean = new ExceltoDBBean();
                count = 0;
                XSSFSheet mySheet = workbook.getSheetAt(i);
                tableName = mySheet.getSheetName();
                Iterator<Row> rowIterator = mySheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> dataValues = new ArrayList<String>();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int index = 0;
                    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
                        Cell cell = row.getCell(c);
                        dataValues.add(c, printCellValue(cell));
                    }

                    if (count == 0) {
                        exceltoDBBean.setPrimaryKeyList(dataValues);
                        count++;
                    } else if (count == 1) {
                        exceltoDBBean.setIndexList(dataValues);
                        count++;
                    } else if (count == 2) {
                        exceltoDBBean.setDataType(dataValues);
                        count++;
                    } else if (count == 3) {
                        exceltoDBBean.setColumnName(dataValues);
                        startCreateTableFunction(exceltoDBBean);
                        startCreateInsertTemplate(dataValues);
                        count++;
                    } else {
                        insertQuery = "";
                        insertRecordFunction(exceltoDBBean, dataValues);
                        count++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR at line :" + count);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR at line :" + count);
            e.printStackTrace();
        }
    }

    private static String printCellValue(Cell cell) {
        String values = "";
        if (cell == null) {
            values = "";
        } else {
            switch (cell.getCellTypeEnum()) {
                case BOOLEAN:
                    values = String.valueOf(cell.getBooleanCellValue());
                    break;
                case STRING:
                    values = cell.getRichStringCellValue().getString();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        values = String.valueOf(cell.getDateCellValue());
                    } else {
                        values = String.valueOf(cell.getNumericCellValue()).split("\\.")[0];
                    }
                    break;
                case FORMULA:
                    values = String.valueOf(cell.getCellFormula());
                    break;
                case BLANK:
                    values = "";
                    break;
                default:
                    values = "";
            }
        }
        return values;
    }

    private void insertRecordFunction(ExceltoDBBean exceltoDBBean, List<String> dataValues) {

        insertQuery += insertPrefix + " (";
        for (int i = 0; i < dataValues.size(); i++) {
            if (i != 0) {
                insertQuery += " , ";
            }
            String data = dataValues.get(i).isEmpty() ? null : dataValues.get(i).toString();

            if (data == null || data.isEmpty()) {
                insertQuery += "null" + " ";
            } else {
                data = data.replace("\'", "\'\'");
                if (exceltoDBBean.getDataType().get(i).equalsIgnoreCase("INT")) {

                    insertQuery += data.split("\\.")[0] + " ";
                } else if (exceltoDBBean.getDataType().get(i).equalsIgnoreCase("DECIMAL")) {
                    insertQuery += data + " ";
                } else {
                    insertQuery += "\'" + data + "\'";
                }
            }

        }
        insertQuery += " )";
        try {
            statement.execute(insertQuery);
        } catch (SQLException e) {
            System.out.println(insertQuery);
            e.printStackTrace();
        }
    }

    private void startCreateInsertTemplate(List<String> dataValues) {
        insertPrefix = "insert into " + schemaName + "." + tableName;
        insertPrefix += "(";
        for (int i = 0; i < dataValues.size(); i++) {
            if (i != 0) {
                insertPrefix += ",";
            }
            insertPrefix += QUOTE + dataValues.get(i) + QUOTE + " ";
        }
        insertPrefix += ")";
        insertPrefix += " values ";
    }

    private void startCreateTableFunction(ExceltoDBBean exceltoDBBean) {
        try {
            createQuery = "CREATE TABLE IF NOT EXISTS " + schemaName + "." + tableName + " ( " + getColumnCreation(exceltoDBBean) + " )";
           // System.out.println("Created Query :" + createQuery);
            statement.execute(createQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String getColumnCreation(ExceltoDBBean exceltoDBBean) {
        String columnCreation = "";

        for (int i = 0; i < exceltoDBBean.getColumnName().size(); i++) {
            if (i != 0) {
                columnCreation += " ,";
            }
            columnCreation += QUOTE + exceltoDBBean.getColumnName().get(i) + QUOTE + " " + exceltoDBBean.getDataType().get(i);
            if (i <= exceltoDBBean.getPrimaryKeyList().size() - 1 && exceltoDBBean.getPrimaryKeyList().get(i).equalsIgnoreCase("PK")) {
                columnCreation += " NOT NULL ";
            } else {
                columnCreation += " NULL ";
            }

        }
        //primary
        for (int i = 0; i < exceltoDBBean.getPrimaryKeyList().size(); i++) {

            if (exceltoDBBean.getPrimaryKeyList().get(i).equalsIgnoreCase("PK")) {
                columnCreation += " ,";
                columnCreation += " PRIMARY KEY (" + exceltoDBBean.getColumnName().get(i) + " )";
            }
        }
        //index
        for (int i = 0; i < exceltoDBBean.getIndexList().size(); i++) {
            if (exceltoDBBean.getIndexList().get(i).equalsIgnoreCase("TRUE")) {
                columnCreation += " ,";
                columnCreation += " INDEX(" + exceltoDBBean.getColumnName().get(i) + " )";
            }
        }
        return columnCreation;
    }
}

