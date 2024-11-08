package one.empty3.libs;

import one.empty3.libs.commons.IImageMp;

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

    @Override
    public boolean saveToFile(String s) {
        try {
            if(ImageIO.write(this, s.substring(s.lastIndexOf(".")+1), new File(s)));
            return true;
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Image not save to file"+toString()+"///"+s);
            return false;
        }
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
    public void loadFile(File path) {
        try {
            bi = ImageIO.read(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveFile(Images images, File path) {

        try {
            ImageIO.write(bi, path.getAbsolutePath().substring(path.getAbsolutePath().lastIndexOf("."+1)),
                    path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
