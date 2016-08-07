@version 0.9 - Stable (Final für Abgabe)
@author Ole Richter
@mail github@olerichter.com
@web https://olerichter.com

Das Spiel wird durch die Klasse eichhoernchenspiel.Eichhoernchenspiel.java gestartet

Das Programm ist mit Java 1.6.0_33 gebaut, Mac OS X Server 10.7.3 (11D50),Kernel: Darwin 11.3.0.
Es startet im Vollbildmodus und ist für 16:10 Bildschirme optimiert, in allen anderen Seitenverhältnissen
wird das Spiel dennoch korrekt dargestellt.
Das Programm startet immer auf dem primären Monitor.

Das Programm muss unter MacOs / Linux mit $ java -jar Haselnuesse-Haselnuesse-Haselnuesse.jar gestartet werden, bei einem Doppelklick funktionieren die Tastatureingaben unzuverlässig.

alle Klassen sind dokumentiert - JAVA-DOC vorhanden


--ALPHA--Das Spielfeld ist oberirdisch und unendlich groß, es wird in Quadranten generiert, 
         die Quadranten selbst werden rekursiv generiert und zwar erst, wenn sie benötigt werden.
         D.h. das Spielfeld wird während des Spiels immer weiter generiert - aber immer nur soviel wie gerade benötigt wird.


--BETA-- Der Kampf erfolgt in einem eigenem Kampfmodus, in dem das Spielfeld ausgeblendet wird (Beta).
         Der Kampf erfolgt rundenbasiert, der Spieler startet.

--ALPHA--der Spieler wird mit "WASD" gesteuert, er kann auf Bergen und Wasser nicht laufen, auf Gras, Wald, 
         sowie auf Gebüsch, Ufer und Geröll jedoch schon.

--BETA-- Die Menüs werden mit den Pfeiltasten und Enter bedient
         das Hauptmenü kann über ESC aufgerufen werden - während eines Kampfes ist dies nicht möglich.

         Der Rucksack (Inventar) wird über R aufgerufen und kann durch ESC verlassen werden.
         Im Rucksack ist es dem Spieler möglich seine Lebenspunkte durch verzehren von Nüssen wieder aufzufüllen (auch geplant für Kampfmodus)
         
         Der Spieler steigt ein Level auf wenn er genug Nüsse gesammelt hat (Anzeige im Rucksack).

         Speichern und Laden erfolgt im Hauptmenü

         Der Spieler sammelt Gegenstände auf wenn er über sie läuft
         Der Kampfmodus startet sich, wenn Gegner sich auf das Feld des Spielers bewegen 
         oder der Spieler auf ein Feld bewegt wo ein Gegner sitzt.

--STABLE V0.9--
         Das Spiel startet im Vollbildmodus. Es ist Auflösungsunabhängig (bis K8) - optimiert ist es auf 16:10.
         Schriften,Positionen usw. werden skaliert und der Bildschirmgröße angepasst.
         Bei Seitenverhältnissen die nicht 16:10 entsprechen kann es zu Alising-/Treppeneffekten kommen.

         Die Gegner nutzen unterschiedliche Attacken, die an die Stärke des Spielers angepasst werden.
         Auch die Lebenspunkte der Gegner sind proportional an den Spieler angepasst.

Known Bugs:

--

Missing Optional:
- Zwischensequenzen / Story
- erweiterte Tests