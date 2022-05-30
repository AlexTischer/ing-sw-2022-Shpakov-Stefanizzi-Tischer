package server.model;

import java.util.*;

public enum TowerColor {

    WHITE("\u001b[37m", "white"),
    BLACK("\u001b[30m", "black"),
    GREY("\u001b[30;1m", "grey");


    public final String ansi;
    public final String label;

    TowerColor(String ansi, String label) {
        this.ansi = ansi;
        this.label = label;
    }

    public static Optional<Color> getTowerColorByLabel(String value) {
        return Arrays.stream(Color.values())
                .filter(towerColor -> towerColor.label.equals(value))
                .findFirst();
    }

}