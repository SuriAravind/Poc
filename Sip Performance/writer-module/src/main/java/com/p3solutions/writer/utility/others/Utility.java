package com.p3solutions.writer.utility.others;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Utility methods.
 */
public final class Utility {
    
    public static final Predicate<String> filterOutBlank = word -> !isBlank(word);
    private static final Logger LOGGER = Logger.getLogger(Utility.class.getName());
    
    private Utility() { // Prevent instantiation
    }
    
    public static String commonPrefix(final String string1, final String string2) {
        final int index = indexOfDifference(string1, string2);
        if (index == -1) {
            return null;
        } else {
            return string1.substring(0, index).toLowerCase();
        }
    }
    
    private static int indexOfDifference(final String string1,
                                         final String string2) {
        if (string1 == null || string2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < string1.length() && i < string2.length(); ++i) {
            if (string1.charAt(i) != string2.charAt(i)) {
                break;
            }
        }
        if (i < string2.length() || i < string1.length()) {
            return i;
        }
        return -1;
    }
    
    /**
     * Checks if the text contains whitespace.
     *
     * @param text
     *         Text to check.
     *
     * @return Whether the string contains whitespace.
     */
    public static boolean containsWhitespace(final CharSequence text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        
        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    public static String convertForComparison(final String text) {
        if (text == null || text.length() == 0) {
            return text;
        }
        
        final StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            final char ch = text.charAt(i);
            if (Character.isLetterOrDigit(ch) || ch == '_' || ch == '.') {
                builder.append(Character.toLowerCase(ch));
            }
        }
        
        final String textWithoutQuotes = builder.toString();
        return textWithoutQuotes;
    }
    
    public static <E extends Enum<E>> E enumValue(final String value, final E defaultValue) {
        requireNonNull(defaultValue, "No default value provided");
        E enumValue;
        if (value == null) {
            enumValue = defaultValue;
        } else {
            try {
                Class<?> enumClass = defaultValue.getClass();
                if (enumClass.getEnclosingClass() != null) {
                    enumClass = enumClass.getEnclosingClass();
                }
                enumValue = Enum.valueOf((Class<E>) enumClass, value);
            } catch (final Exception e) {
                enumValue = defaultValue;
            }
        }
        return enumValue;
    }
    
    public static <E extends Enum<E> & IdentifiedEnum> E enumValueFromId(final int value, final E defaultValue) {
        requireNonNull(defaultValue, "No default value provided");
        try {
            final Class<E> enumClass = (Class<E>) defaultValue.getClass();
            for (final E enumValue : EnumSet.allOf(enumClass)) {
                if (enumValue.getId() == value) {
                    return enumValue;
                }
            }
        } catch (final Exception e) {
            // Ignore
        }
        return defaultValue;
    }
    
    /**
     * Checks if the text is null or empty.
     *
     * @param text
     *         Text to check.
     *
     * @return Whether the string is blank.
     */
    public static boolean isBlank(final CharSequence text) {
        if (text == null || text.length() == 0) {
            return true;
        }
        
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the text contains an integer only.
     *
     * @param text
     *         Text to check.
     *
     * @return Whether the string is an integer.
     */
    public static boolean isIntegral(final CharSequence text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        
        for (int i = 0; i < text.length(); i++) {
            final char ch = text.charAt(i);
            if (!Character.isDigit(ch) && ch != '+' && ch != '-') {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the text is all lowercase.
     *
     * @param text
     *         Text to check.
     *
     * @return Whether the string is all lowercase.
     */
    public static boolean isLowerCase(final String text) {
        return text != null && text.equals(text.toLowerCase());
    }
    
    public static String join(final Map<String, String> map, final String separator) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        
        final StringBuilder buffer = new StringBuilder(1024);
        final Set<Entry<String, String>> entrySet = map.entrySet();
        for (final Iterator<Entry<String, String>> iterator = entrySet
                                                                      .iterator(); iterator.hasNext(); ) {
            final Entry<String, String> entry = iterator.next();
            buffer.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                buffer.append(separator);
            }
        }
        
        return buffer.toString();
    }
    
    public static String join(final String[] collection, final String separator) {
        return join(Arrays.asList(collection), separator);
    }
    
    public static String join(final Collection<String> collection, final String separator) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        
        final StringBuilder buffer = new StringBuilder(1024);
        for (final Iterator<String> iterator = collection.iterator(); iterator.hasNext(); ) {
            buffer.append(iterator.next());
            if (iterator.hasNext()) {
                buffer.append(separator);
            }
        }
        
        return buffer.toString();
    }
    
    public static String readResourceFully(final String resource) {
        return readFully(Utility.class.getResourceAsStream(resource));
    }
    
    public static String readFully(final InputStream stream) {
        if (stream == null) {
            return null;
        }
        final Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        return readFully(reader);
    }
    
    /**
     * Reads the stream fully, and returns a byte array of data.
     *
     * @param reader
     *         Reader to read.
     *
     * @return Byte array
     */
    public static String readFully(final Reader reader) {
        if (reader == null) {
            LOGGER.log(Level.WARNING, "Cannot read null reader");
            return "";
        }
        
        try {
            final StringWriter writer = new StringWriter();
            copy(reader, writer);
            writer.close();
            return writer.toString();
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return "";
        }
        
    }
    
    /**
     * Reads the stream fully, and writes to the writer.
     *
     * @param reader
     *         Reader to read.
     *
     * @return Byte array
     */
    public static void copy(final Reader reader, final Writer writer) {
        if (reader == null) {
            LOGGER.log(Level.WARNING, "Cannot read null reader");
            return;
        }
        if (writer == null) {
            LOGGER.log(Level.WARNING, "Cannot write null writer");
            return;
        }
        
        final char[] buffer = new char[0x10000];
        try {
            // Do not close resources - that is the responsibility of the
            // caller
            final Reader bufferedReader = new BufferedReader(reader, buffer.length);
            final BufferedWriter bufferedWriter = new BufferedWriter(writer,
                                                                     buffer.length);
            
            int read;
            do {
                read = bufferedReader.read(buffer, 0, buffer.length);
                if (read > 0) {
                    bufferedWriter.write(buffer, 0, read);
                }
            }
            while (read >= 0);
            
            bufferedWriter.flush();
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }
    
    /**
     * Sets the application-wide log level.
     *
     * @param logLevel
     *         Log level to set
     */
    public static void setApplicationLogLevel(final Level logLevel) {
        final LogManager logManager = LogManager.getLogManager();
        final List<String> loggerNames = Collections
                                                 .list(logManager.getLoggerNames());
        for (final String loggerName : loggerNames) {
            final Logger logger = logManager.getLogger(loggerName);
            if (logger != null) {
                logger.setLevel(null);
                for (final Handler handler : logger.getHandlers()) {
                    handler.setLevel(logLevel);
                }
            }
        }
        
        final Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(logLevel);
    }
    
    public static String numberFormatter(int i) {
        return String.format("%08d", i);
    }
    
    public static String checkValidFolder(String name) {
        if (name != null) {
            return getFolderFormatted(name);
        }
        return "unnamed";
    }
    
    public static String getFolderFormatted(String string) {
        string = string.trim().replaceAll("[^_^\\p{Alnum}.]", "_").replace("^", "_").replaceAll("\\s+", "_")
                         .toUpperCase();
        string = (
                (string.startsWith("_") && string.endsWith("_") && string.length() > 2)
                ? string.substring(1).substring(0, string.length() - 2)
                : string);
        return string;
    }
    
    public static String checkValidFile(String name) {
        if (name != null) {
            return getFileFormatted(name);
        }
        return "unnamed";
    }
    
    public static String getFileFormatted(String string) {
        string = string.trim().replaceAll("[^_^\\p{Alnum}.]", "_").replace("^", "_").replaceAll("\\s+", "_");
        string = (
                (string.startsWith("_") && string.endsWith("_") && string.length() > 2)
                ? string.substring(1).substring(0, string.length() - 2)
                : string);
        return string;
    }
    
    public static String getTextFormatted(String string) {
        string = string.trim().replaceAll("[^_^\\p{Alnum}.]", "_").replace("^", "_").replaceAll("\\s+", "_");
        string = (
                (string.startsWith("_") && string.endsWith("_") && string.length() > 2) ?
                string.substring(1).substring(0, string.length() - 2) : string);
        return string.length() > 0 ? ((string.charAt(0) >= '0' && string.charAt(0) <= '9') ? "_" : "") + string :
               string;
    }

    public static String rightPadding(String str, int num) {
        return String.format("%1$-" + num + "s", str);
    }

    public static String leftPadding(String str, int num) {
        return "                                                         ".substring(0, (num - str.length())) + str;
    }

    public static String timeDiff(long diff) {
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        String dateFormat = "";
        if (diffDays > 0) {
            dateFormat += diffDays + " day ";
        }
        diff -= diffDays * (24 * 60 * 60 * 1000);

        int diffhours = (int) (diff / (60 * 60 * 1000));
        if (diffhours > 0) {
            dateFormat += leftNumPadding(diffhours, 2) + " hour ";
        } else if (dateFormat.length() > 0) {
            dateFormat += "00 hour ";
        }
        diff -= diffhours * (60 * 60 * 1000);

        int diffmin = (int) (diff / (60 * 1000));
        if (diffmin > 0) {
            dateFormat += leftNumPadding(diffmin, 2) + " min ";
        } else if (dateFormat.length() > 0) {
            dateFormat += "00 min ";
        }

        diff -= diffmin * (60 * 1000);

        int diffsec = (int) (diff / (1000));
        if (diffsec > 0) {
            dateFormat += leftNumPadding(diffsec, 2) + " sec ";
        } else if (dateFormat.length() > 0) {
            dateFormat += "00 sec ";
        }

        int diffmsec = (int) (diff % (1000));
        dateFormat += leftNumPadding(diffmsec, 3) + " ms";
        return dateFormat;
    }

    private static String leftNumPadding(int str, int num) {
        return String.format("%0" + num + "d", str);
    }
}
