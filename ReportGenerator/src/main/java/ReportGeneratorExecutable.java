import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

public class ReportGeneratorExecutable {

    int FONT_SIZE = 9;
    Document document;
    String location;
    String exactLocation;
    String title;
    Table table;
    PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
    PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

    public ReportGeneratorExecutable(String title, String location) throws IOException {
        System.out.println(new Date().toGMTString());
        this.location = location + "Data_Extraction_Report_temp.pdf";
        this.exactLocation = location + "Data_Extraction_Report_Final.pdf";
        this.title = title;
    }

    public void createStartPage() throws IOException {
        PdfWriter writer = new PdfWriter(location);
        PdfDocument pdf = new PdfDocument(writer);
        document = new Document(pdf);
        document.add(new Paragraph(new Text(title).setFontSize(20)).setTextAlignment(TextAlignment.CENTER));
        ImageData img = ImageDataFactory.create(getWaterMark());
        float w = img.getWidth();
        float h = img.getHeight();
        Rectangle pagesize = document.getPdfDocument().getPage(1).getPageSize();
        float x = (pagesize.getLeft() + pagesize.getRight()) / 2;
        float y = (pagesize.getTop() + pagesize.getBottom()) / 2;
        document.add(new Image(ImageDataFactory.create(getWaterMark()), x - (w / 2), y - (h / 2)));
        addEmptyLines(document, 25);
        document.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE));
        document.add(new Paragraph(new Text("Job Id : " + UUID.randomUUID().toString()).setFontSize(8)));
        document.add(new Paragraph(new Text("Report Generated Date : " + new Date().toGMTString()).setFontSize(8)));
        document.add(new Paragraph(new Text("Note: This is a system generated Report").setFontSize(6)));
    }
    private void addEmptyLines(Document doc, int n) {
        for (int i = 0; i < n; i++)
            doc.add(new Paragraph(new Text("\n")));
    }

    public byte[] getWaterMark() throws IOException {
        BufferedImage bImage = ImageIO.read(new File("archon.png"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);
        byte[] watermark = bos.toByteArray();
        return watermark;
    }

    public void finalReport() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(location), new PdfWriter(exactLocation));
        ImageData img = ImageDataFactory.create(getWaterMark());
        float w = img.getWidth();
        float h = img.getHeight();
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.1f);
        PdfCanvas over;
        int numberOfPages = pdfDoc.getNumberOfPages();
        for (int i = 2; i <= numberOfPages; i++) {
            PdfPage pdfPage = pdfDoc.getPage(i);
            pdfPage.setIgnorePageRotationForContent(true);
            over = new PdfCanvas(pdfDoc.getPage(i));
            over.saveState();
            over.setExtGState(gs1);
            over.addImage(img, w, 0, 0, h, 550 - w, 175 - h, false);
            over.restoreState();
        }
        pdfDoc.close();
        File file = new File(location);
        file.delete();
    }

    public void process( String line, PdfFont font, boolean isHeader) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(new Cell().add(new Paragraph(tokenizer.nextToken().toUpperCase())
                        .setFont(font).setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(FONT_SIZE))
                        .setBackgroundColor(isHeader ? Color.GRAY : Color.WHITE)
                        .setFontColor(isHeader ? Color.WHITE : Color.BLACK)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER));
            } else {
                table.addCell(new Cell().add(new Paragraph(tokenizer.nextToken())
                        .setFont(font)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(FONT_SIZE))
                        .setBackgroundColor(isHeader ? Color.GRAY : Color.WHITE)
                        .setFontColor(isHeader ? Color.WHITE : Color.BLACK)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER));
            }
        }

    }

    public void setColumnHeader(List<String> columnHeader) {
        float cellWidth = (500 / (columnHeader.size() ));
        float[] pointColumnWidths = new float[columnHeader.size() ];
        for (int i = 0; i < pointColumnWidths.length; i++)
            pointColumnWidths[i] = cellWidth;
        table = new Table(pointColumnWidths);
        table.setWidthPercent(100);
        process(String.join(",", columnHeader), bold, true);
        document.setMargins(20, 20, 20, 20);
        document.add(new AreaBreak());
        addEmptyLines(document, 1);
        document.add(new Paragraph("Extraction Table Details"));
        addEmptyLines(document, 1);
    }

    public void setColumnValues(List<String> columnValuesList) {
        process(String.join(",", columnValuesList), font, false);
    }

    public void close() throws IOException {
        document.add(table);
        document.close();
        finalReport();
        System.out.println(new Date().toGMTString());
    }
}
