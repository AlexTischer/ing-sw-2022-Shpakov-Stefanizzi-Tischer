package server.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TowerColor {

    WHITE("\u001b[37m", "white"),
    BLACK("\u001b[30m", "black"),
    GREY("\u001b[30;1m", "grey");


    public final String ansi;
    public final String label;

    private TowerColor(String ansi, String label) {
        this.ansi = ansi;
        this.label = label;
    }

}