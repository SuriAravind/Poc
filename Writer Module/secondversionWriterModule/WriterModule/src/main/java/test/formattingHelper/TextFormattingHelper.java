package test.formattingHelper;

import test.utility.html.Alignment;
import test.utility.others.Color;

/**
 * Methods to format entire rows of output.
 */
public interface TextFormattingHelper {
    
    TextFormattingHelper append(String text);
    
    /**
     * Creates a new anchor tag.
     *
     * @return Anchor tag
     */
    String createAnchor(String text, String link);
    
    /**
     * Creates an arrow symbol.
     *
     * @return Arrow symbol
     */
    String createLeftArrow();
    
    /**
     * Creates an arrow symbol.
     *
     * @return Arrow symbol
     */
    String createRightArrow();
    
    /**
     * Creates an arrow symbol.
     *
     * @return Arrow symbol
     */
    String createWeakLeftArrow();
    
    /**
     * Creates an arrow symbol.
     *
     * @return Arrow symbol
     */
    String createWeakRightArrow();
    
    void println();
    
    /**
     * Creates a description row with a blank spacer cells.
     *
     * @param description
     *         Description
     *
     * @return Row as a string
     */
    void writeDescriptionRow(String description);
    
    /**
     * Creates a detail row, with four fields. The name can be emphasized.
     *
     * @param ordinal
     *         Ordinal value
     * @param subName
     *         Name
     * @param escapeText
     *         Format escape characters
     * @param type
     *         Type
     * @param emphasize
     *         Emphasize name.
     *
     * @return Row as a string
     */
    void writeDetailRow(String ordinal, String subName, boolean escapeText, String type, boolean emphasize);
    
    /**
     * Creates a detail row, with four fields.
     *
     * @param ordinal
     *         Ordinal value
     * @param subName
     *         Name
     * @param type
     *         Type
     *
     * @return Row as a string
     */
    void writeDetailRow(String ordinal, String subName, String type);
    
    /**
     * Document end.
     *
     * @return Document end
     */
    void writeDocumentEnd();
    
    /**
     * Document start.
     *
     * @return Document start
     */
    void writeDocumentStart();
    
    /**
     * Create an empty row.
     *
     * @return Row as a string
     */
    void writeEmptyRow();
    
    /**
     * Creates a section header.
     *
     * @param type
     *         Type of header
     * @param header
     *         Header text
     *
     * @return Section header
     */
    void writeHeader(DocumentHeaderType type, String header);
    
    /**
     * Create a name and description row.
     *
     * @param name
     *         Name
     * @param description
     *         Description
     *
     * @return Row as a string
     */
    void writeNameRow(String name, String description);
    
    /**
     * Create a name and value row.
     *
     * @param name
     *         Name
     * @param value
     *         Value
     * @param valueAlignment
     *         Alignment of the value
     *
     * @return Row as a string
     */
    void writeNameValueRow(String name, String value, Alignment valueAlignment);
    
    /**
     * Database object end.
     *
     * @return Database object end
     */
    void writeObjectEnd();
    
    /**
     * Create a name and description row.
     *
     * @param id
     *         Identifier
     * @param name
     *         Name
     * @param description
     *         Description
     *
     * @return Row as a string
     */
    void writeObjectNameRow(String id, String name, String description, Color backgroundColor);
    
    /**
     * Database object start.
     *
     * @return Database object start
     */
    void writeObjectStart();
    
    /**
     * Creates a row of data.
     *
     * @param columnData
     *         Column data
     *
     * @return Row of data
     */
    void writeRow(Object... columnData);
    
    /**
     * Creates a header row for data.
     *
     * @param columnNames
     *         Column names
     *
     * @return Header row for data
     */
    void writeRowHeader(String... columnNames);
    
    /**
     * Creates a definition row.
     *
     * @param definition
     *         Definition
     * @param style
     *         Formatting style
     *
     * @return Row as a string
     */
    void writeWideRow(String definition, String style);
    
    /**
     * Creates a value in XML.
     *
     * @param value
     *         Text to write
     */
    void writeValue(String value);
    
    /**
     * Close XML Element
     */
    void writeElementEnd();
    
    /**
     * Creates XML Element
     *
     * @param elm
     *         Element Name
     */
    void writeElementStart(String elm);
    
    /**
     * Creates XML Parent Element
     *
     * @param recordElm
     *         Element Name
     * @param suffix
     *         Text to append
     */
    void writeRecordStart(String recordElm, String suffix);
    
    /**
     * Close XML Record Element
     */
    void writeRecordEnd();
    
    /**
     * Close XML Root Element
     */
    void writeRootElementEnd();
    
    /**
     * Creates XML Root Element
     *
     * @param rootElm
     *         Element Name
     */
    void writeRootElementStart(String rootElm);
    
    
    /**
     * flush content to files
     */
    void flush();
    
    /**
     * close content to files
     */
    void close();
    
    /**
     * Insert Line Break
     */
    void newlineWriter();
    
    
    /**
     * Create XML attribute
     *
     * @param name
     *         Attribute name
     * @param value
     *         Attribute text
     */
    void writeAttribute(String name, String value);
    
    
    /**
     * Creates CDATA content in XML
     *
     * @param value
     */
    void writeCData(String value);
    
    
    enum DocumentHeaderType {
        title {
            @Override
            String getHeaderTag() {
                return "h1";
            }
            
            @Override
            String getPrefix() {
                return "<p>&#160;</p>";
            }
        },
        subTitle {
            @Override
            String getHeaderTag() {
                return "h2";
            }
            
            @Override
            String getPrefix() {
                return "<p>&#160;</p>";
            }
        },
        section {
            @Override
            String getHeaderTag() {
                return "h3";
            }
            
            @Override
            String getPrefix() {
                return "";
            }
        };
        
        abstract String getHeaderTag();
        
        abstract String getPrefix();
        
    }
    
}
