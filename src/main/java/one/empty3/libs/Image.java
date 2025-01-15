package one.empty3.libs;

import one.empty3.libs.commons.IImageMp;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Image extends BufferedImage implements IImageMp {
    private BufferedImage bi;

    public Image(BufferedImage image) {
        super(1, 1, 1);
        this.bi = image;
    }
    public Image(int x, int y, int type) {
        super(x, y, type);
        this.bi = new BufferedImage(x, y, type);
    }
    public Image(int x, int y) {
        super(x,y,BufferedImage.TYPE_INT_RGB);
        bi = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
    }
    public int getRgb(int x, int y) {
        return bi.getRGB(x, y);
    }

    public IImageMp getFromFile(File file) {
        try {
            return new Image(ImageIO.read(file));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public BufferedImage getBi() {
        return bi;
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }


    @Override
    public boolean saveToFile(String s) {
        try {
            if(ImageIO.write(bi, "jpg", new File(s))) {
                return true;
            }
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Image not save to file"+toString()+"///"+s);
            throw new RuntimeException(e);
        }
        return false;
    }


    @Override
    public void setImageToMatrix(int[][] ints) {

    }

    @Override
    public int[][] getMatrix() {
        return new int[0][];
    }

    public void setRgb(int x, int y, int rgb) {
        bi.setRGB(x, y, rgb);
    }

    public static IImageMp loadFile(File path) {
        try {
            return new Image(ImageIO.read(path));
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Image not load from file"+path);
            return null;
        }
    }
    public void saveFile(File path) {

        try {
            ImageIO.write(bi, "jpg",
                    path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Image staticLoadFile(File path) {
        Image image = new Image(null);
        try {
            image.setBi(ImageIO.read(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
}
