package test.formattingHelper;

import test.options.TextOutputFormat;
import test.utility.others.Color;

import java.io.PrintWriter;

import static test.utility.html.Entities.escapeForXMLElement;
import static test.utility.others.Utility.isBlank;
import static test.utility.others.Utility.readResourceFully;

/**
 * Methods to format entire rows of output as HTML.
 *
 * @author Sualeh Fatehi
 */
public final class HtmlFormattingHelper extends BaseTextFormattingHelper {
    
    private static final String HTML_HEADER = htmlHeader("Archon - HTML Output");
    private static final String HTML_FOOTER = "</body>" + System.lineSeparator() + "</html>";
    
    public HtmlFormattingHelper(final PrintWriter out, final TextOutputFormat outputFormat) {
        super(out, outputFormat);
    }
    
    private static String htmlHeader(String title) {
        final StringBuilder styleSheet = new StringBuilder(4096);
        styleSheet.append(System.lineSeparator()).append(readResourceFully("/sc.css")).append(System.lineSeparator())
                .append(readResourceFully("/sc_output.css")).append(System.lineSeparator());
        
        return "<!DOCTYPE html>" + System.lineSeparator() + "<html lang=\"en\">" + System.lineSeparator() + "<head>"
               + System.lineSeparator() + "  <title>" + title + "</title>" + System.lineSeparator()
               + "  <meta charset=\"utf-8\"/>" + System.lineSeparator() + "  <style>" + styleSheet + "  </style>"
               + System.lineSeparator() + "</head>" + System.lineSeparator() + "<body>" + System.lineSeparator();
    }
    
    @Override
    public String createLeftArrow() {
        return "\u2190";
    }
    
    @Override
    public String createRightArrow() {
        return "\u2192";
    }
    
    @Override
    public String createWeakLeftArrow() {
        return "\u21dc";
    }
    
    @Override
    public String createWeakRightArrow() {
        return "\u21dd";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDocumentEnd() {
        out.println(HTML_FOOTER);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDocumentStart() {
        out.println(HTML_HEADER);
    }
    
    @Override
    public void writeHeader(final DocumentHeaderType type, final String header) {
        if (!isBlank(header) && type != null) {
            out.println(String.format("%s%n<%s>%s</%s>%n", type.getPrefix(), type.getHeaderTag(), header,
                                      type.getHeaderTag()));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeObjectEnd() {
        out.append("</table>").println();
        out.println("<p>&#160;</p>");
        out.println();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeObjectNameRow(final String id, final String name, final String description,
                                   final Color backgroundColor) {
        final StringBuilder buffer = new StringBuilder(1024);
        buffer.append("  <caption style='background-color: ").append(backgroundColor).append(";'>");
        if (!isBlank(name)) {
            buffer.append("<span");
            if (!isBlank(id)) {
                buffer.append(" id='").append(id).append("'");
            }
            buffer.append(" class='caption_name'>").append(escapeForXMLElement(name)).append("</span>");
        }
        if (!isBlank(description)) {
            buffer.append(" <span class='caption_description'>").append(escapeForXMLElement(description))
                    .append("</span>");
        }
        buffer.append("</caption>").append(System.lineSeparator());
        
        out.println(buffer.toString());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeObjectStart() {
        out.println("<table>");
    }
    
    @Override
    public void writeValue(String string) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeElementEnd() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeElementStart(String elm) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeRecordStart(String recordElm, String suffix) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeRecordEnd() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeRootElementEnd() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeRootElementStart(String rootElm) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void flush() {
        out.flush();
    }
    
    @Override
    public void close() {
        if (out != null) {
            out.flush();
            out.close();
        }
    }
    
    @Override
    public void newlineWriter() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeAttribute(String name, String value) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeCData(String object) {
        // TODO Auto-generated method stub
        
    }
    
}
