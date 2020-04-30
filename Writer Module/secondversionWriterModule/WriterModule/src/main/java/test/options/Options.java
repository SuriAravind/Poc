package test.options;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.exceptions.WriterException;
import test.iosource.CompressedFileOutputResource;
import test.iosource.ConsoleOutputResource;
import test.iosource.FileOutputResource;
import test.iosource.OutputResource;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.TreeMap;

import static java.util.Objects.requireNonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Options {
    // supporting
    private static final SecureRandom random = new SecureRandom();

    // config
    @Builder.Default
    private boolean splitDate = false;
    @Builder.Default
    private boolean appendOutput = false;

    // Split
    @Builder.Default
    private long fileSplitBySize = 100 * 1024 * 1024; // 100 MB
    @Builder.Default
    private long FileSplitByRecords = 99999999;
    @Builder.Default
    private long thresholdSize = 2 * 1024 * 1024; // 2MB

    // Charset
    @Builder.Default
    private Charset inputEncodingCharset = StandardCharsets.UTF_8;
    @Builder.Default
    private Charset outputEncodingCharset = StandardCharsets.UTF_8;

    // Others
    private OutputResource outputResource;
    private TextOutputFormat outputFormatValue;

    // reports
    @Builder.Default
    private long recordsProcessed = 0;
    @Builder.Default
    private int fileCounter = 0;
    @Builder.Default
    private TreeMap<String, Long> srcColumnBlobCount = new TreeMap<String, Long>();
    @Builder.Default
    private TreeMap<String, Long> destColumnBlobCount = new TreeMap<String, Long>();

    @Builder.Default
    private SipConfigOptions sipConfigOptions = new SipConfigOptions();


    /**
     * Character encoding for input files, such as scripts and templates.
     */
    public Charset getInputCharset() {
        if (inputEncodingCharset == null) {
            return StandardCharsets.UTF_8;
        } else {
            return inputEncodingCharset;
        }
    }

    /**
     * Gets the output reader. If the output resource is null, first set it to
     * console output.
     *
     * @throws WriterException
     */
    public Writer openNewOutputWriter(final boolean appendOutput) throws IOException {
        return obtainOutputResource().openNewOutputWriter(getOutputCharset(), appendOutput);
    }

    /**
     * Gets the output resource. If the output resource is null, first set it to
     * console output.
     */
    private OutputResource obtainOutputResource() {
        if (outputResource == null) {
            outputResource = new ConsoleOutputResource();
        }
        return outputResource;
    }

    /**
     * Character encoding for output files.
     */
    public Charset getOutputCharset() {
        if (outputEncodingCharset == null) {
            return StandardCharsets.UTF_8;
        } else {
            return outputEncodingCharset;
        }
    }


    public Path getOutputFile() {
        final Path outputFile;
        if (outputResource instanceof FileOutputResource) {
            outputFile = ((FileOutputResource) outputResource).getOutputFile();
        } else if (outputResource instanceof CompressedFileOutputResource) {
            outputFile = ((CompressedFileOutputResource) outputResource).getOutputFile();
        } else {
            // Tacky hack for htmlx format
            final String extension;
            if ("htmlx".equals(outputFormatValue)) {
                extension = "svg.html";
            } else {
                extension = outputFormatValue.getFormat();
            }
            // Create output file path
            outputFile = Paths.get(".", String.format("sc.%s.%s", nextRandomString(), extension)).normalize()
                    .toAbsolutePath();
        }
        return outputFile;
    }

    private String nextRandomString() {
        final int length = 8;
        return new BigInteger(length * 5, random).toString(32);
    }

    /**
     * Sets the name of the output file. It is important to note that the output
     * encoding should be available at this point.
     *
     * @param outputFile Output file name.
     */
    public void setOutputFile(final Path outputFile) {
        requireNonNull(outputFile, "No output file provided");
        outputResource = new FileOutputResource(outputFile);
    }
}
