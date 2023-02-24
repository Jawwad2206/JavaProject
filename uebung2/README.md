# Uebung2

Willkommen zu ersten Abgabe vom ProgrammierPraktikum (PPR).

_______________________________________________________________

Um das Programm zu starten ist es wichtig, erstmal Java 1.8 sich zu installieren und die Prgramm Structure Level auf 8
setzen. Wenn man das gemacht hat, kann man dann die main.java starten.

Wichtig hier ist, dass bevore Java sich einrichtitet, dass man sich seine txt File mit den Zugangsdaten zu einer MongoDB holt.
Default wird auch meine vorgschlagen, die man benutzen kann. Es wird dann oberhalb der Eingabe in der Konsole ein Pfad angezeigt den
man einfach copy pasten kann.

Wenn man die main.java startet, wird man aller erstes nach dem Pfad zu den Zugangsdaten gefragt, wie schon erwähnt kann man seine oder
meine Benutzen. Wenn man seine eigene Benutzt muss man darauf achten, dass nicht alles funktionieren wird, weil ich teilweise mit der
default collection arbeite aber auch mit eigenen. Auch bei der default collection habe ich die individualisiert und eigene colums erstellt
und bennant. Ansonsten sind keine Probleme/Bugs bekannt die was mit anderer Datenbank benutzen zu tun haben.
_______________________________________________________________________________________________________________

Wenn man die main.java erfolgreich ausgefrührt hat, wird man dann zum UserInterface weitergeleitet, dort werden einem erstmal 3 Optionen angezeigt:

Task 2, welche die Anforderungen Aufgabe 2 implementiert.

TaskSheet1MongoQuery, welche die Anforderungen für die Bonus Aufgabe  implementiert.

End the programm, welches das ganze programm beendet.

wichtig ist, dass man hier die ensprechende Zahl eintippt, also für die Task2 soll man eine 1 eingeben und so weiter.
Am besten soll man genau den Befehlen der Konsole befolgen. 

Wichtig ist auch, alle Teilaufgaben sind schon mit dem Datumfilter implementiert, das heißt um beispielweise alle Redner zu bekommen,
wählt man die 1 und dort dann 2.Search in a range of date dort wird dann einem zwei Datum vorgeschlagen, welche man eintippen kann, damit
man alle Redner bekommt.

________________________________________________________________________________________________________________

Task 2:

Wenn man sich für die Task2 entschieden hat, werden einem 4 Optionen angezeigt. Um eine von denen auszuführen, soll man die ensprechende
Zahl von der Option eintippen.

____________________________________________________________________________________________________________________________________________________
1.Speakers sorted(desc) by the number speeches with filter for fraction and party 

1.1: Get all Speaker 
Hier bekommt man alle Redner nach deren Anzahl der Reden absteigend sortiert.

1.1.0: return to selection screen

1.1.1: Get all Speaker with a Search for a specific date; 

Hier gibt man ein Datum in der Form yyyy-mm-dd an und falls es an diesem Datum eine Sitzung gab, werden einem alle Redner von dieser
Sitzung ausgegeben

1.1.2: Get all Speaker with a Search in a range of date;

Hier gibt man zwei Datum in der Form yyyy-mm-dd an und falls es an diesem Intervall Sitzungen gab, werden einem alle Redner von diesen
Sitzungen ausgegeben 

1.2.0: return to selection screen

1.2.1: Get Speaker of a certain fraction with a Search for a specific date;

Erstmal wird man nach der Eingabe für einen Fraktionsnamen gefragt, falls man nicht genau weiß welche angeboten werden, erscheint als
Hilfe eine Liste mit allen Fraktionen im Bundestag und man kann eine Copy Pasten aus der Liste (wird empfohlen es so zu tun)

Danach gibt man ein Datum in der Form yyyy-mm-dd an und falls es an diesem Datum eine Sitzung gab, werden einem alle Redner von dieser
Sitzung und ensprechenden Fraktion ausgegeben

1.2.2: Get Speaker of a certain fraction with a Search in a range of date;

Erstmal wird man nach der Eingabe für einen Fraktionsnamen gefragt, falls man nicht genau weiß welche angeboten werden, erscheint als
Hilfe eine Liste mit allen Fraktionen im Bundestag und man kann eine Copy Pasten aus der Liste (wird empfohlen es so zu tun)

Danach gibt man zwei Datum in der Form yyyy-mm-dd an und falls es an diesem Intervall Sitzungen gab, werden einem alle Redner von diesen
Sitzungen und ensprechenden Fraktion ausgegeben

1.3.0: Return to selection Screen.

1.3.1: Get Speaker of a certain party with a Search for a specific date;

Erstmal wird man nach der Eingabe für einen Partei gefragt, falls man nicht genau weiß welche angeboten werden, erscheint als
Hilfe eine Liste mit allen Parteien im Bundestag und man kann eine Copy Pasten aus der Liste (wird empfohlen es so zu tun)

Danach gibt man ein Datum in der Form yyyy-mm-dd an und falls es an diesem Datum eine Sitzung gab, werden einem alle Redner von dieser
Sitzung und ensprechenden Partei ausgegeben

1.3.2: Get Speaker of a certain party with a Search in a range of date;

Erstmal wird man nach der Eingabe für einen Partei gefragt, falls man nicht genau weiß welche angeboten werden, erscheint als
Hilfe eine Liste mit allen Parteien im Bundestag und man kann eine Copy Pasten aus der Liste (wird empfohlen es so zu tun)

Danach gibt man zwei Datum in der Form yyyy-mm-dd an und falls es an diesem Intervall Sitzungen gab, werden einem alle Redner von diesen
Sitzungen  und ensprechenden Partei ausgegeben

______________________________________________________________________________________________________________________________________________
2.Get the average speech length of every Speaker, fraction and party

2.1.0: return to selection screen

2.1.1: Get average speech length of every Speaker with a Search for a specific date;

Hier gibt man ein Datum in der Form yyyy-mm-dd an und falls es an diesem Datum eine Sitzung gab, werden einem alle Redner von dieser
Sitzung ausgegeben

2.1.2: Get average speech length of every Speaker with a Search in a range of date;

Hier gibt man zwei Datum in der Form yyyy-mm-dd an und falls es an diesem Intervall Sitzungen gab, werden einem alle Redner von diesen
Sitzungen ausgegeben

2.2.0: return to selection screen

2.2.1: et the average speech length of every fraction with a Search for a specific date;

hier gibt man ein Datum in der Form yyyy-mm-dd an und falls es an diesem Datum eine Sitzung gab, werden einem alle Fraktionen
angezeigt und deren durschnittliche Redebeitrag an diesem Tag

2.2.2: Get Speaker of a certain fraction with a Search in a range of date;

hier gibt man zwei Datum in der Form yyyy-mm-dd an und falls es an diesem Intervall Sitzungen gab, werden einem alle Fraktionen
angezeigt und deren durschnittliche Redebeitrag an von diesem Intervall

2.3.0: Return to selection Screen.

2.3.1: Get Speaker of a certain party with a Search for a specific date;

hier gibt man ein Datum in der Form yyyy-mm-dd an und falls es an diesem Datum eine Sitzung gab, werden einem alle Parteien
angezeigt und deren durschnittliche Redebeitrag an diesem Tag

2.3.2: Get Speaker of a certain party with a Search in a range of date;

hier gibt man zwei Datum in der Form yyyy-mm-dd an und falls es an diesem Intervall Sitzungen gab, werden einem alle Parteien
angezeigt und deren durschnittliche Redebeitrag an von diesem Intervall


____________________________________________________________________________________________________________________________________________
3.Get the duration of every session sorted desc

3.0: Return to selection screen.

3.1.0: Return to selection screen.

3.1.1: Get the duration of every session sorted desc with a Search for a specific date

hier gibt man ein Datum in der Form yyyy-mm-dd an und falls es an diesem Datum eine Sitzung gab, wird einem die dauer dieser
Sitzung ausgegeben

3.1.2: Get the duration of every session sorted desc with a Search in a range of date

hier gibt man zwei Datum in der Form yyyy-mm-dd an und falls es an diesem Intervall Sitzungen gab, wird einem die dauer dieser
Sitzungen ausgegeben
_____________________________________________________________________________________________________________________________________________
4.Return to menu

-> man kommt wieder zurück zum MainMenu, wo man dann weiter zur Task 3 gehen kann.

_______________________________________________________________________________________________________________________

Wenn man sich für die TaskSheet1MongoQuery entschieden hat, werden einem 7 Optionen angezeigt. Um eine von denen auszuführen, soll man die ensprechnede
Zahl vor der Option eintippen.

1Get a sorted list of all the Speakers.

Annahme/Probleme: Beim Parsen ist mir aufgefallen, dass ich manche Abgeordnete doppelt bekommen. Das Problem ist, dass manche Abgeordente
mehrere IDs haben, also Bspw hat Angela Merkel eine ID als Mitglied der CDU/CSU und einmal eine ID als Bundeskanzlerin. Deshalb
werden manchmal Abgeordenete doppelt angezeigt. Desweiteren gibt es Probleme wie, dass Abgeordnete der Bündnis 90/Die Grünen doppelt
erscheinen, weil die Partei in unterschiedliche Codierungsarten geschrieben/gespeichert wurden. So ist Bündnis 90/Die Grünen einmal in UTF-8 und
einmal anders gespeichert worden. Auch wurden manchmal Abgeordnete mit eine gewissen ID mit einem falschen namen gespeichert, also Bspw. steht da
die ID von Schäuble aber es wurde der Name von Otto Solms zu der ID gespeichert.


-> Wenn man diese Option auswählt, wird einem eine alphabetisch sortierte Reihenfolge der Redner ausgegeben. Sortiert wurde hier nach
Vornamen.


2.Search for a certain Speaker or search for Party members

Annahme/Probleme: Beim Parsen ist mir aufgefallen, dass ich manche Abgeordnete doppelt bekommen. Das Problem ist, dass manche Abgeordente
mehrere IDs haben, also Bspw hat Angela Merkel eine ID als Mitglied der CDU/CSU und einmal eine ID als Bundeskanzlerin. Deshalb
werden manchmal Abgeordenete doppelt angezeigt. Desweiteren gibt es Probleme wie, dass Abgeordnete der Bündnis 90/Die Grünen doppelt
erscheinen, weil die Partei in unterschiedliche Codierungsarten geschrieben/gespeichert wurden. So ist Bündnis 90/Die Grünen einmal in UTF-8 und
einmal anders gespeichert worden. Auch wurden manchmal Abgeordnete mit eine gewissen ID mit einem falschen namen gespeichert, also Bspw. steht da
die ID von Schäuble aber es wurde der Name von Otto Solms zu der ID gespeichert.

-> Hier kann man nach einem oder mehreren Abgeordneten suchen, indem man einen Namen eintippt, also Bspw. Carsten = Alle Redner die Vorname
Carsten haben werden ausgegeben mit Partei. Anders kann man auch "ba" eintippen und werden alle Redner ausgegeben die in ihren Vor-oder Nachname
"ba" enthalten. Man kann aber auch eine Partei eingeben wie bspw CDU und es werden einem alle Redner der Parte CDU ausgegeben.


3.Get how many times a MP lead chaired a seasson

Annahme/Probleme: Es wurde einmal der Name von Wolfgang Schäuble mit einem abstand und dann : geschrieben also: Präsident Dr. Wolfgang Schäuble : und
Präsident Dr. Wolfgang Schäuble: . Dies ist ein fehler der XML und wegen diesem, bekomme ich zwei mal XML.

-> Einfach Ausgabe von allen Sitzungsleitern und wie oft diese eine Sitzung geleitet haben.


4.Get the average speech length of all speeches

Annahme/Probleme: Ich habe folgendes Problem, ich bekomme die Reden mit den Redetext und den namen des Redners, dieser wird
also wenn ich die länge der Reden berrechne mit gezählt, also habe ich ein bisschen mehr chars, als eigentlich gewollt. Um das zu umgehen
korrigiere ich die berrechnete Anzahl, indem ich die insegsamt länge der Rede mit Namen zähle und anschließend subtrahiere mit den
Namen des Redners. Das Funktioniert leider nicth immer, da manchmal fehler in der XML sind und der Name zwei mal auftaucht, also kann man mit
meinen Werten ungefähr von einer Abweichung von -150 bis 250 der ausgegeben ergebniss rechnen.

-> Man bekommt die durschnittliche Redelänge über alle Beiträge

5.Get the average speech length of every speaker

Annahme/Probleme: gleich wie bei der 1.

-> Man bekommt die durschnittliche Redelänge von jedem Redner.

6.Get the average speech length of every fraction

Annahme/Probleme: gleich wie bei der 1.

-> Man bekommt die durschnittliche Redelänge von allen Fraktionen.

7.Return to menu

-> Man kommt zurück zum MainMenu wo man dann die 3. End the Programm wählen kann.

__________________________________________________________________________________________________________________________

3. End the Programm

Beendet das ganze Programm und man muss wenn man es wieder startet alles von vorne machen.

Bei weiteren fragen, mich bitte per E-Mail oder per Discord Scaramouche#9008 anschreiben.

Viel Spaß mit dem Programm :).