
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

# FileRunner

Das Programm bietet die Möglichkeit, Ordner und Dateien zu vergleichen. Von Ordnern können "Dateilisten" erstellt und gespeichert werden. Die Ordner können dann gegen die gespeicherten Dateilisten verglichen werden um Änderungen aufzudecken. Weiter können auch einzelne Dateien verglichen werden.

**Tab: Ordner vergleichen**

In der rechten und linken Seite des Programms wird je ein Ordner oder eine Dateiliste geladen. Die Tabelle zeigt dann den Inhalt des Ordners / der Dateiliste an. Mit den Button zwischen den zwei Listen kann man sich dann *alle*, *alle gleichen* oder *unterschiedliche* Dateien anzeigen lassen. Der Tooltip der Button (dazu die Maus kurz über den Button halten) verrät genau, was angezeigt wird.

Unter den Tabellen kann die Dateiliste in einer Hashdatei für einen späteren Vergleich gespeichert werden. Der Button mit den zwei gedrehten Pfeilen schlägt einen Dateinamen vor (unterschiedliche Vorschläge bei mehrmaligem Klick).

**Tab: Dateien vergleichen**

Hier kann der Hash einer einzelnen Datei erstellt werden. Sollen zwei Dateien verglichen werden (oder der Hash einer Datei geprüft werden), wird erst eine Datei ausgewählt und dann der Hash erstellt. Stimmt der mit dem Hash der zweiten Datei überein, sind die Dateien identisch. Das Textfeld des Hash signalisiert das mit einem grünen Hintergrund.

Der Button unten *Dateien vergleichen* startet die Erstellung des Hash für beide Dateien. Der Vorgang ist identisch mit je einem Klick auf "Hash erstellen" für jede Datei einzeln.


## Systemvoraussetzungen

Unterstützt wird Windows (Vista, 7, 8, 10) und Linux. Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: 1.8 (= Java 8). Für Linux-Benutzer wird OpenJDK8 empfohlen, außerdem benötigen Linux Benutzer die aktuelle Version von JavaFX (OpenJFX).


## Links
- [www.p2tools.de/filerunner/](https://www.p2tools.de/filerunner/)
