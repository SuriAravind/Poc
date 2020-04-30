package test.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {
    private String column;

    @Builder.Default
    private String dataType = "STRING";

    @Builder.Default
    private JavaDataType category = JavaDataType.NORMAL;

}
