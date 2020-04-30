package com.p3solutions.writer.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SipConfigOptions {

    @Builder.Default
    private String holding = "archon-holding";

    @Builder.Default
    private String application = "archon-ia-app";

    @Builder.Default
    private String entity = "entity";

    @Builder.Default
    private String producer = "archon";

    @Builder.Default
    private String schema = "urn:x-emc:ia:schema:archon-holding:1.0";

    @Builder.Default
    private String rootElement = "objects";

    @Builder.Default
    private String recordElement = "object";

    @Builder.Default
    private Boolean overrideRecordElement =false;

    public String generateNameSpaceFromHolding() {
        return URI.create("urn:x-emc:ia:schema:" + holding.toLowerCase() + ":1.0").toString();
    }



    
}
