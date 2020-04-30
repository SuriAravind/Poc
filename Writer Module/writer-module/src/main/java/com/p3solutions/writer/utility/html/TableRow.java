package com.p3solutions.writer.utility.html;


import com.p3solutions.writer.options.TextOutputFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents an HTML table row.
 */
public final class TableRow {

    private final TextOutputFormat outputFormat;
    private final List<TableCell> cells;

    public TableRow(final TextOutputFormat outputFormat) {
        this.outputFormat = outputFormat;
        cells = new ArrayList<>();
    }

    public TableRow add(final TableCell cell) {
        cells.add(cell);
        return this;
    }

    public TableCell firstCell() {
        if (cells.isEmpty()) {
            return null;
        }
        return cells.get(0);
    }

    public TableCell lastCell() {
        if (cells.isEmpty()) {
            return null;
        }
        return cells.get(cells.size() - 1);
    }

    /**
     * Converts the table row to HTML.
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
     * Converts the table row to HTML.
     *
     * @return HTML
     */
    private String toHtmlString() {
        final StringBuilder buffer = new StringBuilder(1024);
        buffer.append("\t<tr>").append(System.lineSeparator());
        for (final TableCell cell : cells) {
            buffer.append("\t\t").append(cell).append(System.lineSeparator());
        }
        buffer.append("\t</tr>");

        return buffer.toString();
    }

    /**
     * Converts the table row to CSV.
     *
     * @return CSV
     */
    private String toPlainTextString() {
        final StringBuilder buffer = new StringBuilder(1024);

        List<Integer> ij = IntStream.range(0, cells.size()).boxed().collect(Collectors.toList());

        for (int i = 0; i < cells.size(); i++) {
            final TableCell cell = cells.get(i);
            if (i > 0) {
                buffer.append(getFieldSeparator());
            }
            buffer.append(cell.toString());
        }

        return buffer.toString();
    }

    private String getFieldSeparator() {
        String fieldSeparator;
        switch (outputFormat) {
            case txt:
            case csv:
                fieldSeparator = ",";
                break;
            case tsv:
                fieldSeparator = "\t";
                break;
            default:
                fieldSeparator = "  ";
        }
        return fieldSeparator;
    }

}