import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ReportGeneratorEngine {

    String title;
    String location;
    boolean isSummaryNeed;
    boolean isDetailedNeed;
    boolean isFull;
    int numberOfSummaryPerPage;
    public static final String PDF_EXT = ".pdf";
    String jobId;
    String summaryPdfTemp;
    String detailsPdfTemp;
    String summaryPdfFinal;
    String detailsPdfFinal;
    String finalReportPdf;

    Document summaryDocumentTemp;
    Document detailsDocumentTemp;
    Document reportStartPage;

    public ReportGeneratorEngine(String title, String location, boolean isSummaryneed, boolean isDetailedNeed, boolean isFull, int numberOfSummaryPerPage) throws IOException {
        this.title = title;
        this.location = location;
        this.isSummaryNeed = isSummaryneed;
        this.isDetailedNeed = isDetailedNeed;
        this.isFull = isFull;
        this.numberOfSummaryPerPage = numberOfSummaryPerPage;
        this.reportStartPage=initiate(location+File.separator +"Start Page"+PDF_EXT);
        createStartPage();
        createTempDocument(isSummaryneed, isDetailedNeed);
    }

    private void createTempDocument(boolean isSummaryneed, boolean isDetailedNeed) throws FileNotFoundException {
        this.jobId = "123456789";

        if (isSummaryneed) {
            this.summaryPdfTemp = location + File.separator + "temp_summary_" + jobId.substring(jobId.length() - 5) + PDF_EXT;
            this.summaryPdfFinal = location + File.separator + "final_summary_" + jobId.substring(jobId.length() - 5) + PDF_EXT;
            this.summaryDocumentTemp = initiate(summaryPdfTemp);
        }
        if (isDetailedNeed) {
            this.detailsPdfTemp = location + File.separator + "temp_details_" + jobId.substring(jobId.length() - 5) + PDF_EXT;
            this.detailsPdfFinal = location + File.separator + "final_details_" + jobId.substring(jobId.length() - 5) + PDF_EXT;
            this.detailsDocumentTemp = initiate(detailsPdfTemp);
        }
        if (isFull) {
            this.finalReportPdf = location + File.separator + "Chain-of-custody-Report_" + jobId.substring(jobId.length() - 5) + PDF_EXT;
        }
    }

    private Document initiate(String dest) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        return new Document(pdfDoc);
    }

    public void createStartPage() throws IOException {
        reportStartPage.add(new Paragraph(new Text(title).setFontSize(20)).setTextAlignment(TextAlignment.CENTER));
        ImageData img = ImageDataFactory.create(getWaterMark(IconConstant.WATERMARK_IMG));
        float w = img.getWidth();
        float h = img.getHeight();
        Rectangle pagesize = reportStartPage.getPdfDocument().getPage(1).getPageSize();
        float x = (pagesize.getLeft() + pagesize.getRight()) / 2;
        float y = (pagesize.getTop() + pagesize.getBottom()) / 2;
        reportStartPage.add(new Image(ImageDataFactory.create(getWaterMark(IconConstant.WATERMARK_IMG)), x - (w / 2), y - (h / 2)));
        addEmptyLines(reportStartPage, 25);
        reportStartPage.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE));
        reportStartPage.add(new Paragraph(new Text("Job Id : " + UUID.randomUUID().toString()).setFontSize(8)));
        reportStartPage.add(new Paragraph(new Text("Report Generated Date : " + new Date().toGMTString()).setFontSize(8)));
        reportStartPage.add(new Paragraph(new Text("Note: This is a system generated Report").setFontSize(6)));
        reportStartPage.close();
    }
    private void addEmptyLines(Document doc, int n) {
        for (int i = 0; i < n; i++)
            doc.add(new Paragraph(new Text("\n")));
    }
    public byte[] getWaterMark(String imageName) throws IOException {
        BufferedImage bImage = ImageIO.read(new File(imageName));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);
        byte[] watermark = bos.toByteArray();
        return watermark;
    }
    public void addReportHeader(Map<String, String> summaryHeader, Map<String, String> detailedHeader) {

    }
}
