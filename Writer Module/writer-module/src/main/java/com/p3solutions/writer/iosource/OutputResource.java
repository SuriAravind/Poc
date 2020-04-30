package com.p3solutions.writer.iosource;


import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public interface OutputResource {
    
    Writer openNewOutputWriter(Charset charset, boolean appendOutput) throws IOException;
    
}
