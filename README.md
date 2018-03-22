
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

## FileRunner

Das Programm bietet die Möglichkeit, Ordner und Dateien zu vergleichen.

Zwei Ordner können direkt verglichen werden. Von Ordnern können aber auch "Dateilisten" erstellt und gespeichert werden mit denen der Ordner später immer wieder auf Änderungen überprüft werden kann.

Weiter können auch zwei Dateien direkt verglichen werden (oder eine Datei direkt gegen einen Hash kontrolliert werden).


<br />

**Ordner vergleichen**

In der rechten und linken Seite des Programms kann je ein Ordner oder eine Dateiliste geladen werden. Die Tabelle zeigt dann den Inhalt des Ordners oder der Dateiliste an. Unter "Suchen" kann man die Liste der angezeigten Dateien einschränken, z.B. mit ".java" nur Java- oder mit ".jpg" nur JPG-Dateien anzeigen.

Die Buttons zwischen den zwei Listen zeigen *alle*, *alle gleichen* oder *unterschiedliche* Dateien an. Der Tooltip der Buttons (dazu die Maus kurz über den Button halten) verrät genau, was dieser dann angezeigt.

Unter den Tabellen kann die Liste der Dateien in einer Dateiliste für einen späteren Vergleich gespeichert werden. Der Button mit den zwei gedrehten Pfeilen schlägt einen Dateinamen vor (unterschiedliche Vorschläge bei mehrmaligem Klick).

Die Dateilisten sind MD5-Hashes aller Dateien eines Ordners. Damit kann man Dateien auf Veränderung überprüfen.

<br />

**Dateien vergleichen**

Hier kann der Hash einer einzelnen Datei erstellt werden. Sollen zwei Dateien verglichen werden (oder der Hash einer Datei geprüft werden), wird erst eine Datei ausgewählt und dann der Hash erstellt. Stimmt der mit dem Hash der zweiten Datei überein, sind die Dateien identisch. Das Textfeld des Hash signalisiert das mit einem grünen Hintergrund.

Der Button unten *Dateien vergleichen* startet die Erstellung des Hash für beide Dateien. Der Vorgang ist identisch mit je einem Klick auf "Hash erstellen" für jede Datei einzeln.


<br />
<br />

**Systemvoraussetzungen**

Unterstützt wird Windows (Vista, 7, 8, 10) und Linux. Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: 1.8 (= Java 8). Für Linux-Benutzer wird OpenJDK8 empfohlen, außerdem benötigen Linux Benutzer die aktuelle Version von JavaFX (OpenJFX).


<br />
<br />

**Links**

[www.p2tools.de/filerunner/](https://www.p2tools.de/filerunner/)
