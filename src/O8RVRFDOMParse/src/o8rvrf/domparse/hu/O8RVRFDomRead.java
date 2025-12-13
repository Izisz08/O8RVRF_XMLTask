package o8rvrf.domparse.hu;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

/**
 * O8RVRFDomRead - XML dokumentum beolvasása és teljes tartalom kiírása
 * DOM API használatával végigjárja az XML fát és kiírja az elemeket
 */
public class O8RVRFDomRead {

    public static void main(String[] args) {
        try {
            // XML fájl elérési útja - MOST A PACKAGE-BEN VAN!
            File xmlFile = new File("src/o8rvrf/domparse/hu/O8RVRF_XML.xml");

            // DocumentBuilderFactory és DocumentBuilder létrehozása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // XML dokumentum beolvasása
            Document document = builder.parse(xmlFile);

            // Dokumentum normalizálása (whitespace kezelés)
            document.getDocumentElement().normalize();

            System.out.println("=== XML Dokumentum Olvasása ===");
            System.out.println("Gyökér elem: " + document.getDocumentElement().getNodeName());
            System.out.println("\n--- Teljes XML tartalom ---\n");

            // Rekurzív bejárás a gyökérelemtől
            printNode(document.getDocumentElement(), "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rekurzív metódus az XML fa bejárására
     * @param node Az aktuális csomópont
     * @param indent Behúzás a hierarchia megjelenítéséhez
     */
    private static void printNode(Node node, String indent) {

        // Ha elem típusú node
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            // Elem neve kiírása
            System.out.print(indent + "<" + element.getNodeName());

            // Attribútumok kiírása, ha vannak
            NamedNodeMap attributes = element.getAttributes();
            if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attr = attributes.item(i);
                    System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
                }
            }
            System.out.println(">");

            // Gyermek node-ok bejárása
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                printNode(child, indent + "  ");
            }

            // Záró tag kiírása
            System.out.println(indent + "</" + element.getNodeName() + ">");
        }

        // Ha szöveg típusú node (elem tartalma)
        else if (node.getNodeType() == Node.TEXT_NODE) {
            String content = node.getNodeValue().trim();
            if (!content.isEmpty()) {
                System.out.println(indent + content);
            }
        }
    }
}