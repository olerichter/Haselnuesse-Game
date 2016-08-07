package eichhoernchenspiel;

import controller.game.HauptMenue;

/**
 * dies ist die Startklasse des Spiel die ab Version 0.7 einen StartController /
 * MenuController startet
 *
 * von Version 0.2 prealfa bis Version 0.6a iniatialisiert sie das Spiel ,
 * erstellt einen neuen Spieler + Spielwelt
 *
 * für 0.5 alfa bewegt "WASD" den Spieler, Interaktion noch nicht möglich,
 * deshalb sind auch noch keine anderen Tasten belegt
 *
 * @version 0.5a
 * @author Ole Richter
 *
 *
 */
public class EichhoernchenSpiel {

    /**
     *
     *
     * command line arguments werden ignoriert
     *
     * startet das Spiel
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO start StartController for 0.7
//        for (int i = 0; i < ImageIO.getReaderFormatNames().length; i++) {
//            System.out.println((ImageIO.getReaderFormatNames())[i]);
//        }
//        for (int i = 0; i < ImageIO.getReaderMIMETypes().length; i++) {
//            System.out.println((ImageIO.getReaderMIMETypes())[i]);
//        }

        new HauptMenue(false);



    }
}
