/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.p2Lib.configFile;

import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigConfigsData;
import de.p2tools.p2Lib.configFile.config.ConfigConfigsList;
import de.p2tools.p2Lib.configFile.configList.ConfigList;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.SysMsg;
import javafx.collections.ObservableList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class SaveConfigFile implements AutoCloseable {

    private XMLStreamWriter writer = null;
    private OutputStreamWriter out = null;
    private Path xmlFilePath = null;
    private OutputStream os = null;
    private ProgData progData = null;

    private final ArrayList<ConfigsDataList> configsDataList;
    private final ArrayList<ConfigsData> configsData;

    SaveConfigFile(Path filePath, ArrayList<ConfigsDataList> configsDataList, ArrayList<ConfigsData> configsData) {
        xmlFilePath = filePath;
        this.configsDataList = configsDataList;
        this.configsData = configsData;
    }

    synchronized void write() {
        SysMsg.sysMsg("ProgData Schreiben nach: " + xmlFilePath.toString());
        xmlDatenSchreiben();
    }


    private void xmlDatenSchreiben() {
        try {
            xmlSchreibenStart();

            for (ConfigsData configsData : configsData) {

                writer.writeCharacters("\n\n");
                writer.writeComment(configsData.getComment());
                writer.writeCharacters("\n");
                write(configsData, 0);
            }

            for (ConfigsDataList cl : configsDataList) {
                writer.writeCharacters("\n");
                writer.writeComment(cl.getComment());
                write(cl, 0);
            }

            writer.writeCharacters("\n\n");
            xmlSchreibenEnde();
        } catch (final Exception ex) {
            Log.errorLog(656328109, ex);
        }
    }

    private void xmlSchreibenStart() throws IOException, XMLStreamException {
        SysMsg.sysMsg("Start Schreiben nach: " + xmlFilePath.toAbsolutePath());
        os = Files.newOutputStream(xmlFilePath);
        out = new OutputStreamWriter(os, StandardCharsets.UTF_8);

        final XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
        writer = outFactory.createXMLStreamWriter(out);
        writer.writeStartDocument(StandardCharsets.UTF_8.name(), "1.0");
        writer.writeCharacters("\n");
        writer.writeStartElement(ProgConst.XML_START);
        writer.writeCharacters("\n");
    }

    private void xmlSchreibenEnde() throws XMLStreamException {
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        SysMsg.sysMsg("geschrieben!");
    }

    @Override
    public void close() throws IOException, XMLStreamException {
        writer.close();
        out.close();
        os.close();
    }


    private void write(Object o, int tab) throws XMLStreamException {
        if (o instanceof ConfigsData) {
            writeConfigsData((ConfigsData) o, tab);

        } else if (o instanceof ConfigsDataList) {
            writeConfigsList((ConfigsDataList) o, tab);

        } else if (o instanceof ConfigConfigsList) {
            writeConfigConfigsList((ConfigConfigsList) o, tab);

        } else if (o instanceof ConfigConfigsData) {
            writeConfigConfigsData((ConfigConfigsData) o, tab);

        } else if (o instanceof ConfigList) {
            writeConfList((ConfigList) o, tab);

        } else if (o instanceof Config) {
            writeConf((Config) o, tab);
        } else {
            SysMsg.sysMsg("Fehler beim Schreiben von: " + o.getClass().toString());
        }
    }

    private void writeConfigsData(ConfigsData configsData, int tab) throws XMLStreamException {

        String xmlName = configsData.getTag();

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeStartElement(xmlName);
        writer.writeCharacters("\n"); // neue Zeile

        ++tab;
        for (Config config : configsData.getConfigsArr()) {
            write(config, tab);
        }
        --tab;

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeEndElement();
        writer.writeCharacters("\n"); // neue Zeile
    }


    private void writeConfigsList(ConfigsDataList configsDataList, int tab) throws XMLStreamException {

        String xmlName = configsDataList.getTag();
        writer.writeCharacters("\n"); // neue Zeile

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeStartElement(xmlName);
        writer.writeCharacters("\n"); // neue Zeile

        ++tab;
        for (Object configsData : configsDataList) {
            write(configsData, tab);
        }
        --tab;

        for (int t = 0; t < tab; ++t) {
            writer.writeCharacters("\t"); // Tab
        }
        writer.writeEndElement();
        writer.writeCharacters("\n"); // neue Zeile
    }

    private void writeConfigConfigsList(ConfigConfigsList configConfigsList, int tab) throws XMLStreamException {
        ConfigsDataList<? extends ConfigsData> list = configConfigsList.getActValue();
        writeConfigsList(list, tab);
    }

    private void writeConfigConfigsData(ConfigConfigsData configConfigsData, int tab) throws XMLStreamException {
        ConfigsData configsData = configConfigsData.getActValue();
        writeConfigsData(configsData, tab);
    }

    private void writeConfList(ConfigList config, int tab) throws XMLStreamException {
        System.out.println(config.getActValue().toString());
        if (!config.getActValue().isEmpty()) {

            ObservableList<Object> actValue = config.getActValue();
            for (Object o : actValue) {
                for (int t = 0; t < tab; ++t) {
                    writer.writeCharacters("\t"); // Tab
                }
                writer.writeStartElement(config.getKey());
                writer.writeCharacters(o.toString());
                writer.writeEndElement();
                writer.writeCharacters("\n"); // neue Zeile
            }

        }
    }

    private void writeConf(Config config, int tab) throws XMLStreamException {
        if (config.getActValue() != null && !config.getActValueString().isEmpty()) {
            for (int t = 0; t < tab; ++t) {
                writer.writeCharacters("\t"); // Tab
            }
            writer.writeStartElement(config.getKey());
            writer.writeCharacters(config.getActValueString());
            writer.writeEndElement();
            writer.writeCharacters("\n"); // neue Zeile
        }
    }


}
