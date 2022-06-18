package it.polimi.ingsw.server.model;

import java.util.*;

public enum TowerColor {

    WHITE("\u001b[37m", "white", "/images/towers/white_tower.png"),
    BLACK("\u001b[30m", "black", "/images/towers/black_tower.png"),
    GREY("\u001b[30;1m", "grey", "/images/towers/grey_tower.png");


    public final String ansi;
    public final String label;

    public final String tower;

    TowerColor(String ansi, String label, String tower) {
        this.ansi = ansi;
        this.label = label;
        this.tower = tower;
    }

    public static Optional<Color> getTowerColorByLabel(String value) {
        return Arrays.stream(Color.values())
                .filter(towerColor -> towerColor.label.equals(value))
                .findFirst();
    }

}