package com.p3solutions.writer.utility.html;

import com.p3solutions.writer.options.TextOutputFormat;
import com.p3solutions.writer.utility.others.Color;

public final class TableHeaderCell
        extends TableCell {
    
    public TableHeaderCell(final String text, final int characterWidth, final Alignment align,
                           final boolean emphasizeText, final String styleClass, final Color bgColor,
                           final int colSpan, final TextOutputFormat outputFormat) {
        super(text, true, characterWidth, align, emphasizeText, styleClass, bgColor, colSpan, outputFormat);
    }
    
    @Override
    protected String getTag() {
        return "th";
    }
    
}
