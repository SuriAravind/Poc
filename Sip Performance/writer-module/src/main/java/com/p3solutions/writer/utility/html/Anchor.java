package com.p3solutions.writer.utility.html;


import com.p3solutions.writer.options.TextOutputFormat;
import com.p3solutions.writer.utility.others.Color;

import static com.p3solutions.writer.utility.others.Utility.isBlank;

/**
 * Represents an HTML link.
 */
public class Anchor extends BaseTag {
    
    public Anchor(final String text, final boolean escapeText, final int characterWidth, final Alignment align,
                  final boolean emphasizeText, final String styleClass, final Color bgColor, final String link,
                  final TextOutputFormat outputFormat) {
        super(text, escapeText, characterWidth, align, emphasizeText, styleClass, bgColor, outputFormat);
        if (!isBlank(link)) {
            addAttribute("href", link);
        }
    }
    
    @Override
    protected String getTag() {
        return "a";
    }
    
}
