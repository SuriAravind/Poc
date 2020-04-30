package com.p3solutions.writer.utility.blobbean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private String path;
    private String size;
    private String status;
    private String name;

    @Override
    public String toString() {
        return getPath();
    }
}
