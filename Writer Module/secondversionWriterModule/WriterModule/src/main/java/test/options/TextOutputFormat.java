package test.options;


/**
 * Enumeration for text format type.
 */
public enum TextOutputFormat
        implements OutputFormat {
    
    text("Plain text format"),
    html("HyperText Markup Language (HTML) format"),
    txt("Text File (TXT) format"),
    csv("Comma-separated values (CSV) format"),
    tsv("Tab-separated values (TSV) format"),
    json("JavaScript Object Notation (JSON) format"),
    xml("eXtensive Markup Language (XML) format"),
    sip("Submissive Information Package"),
    ;
    
    
    private final String description;
    
    TextOutputFormat(final String description) {
        this.description = description;
    }
    
    public static TextOutputFormat valueOfFromString(final String format) {
        TextOutputFormat outputFormat;
        try {
            outputFormat = TextOutputFormat.valueOf(format);
        } catch (final IllegalArgumentException | NullPointerException e) {
            outputFormat = text;
        }
        return outputFormat;
    }
    
    public static boolean isTextOutputFormat(final String format) {
        try {
            TextOutputFormat.valueOf(format);
            return true;
        } catch (final IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getFormat() {
        return name();
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s", getFormat(), description);
    }
    
}
