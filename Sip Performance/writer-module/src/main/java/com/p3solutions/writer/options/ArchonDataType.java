package com.p3solutions.writer.options;

public enum ArchonDataType implements DataTypeCategory {
    ARCHON_DATE(0.00, "Archon Date for Date Type"),
    ARCHON_DATE_TIME(0.01, "Archon Date time for Date Type"),
    ARCHON_DATE_TIME_XML(0.02, "Archon XML compatible Date Time for Date Type"),
    STRING(1.00, "String type"),
    NUMBER(2.00,"Integer Value"),
    DECIMAL(3.00,"Decimal Value"),
    BOOLEAN(4.00,"Boolean Value"),
    DATE(5.00, "Date type"),
    TIME(5.01, "Time type"),
    DATETIME(5.02, "Date & Time type"),
    CLOB(6.00, "Character LOB type"),
    BLOB(7.00, "Binary LOB type"),
    FILE_BLOB(7.01, "Download File path String type")
    ;
    
    private final double id;
    private final String description;
    
    ArchonDataType(double id, String description) {
        this.id = id;
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %.2f, %s", getCategory(), getId(), description);
    }
    
    @Override
    public Double getId() {
        return id;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getCategory() {
        return name();
    }
}
