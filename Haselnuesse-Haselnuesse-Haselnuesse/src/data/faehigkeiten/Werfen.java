package data.faehigkeiten;

import controller.game.trigger.MeldungTrigger;
import java.io.Serializable;

/**
 * Ist die Fähigkeit eine Unreife Kastanie zu werfen - benötigt eine Kastanie
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Werfen implements Faehigkeit,Serializable{
    private int level;
    private int uebung;
    private boolean spieler;

    /**
     * Instanzieiert das Kastanie werfen
     * @param level das start Level der Fähigkeit
     * @param spieler ob die 
     */
    public Werfen(int level, boolean spieler) {
        this.level = level;
        this.uebung = 0;
        this.spieler = spieler;
    }
        
    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public int getID() {
        return WERFEN;
    }

    /**
     * Liefert die Beschreibeung der Fähigkeit (Name + Level + Hinweis + Schaden)
     * @return die Beschreibeung der Fähigkeit
     */
    @Override
    public String getBeschreibeung() {
          return "Werfen "+level+" (-1 Kastanie) "+getDmgBeschreibung();
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public int getDmg() {
        return 8+5*level + (int) ((20+5*level )* Math.random());
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public String getDmgBeschreibung() {
        return (8+5*level)+"-"+((20+5*level)+(8+5*level));
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public String toString() {
        return "Werfen "+level+" ";
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public int getLevel() {
        return level;
    }
    
    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public void benutzt() {
        uebung += 5+(int) (5*Math.random());
        if (uebung > 100*level){
            uebung -=100*level;
            level++;
            if(spieler) MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.POSITIV, "Fähigkeit verbessert: "+toString());
            else MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Gegner hat Fähigkeit verbessert: "+toString());
        }
    }
    
    
}
