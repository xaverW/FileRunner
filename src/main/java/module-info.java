module filerunner {
    opens de.p2tools.filerunner;
    exports de.p2tools.filerunner;

    requires de.p2tools.p2lib;
    requires javafx.controls;
    requires org.controlsfx.controls;

    requires java.logging;
    requires java.desktop;

    requires commons.cli;
    requires org.apache.commons.io;

}

