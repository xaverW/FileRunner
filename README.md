
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

# FileRunner

Die Hauptintention des Programms ist es, Ordnerinhalte auf Veränderungen zu überprüfen. 

<br />
## Infos

Das Programm bietet dazu die Möglichkeit, Listen mit Hashwerten aller Dateien eines Ordners anzulegen. Mit den Hashlisten können die Ordner dann immer wieder auf Veränderungen kontrolliert werden. Es ist auch möglich, zwei Ordner direkt zu vergleichen oder auch einen Ordner mit dem Inhalt einer ZIP-Dateien zu vergleichen.

Weiter ist es auch möglich, zwei Dateien direkt oder eine Datei mit einem Hashwert zu vergleichen.

### Dateilisten vergleichen

Eine Dateiliste 
- kann eine mit dem Programm erstellte Liste mit Hashwerten von den Dateien eines Ordners sein.
- können alle aktuellen Dateien in einem Ordner sein.
- kann der Inhalt einer Zip-Datei sein.

Zwei dieser Dateilisten können dann direkt verglichen werden. In der rechten und linken Seite des Programms kann je eine Dateiliste (eine Hashliste, der Inhalt eines Ordner oder der Inhalt einer ZIP-Datei) geladen werden. Die Tabelle zeigt dann die enthaltenen Dateien an. 

Unter "Filter" kann man die Liste der angezeigten Dateien einschränken, z.B. mit ".java" nur Java- oder mit ".jpg" nur JPG-Dateien anzeigen.

Die Buttons zwischen den zwei Listen zeigen *alle*, *alle gleichen* oder *unterschiedliche* Dateien an. Der Tooltip der Buttons (dazu die Maus kurz über den Button halten) verrät genau, was dieser dann anzeigt.

Unter den Tabellen kann die angezeigte Liste der Dateien in einer Datei für einen späteren Vergleich gespeichert werden ("Dateiliste in einer Datei"). Der Button mit den zwei gedrehten Pfeilen schlägt einen Dateinamen vor (unterschiedliche Vorschläge bei mehrmaligem Klick).

Die Dateilisten sind MD5-Hashes aller Dateien eines Ordners (und deren Unterordner) oder ZIP-Datei. Damit kann man Dateien auf Veränderung überprüfen.

Mögliche Szenarien zum Finden von gleichen und unterschiedlichen Dateien:
- Ich vergleiche direkt zwei Ordner.
- Ich vergleiche direkt zwei ZIP-Dateien.
- Ich vergleiche einen Ordner mit einer ZIP-Datei.
- Ich erstelle eine Dateiliste (aus Ordner oder ZIP-Datei) und speichere die Dateiliste (unter der Tabelle) in einer Datei. Anschließend kann ich einen Ordner/eine ZIP-Datei mit der gespeicherten Liste immer wieder vergleichen und sehe was sich inzwischen geändert hat.
- Ich kann zwei gespeicherte Dateilisten vergleichen und sehe welche Dateien sich unterscheiden.

### Dateien vergleichen

Es können auch zwei Dateien direkt verglichen werden (oder eine Datei direkt gegen einen Hash kontrolliert werden). Dabei sind lokale Dateien und auch URLs möglich. Als Hash kann man ein Hashfile (z.B. datei.txt.md5) oder den Hash direkt angeben.

Sollen zwei Dateien verglichen werden, wird je eine Datei in **Datei 1 -> Datei/URL"** und in **Datei 2 -> Datei/URL"** eingetragen. Mit **Dateien vergleichen** wird dann der Hash der beiden Dateien erstellt. Ein grüner Rahmen signalisiert, dass die beiden Dateien identisch sind.

Soll eine Datei mit einem Hash verglichen werden, wird die Datei in **Datei 1 -> Datei/URL"** eingetragen. Der Hash wird aus **Datei 2 -> Hashdatei** ausgelesen oder direkt in **Datei 2 -> Hash** eingetragen. Mit **Hash erstellen** unter der zu prüfenden Datei wird ihr Hash erstellt, ein grüner Rahmen signalisiert wieder, wenn der zu prüfende Hash stimmt.

Als Dateien kann man lokale Dateien angeben und auch URLs von Dateien. Diese werden dann geladen (was bei sehr großen Dateien dauern kann!) und dabei wird der Hash ermittelt.

Der Button unten *Dateien vergleichen* startet die Erstellung des Hash für beide Dateien. Der Vorgang ist identisch mit je einem Klick auf "Hash erstellen" für jede Datei einzeln.

<br />

## Systemvoraussetzungen

Unterstützt wird Windows und Linux.

Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: Java 11. Für Linux-Benutzer wird OpenJDK11 empfohlen. (FX-Runtime bringt das Programm bereits mit und muss nicht installiert werden).

<br />

## Download

Das Programm wird in drei Paketen angeboten. Diese unterscheiden sich nur im "Zubehör", das Programm selbst ist in allen Paketen identisch:

- **FileRunner-4.zip**  
Das Programmpaket bringt nur das Programm aber kein Java mit. Auf dem Rechner muss eine Java-Laufzeitumgebung ab Java11 installiert sein. Dieses Programmpaket kann auf allen Betriebssystemen verwendet werden. Es bringt Startdateien für Linux und Windows mit.

- **FileRunner-4__Linux+Java.zip**  
  **FileRunner-4__Windows+Java.zip**  
Diese Programmpakete bringen die Java-Laufzeitumgebung mit und sind nur für das angegebene Betriebssystem: Linux oder Windows. Es muss kein Java auf dem System installiert sein. (Die Java-Laufzeitumgebung liegt im Ordner "Java" und kommt von jdk.java.net).

zum Download: [github.com/xaverW/FileRunner/releases](https://github.com/xaverW/FileRunner/releases)  

<br />

## Website

zur Website: [www.p2tools.de/filerunner/](https://www.p2tools.de/filerunner/)


