package com.p3solutions.writer.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Types;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {
    private String column;

    @Builder.Default
    private String dataType = "STRING";

    @Builder.Default
    private ArchonDataType category = ArchonDataType.STRING;

    private int javaDataType = Types.VARCHAR;

}
