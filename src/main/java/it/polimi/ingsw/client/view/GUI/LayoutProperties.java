package it.polimi.ingsw.client.view.GUI;

public class LayoutProperties {

    private int imageWidth;
    private int imageHeight;
    private int xPos;
    private int yPos;
    private String path;

    public LayoutProperties(int imageWidth, int imageHeight, int xPos, int yPos, String path) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.xPos = xPos;
        this.yPos = yPos;
        this.path = path;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public String getPath() {
        return path;
    }
}
