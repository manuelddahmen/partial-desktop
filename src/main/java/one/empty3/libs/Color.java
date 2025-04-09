package one.empty3.libs;

import one.empty3.libs.commons.IColorMp;

import java.io.File;

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

    public static boolean saveFile(Image image, String format, boolean shouldOverwrite,  File out) {
        try {
            if(image.saveToFile(out.getAbsolutePath()))
                return true;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @Override
    public int getColor() {
        return this.getRGB()& 0x00FFFFFF;
    }

    public int getRgb() {
        int red = (int) (red() * 255f);
        int green = (int) (green() * 255);
        int blue = (int) (blue() * 255);
        int color = ((red << 16) | (green << 8) | blue);;
        return color|0xFF000000;
    }


    public static Color newCol(float r1, float r2, float r3) {
        java.awt.Color color = new java.awt.Color(r1, r2, r3);
        return new Color(color.getRGB());
    }

    public int getRGB() {
        return super.getRGB() & 0x00FFFFFF;
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


    public float red() {return super.getRed()/255f;}
    public float green() {
        return super.getGreen()/255f;
    }
    public float blue() {
        return super.getBlue()/255f;
    }
    public float alpha() {
        return super.getAlpha()/255f;
    }

    public float color() {
        return 1.0f;
    }
}
