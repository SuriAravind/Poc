import org.json.JSONObject;
import org.json.XML;

/**
 * Created by Suriyanarayanan K
 * on 30/04/20 3:52 PM.
 */
public class JsonToXml {
    public static void main(String[] args) {
        String jsonString = "{\n" +
                "\t\"roles\": [\n" +
                "\t\t\"1\":{\n" +
                "\t\t\t\"id\": \"1\",\n" +
                "\t\t\t\"position\": \"head\",\n" +
                "\t\t\t\"salary\": \"10k\",\n" +
                "\t\t\t\"persons\": [\n" +
                "\t\t\t\t\"1\":{\n" +
                "\t\t\t\t\t\"id\": \"1\",\n" +
                "\t\t\t\t\t\"name\": \"Red\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\t\t\n" +
                "\t]\t\n" +
                "}";

        JSONObject json = new JSONObject(jsonString);
        String xml = XML.toString(json);

        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root>\n" + xml + "</root>");
    }
}
