package Dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Created by Suriyanarayanan K
 * on 27/04/20 12:34 PM.
 */
public class Domparser {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        String FILE_PATH = "src/main/resources/dom/test.xml";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File("src/main/resources/dom/test.xml"));
        doc.getDocumentElement().normalize();

        NodeList nList = null;
        nList = doc.getElementsByTagName("Call");
        for (int indx = 0; indx < nList.getLength(); indx++) {
            Element eElement = (Element) nList.item(indx);
            if (eElement.getAttribute("id").equalsIgnoreCase("1")) {
                System.out.println("Attr id :" + eElement.getAttribute("id"));
                System.out.println("SentToArchiveDate" + eElement.getElementsByTagName("SentToArchiveDate").item(indx).getTextContent());
                System.out.println("CallStartDate" + eElement.getElementsByTagName("CallStartDate").item(indx).getTextContent());
                System.out.println("CallEndDate" + eElement.getElementsByTagName("CallEndDate").item(indx).getTextContent());
                NodeList childList = null;
                childList = doc.getElementsByTagName("Attachments");
                for (int indx1 = 0; indx1 < nList.getLength(); indx1++) {
                    Element childElement = (Element) nList.item(indx);
                    System.out.println("AttachmentName :" + childElement.getElementsByTagName("AttachmentName").item(indx).getTextContent());
                    System.out.println("FileName :" + childElement.getElementsByTagName("FileName").item(indx).getTextContent());
                    System.out.println("CreatedBy :" + childElement.getElementsByTagName("CreatedBy").item(indx).getTextContent());
                }
                Node child = doc.createElement("Attachment");
                ((Element) child).setAttribute("attr_name","attr_value");
                Element age = doc.createElement("AttachmentName");
                age.appendChild(doc.createTextNode("recording"));
                Element age1 = doc.createElement("FileName");
                age1.appendChild(doc.createTextNode("recording11.mp3"));
                Element age2 = doc.createElement("CreatedBy");
                age2.appendChild(doc.createTextNode("PhoneRecorder"));
                Element age3 = doc.createElement("CreatedOnDate");
                age3.appendChild(doc.createTextNode("2001-11-02T21:50:07.104+01:00"));
                child.appendChild(age);
                child.appendChild(age1);
                child.appendChild(age2);
                child.appendChild(age3);
                childList.item(indx).appendChild(child);
            }
        }
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(FILE_PATH));
        transformer.transform(source, result);
    }
}
