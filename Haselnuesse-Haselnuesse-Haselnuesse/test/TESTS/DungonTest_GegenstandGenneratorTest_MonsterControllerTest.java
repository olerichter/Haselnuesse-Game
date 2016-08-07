/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TESTS;

import controller.game.HauptMenue;
import controller.toolkit.Koordinaten;
import data.MapDataFeld;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ole
 */
public class DungonTest_GegenstandGenneratorTest_MonsterControllerTest {

    public DungonTest_GegenstandGenneratorTest_MonsterControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of genDungonRek method, of class Dungon.
     */
    @Test
    public void testIndividuuenImStartBereich50mal() {
        System.out.println("Teste an 50 Spielen ob keine Individuuen in den Startbereich gesetzt wurden");
        HauptMenue hauptMenue = new HauptMenue(true);


        for (int i = 0; i < 10; i++) {

            hauptMenue.neuesSpiel();
            hauptMenue.hauptMenueAnzeigen();

            for (int j = -4; j <= 4; j++) {
                for (int k = 3; k <= 11; k++) {
                    if (hauptMenue.getFeldFuerTEST().getMapDataFeld(new Koordinaten(j, k)).getIndividuum() != null) {
                        fail("individuum in der Sartzone");
                    }


                }

            }


        }

    }

    /**
     * Test of genDungonRek method, of class Dungon.
     */
    @Test
    public void testIndividuuenUndGegenstaendeImQuadrantX_50malNeuesSpiel() {
        System.out.println("Teste an 50 Quadranten ob der IndividuuenCOntroller(MonsterController) \nund der GegenstandGennerator den Dungon ordnungsgemäß bestücken ");
        HauptMenue hauptMenue = new HauptMenue(true);
        for (int i = 0; i < 50; i++) {
            hauptMenue.neuesSpiel();
            hauptMenue.hauptMenueAnzeigen();
            int gegenstand = 0, individuum = 0;
            Koordinaten koordinaten = new Koordinaten((int) (2000 * Math.random()) - 1000, (int) (2000 * Math.random()) - 1000);
            koordinaten = hauptMenue.getFeldFuerTEST().getQuadrant(koordinaten);
            for (int j = koordinaten.getX(); j <= koordinaten.getX() + 19; j++) {
                for (int k = koordinaten.getY(); k <= koordinaten.getY() + 19; k++) {
                    if (hauptMenue.getFeldFuerTEST().getMapDataFeld(new Koordinaten(j, k)).getIndividuum() != null) {
                        individuum++;
                    }
                    if (hauptMenue.getFeldFuerTEST().getMapDataFeld(new Koordinaten(j, k)).getGegenstand() != null) {
                        gegenstand++;
                    }

                }

            }
            if (individuum == 0 || gegenstand == 0) {
                fail("Gegenstand" + gegenstand + " / Individuum" + individuum + " nicht gefunden");
            }


        }
    }

    /**
     * Test of genDungonRek method, of class Dungon.
     */
    @Test
    public void testIndividuuenAufUnbegehbaremBodenQuadrantX_100malNeuesSpiel() {
        System.out.println("Teste an 100 Quadranten ob Individuuen nur auf begehbare Felder gesetzt werden");
        HauptMenue hauptMenue = new HauptMenue(true);
        for (int i = 0; i < 100; i++) {
            hauptMenue.neuesSpiel();
            hauptMenue.hauptMenueAnzeigen();
            Koordinaten koordinaten = new Koordinaten((int) (2000 * Math.random()) - 1000, (int) (2000 * Math.random()) - 1000);
            koordinaten = hauptMenue.getFeldFuerTEST().getQuadrant(koordinaten);
            for (int j = koordinaten.getX(); j <= koordinaten.getX() + 19; j++) {
                for (int k = koordinaten.getY(); k <= koordinaten.getY() + 19; k++) {
                    MapDataFeld feld = hauptMenue.getFeldFuerTEST().getMapDataFeld(new Koordinaten(j, k));

                    if (feld.getIndividuum() != null && !feld.getBeschaffenheit().getBegehbar()) {
                        fail("individuum sitzt an falschem ort");
                    }

                }

            }



        }

    }
}
