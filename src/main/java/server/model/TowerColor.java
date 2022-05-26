package server.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TowerColor {

    WHITE("\u001b[37m"),
    BLACK("\u001b[30m"),
    GREY("\u001b[30;1m");

    public final String label;

    private TowerColor(String label) {
        this.label = label;
    }

}