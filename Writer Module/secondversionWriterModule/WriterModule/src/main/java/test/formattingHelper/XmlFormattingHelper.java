package test.formattingHelper;


import org.apache.xerces.util.XMLChar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.options.TextOutputFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TreeMap;
import java.util.TreeSet;

import static test.utility.others.Utility.getTextFormatted;

public class XmlFormattingHelper extends PlainTextFormattingHelper {
    
    private static TreeMap<Integer, TreeMap<String, TreeSet<Integer>>> arMap;
    private static TreeMap<String, String> charReplace = null;
    XMLOutputFactory factory;
    XMLStreamWriter writer;
    PrintWriter writerConsolidate;
    TextOutputFormat outputFormatRef;
    
    public XmlFormattingHelper(final PrintWriter out, final TextOutputFormat outputFormat) {
        super(out, outputFormat);
        outputFormatRef = outputFormat;
        factory = XMLOutputFactory.newInstance();
        try {
            writer = factory.createXMLStreamWriter(out);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public static TreeMap<String, String> getCharReplace() {
        return charReplace;
    }
    
    public static void setCharReplace(TreeMap<String, String> charReplace) {
        XmlFormattingHelper.charReplace = charReplace;
    }
    
    public void writeDocumentEnd() {
        try {
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeDocumentStart() {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeValue(String value) {
        try {
            writer.writeCharacters(stripNonValidXMLCharacters(value));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeElementEnd() {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeElementStart(String columnName) {
        try {
            writer.writeCharacters("\n\t\t");
            writer.writeStartElement(getTextFormatted(columnName.toUpperCase()));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeRecordStart(String tablename, String suffix) {
        try {
            writer.writeCharacters("\n\t");
            writer.writeStartElement(getTextFormatted((tablename + suffix).toUpperCase()));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeRecordEnd() {
        try {
            writer.writeCharacters("\n\t");
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeRootElementEnd() {
        try {
            writer.writeCharacters("\n");
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeRootElementStart(String tablename) {
        try {
            writer.writeCharacters("\n");
            writer.writeStartElement(getTextFormatted(tablename.toUpperCase()));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void newlineWriter() {
        try {
            writer.writeCharacters("\n");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeAttribute(String name, String value) {
        try {
            writer.writeAttribute(getTextFormatted(name), value);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public void writeCData(String value) {
        try {
            if (value == null) {
                writeValue("");
            } else if (value.equals("")) {
                writer.writeCData("");
            } else {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                Element rootElement = document.createElement("_");
                document.appendChild(rootElement);
                rootElement.appendChild(document.createCDATASection(stripNonValidXMLCharacters(value)));
                String xmlString = getXMLFromDocument(document);
                writer.writeCData(xmlString.substring(0, xmlString.length() - 7).substring(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void flushOutput() {
        try {
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    
    public String getXMLFromDocument(Document document) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StreamResult streamResult = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(document);
        transformer.transform(source, streamResult);
        return streamResult.getWriter().toString();
    }
    
    private String stripNonValidXMLCharacters(String in) {
        if (in == null || ("".equals(in))) {
            return null;
        }
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (XMLChar.isValid(c)) {
                out.append(c);
            } else {
                out.append(checkReplace(in.codePointAt(i), true));
            }
        }
        return (out.toString().trim());
    }
    
    private String checkReplace(int key, boolean extract) {
        if (getCharReplace() != null && getCharReplace().containsKey(key + "")) {
            return getCharReplace().get(key + "");
        }
        return "";
    }
}
