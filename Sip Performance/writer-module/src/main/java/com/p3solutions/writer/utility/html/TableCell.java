package com.p3solutions.writer.utility.html;

import com.p3solutions.writer.options.TextOutputFormat;
import com.p3solutions.writer.utility.others.Color;

/**
 * Represents an HTML table cell.
 */
public class TableCell
        extends BaseTag {
    
    public TableCell(final String text, final boolean escapeText, final int characterWidth, final Alignment align,
                     final boolean emphasizeText, final String styleClass, final Color bgColor, final int colSpan,
                     final TextOutputFormat outputFormat) {
        super(text, escapeText, characterWidth, align, emphasizeText, styleClass, bgColor, outputFormat);
        if (colSpan > 1) {
            addAttribute("colspan", String.valueOf(colSpan));
        }
    }
    
    @Override
    protected String getTag() {
        return "td";
    }
    
}
