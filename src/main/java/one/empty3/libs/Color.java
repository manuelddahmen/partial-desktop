package one.empty3.libs;

import one.empty3.libs.commons.IColorMp;

public class Color extends java.awt.Color implements IColorMp {
    public Color(int rgb) {
        super(rgb);
    }
    public Color(java.awt.Color color) {
        super(color.getRGB());
    }

    public Color getColorObject() {
        return this;
    }


    @Override
    public int getColor() {
        return this.getRGB();
    }



    public int getRGB() {
        return super.getRGB();
    }
    public int getRed() {
        return super.getRed();
    }
    public int getGreen() {
        return super.getGreen();
    }
    public int getBlue() {
        return super.getBlue();
    }
    public int getAlpha() {
        return super.getAlpha();
    }

    public float color() {
        return 1.0f;
    }
}
