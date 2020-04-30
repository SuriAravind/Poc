package test.utility.html;


import test.options.TextOutputFormat;
import test.utility.others.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static test.utility.html.Entities.escapeForLineBreaks;
import static test.utility.html.Entities.escapeForXMLElement;
import static test.utility.others.Utility.isBlank;

/**
 * Represents an HTML anchor.
 */
abstract class BaseTag {
    
    private final TextOutputFormat outputFormat;
    private final String styleClass;
    private final int characterWidth;
    private final Alignment align;
    private final String text;
    private final boolean escapeText;
    private final Color bgColor;
    private final boolean emphasizeText;
    private final Map<String, String> attributes;
    
    protected BaseTag(final String text, final boolean escapeText, final int characterWidth, final Alignment align,
                      final boolean emphasizeText, final String styleClass, final Color bgColor,
                      final TextOutputFormat outputFormat) {
        this.outputFormat = outputFormat;
        this.styleClass = styleClass;
        this.text = text == null ? "-NULL-" : text;
        this.escapeText = escapeText;
        this.characterWidth = characterWidth;
        this.align = align;
        this.bgColor = bgColor;
        this.emphasizeText = emphasizeText;
        attributes = new HashMap<>();
    }
    
    public String addAttribute(final String key, final String value) {
        return attributes.put(key, value);
    }
    
    /**
     * Converts the table cell to HTML.
     *
     * @return HTML
     */
    @Override
    public String toString() {
        if (outputFormat == TextOutputFormat.html) {
            return toHtmlString();
        } else {
            return toPlainTextString();
        }
    }
    
    /**
     * Converts the tag to HTML.
     *
     * @return HTML
     */
    private String toHtmlString() {
        final StringBuilder buffer = new StringBuilder(1024);
        buffer.append("<").append(getTag());
        for (final Entry<String, String> attribute : attributes.entrySet()) {
            buffer.append(" ").append(attribute.getKey()).append("='")
                    .append(attribute.getValue()).append("'");
        }
        if (bgColor != null && !bgColor.equals(Color.white)) {
            buffer.append(" bgcolor='").append(bgColor).append("'");
        }
        if (!isBlank(styleClass)) {
            buffer.append(" class='").append(styleClass).append("'");
        } else if (align != null && align != Alignment.inherit) {
            buffer.append(" align='").append(align).append("'");
        }
        buffer.append(">");
        if (emphasizeText) {
            buffer.append("<b><i>");
        }
        buffer.append(escapeForLineBreaks(escapeText ? escapeForXMLElement(text) : text));
        if (emphasizeText) {
            buffer.append("</i></b>");
        }
        buffer.append("</").append(getTag()).append(">");
        
        return buffer.toString();
    }
    
    /**
     * Converts the tag to text.
     *
     * @return Text
     */
    private String toPlainTextString() {
        if (outputFormat == TextOutputFormat.csv || outputFormat == TextOutputFormat.txt ||
            outputFormat == TextOutputFormat.text || outputFormat == TextOutputFormat.tsv) {
            return escapeText ? escapeAndQuoteCsv(text) : text;
        } else if (outputFormat == TextOutputFormat.tsv) {
            return text;
        } else {
            if (characterWidth > 0) {
                final String format = String
                                              .format("%%%s%ds", align == Alignment.right ? "" : "-", characterWidth);
                return String.format(format, text);
            } else {
                return text;
            }
        }
    }
    
    protected abstract String getTag();
    
    /**
     * Enclose the value in quotes and escape the quote and comma
     * characters that are inside.
     *
     * @param text
     *         Text that needs to be escaped and quoted
     *
     * @return Text, escaped and quoted.
     */
    private static String escapeAndQuoteCsv(final String text) {
        final char QUOTE = '\"';
        final char SEPARATOR = ',';
        final char NEWLINE = '\n';
        
        final String value = String.valueOf(text);
        final int length = value.length();
        if (length == 0) {
            return "\"\"";
        }
        
        if (value.indexOf(SEPARATOR) < 0 && value.indexOf(QUOTE) < 0 && value.indexOf(NEWLINE) < 0) {
            return value;
        }
        
        final StringBuilder sb = new StringBuilder(length);
        sb.append(QUOTE);
        for (int i = 0; i < length; i++) {
            final char c = value.charAt(i);
            if (c == QUOTE) {
                sb.append(QUOTE).append(c);
                //            } else if (c == NEWLINE) {
                //                sb.append(CARRIAGE_RETURN);
            } else {
                sb.append(c);
            }
        }
        sb.append(QUOTE);
        
        return sb.toString();
    }
    
}
