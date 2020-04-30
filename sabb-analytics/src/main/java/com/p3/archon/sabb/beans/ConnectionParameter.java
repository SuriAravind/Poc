package com.p3.archon.sabb.beans;

import com.p3.archon.sabb.enums.DBType;
import lombok.*;

/**
 * Created by Suriyanarayanan K
 * on 25/02/20 7:27 PM.
 */
@Data
@Setter
@Getter
@Builder
@ToString
public class ConnectionParameter {

    private String appName;
    private String dbType;
    private String hostName;
    private Long port;
    private String dbName;
    private String schemaName;
    private String userName;
    private String password;
    private String ageByDate;
    private String metadataRequired;
    private String relationShipRequired;
    private String ageRequired;

}
