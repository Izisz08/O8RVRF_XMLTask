package o8rvrf.domparse.hu;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

/**
 * O8RVRFDomQuery - XML dokumentum lekérdezések
 * Konkrét adatok kinyerése az XML-ből különböző feltételek alapján
 */
public class O8RVRFDomQuery {

    private static Document document;

    public static void main(String[] args) {
        try {
            // XML fájl beolvasása
            File xmlFile = new File("src/o8rvrf/domparse/hu/O8RVRF_XML.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            System.out.println("=== XML Dokumentum Lekérdezések ===\n");

            // 1. Lekérdezés: Összes kiadó neve és székhelye
            query1_OsszesKiado();

            // 2. Lekérdezés: Egy adott ID-jú könyv részletes adatai
            query2_KonyvReszletei("KV1");

            // 3. Lekérdezés: Adott kiadó összes könyve
            query3_KiadoKonyvei("K1");

            // 4. Lekérdezés: Olvasó adatai és telefonszámai
            query4_OlvasoAdatai("O1");

            // 5. Lekérdezés: Folyamatban lévő kölcsönzések
            query5_FolyamatbanLevoKolcsonzesek();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1. Lekérdezés: Összes kiadó neve és székhelye
     */
    private static void query1_OsszesKiado() {
        System.out.println("--- 1. LEKÉRDEZÉS: Összes kiadó ---");
        NodeList kiadok = document.getElementsByTagName("kiado");

        for (int i = 0; i < kiadok.getLength(); i++) {
            Element kiado = (Element) kiadok.item(i);
            String id = kiado.getAttribute("kiado_id");
            String nev = kiado.getElementsByTagName("nev").item(0).getTextContent();
            String szekhely = kiado.getElementsByTagName("szekhely").item(0).getTextContent();

            System.out.println("  Kiadó ID: " + id);
            System.out.println("    Név: " + nev);
            System.out.println("    Székhely: " + szekhely);
            System.out.println();
        }
    }

    /**
     * 2. Lekérdezés: Egy adott ID-jú könyv részletes adatai
     */
    private static void query2_KonyvReszletei(String konyvId) {
        System.out.println("--- 2. LEKÉRDEZÉS: Könyv részletei (ID: " + konyvId + ") ---");
        NodeList konyvek = document.getElementsByTagName("konyv");

        for (int i = 0; i < konyvek.getLength(); i++) {
            Element konyv = (Element) konyvek.item(i);
            String id = konyv.getAttribute("konyv_id");

            if (id.equals(konyvId)) {
                String cim = konyv.getElementsByTagName("cim").item(0).getTextContent();
                String isbn = konyv.getElementsByTagName("ISBN").item(0).getTextContent();
                String kiadasiEv = konyv.getElementsByTagName("kiadasi_ev").item(0).getTextContent();
                String oldalszam = konyv.getElementsByTagName("oldalszam").item(0).getTextContent();
                String kiadoId = konyv.getAttribute("kiado_id");

                System.out.println("  Cím: " + cim);
                System.out.println("  ISBN: " + isbn);
                System.out.println("  Kiadási év: " + kiadasiEv);
                System.out.println("  Oldalszám: " + oldalszam);
                System.out.println("  Kiadó ID: " + kiadoId);
                System.out.println();
                return;
            }
        }
        System.out.println("  Nem található könyv ezzel az ID-vel!\n");
    }

    /**
     * 3. Lekérdezés: Adott kiadó összes könyve
     */
    private static void query3_KiadoKonyvei(String kiadoId) {
        System.out.println("--- 3. LEKÉRDEZÉS: Kiadó könyvei (Kiadó ID: " + kiadoId + ") ---");
        NodeList konyvek = document.getElementsByTagName("konyv");
        int szamlalo = 0;

        for (int i = 0; i < konyvek.getLength(); i++) {
            Element konyv = (Element) konyvek.item(i);
            String kid = konyv.getAttribute("kiado_id");

            if (kid.equals(kiadoId)) {
                szamlalo++;
                String cim = konyv.getElementsByTagName("cim").item(0).getTextContent();
                String konyvId = konyv.getAttribute("konyv_id");

                System.out.println("  " + szamlalo + ". " + cim + " (ID: " + konyvId + ")");
            }
        }
        if (szamlalo == 0) {
            System.out.println("  Nincs könyv ettől a kiadótól!");
        }
        System.out.println();
    }

    /**
     * 4. Lekérdezés: Olvasó adatai és telefonszámai
     */
    private static void query4_OlvasoAdatai(String olvasoId) {
        System.out.println("--- 4. LEKÉRDEZÉS: Olvasó adatai (ID: " + olvasoId + ") ---");
        NodeList olvasok = document.getElementsByTagName("olvaso");

        for (int i = 0; i < olvasok.getLength(); i++) {
            Element olvaso = (Element) olvasok.item(i);
            String id = olvaso.getAttribute("olvaso_id");

            if (id.equals(olvasoId)) {
                String nev = olvaso.getElementsByTagName("nev").item(0).getTextContent();
                String email = olvaso.getElementsByTagName("email").item(0).getTextContent();

                Element cimElem = (Element) olvaso.getElementsByTagName("cim").item(0);
                String irszam = cimElem.getElementsByTagName("iranyitoszam").item(0).getTextContent();
                String varos = cimElem.getElementsByTagName("varos").item(0).getTextContent();
                String utca = cimElem.getElementsByTagName("utca").item(0).getTextContent();
                String hazszam = cimElem.getElementsByTagName("hazszam").item(0).getTextContent();

                System.out.println("  Név: " + nev);
                System.out.println("  Email: " + email);
                System.out.println("  Cím: " + irszam + " " + varos + ", " + utca + " " + hazszam);

                // Telefonszámok lekérdezése
                System.out.println("  Telefonszámok:");
                NodeList telefonok = document.getElementsByTagName("olvaso_telefon");
                for (int j = 0; j < telefonok.getLength(); j++) {
                    Element telefon = (Element) telefonok.item(j);
                    String oid = telefon.getAttribute("olvaso_id");
                    if (oid.equals(olvasoId)) {
                        String szam = telefon.getElementsByTagName("telefonszam").item(0).getTextContent();
                        System.out.println("    - " + szam);
                    }
                }
                System.out.println();
                return;
            }
        }
        System.out.println("  Nem található olvasó ezzel az ID-vel!\n");
    }

    /**
     * 5. Lekérdezés: Folyamatban lévő kölcsönzések
     */
    private static void query5_FolyamatbanLevoKolcsonzesek() {
        System.out.println("--- 5. LEKÉRDEZÉS: Folyamatban lévő kölcsönzések ---");
        NodeList kolcsonzesek = document.getElementsByTagName("kolcsonzes");
        int szamlalo = 0;

        for (int i = 0; i < kolcsonzesek.getLength(); i++) {
            Element kolcsonzes = (Element) kolcsonzesek.item(i);
            String statusz = kolcsonzes.getElementsByTagName("statusz").item(0).getTextContent();

            if (statusz.trim().equals("folyamatban")) {
                szamlalo++;
                String kolcsonzesId = kolcsonzes.getAttribute("kolcsonzes_id");
                String olvasoId = kolcsonzes.getAttribute("olvaso_id");
                String konyvId = kolcsonzes.getAttribute("konyv_id");
                String datum = kolcsonzes.getElementsByTagName("kolcsonzes_datuma").item(0).getTextContent();
                String hatarido = kolcsonzes.getElementsByTagName("visszahozasi_hatarido").item(0).getTextContent();

                System.out.println("  " + szamlalo + ". Kölcsönzés ID: " + kolcsonzesId);
                System.out.println("     Olvasó ID: " + olvasoId + ", Könyv ID: " + konyvId);
                System.out.println("     Kölcsönzés: " + datum + " → Határidő: " + hatarido);
                System.out.println();
            }
        }
        if (szamlalo == 0) {
            System.out.println("  Nincs folyamatban lévő kölcsönzés!");
        }
        System.out.println();
    }
}