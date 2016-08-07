package view.bilder;

import controller.game.Kampfsystem;
import controller.game.trigger.MeldungTrigger;
import data.Beschaffenheit;
import data.Gegenstand;
import data.Individuum;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import javax.imageio.ImageIO;

/**
 * diese Klasse ist der Bild-Puffer, sie läd die Bilder und speichert sie
 * zwischen
 *
 * @version 0.9
 * @author Ole Richter
 */
public class GuiBildBeschaffer {

    /**
     * die Eine Map der nativ unterstüzten Auflösungen (nur 16:10), alle anderen
     * Auflösungen werden skaliert
     */
    public static final HashMap<Integer, Integer> aufloesungen = new HashMap<Integer, Integer>(9) {
        {
            put(640, 400);
            put(1024, 640);
            put(1280, 800);
            put(1440, 900);
            put(1680, 1050);
            put(1920, 1200);
            put(2560, 1600);
            put(3840, 2400);
            put(7680, 4800);

        }
    };
    //aktuell gewählte Auflösung
    private static int aufloesung = 0;
    private HashMap<URL, BufferedImage> imageBuffer;
    /**
     * Die Kategoriern der Bilder
     */
    public static final int FELD = 1, GEGENSTAND = 2, INDIVIUUM = 3, KAMPFHINTERGRUND = 4, KAMPFINDIVIDUUM = 5;

    /**
     * bereitet den Bildbeschaffer vor
     */
    public GuiBildBeschaffer() {
        imageBuffer = new HashMap<URL, BufferedImage>();
    }

    /**
     * Die Methode verwalltet den imageBuffer um die Zugriffszeiten und den
     * Speicherverbauch durch die Bilder zu senken
     *
     * @param kategorie Aus welcher Kategorie das Bild stammt - Konstanten
     * dieser Klasse
     * @param bildId Die Bild id der Kategorie - Konstanten der Klassen
     * Beschaffenheit für Felder, Gegenstand, Individuum, Kampfsystem für
     * Hintergründe des Kampfes
     * @return das Bild
     */
    public BufferedImage ladeBild(int kategorie, int bildId) {

        //leitet die Anfrage an die Unterkategoriern weiter
        URL bilddatei = null;
        switch (kategorie) {
            case FELD:
                bilddatei = getFeldURL(bildId);
                break;
            case GEGENSTAND:
                bilddatei = getGegenstandURL(bildId);
                break;
            case INDIVIUUM:
                bilddatei = getIndividuumURL(bildId, true);
                break;
            case KAMPFHINTERGRUND:
                bilddatei = getKampfHinergrundURL(bildId);
                break;
            case KAMPFINDIVIDUUM:
                bilddatei = getIndividuumURL(bildId, false);
                break;

        }
        //überprüft ob der Pfad korrekt ist.
        if (bilddatei == null) {
            if (MeldungTrigger.getDefaultMeldungTrigger() != null) {
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Ein Bild konnte nicht geladen werden ");
            }
            if (kategorie == KAMPFHINTERGRUND) {
                int x = getAufloesung();
                bilddatei = getClass().getResource(x + "x" + aufloesungen.get(x) + "/feld_error.png");
            } else {
                bilddatei = getClass().getResource("error.png");
            }

        }
        //holt das Bild, wenn vorhanden aus dem Buffer
        BufferedImage image = imageBuffer.get(bilddatei);
        if (image == null) {

            //sonst wird es neu geladen
            //  debug   //System.out.println("Bild laden! - " + bilddatei);
            try {
                image = ImageIO.read(bilddatei);
            } catch (IOException ex) {
                if (MeldungTrigger.getDefaultMeldungTrigger() != null && kategorie != KAMPFHINTERGRUND) {
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Bild konnte nicht geladen werden: " + ex.getCause().getMessage());
                }

            }
            //  debug   //System.out.println("Bild geladen! - " + bilddatei);
            //und dem Buffer hinzugefügt
            imageBuffer.put(bilddatei, image);
        }
        return image;
    }

    /*
     * läd das entspechende Feld (Spielfeldhintergrund)
     */
    private URL getFeldURL(int id) {

        String bildname;

        switch (id) {
            case Beschaffenheit.BERG_1:
                bildname = "berg_1";
                break;
            case Beschaffenheit.BERG_2:
                bildname = "berg_2";
                break;
            case Beschaffenheit.BERG_3:
                bildname = "berg_3";
                break;
            case Beschaffenheit.BERG_4:
                bildname = "berg_4";
                break;
            case Beschaffenheit.BERG_GRUENESTAL:
                bildname = "berg_5";
                break;

            case Beschaffenheit.GEBUESCH_2BUESCHE:
                bildname = "gebuesch_4";
                break;
            case Beschaffenheit.GEBUESCH_3BUESCHE:
                bildname = "gebuesch_1";
                break;
            case Beschaffenheit.GEBUESCH_BAUMSTAMM:
                bildname = "gebuesch_2";
                break;
            case Beschaffenheit.GEBUESCH_BUSCH:
                bildname = "gebuesch_3";
                break;
            case Beschaffenheit.GEBUSCH_VERTROCKNET:
                bildname = "gebuesch_5";
                break;

            case Beschaffenheit.GEROELL_1:
                bildname = "geroell_1";
                break;
            case Beschaffenheit.GEROELL_2:
                bildname = "geroell_2";
                break;
            case Beschaffenheit.GEROELL_3:
                bildname = "geroell_3";
                break;
            case Beschaffenheit.GEROELL_4:
                bildname = "geroell_4";
                break;
            case Beschaffenheit.GEROELL_5:
                bildname = "geroell_5";
                break;

            case Beschaffenheit.GRAS_BAUMSTAMM:
                bildname = "gras_2";
                break;
            case Beschaffenheit.GRAS_BLUMEN:
                bildname = "gras_4";
                break;
            case Beschaffenheit.GRAS_EIN_GRASBUESCHEL:
                bildname = "gras_3";
                break;
            case Beschaffenheit.GRAS_LEER:
                bildname = "gras_5";
                break;
            case Beschaffenheit.GRAS_VIELE_GRASBUESCHEL:
                bildname = "gras_1";
                break;

            case Beschaffenheit.GRAS_0:
                bildname = "gras_0";
                break;

            case Beschaffenheit.UFER_PFUETZE:
                bildname = "ufer_1";
                break;
            case Beschaffenheit.UFER_PFUETZE_SCHILFOBEN:
                bildname = "ufer_3";
                break;
            case Beschaffenheit.UFER_PFUETZE_SCHILFUNTEN:
                bildname = "ufer_4";
                break;
            case Beschaffenheit.UFER_SCHILFDREICK:
                bildname = "ufer_2";
                break;
            case Beschaffenheit.UFER_SCHILFRECHTS:
                bildname = "ufer_5";
                break;

            case Beschaffenheit.WALD_1:
                bildname = "wald_1";
                break;
            case Beschaffenheit.WALD_2:
                bildname = "wald_5";
                break;
            case Beschaffenheit.WALD_DUNKEL:
                bildname = "wald_3";
                break;
            case Beschaffenheit.WALD_LICHTUNG:
                bildname = "wald_2";
                break;
            case Beschaffenheit.WALD_VERTROCKNET:
                bildname = "wald_4";
                break;

            case Beschaffenheit.WASSER_1:
                bildname = "wasser_1";
                break;
            case Beschaffenheit.WASSER_2:
                bildname = "wasser_2";
                break;
            case Beschaffenheit.WASSER_3:
                bildname = "wasser_3";
                break;
            case Beschaffenheit.WASSER_4:
                bildname = "wasser_4";
                break;
            case Beschaffenheit.WASSER_5:
                bildname = "wasser_5";
                break;

            default:
                bildname = "error";


        }
        //läd die richtige Auflösung
        int x = getAufloesung();
        return getClass().getResource(x + "x" + aufloesungen.get(x) + "/feld_" + bildname + ".png");

    }

    /**
     * Läd einen Gegenstand
     *
     * @param bildId die kategorie - siehe konstanten in Gegenstand
     * @return Bild
     */
    private URL getGegenstandURL(int bildId) {
        String bildname;
        switch (bildId) {
            case Gegenstand.BUCHEKERN:
                bildname = "buchecker";
                break;
            case Gegenstand.HASELNUSS:
                bildname = "haselnuss";
                break;
            case Gegenstand.UNREIFE_KASTANIE:
                bildname = "kastanie";
                break;
            case Gegenstand.MANDEL:
                bildname = "mandel";
                break;
            case Gegenstand.WALLNUSS:
                bildname = "wallnuss";
                break;
            default:
                bildname = "error";



        }
        //läd die richtige Auflösung
        int x = getAufloesung();
        return getClass().getResource(x + "x" + aufloesungen.get(x) + "/gegenstand_" + bildname + ".png");
    }

    /**
     * Läd einen Individuum
     *
     * @param bildId die kategorie - siehe konstanten in Individuum
     * @param klein true für spielfeld, false für Kampf
     * @return Bild
     */
    private URL getIndividuumURL(int bildId, boolean klein) {
        String bildname;
        switch (bildId) {
            case Individuum.FUCHS:
                bildname = "fuchs";
                break;
            case Individuum.EICHHOERNCHEN_SCHWARZ:
                bildname = "eichhoernchen_1";
                break;
            case Individuum.STREIFENHOERNCHEN:
                bildname = "eichhoernchen_2";
                break;
            case Individuum.ELSTER:
                bildname = "elster";
                break;
            case Individuum.MADER:
                bildname = "mader";
                break;
            case Individuum.SPIELER:
                bildname = "spieler";
                break;
            default:
                bildname = "error";



        }
        int x = getAufloesung();
        return getClass().getResource(x + "x" + aufloesungen.get(x) + "/individuum_" + bildname + (klein ? "_a" : "_b") + ".png");
    }

    private URL getKampfHinergrundURL(int bildId) {
        String bildname;
        switch (bildId) {
            case Kampfsystem.HINTERGRUND_FELD:
                bildname = "1";
                break;
            case Kampfsystem.HINTERGRUND_WALD:
                bildname = "2";
                break;

            default:
                bildname = "error";



        }
        //läd die richtige Auflösung
        int x = getAufloesung();
        return getClass().getResource(x + "x" + aufloesungen.get(x) + "/hintergrund_" + bildname + ".png");
    }

    /**
     * liefert die aktuelle native Auflösung von der ggf skaliert wird
     *
     * @return die aktuelle native Auflösung
     */
    public static int getAufloesung() {
        //wenn die auflösung nicht gesetzt ist übernimmt es die nächst kleinere Auflösung, oder die gleiche des Bildschiermes
        if (aufloesung == 0) {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            //sucht die Auflösungsbreite
            if (aufloesungen.containsKey(d.width)) {
                aufloesung = d.width;
            } //sucht die nächst kleinere Auflösungsbreite
            else {
                Iterator<Integer> it = aufloesungen.keySet().iterator();
                int tmp = 640;
                while (it.hasNext()) {
                    Integer integer = it.next();
                    if (d.width >= integer.intValue()) {
                        if (tmp < integer.intValue()) {
                            tmp = integer.intValue();
                        }
                    }
                }
                aufloesung = tmp;
            }
        }
        //liefert die gewählte Auflösungsbreite
        return aufloesung;
    }

    /**
     * setzt die aktuelle native Auflösung von der ggf skaliert wird
     *
     * @param aufloesung1 die breite des Bildschirmes (dh. bei 1920x1200 ->
     * 1920px)
     */
    public static void setAufloesung(int aufloesung1) {
        if (aufloesungen.containsKey(aufloesung1)) {
            aufloesung = aufloesung1;
        }
    }

    /**
     * liefert das größenverhältnis, für skalierungen. liefert 1 bei 1200px
     * höhe.
     *
     * @return das größenverhältnis, für skalierungen
     */
    public static double groessenVerhaeltnis() {
        return aufloesungen.get(getAufloesung()) / 1200.0;

    }
}
