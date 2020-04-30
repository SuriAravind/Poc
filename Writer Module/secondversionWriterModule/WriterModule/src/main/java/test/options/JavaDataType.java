package test.options;

public enum JavaDataType implements DataTypeCategory {
    ARCHON_DATE(0.00, "Archon Date for Date Type"),
    ARCHON_DATE_TIME(0.01, "Archon Date time for Date Type"),
    ARCHON_DATE_TIME_XML(0.02, "Archon XML compatible Date Time for Date Type"),
    NORMAL(1.00, "normal type"),
    DATE(2.00, "Date type"),
    TIME(2.01, "Time type"),
    DATETIME(2.02, "Date & Time type"),
    CLOB(3.00, "Character LOB type"),
    BLOB(4.00, "Binary LOB type"),
    BOOLEAN(6.00,"Boolean Value"),
    INTEGER(7.00,"Integer Value"),
    DECIMAL(8.00,"Decimal Value");
    ;
    
    private final double id;
    private final String description;
    
    JavaDataType(double id, String description) {
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
