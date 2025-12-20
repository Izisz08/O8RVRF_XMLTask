package o8rvrf.domparse.hu;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

/**
 * O8RVRFDomRead - XML dokumentum beolvasása és teljes tartalom kiírása
 * DOM API használatával végigjárja az XML fát és kiírja az elemeket
 * Minden node típus feldolgozása: Element, Text, Comment, CDATA, stb.
 */
public class O8RVRFDomRead {

    public static void main(String[] args) {
        try {
            // XML fájl elérési útja
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

            // DOM fa statisztika
            System.out.println("\n=== DOM Fa Statisztika ===");
            printStatistics(document.getDocumentElement());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rekurzív metódus az XML fa bejárására - minden node típus kezelésével
     * @param node Az aktuális csomópont
     * @param indent Behúzás a hierarchia megjelenítéséhez
     */
    private static void printNode(Node node, String indent) {

        // ELEMENT típusú node
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

            // Gyermek node-ok rekurzív bejárása
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                printNode(children.item(i), indent + "  ");
            }

            // Záró tag kiírása
            System.out.println(indent + "</" + element.getNodeName() + ">");
        }

        // TEXT típusú node - szöveges tartalom
        else if (node.getNodeType() == Node.TEXT_NODE) {
            String content = node.getNodeValue().trim();
            if (!content.isEmpty()) {
                System.out.println(indent + "[TEXT] " + content);
            }
        }

        // COMMENT típusú node - XML megjegyzések
        else if (node.getNodeType() == Node.COMMENT_NODE) {
            String comment = node.getNodeValue().trim();
            System.out.println(indent + "[COMMENT] <!-- " + comment + " -->");
        }

        // CDATA típusú node - CDATA szekciók
        else if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
            String cdata = node.getNodeValue();
            System.out.println(indent + "[CDATA] <![CDATA[" + cdata + "]]>");
        }

        // PROCESSING_INSTRUCTION típusú node
        else if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
            System.out.println(indent + "[PI] <?" + node.getNodeName() + " " + node.getNodeValue() + "?>");
        }

        // Egyéb node típusok logolása
        else {
            System.out.println(indent + "[NODE TYPE: " + node.getNodeType() + "] " + node.getNodeName());
        }
    }

    /**
     * DOM fa statisztika - node típusok számlálása
     * @param root A gyökérelem, ahonnan a számolás kezdődik
     */
    private static void printStatistics(Node root) {
        int[] counts = new int[4]; // [elemek, szövegek, megjegyzések, attribútumok]
        countNodes(root, counts);

        System.out.println("Elemek száma: " + counts[0]);
        System.out.println("Szöveges node-ok száma: " + counts[1]);
        System.out.println("Megjegyzések száma: " + counts[2]);
        System.out.println("Attribútumok száma: " + counts[3]);
    }

    /**
     * Rekurzív node számláló metódus
     * @param node Az aktuális csomópont
     * @param counts A számláló tömb [elemek, szövegek, megjegyzések, attribútumok]
     * @return A frissített számláló tömb
     */
    private static int[] countNodes(Node node, int[] counts) {
        // Element node
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            counts[0]++;
            // Attribútumok számlálása
            NamedNodeMap attrs = node.getAttributes();
            if (attrs != null) {
                counts[3] += attrs.getLength();
            }
        }
        // Text node (nem üres)
        else if (node.getNodeType() == Node.TEXT_NODE && !node.getNodeValue().trim().isEmpty()) {
            counts[1]++;
        }
        // Comment node
        else if (node.getNodeType() == Node.COMMENT_NODE) {
            counts[2]++;
        }

        // Gyermek node-ok rekurzív számlálása
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            countNodes(children.item(i), counts);
        }

        return counts;
    }
}