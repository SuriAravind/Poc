package com.p3solutions.writer.formattingHelper;

//import static sf.util.Utility.isBlank;

import com.p3solutions.writer.options.TextOutputFormat;
import com.p3solutions.writer.utility.others.Color;

import java.io.PrintWriter;

/**
 * Methods to format entire rows of output as text.
 */
public class PlainTextFormattingHelper extends BaseTextFormattingHelper {
    
    public PlainTextFormattingHelper(final PrintWriter out, final TextOutputFormat outputFormat) {
        super(out, outputFormat);
    }
    
    @Override
    public String createLeftArrow() {
        return "<--";
    }
    
    @Override
    public String createRightArrow() {
        return "-->";
    }
    
    @Override
    public String createWeakLeftArrow() {
        return "<~~";
    }
    
    @Override
    public String createWeakRightArrow() {
        return "~~>";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDocumentEnd() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDocumentStart() {
    }
    
    @Override
    public void writeHeader(final DocumentHeaderType type, final String header) {
        /*if (!isBlank(header)) {
            final String defaultSeparator = separator("=");

            final String prefix;
            final String separator;
            if (type == null) {
                prefix = System.lineSeparator();
                separator = defaultSeparator;
            } else {
                switch (type) {
                    case title:
                        prefix = System.lineSeparator();
                        separator = separator("_");
                        break;
                    case subTitle:
                        prefix = System.lineSeparator();
                        separator = defaultSeparator;
                        break;
                    case section:
                        prefix = "";
                        separator = separator("-=-");
                        break;
                    default:
                        prefix = System.lineSeparator();
                        separator = defaultSeparator;
                        break;
                }
            }
            out.println(System.lineSeparator() + prefix + header + System.lineSeparator()
                    + separator + prefix);
        }*/
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeObjectEnd() {
        out.println();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeObjectNameRow(final String id, final String name, final String description,
                                   final Color backgroundColor) {
        // writeNameRow(name, description);
        // out.println(DASHED_SEPARATOR);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeObjectStart() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void writeValue(String value) {
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
