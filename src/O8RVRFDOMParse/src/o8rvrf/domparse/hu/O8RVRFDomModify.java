package o8rvrf.domparse.hu;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * O8RVRFDomModify - XML dokumentum módosítása
 * Elemek hozzáadása, módosítása, törlése és mentés új fájlba
 */
public class O8RVRFDomModify {

    private static Document document;

    public static void main(String[] args) {
        try {
            // XML fájl beolvasása
            File xmlFile = new File("src/o8rvrf/domparse/hu/O8RVRF_XML.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            System.out.println("=== XML Dokumentum Módosítása ===\n");

            // 1. Módosítás: Új kiadó hozzáadása
            modify1_UjKiadoHozzaadasa();

            // 2. Módosítás: Könyv adatainak módosítása
            modify2_KonyvAdatModositasa("KV1", "350");

            // 3. Módosítás: Új olvasó hozzáadása
            modify3_UjOlvasoHozzaadasa();

            // 4. Módosítás: Kölcsönzés státuszának módosítása
            modify4_KolcsonzesStatuszModositasa("KOLCS1", "visszahozva");

            // 5. Módosítás: Elem törlése (egy kölcsönzés)
            modify5_KolcsonzesTorlese("KOLCS2");

            // Módosított XML mentése új fájlba
            saveModifiedXML("src/o8rvrf/domparse/hu/O8RVRF_XML_MODIFIED.xml");

            System.out.println("\n✓ Minden módosítás sikeresen végrehajtva!");
            System.out.println("✓ Módosított XML mentve: O8RVRF_XML_MODIFIED.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1. Módosítás: Új kiadó hozzáadása
     */
    private static void modify1_UjKiadoHozzaadasa() {
        System.out.println("--- 1. MÓDOSÍTÁS: Új kiadó hozzáadása ---");

        // Kiadók elem megkeresése
        NodeList kiadokList = document.getElementsByTagName("kiadok");
        Element kiadok = (Element) kiadokList.item(0);

        // Új kiadó elem létrehozása
        Element ujKiado = document.createElement("kiado");
        ujKiado.setAttribute("kiado_id", "K3");

        // Név elem
        Element nev = document.createElement("nev");
        nev.setTextContent("Helikon Kiadó");
        ujKiado.appendChild(nev);

        // Székhely elem
        Element szekhely = document.createElement("szekhely");
        szekhely.setTextContent("Budapest");
        ujKiado.appendChild(szekhely);

        // Alapítási év elem
        Element alapitasiEv = document.createElement("alapitasi_ev");
        alapitasiEv.setTextContent("1988");
        ujKiado.appendChild(alapitasiEv);

        // Hozzáadás a kiadók listához
        kiadok.appendChild(ujKiado);

        System.out.println("  ✓ Új kiadó hozzáadva: Helikon Kiadó (K3)\n");
    }

    /**
     * 2. Módosítás: Könyv oldalszámának módosítása
     */
    private static void modify2_KonyvAdatModositasa(String konyvId, String ujOldalszam) {
        System.out.println("--- 2. MÓDOSÍTÁS: Könyv oldalszámának módosítása ---");
        NodeList konyvek = document.getElementsByTagName("konyv");

        for (int i = 0; i < konyvek.getLength(); i++) {
            Element konyv = (Element) konyvek.item(i);
            String id = konyv.getAttribute("konyv_id");

            if (id.equals(konyvId)) {
                Element oldalszam = (Element) konyv.getElementsByTagName("oldalszam").item(0);
                String regiErtek = oldalszam.getTextContent();
                oldalszam.setTextContent(ujOldalszam);

                System.out.println("  ✓ Könyv ID: " + konyvId);
                System.out.println("    Régi oldalszám: " + regiErtek);
                System.out.println("    Új oldalszám: " + ujOldalszam + "\n");
                return;
            }
        }
        System.out.println("  ✗ Nem található könyv ezzel az ID-vel!\n");
    }

    /**
     * 3. Módosítás: Új olvasó hozzáadása összetett címmel
     */
    private static void modify3_UjOlvasoHozzaadasa() {
        System.out.println("--- 3. MÓDOSÍTÁS: Új olvasó hozzáadása ---");

        // Olvasók elem megkeresése
        NodeList olvasokList = document.getElementsByTagName("olvasok");
        Element olvasok = (Element) olvasokList.item(0);

        // Új olvasó elem létrehozása
        Element ujOlvaso = document.createElement("olvaso");
        ujOlvaso.setAttribute("olvaso_id", "O3");

        // Név
        Element nev = document.createElement("nev");
        nev.setTextContent("Tóth Eszter");
        ujOlvaso.appendChild(nev);

        // Email
        Element email = document.createElement("email");
        email.setTextContent("toth.eszter@email.hu");
        ujOlvaso.appendChild(email);

        // Cím (összetett attribútum)
        Element cim = document.createElement("cim");

        Element iranyitoszam = document.createElement("iranyitoszam");
        iranyitoszam.setTextContent("4024");
        cim.appendChild(iranyitoszam);

        Element varos = document.createElement("varos");
        varos.setTextContent("Debrecen");
        cim.appendChild(varos);

        Element utca = document.createElement("utca");
        utca.setTextContent("Egyetem tér");
        cim.appendChild(utca);

        Element hazszam = document.createElement("hazszam");
        hazszam.setTextContent("1");
        cim.appendChild(hazszam);

        ujOlvaso.appendChild(cim);

        // Hozzáadás az olvasók listához
        olvasok.appendChild(ujOlvaso);

        System.out.println("  ✓ Új olvasó hozzáadva: Tóth Eszter (O3)\n");
    }

    /**
     * 4. Módosítás: Kölcsönzés státuszának módosítása
     */
    private static void modify4_KolcsonzesStatuszModositasa(String kolcsonzesId, String ujStatusz) {
        System.out.println("--- 4. MÓDOSÍTÁS: Kölcsönzés státuszának módosítása ---");
        NodeList kolcsonzesek = document.getElementsByTagName("kolcsonzes");

        for (int i = 0; i < kolcsonzesek.getLength(); i++) {
            Element kolcsonzes = (Element) kolcsonzesek.item(i);
            String id = kolcsonzes.getAttribute("kolcsonzes_id");

            if (id.equals(kolcsonzesId)) {
                Element statusz = (Element) kolcsonzes.getElementsByTagName("statusz").item(0);
                String regiStatusz = statusz.getTextContent().trim();
                statusz.setTextContent(ujStatusz);

                System.out.println("  ✓ Kölcsönzés ID: " + kolcsonzesId);
                System.out.println("    Régi státusz: " + regiStatusz);
                System.out.println("    Új státusz: " + ujStatusz + "\n");
                return;
            }
        }
        System.out.println("  ✗ Nem található kölcsönzés ezzel az ID-vel!\n");
    }

    /**
     * 5. Módosítás: Kölcsönzés törlése
     */
    private static void modify5_KolcsonzesTorlese(String kolcsonzesId) {
        System.out.println("--- 5. MÓDOSÍTÁS: Kölcsönzés törlése ---");
        NodeList kolcsonzesek = document.getElementsByTagName("kolcsonzes");

        for (int i = 0; i < kolcsonzesek.getLength(); i++) {
            Element kolcsonzes = (Element) kolcsonzesek.item(i);
            String id = kolcsonzes.getAttribute("kolcsonzes_id");

            if (id.equals(kolcsonzesId)) {
                Node parent = kolcsonzes.getParentNode();
                parent.removeChild(kolcsonzes);

                System.out.println("  ✓ Kölcsönzés törölve: " + kolcsonzesId + "\n");
                return;
            }
        }
        System.out.println("  ✗ Nem található kölcsönzés ezzel az ID-vel!\n");
    }

    /**
     * Módosított XML mentése fájlba
     */
    private static void saveModifiedXML(String filename) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Formázás beállítása (szépen formázott XML)
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filename));

        transformer.transform(source, result);
    }
}