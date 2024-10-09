package one.empty3.libs;

import java.awt.*;

public class ColorMp extends Color implements IColorMp {
    private Color color;

    public ColorMp(Color color) {
        super(color.getRGB());
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRGB(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }
    public void setRGB(int rgb) {
        this.color = new Color(rgb);
    }
    public int getRGB() {
        return color.getRGB();
    }
    public int getRed() {
        return color.getRed();
    }
    public int getGreen() {
        return color.getGreen();
    }
    public int getBlue() {
        return color.getBlue();
    }
}
