package com.p3.archon.rdbms_extrator.beans;

import com.p3.archon.rdbms_extrator.core.ConnectionChecker;
import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

@Data
public class ConnectionPool {
    private boolean inUse;
    private Connection connection;

    public void reset(InputArgs inputBean) {
        if(inputBean.isResetConnection) {
            if (connection != null) {
                try {
                    this.connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            this.connection = new ConnectionChecker().checkConnection(inputBean);
        }
        this.inUse = false;
    }
}
