/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TESTS;

import controller.game.HauptMenue;
import controller.game.MapDataBeschaffer;
import controller.toolkit.Koordinaten;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ole
 */
public class MapDataBeschafferTest_GenerierungTest {

    public MapDataBeschafferTest_GenerierungTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMapDataFeld method, of class MapDataBeschaffer.
     */
//    @Test
//    public void testGetMapDataFeld() {
//        System.out.println("getMapDataFeld");
//        Koordinaten k = null,null,null;
//        MapDataBeschaffer instance = null,null,null;
//        MapDataFeld expResult = null,null,null;
//        MapDataFeld result = instance.getMapDataFeld(k);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of saveMe method, of class MapDataBeschaffer.
//     */
//    @Test
//    public void testSaveMe() {
//        System.out.println("saveMe");
//        MapDataFeld aThis = null,null,null;
//        MapDataBeschaffer instance = null,null,null;
//        instance.saveMe(aThis);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant1() {
        System.out.print("teste Quadranensuche an Quadranten: 1, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(0, 0));
        assertEquals(new Koordinaten(0, 0), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant2() {
        System.out.print("2, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(-1, 15));
        assertEquals(new Koordinaten(-20, -4), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant3() {
        System.out.print("3, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(-15, 20));
        assertEquals(new Koordinaten(-20, 16), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant8() {
        System.out.print("8, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(19, -19));
        assertEquals(new Koordinaten(0, -20), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant11() {
        System.out.print("11, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(-36, 0));
        assertEquals(new Koordinaten(-40, -8), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant15() {
        System.out.print("15, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(10, 59));
        assertEquals(new Koordinaten(0, 40), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant17() {
        System.out.print("17, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(44, 50));
        assertEquals(new Koordinaten(40, 48), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant20() {
        System.out.print("20, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(40, -2));
        assertEquals(new Koordinaten(40, -12), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrant24() {
        System.out.print("24, ");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(-10, -40));
        assertEquals(new Koordinaten(-20, -44), result);
    }

    /**
     * Test of getQuadrant method, of class MapDataBeschaffer.
     */
    @Test
    public void testGetQuadrantX_notRandom() {
        System.out.println("X");

        MapDataBeschaffer instance = new MapDataBeschaffer(null, null, null);
        Koordinaten result = instance.getQuadrant(new Koordinaten(10, 161));
        assertEquals(new Koordinaten(0, 160), result);
    }

    /**
     * Test der Beschaffung von 2000 zufällig ausgewählten Feldern aus der
     * Datenbank
     *
     * da diese meist in noch nicht generierten Quadranten liegen, werden sie
     * mit Hilfe des Dungongenerator erzeugt Felder liegen im ereich [-1000,
     * 1000] für x,y
     */
    @Test
    public void testGetUndGenMapData_2000x_Random() {
        HauptMenue hauptMenue = new HauptMenue(true);
        hauptMenue.neuesSpiel();
        hauptMenue.hauptMenueAnzeigen();
        MapDataBeschaffer beschaffer = hauptMenue.getFeldFuerTEST();

        int x, y;
        System.out.println("teste Generierung + Abspeicherung in Datenbank(aktiv -> inaktiver Felder) + Laden aus aktiven (aktive Felder) und passiven (passive Felder) Puffer");
        for (int i = 0; i < 2000; i++) {
            x = (int) (Math.random() * 2000) - 1000;
            y = (int) (Math.random() * 2000) - 1000;
            System.out.print(x + "x" + y + ", ");
            //Test bricht ab wenn eine Exeption auftritt
            if (beschaffer.getMapDataFeld(new Koordinaten(x, y)) == null) {
                fail("Gennerierung/Laden fehlgeschlagen");
            }
            if (i % 200 == 0) {
                System.out.println("");


            }



        }

    }
}
