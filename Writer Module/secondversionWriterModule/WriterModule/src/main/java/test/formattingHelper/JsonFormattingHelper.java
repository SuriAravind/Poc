package test.formattingHelper;


import test.exceptions.WriterException;
import test.options.TextOutputFormat;
import test.utility.json.JSONException;
import test.utility.json.JSONObject;

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
