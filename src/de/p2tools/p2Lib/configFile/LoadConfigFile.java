/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigConfigsData;
import de.p2tools.p2Lib.configFile.config.ConfigConfigsList;
import de.p2tools.p2Lib.tools.Duration;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.SysMsg;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class LoadConfigFile implements AutoCloseable {

    private XMLInputFactory inFactory;
    private final Path xmlFilePath;

    private ArrayList<ConfigsList> configsListArr = null;
    private ArrayList<ConfigsData> configsDataArr = null;

    LoadConfigFile(Path filePath, ArrayList<ConfigsList> configsListArrayList, ArrayList<ConfigsData> configsDataArr) {
        this.xmlFilePath = filePath;
        this.configsListArr = configsListArrayList;
        this.configsDataArr = configsDataArr;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    LoadConfigFile(Path filePath, ArrayList<ConfigsData> configsDataArr) {
        this.xmlFilePath = filePath;
        this.configsDataArr = configsDataArr;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    boolean readConfiguration() {
        Duration.counterStart("Konfig lesen");
        boolean ret = false;

        if (!Files.exists(xmlFilePath)) {
            Duration.counterStop("Konfig lesen");
            return ret;
        }

        XMLStreamReader parser = null;
        try (InputStream is = Files.newInputStream(xmlFilePath);
             InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            parser = inFactory.createXMLStreamReader(in);

            nextTag:
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue nextTag;
                }

                String xmlElem = parser.getLocalName();
                if (get(parser, xmlElem)) {
                    continue nextTag;
                }
//                if (configsListArr != null) {
//                    for (ConfigsList configsList : configsListArr) {
//                        if (configsList.getTag().equals(xmlElem)) {
//                            getConf(parser, configsList);
//                            continue nextTag;
//                        }
//                    }
//                }
//
//                if (configsDataArr != null) {
//                    for (ConfigsData configsData : configsDataArr) {
//                        if (configsData.getTag().equals(xmlElem)) {
//                            getConf(parser, configsData);
//                            continue nextTag;
//                        }
//                    }
//                }

            }
            ret = true;

        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(732160795, ex);
        } finally {
            try {
                if (parser != null) {
                    parser.close();
                }
            } catch (final Exception ignored) {
            }
        }

        Duration.counterStop("Konfig lesen");
        return ret;
    }

    private boolean get(XMLStreamReader parser, String xmlElem) {
        if (configsListArr != null) {
            for (ConfigsList configsList : configsListArr) {
                if (configsList.getTag().equals(xmlElem)) {
                    getConf(parser, configsList);
                    return true;
                }
            }
        }

        if (configsDataArr != null) {
            for (ConfigsData configsData : configsDataArr) {
                if (configsData.getTag().equals(xmlElem)) {
                    getConf(parser, configsData);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getConf(XMLStreamReader parser, Object o) {
//        System.out.println("getConf: " + o.getClass());
        if (o instanceof ConfigsData) {
            return getConfigData(parser, (ConfigsData) o);

        } else if (o instanceof ConfigsList) {
            return getConfigsList(parser, (ConfigsList) o);

        } else if (o instanceof ConfigConfigsList) {
            ConfigsList<? extends ConfigsData> actValue = ((ConfigConfigsList) o).getActValue();
            return getConfigsList(parser, actValue);

        } else if (o instanceof ConfigConfigsData) {
            ConfigsData cd = ((ConfigConfigsData) o).getActValue();
            return getConfigData(parser, cd);

        } else if (o instanceof Config) {
            return getConfig(parser, (Config) o);

        } else {
            SysMsg.sysMsg("Fehler beim Lesen: " + o.getClass().toString());
            return false;
        }

    }

    private boolean getConfigsList(XMLStreamReader parser, ConfigsList configsList) {
        boolean ret = false;
        final String configsListTagName = configsList.getTag();

        try {
            ConfigsData configsData = configsList.getNewItem();
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(configsListTagName)) {
                    break;
                }

                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                String s = parser.getLocalName();
                if (!configsData.getTag().equals(s)) {
                    continue;
                }

                if (getConf(parser, configsData)) {
                    ret = true;
                    configsList.addNewItem(configsData);
                    configsData = configsList.getNewItem();
                }

            }
        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(302104541, ex);
        }

        return ret;
    }

    private boolean getConfigData(XMLStreamReader parser, ConfigsData configsData) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();

        try {
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(xmlElem)) {
                    break;
                }
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                final String localName = parser.getLocalName();
                for (Config config : configsData.getConfigsArr()) {

                    String key = config.getKey();
                    if (config.getKey().equals(localName)) {
                        getConf(parser, config);
                        break;
                    }

                }

            }
            ret = true;

        } catch (final Exception ex) {
            Log.errorLog(302104541, ex);
        }

        return ret;
    }

    private boolean getConfig(XMLStreamReader parser, Config config) {
        try {
            final String n = parser.getElementText();
            config.setActValue(n);
        } catch (XMLStreamException ex) {
            return false;
        }

        return true;
    }

    @Override
    public void close() throws Exception {
    }

}
