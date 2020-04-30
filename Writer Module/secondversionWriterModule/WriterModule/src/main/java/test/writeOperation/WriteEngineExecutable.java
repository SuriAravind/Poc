package test.writeOperation;

import test.options.ColumnInfo;
import test.options.JavaDataType;
import test.options.Options;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static test.options.TextOutputFormat.xml;

public final class WriteEngineExecutable {
    
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    
    private final Options options;
    private WriteEngineHandler createdHandler;
    private String title;
    private List<ColumnInfo> columns;
    
    public WriteEngineExecutable(Options options, String title, List<ColumnInfo> columnsInfo) {
        this.options = options;
        this.title = title;
        processColumns(columnsInfo);
    }
    
    private void processColumns(List<ColumnInfo> columnsInfo) {
        columns = new ArrayList<>();
        if (options.isSplitDate()) {
            for (ColumnInfo columninfo : columnsInfo) {
                switch (columninfo.getCategory()) {
                    case DATE:
                    case DATETIME:
                        columns.add(columninfo);
                        columns.add(new ColumnInfo(columninfo.getColumn() + "_DT", "ARCHON_DATE",
                                                   JavaDataType.ARCHON_DATE));
                        columns.add(new ColumnInfo(columninfo.getColumn() + "_DTM", "ARCHON_DATE_TIME",
                                                   JavaDataType.ARCHON_DATE_TIME));
                        columns.add(new ColumnInfo(columninfo.getColumn() + "_DTMX", "ARCHON_DATE_TIME_XML",
                                                   JavaDataType.ARCHON_DATE_TIME_XML));
                        break;
                    default:
                        columns.add(columninfo);
                }
            }
        } else {
            columns.addAll(columnsInfo);
        }
        LOGGER.log(Level.INFO, "Column List - " + columns.stream().map(ColumnInfo::getColumn).collect(
                Collectors.joining(", ")));
    }
    
    public WriteEngineHandler getWriteEngineHandler() throws Exception {
        final WriteEngineHandler handler = getExecutionHandler();
        createdHandler = handler;
        return handler;
    }
    
    private WriteEngineHandler getExecutionHandler() throws Exception {
        switch (options.getOutputFormatValue()) {
            case xml:
                return new XmlFormatterEngine(this.options, title, columns);
            case json:
                return new JsonFormatterEngine(this.options, title, columns);
            default:
                return new TextFormatterEngine(this.options, title, columns);
        }
    }
    
    public void generateReport() {
        LOGGER.log(Level.INFO, "Records Processed : " + title + " : " + options.getRecordsProcessed());
    }
}
