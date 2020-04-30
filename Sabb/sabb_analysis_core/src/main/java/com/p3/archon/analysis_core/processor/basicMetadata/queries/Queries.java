package com.p3.archon.analysis_core.processor.basicMetadata.queries;


import com.p3.archon.analysis_core.beans.ConnectionParameter;
import com.p3.archon.analysis_core.enums.Type;

public class Queries {
    private ConnectionParameter dbConfigDto;
    private String mysqlMetadataQuery;
    private String sqlMetadataQuery;
    private String as400MetadataQuery;
    private String oracleMetadataQuery;
    private String db2MetadataQuery;
    private Type dbType;

    public Queries(ConnectionParameter dbConfigDto, Type dbType) {
        this.dbType = dbType;
        this.dbConfigDto = dbConfigDto;
        setMetadataQueryByType();
    }

	/*private void setMetadataQueryByType() {
		mysqlMetadataQuery = "SELECT TABLE_CATALOG,TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME as COL_NAME,DATA_TYPE as "
				+ "COL_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA in ('" + dbConfigDto.getSchema().get(0)
				+ "')";

		sqlMetadataQuery = "SELECT TABLE_CATALOG,TABLE_NAME,COLUMN_NAME as "
				+ "COL_NAME,DATA_TYPE as COL_TYPE FROM (select A.TABLE_CATALOG, A.TABLE_NAME,A.COLUMN_NAME , "
				+ "A.DATA_TYPE  FROM INFORMATION_SCHEMA.COLUMNS  A, "
				+ "INFORMATION_SCHEMA.TABLES B WHERE B.TABLE_TYPE='BASE TABLE' AND A.TABLE_NAME=B.TABLE_NAME ) ABC "
				+ "WHERE ABC.DATA_TYPE LIKE '%int%' OR ABC.DATA_TYPE LIKE "
				+ "'%varchar%' OR ABC.DATA_TYPE LIKE '%decimal%' ORDER BY ABC.TABLE_NAME,ABC.COLUMN_NAME";

		oracleMetadataQuery = "SELECT B.OWNER,B.TABLE_NAME,B.COLUMN_NAME COL_NAME, "
				+ "DATA_TYPE COL_TYPE From ALL_TABLES A,ALL_TAB_COLUMNS B " + "WHERE A.OWNER in ('"
				+ dbConfigDto.getSchema().get(0) + "')  AND A.TABLE_NAME=B.TABLE_NAME AND "
				+ "( DATA_TYPE LIKE '%NUM%' OR DATA_TYPE LIKE '%CHAR%' OR DATA_TYPE LIKE '%DEC%' OR DATA_TYPE LIKE '%INT%' "
				+ "OR DATA_TYPE LIKE '%REAL%') ORDER BY TABLE_NAME,COLUMN_NAME";

		db2MetadataQuery = "SELECT  TBNAME TABLE_NAME,NAME COL_NAME, COLTYPE  COL_TYPE FROM SYSIBM.SYSCOLUMNS "
				+ "WHERE TBCREATOR='" + dbConfigDto.getSchema().get(0) + "'";

		as400MetadataQuery = "SELECT TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME as COL_NAME,DATA_TYPE as COL_TYPE "
				+ "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA IN '" + dbConfigDto.getDatabaseName() + "'";
	}*/


    private void setMetadataQueryByType() {
        mysqlMetadataQuery = "SELECT TABLE_CATALOG,TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME as COL_NAME,DATA_TYPE as "
                + "COL_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA in ('" + dbConfigDto.getSchema()
                + "')";

        sqlMetadataQuery = "SELECT TABLE_CATALOG,TABLE_NAME,COLUMN_NAME as "
                + "COL_NAME,DATA_TYPE as COL_TYPE FROM (select A.TABLE_CATALOG, A.TABLE_NAME,A.COLUMN_NAME , "
                + "A.DATA_TYPE  FROM INFORMATION_SCHEMA.COLUMNS  A, "
                + "INFORMATION_SCHEMA.TABLES B WHERE B.TABLE_TYPE='BASE TABLE' AND A.TABLE_NAME=B.TABLE_NAME ) ABC "
                + " ORDER BY ABC.TABLE_NAME,ABC.COLUMN_NAME";

        oracleMetadataQuery = "SELECT B.OWNER,B.TABLE_NAME,B.COLUMN_NAME COL_NAME, "
                + "DATA_TYPE COL_TYPE From ALL_TABLES A,ALL_TAB_COLUMNS B " + "WHERE A.OWNER in ('"
                + dbConfigDto.getSchema() + "')  AND A.TABLE_NAME=B.TABLE_NAME "
                + " ORDER BY TABLE_NAME,COLUMN_NAME";

        db2MetadataQuery = "SELECT  TBNAME TABLE_NAME,NAME COL_NAME, COLTYPE  COL_TYPE FROM SYSIBM.SYSCOLUMNS "
                + "WHERE TBCREATOR='" + dbConfigDto.getSchema() + "'";

        as400MetadataQuery = "SELECT TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME as COL_NAME,DATA_TYPE as COL_TYPE "
                + "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA IN '" + dbConfigDto.getDbName() + "'";
    }

    public String getMetadataQuery() {

        switch (dbType) {
            case MYSQL:
                return mysqlMetadataQuery;
            case SQL:
                return sqlMetadataQuery;
            case ORACLE:
                return oracleMetadataQuery;
            case ORACLE_SERVICE:
                return oracleMetadataQuery;
            case AS400:
                return as400MetadataQuery;
            case DB2:
                return db2MetadataQuery;
            default:
                return null;
        }

    }
}
