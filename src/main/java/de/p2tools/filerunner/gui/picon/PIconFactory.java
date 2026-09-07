package de.p2tools.filerunner.gui.picon;

import de.p2tools.p2lib.ikonli.P2IconFactory;
import org.kordamp.ikonli.javafx.FontIcon;

public class PIconFactory {

    public enum PICON implements P2IconFactory.P2Icon {
        BTN_FILTER_ALL("mdi2a-arrow-left-right-bold", 18),
        BTN_FILTER_EQUAL("mdoal-equals", 18),
        BTN_FILTER_NOT_EQUAL("mdrmz-not_equal", 18),
        BTN_FILTER_DIFF_ONLY("gmi-zoom-out-map", 18),
        BTN_FILTER_DIFF_RIGHT("mdi2t-transfer-right", 18),
        BTN_FILTER_DIFF_LEFT("mdi2t-transfer-left", 18),

        BTN_START_ALL("gmi-save-alt", 18),
        BTN_GEN_HASH("gmi-double-arrow", 18),

        BTN_HELP("mdi-help"),
        BTN_DIR_OPEN("mdi2f-folder-open-outline", 18),
        BTN_START("mdal-double_arrow", 18),

        BTN_CLEAR("gmi-clear", 18),
        BTN_RESET("mdi-rotate-3d", 18),
        BTN_SPEED("mdi2s-speedometer", 24),
        BTN_SPEED_LOW("mdi2s-speedometer-slow", 24),

        ICON_BOOLEAN_ON("mdral-done_outline", 20),
        ICON_BOOLEAN_OFF("gmi-close", 20),
        PROG_MENU("gmi-menu", 25);

        private final String literal;
        private int size = 18;

        PICON(String literal) {
            this.literal = literal;
        }

        PICON(String literal, int size) {
            this.literal = literal;
            this.size = size;
        }

        public String getLiteral() {
            return literal;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public FontIcon getFontIcon() {
            return P2IconFactory.getIcon(literal, size);
        }

        public FontIcon getFontIcon(int size) {
            this.size = size;
            return P2IconFactory.getIcon(literal, size);
        }
    }

    private PIconFactory() {
    }
}
