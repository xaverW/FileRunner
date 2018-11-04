
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

## FileRunner

Das Programm bietet die Möglichkeit, Dateilisten und einzelne Dateien zu vergleichen.

<br />

**Dateilisten vergleichen**

Eine Dateiliste 
- können die Dateien in einem Ordner sein, 
- kann der Inhalt einer Zip-Datei sein 
- kann eine vorher mit dem Programm gespeicherte Liste von Dateien sein.

Zwei dieser Dateilisten können dann direkt verglichen werden. In der rechten und linken Seite des Programms kann je eine Dateiliste (ein Ordner, eine ZIP-Datei oder eine in einer Datei gespeicherte Dateiliste) geladen werden. Die Tabelle zeigt dann die Dateien in der Liste an. 

Unter "Filter" kann man die Liste der angezeigten Dateien einschränken, z.B. mit ".java" nur Java- oder mit ".jpg" nur JPG-Dateien anzeigen.

Die Buttons zwischen den zwei Listen zeigen *alle*, *alle gleichen* oder *unterschiedliche* Dateien an. Der Tooltip der Buttons (dazu die Maus kurz über den Button halten) verrät genau, was dieser dann angezeigt.

Unter den Tabellen kann die angezeigte Liste der Dateien in einer Datei für einen späteren Vergleich gespeichert werden ("Dateiliste in einer Datei"). Der Button mit den zwei gedrehten Pfeilen schlägt einen Dateinamen vor (unterschiedliche Vorschläge bei mehrmaligem Klick).

Die Dateilisten sind MD5-Hashes aller Dateien eines Ordners oder ZIP-Datei. Damit kann man Dateien auf Veränderung überprüfen.

Mögliche Szenarien zum Finden von gleichen und unterschiedlichen Dateien:
- Ich vergleiche direkt zwei Ordner
- Ich vergleiche direkt zwei ZIP-Dateien
- Ich vergleiche einen Ordner mit einer ZIP-Datei
- Ich erstelle eine Dateiliste (aus Ordner oder ZIP-Datei) und speichere die Dateiliste (unter der Tabelle) in einer Datei. Anschließend kann ich einen Ordner/eine ZIP-Datei mit der gespeicherten Liste immer wieder vergleichen und sehe was sich inzwischen geändert hat.
- Ich kann zwei gespeicherte Dateilisten vergleichen und sehe welche Dateien sich unterscheiden

<br />

**Dateien vergleichen**

Es können auch zwei Dateien direkt verglichen werden (oder eine Datei direkt gegen einen Hash kontrolliert werden). Dabei sind lokale Dateien und auch URLs möglich.

Sollen zwei Dateien verglichen werden, wird je eine Datei in **Datei 1 -> Datei/URL"** und in **Datei 2 -> Datei/URL"** eingetragen. Mit **Dateien vergleichen** wird dann der Hash der beiden Dateien erstellt. Ein grüner Hintergrund signalisiert, dass die beiden Dateien identisch sind.

Soll eine Datei mit einem Hash verglichen werden, wird die Datei in **Datei 1 -> Datei/URL"** eingetragen. Der Hash wird in **Datei 2 -> Hash** eingetragen. Mit **Hash erstellen** unter der zu prüfenden Datei wird ihr Hash erstellt, ein grüner Hintergrund signalisiert wieder wenn der zu prüfende Hash stimmt.

Als Dateien kann man lokale Dateien angeben und auch URLs von Dateien. Diese werden dann geladen (was bei sehr großen Dateien dauern kann!) und dabei wird der Hash ermittelt.

Der Button unten *Dateien vergleichen* startet die Erstellung des Hash für beide Dateien. Der Vorgang ist identisch mit je einem Klick auf "Hash erstellen" für jede Datei einzeln.

<br />
<br />

**Systemvoraussetzungen**

Unterstützt wird Windows (7, 8, 10) und Linux. Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: 1.8 (= Java 8).

Für Linux-Benutzer wird OpenJDK8 empfohlen. Außerdem benötigen Linux Benutzer die aktuelle Version von JavaFX (OpenJFX). OpenJFX ist aber bis jetzt nur für OpenJDK8 ohne Probleme zu installieren. Soll es Java 10 sein, wäre das Oracle Java SE 10 eine Alternative (und das bringt JavaFX schon mit).


**Links**

[www.p2tools.de/filerunner/](https://www.p2tools.de/filerunner/)
