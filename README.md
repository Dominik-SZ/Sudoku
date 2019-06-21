# Sudoku

![Preview](src/main/resources/Sudoku_Screenshot.png)

Sudoku Generator ReadMe:

Die wichtigsten Funktionen erklären sich von selbst oder durch Tooltips.
Das sind die Texte, die erscheinen, wenn man über einem Knopf o.ä. mit dem Mauszeiger wartet.

Was nicht auf Anhieb sichtbar ist (und damit der interessante Teil des ReadMe), ist die Tastatursteuerung:

Zum Eingeben der Zahlen kann sowohl die standard Zahlenleiste als auch der Nummernblock verwendet
werden.
Zwischen Feldern wechseln geht mit WASD, den Pfeiltasten und dem Nummbernblock, wenn gleichzeitig
Shift/Umschalttaste (ganz links, zweite von unten) gedrückt ist. Diagonales Wechseln ist ebenfalls
möglich. Auf dem Nummernblock sind 1,3,7 und 9 mit shift gedrückt dafür zuständig.
Achtet grundsätzlich darauf ob euer Nummernblock eingeschalten ist (Num Lock schaltet ihn ein und aus).

Es ist möglich die Schriftgröße der editierbaren Felder zu ändern um anzuzeigen, dass man sich nur
Notizen darin macht. Die Prüfmethode "Suche nach Fehlern" z.B. überspringt solche Felder.
Der Wechsel erfolgt über das Anklicken mit der rechten Maustaste oder dem Drücken von Q oder
dem Drücken von Nummernblock 5 gleichzeitig mit Shift, während das gewünschte Felde gerade
ausgewählt ist.

Bedienungsvorschläge wären z.B. das Manövrieren mit WASD, Wechsel zwischen Notiz- und Standardmodus
mit Q und Eingeben der Zahlen mit der rechten Hand auf dem Nummernblock.
Ein anderer wäre das Manövrieren mit Shift+Nummbernblock, der Wechsel zwischen Notiz- und Standardmodus
mit Shift+5 und das Eingeben der Zahlen auch mit dem Nummernblock nur ohne Shift.

Die Felder können mit einer Farbe versehen werden, indem man das Zielfeld doppelklickt. 
Die Einfärbung kann mit einem doppelten Rechtsklick rückgängig gemacht werden.


Falls die Tooltips zu klein sind zum lesen, schreibe ich hier auch noch einmal alle Funktionen auf
(Das ist der langweilige, der Vollständigkeits halber enthaltene Teil des ReadMe):

Das Startfenster:
Hier kann man entweder ein neues Sudoku erstellen lassen oder ein abgespeichertes laden.

Wenn man ein neues Sudoku erstellen lassen möchte, gibt man die gewünschte Länge und den gewünschten
Schwierigkeitsgrad an.
Sinnvolle Längeneingaben sind momentan 4, 9 und 16 wobei man bei 16 bis zu einer Minute warten muss,
bis das Sudoku generiert ist. 
Sinnvolle Schwierigkeitsgrade sind 1-10, wobei 10 die höchste Stufe darstellt.
Hat man diese eingegeben, bestätigt man mit Klick auf den Start-Knopf.

Möchte man hingegen ein abgespeichertes Sudoku laden, kann man mit einem Klick auf den Laden Knopf 
seine Datei in einem Dateibrowser aussuchen.

Möchte man das Feld schließen, klickt man abbrechen oder das rote X oben rechts.



Das Sudokufenster:
Hier löst man sein Sudoku. 

Am oberen Rand sind lediglich Informationen angezeigt und es besteht keine Interaktionsmöglichkeit.

Am rechten Rand wird angezeigt welche Zahl wie oft bereits im Sudoku eingesetzt ist. Eine beliebte
Strategie ist es, Zahlen zu suchen, welche schon oft eingesetzt sind. Alle gleichen Zahlen können
auch hervorgehoben werden, indem der entsprechende Knopf an der rechten Seite dafür gedrückt wird
(z.B. der Knopf mit der "3er" Beschriftung für alle eingesetzten 3er). 
Erneutes Drücken des selben Knopfes nimmt das Hervorheben zurück.


Am unteren Rand sind umfangreiche Hilfsmöglichkeiten, ausgelöst durch entsprechende Knöpfe, angebracht.

Mit dem "Fertig?"-Knopf wird geprüft ob das Sudoku korrekt gelöst wurde.

Mit dem "Suche nach Fehlern"-Knopf werden falsch befüllte Felder gesucht und rot hinterlegt.
Das mag vielen als "Schummeln" erscheinen, aber in Maßen benutzt kann er ein sehr hilfreiches
Werkzeug sein, wenn man sich vertan hat und keine volle Stunde für die Fehlersuche aufbringen möchte.

Mit dem "Reset"-Knopf wird das Sudoku auf seinen Ausgangszustand zurückgesetzt. Alle Eingaben
des Spielers werden entfernt.

Mit dem "Lösung"-Knopf kann man sich ein Lösungsfenster anzeigen lassen, welches in etwa das 
Nachblättern der Lösung in einem Rätselheft nachempfinden soll. Von Anfang an eingefügte
Zahlen werden darin schwarz angezeigt, vom Spieler einzufügende blau; genau so wie im Standard Fenster.
Begrenztes Benutzen dieser Funktion versteht sich von selbst.

Mit dem "Neustart"-Knopf kann man das momentane Sudoku durch ein neues Sudoku der selben Länge und der selben
Schwierigkeit ersetzen lassen. Das könnte sinnvoll sein, wenn einem die Verteilung der gegebenen Zahlen
nicht gefällt o.ä..
Mit dem "Speichern"-Knopf werden die wichtigen Infos des momentanen Sudokus gespeichert.
Gespeichert werden:
- Das Feld mit dem gesamten Fortschritt des Spielers
- Die Lösung des Feldes
- Die Farbauswahlen
- Die Auswahl der Radio Boxen "Runterzählen" und "Rechte Seite"
- Spielzeit

Mit dem "Laden"-Knopf kann man den Dateibrowser öffnen um einen Sudoku Speicherstand zu laden.


Am linken Rand kann man die Darstellungsoptionen ändern.

Es gibt eine Auswahlbox für die Schriftfarbe und eine für die Hintergrundfarbe, die jeweils
selbsterklärend sein dürften.

Außerdem gibt es die Möglichkeit auszuwählen ob die Zahlen am rechten Rand, welche angeben wie oft
welche Zahl bereits eingesetzt ist, runterzählen zu lassen, was dann bedeutet, dass sie angeben
wie oft die jeweilige Zahl eingesetzt werden muss. Dazwischen wechselt "Runterzählen".

Die andere Auswahlmöglichkeit "Rechte Seite" entscheidet ob die Spielhilfen am rechten Spielfeldrand
angezeigt werden oder nicht. Denjenigen, denen das zu viel "Schummeln" ist, können somit ohne diese
Funktionen spielen.


Copyright Dominik 2016. Wer das liest ist toll. Free Tibet.
