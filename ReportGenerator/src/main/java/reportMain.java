

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class reportMain {
    public static void main(String[] args) throws IOException {
        System.out.println("Report generation Started");

        //initialize inputs
        //title
        //location
        //isSummaryneed
        //isDetailedNeed
        //isFull
        //numberofsummarybyPage

        //summary header


        String title = "Service Name Data Extraction report";
        String location = "D:\\report_output\\";
        boolean isSummaryneed = true;
        boolean isDetailedNeed = true;
        boolean isFull = true;
        int numberOfSummaryPerPage = 3;

        Map<String, String> detailedHeader = new LinkedHashMap<>();
        detailedHeader.put("Test","CENTER");
        detailedHeader.put("Start Time","CENTER");
        detailedHeader.put("End Time","CENTER");
        detailedHeader.put("Execution Time","RIGHT");
        detailedHeader.put("Status","LEFT");
        detailedHeader.put("Message(If any)","LEFT");

        Map<String, String> summaryHeader = new LinkedHashMap<>();
        summaryHeader.put("Table","LEFT");
        summaryHeader.put("Column Data","LEFT");
        summaryHeader.put("Metadata","LEFT");
        summaryHeader.put("Table Data","LEFT");
        summaryHeader.put("Table Count","LEFT");
        summaryHeader.put("Table Row Count","LEFT");

        ReportGeneratorEngine reportGeneratorEngine = new ReportGeneratorEngine(title, location, isSummaryneed, isDetailedNeed, isFull, numberOfSummaryPerPage);
        reportGeneratorEngine.addReportHeader(summaryHeader,detailedHeader);
        



























//
//        List<String> columnHeader = new ArrayList<String>();
//        columnHeader.add("Tables Name");
//        columnHeader.add("Source count");
//        columnHeader.add("Destination Count");
//        columnHeader.add("Start Time");
//        columnHeader.add("End Time");
//
//        ReportGeneratorExecutable reportGenerator = new ReportGeneratorExecutable(title, "D:\\report_output\\");
//        reportGenerator.createStartPage();
//        reportGenerator.setColumnHeader(columnHeader);
//
//
//        List<String> columnValuesList = new ArrayList<String>();
//        columnValuesList.add("mysql");
//        columnValuesList.add("Port");
//        columnValuesList.add("3306");
//        columnValuesList.add("UserName");
//        columnValuesList.add("root");
//
//
//        List<String> columnValuesList1 = new ArrayList<String>();
//        columnValuesList1.add("mysql");
//        columnValuesList1.add("Port");
//        columnValuesList1.add("3306");
//        columnValuesList1.add("UserName");
//        columnValuesList1.add("root");
//
//
//        ///every table ending we have to call this method(setColumnValues with list value)
//        int i = 0;
//        while (i < 20) {
//            reportGenerator.setColumnValues(columnValuesList);
//            reportGenerator.setColumnValues(columnValuesList1);
//            i++;
//        }
//        reportGenerator.close();
        System.out.println("Report generation Ended");
    }
}
