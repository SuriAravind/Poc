package com.p3solutions.writer.formattingHelper;


import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.options.TextOutputFormat;
import com.p3solutions.writer.utility.json.JSONException;
import com.p3solutions.writer.utility.json.JSONObject;

import java.io.PrintWriter;

public class JsonFormattingHelper
        extends PlainTextFormattingHelper {
    
    public JsonFormattingHelper(final PrintWriter out,
                                final TextOutputFormat outputFormat) {
        super(out, outputFormat);
    }
    
    public void write(final JSONObject jsonObject)
            throws WriterException {
        try {
            jsonObject.write(out, 2);
        } catch (final JSONException e) {
            throw new WriterException("Could not write database", e);
        }
    }
    
}
