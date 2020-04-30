package com.p3.archon.analysis_core.constants;





import com.p3.archon.analysis_core.beans.ConnectionParameter;
import com.p3.archon.analysis_core.enums.Type;

import java.sql.Connection;
import java.sql.Date;

public class QueriesAge {

//    private RDBMSConnectionInfoModel dbConfigDto;

    private ConnectionParameter dbConfigDto;

    private String mysqlCountRecords;
    private String sqlCountRecords;
    private String oracleCountRecords;
    private String as400CountRecords;
    private String db2CountRecords;

    private String mysqlTableSize;
    private String oracleTableSize;
    private String sqlTableSize;
    private String db2TableSize;
    private String as400TableSize;

    private String mysqlTableDataAfterDate;
    private String sqlTableDataAfterDate;
    private String oracleTableDataAfterDate;
    private String as400TableDataAfterDate;
    private String db2TableDataAfterDate;

    private String mysqlSecDataFromPriData;
    private String sqlSecDataFromPriData;
    private String oracleSecDataFromPriData;
    private String as400SecDataFromPriData;
    private String db2SecDataFromPriData;

//    public QueriesAge(ConnectionParameter connectionInfo, String tableList) {
//        this.dbConfigDto = connectionInfo;
//    }
    public QueriesAge(String tableName, ConnectionParameter connectionInfo, String probableDateCol, Date date) {
        this.dbConfigDto = connectionInfo;
        setCountRecordsQuery(tableName);
        setTableSizeQuery(tableName);
        setTableDateAfterDateQuery(tableName, probableDateCol, date);
    }

    public QueriesAge(String tableName, ConnectionParameter connectionInfo) {
        this.dbConfigDto = connectionInfo;
        setCountRecordsQuery(tableName);
        setTableSizeQuery(tableName);
    }

//    public QueriesAge(ConnectionParameter connectionInfo, String primaryTableName, String criteriaColumn, Date date) {
//        this.dbConfigDto = connectionInfo;
//        setTableDateAfterDateQuery(primaryTableName, criteriaColumn, date);
//    }

//    public QueriesAge(ConnectionParameter connectionInfo, String secondaryTableName, String secondaryColumn, String dataFromPrimaryTable) {
//        this.dbConfigDto = connectionInfo;
//        setSecDataFromPriData(secondaryTableName, secondaryColumn, dataFromPrimaryTable);
//    }

    private void setSecDataFromPriData(String secondaryTableName, String secondaryColumn, String dataFromPrimaryTable) {
        mysqlSecDataFromPriData = "SELECT * FROM " + dbConfigDto.getSchema() + "." + secondaryTableName + " WHERE " + secondaryColumn + " IN (" +
                dataFromPrimaryTable + ")";
        sqlSecDataFromPriData = "SELECT * FROM " + dbConfigDto.getSchema() + "." + secondaryTableName + " WHERE " + secondaryColumn + " IN (" +
                dataFromPrimaryTable + ")";
        oracleSecDataFromPriData = "SELECT * FROM " + dbConfigDto.getSchema() + "." + secondaryTableName + " WHERE " + secondaryColumn + " IN (" +
                dataFromPrimaryTable + ")";
        db2SecDataFromPriData = "SELECT * FROM " + dbConfigDto.getSchema()+ "." + secondaryTableName + " WHERE " + secondaryColumn + " IN (" +
                dataFromPrimaryTable + ")";
        as400SecDataFromPriData = "SELECT * FROM " + dbConfigDto.getSchema() + "." + secondaryTableName + " WHERE " + secondaryColumn + " IN (" +
                dataFromPrimaryTable + ")";
    }
    public String getSecDataFromPriData() {
        switch (dbConfigDto.getType()) {
            case MYSQL:
                return mysqlSecDataFromPriData;
            case SQL:
                return sqlSecDataFromPriData;
            case ORACLE:
                return oracleSecDataFromPriData;
            case AS400:
                return as400SecDataFromPriData;
            case DB2:
                return db2SecDataFromPriData;
            default:
                return "";
        }
    }

    private void setTableDateAfterDateQuery(String primaryTableName, String criteriaColumn, Date date) {
        mysqlTableDataAfterDate = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + primaryTableName + " WHERE " + criteriaColumn + " > '" + date + "'";
        sqlTableDataAfterDate = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + primaryTableName + " WHERE CONVERT(VARCHAR(10)," + criteriaColumn + ",120) > '" + date + "'";
        oracleTableDataAfterDate = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + primaryTableName + " WHERE TO_CHAR(" + criteriaColumn + ",'YYYY-MM-DD') > '" + date + "'";
        as400TableDataAfterDate = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + primaryTableName + " WHERE " + criteriaColumn + " > '" + date + "'";
        db2TableDataAfterDate = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + primaryTableName + " WHERE " + criteriaColumn + " > '" + date + "'";
    }
    public String getTableDateAfterDateQuery() {
        switch (dbConfigDto.getType()) {
            case SQL:
                return sqlTableDataAfterDate;
            case ORACLE:
                return oracleTableDataAfterDate;
            case AS400:
                return as400TableDataAfterDate;
            case DB2:
                return db2TableDataAfterDate;
            case MYSQL:
                return mysqlTableDataAfterDate;
            default:
                return "";
        }
    }

    private void setTableSizeQuery(String tableName) {
        mysqlTableSize = "SELECT TABLE_NAME AS 'Table', ((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024)" +
                "AS 'Size' FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '" + dbConfigDto.getSchema() +
                "' AND TABLE_TYPE = 'BASE TABLE' AND TABLE_NAME = '" + tableName + "'";
        oracleTableSize = "SELECT SEGMENT_NAME, bytes/1024/1024 MB FROM DBA_SEGMENTS WHERE OWNER = '" +
                dbConfigDto.getSchema() + " AND SEGMENT_TYPE = 'TABLE'";
        sqlTableSize = "SELECT t.NAME AS TableName, (SUM(a.total_pages) * 8 / 1024.00) AS Size " +
                    "FROM sys.tables t INNER JOIN sys.indexes i ON t.OBJECT_ID = i.object_id INNER JOIN " +
                    "sys.partitions p ON i.object_id = p.OBJECT_ID AND i.index_id = p.index_id INNER JOIN " +
                    "sys.allocation_units a ON p.partition_id = a.container_id LEFT OUTER JOIN sys.schemas " +
                    "s ON t.schema_id = s.schema_id WHERE t.NAME NOT LIKE 'dt%' AND t.is_ms_shipped = 0 AND " +
                    "i.OBJECT_ID > 255 and t.NAME = '" + tableName + "' and s.name='" + dbConfigDto.getSchema() + "' " +
                    "GROUP BY t.Name, s.Name, p.Rows";

//        db2TableSize = "SELECT X.TB AS SEGMENT_NAME, (X.KB + Y.KB) AS MB FROM (SELECT A.NAME AS TB, SUM(B.SPACE) AS KB" +
//                "FROM SYSIBM.SYSTABLES A, SYSIBM.SYSTABLEPART AS B WHERE A.CREATOR = 'MATEDK' AND A.DBNAME = B.DBNAME AND " +
//                "A.TSNAME = B.TSNAME GROUP BY A.NAME ) AS X, (SELECT A.TBNAME AS TB, SUM(B.SPACE) AS KB FROM SYSIBM.SYSINDEXES A, " +
//                "SYSIBM.SYSINDEXPART B WHERE B.IXCREATOR = 'MATEDK' AND AND A.CREATOR = B.IXCREATOR AND A.NAME = B.IXNAME " +
//                "GROUP BY A.TBNAME ) AS Y WHERE X>TB=Y>TB";
        as400TableSize = "SELECT TABLE_NAME, DATA_SIZE/1024/1024 MB FROM QSYS.SYSTABLESTAT WHERE OWNER = '" +
                dbConfigDto.getSchema() + "' AND SEGMENT_TYPE = 'TABLE'";
    }

    public String getTableSizeQuery() {
        switch (dbConfigDto.getType()) {
            case MYSQL :
                return mysqlTableSize;
            case ORACLE:
                return oracleTableSize;
            case SQL:
                return sqlTableSize;
            case DB2:
                return db2TableSize;
            case AS400:
                return as400TableSize;
            default:
                return "";
        }
    }

    private void setCountRecordsQuery(String tableName) {
        mysqlCountRecords = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + getQuoteIdentifier(dbConfigDto.getType()) + tableName + getQuoteIdentifier(dbConfigDto.getType());
        sqlCountRecords = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + getQuoteIdentifier(dbConfigDto.getType()) + tableName + getQuoteIdentifier(dbConfigDto.getType());
        oracleCountRecords = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + getQuoteIdentifier(dbConfigDto.getType()) + tableName + getQuoteIdentifier(dbConfigDto.getType());
        as400CountRecords = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + getQuoteIdentifier(dbConfigDto.getType()) + tableName + getQuoteIdentifier(dbConfigDto.getType());
        db2CountRecords = "SELECT COUNT(1) FROM " + dbConfigDto.getSchema() + "." + getQuoteIdentifier(dbConfigDto.getType()) + tableName + getQuoteIdentifier(dbConfigDto.getType());
    }

    public String getCountRecordsQuery() {
        switch (dbConfigDto.getType()) {
            case MYSQL :
                return mysqlCountRecords;
            case SQL:
                return sqlCountRecords;
            case ORACLE:
                return oracleCountRecords;
            case AS400:
                return as400CountRecords;
            case DB2:
                return db2CountRecords;
            default:
                return "";
        }
    }

    private String getQuoteIdentifier(Type dbType) {

            switch (dbType) {
                case SQL:
                    return "\"";
                case MYSQL:
                    return "`";
                case ORACLE:
                case ORACLE_SERVICE:
                    return "\"";
                case AS400:
                    return "\"";
                default:
                    return "";
            }
        }

}
