package com.p3solutions.writer.utility.others;

import java.util.Formatter;
import java.util.function.Supplier;

import static com.p3solutions.writer.utility.others.Utility.isBlank;

public final class StringFormat implements Supplier<String> {
    
    private final String format;
    private final Object[] args;
    
    public StringFormat(final String format, final Object... args) {
        this.format = format;
        this.args = args;
    }
    
    @Override
    public String toString() {
        return get();
    }
    
    @Override
    public String get() {
        if (isBlank(format) || args == null || args.length == 0) {
            return format;
        }
        
        try (final Formatter formatter = new Formatter()) {
            return formatter.format(format, args).toString();
        }
    }
    
}
