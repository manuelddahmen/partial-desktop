package one.empty3.libs;

import one.empty3.libs.commons.IImageMp;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Image extends BufferedImage implements IImageMp {
    private BufferedImage bi;

    public Image(BufferedImage image) {
        super(1, 1, image.getType());
        this.bi = image;
    }

    public Image(File image) {
        super(1, 1, BufferedImage.TYPE_INT_ARGB);
        try {
            this.bi = ImageIO.read(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Image(int x, int y, int type) {
        super(x, y, type);
        this.bi = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
    }
    public Image(int x, int y) {
        super(1,1,BufferedImage.TYPE_INT_ARGB);
        bi = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
    }
    public int getRgb(int x, int y) {
        return bi!=null?bi.getRGB(x, y):this.getRGB(x, y);
    }

    public static IImageMp getFromFile(File file) {
        try {
            return new Image(ImageIO.read(file));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static IImageMp getFromInputStream(InputStream stream) {
        try {
            BufferedImage write;
            if((write = ImageIO.read(stream))!=null) {
                return new Image(write);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public boolean toOutputStream(OutputStream stream) {
        try {
            boolean write = false;
            Logger.getLogger(getClass().getCanonicalName()).info("Saving as png");
            if(write == ImageIO.write((null != this.bi) ? convertToRGB(bi) : this, "png", stream)) {
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static Image convertToRGB(BufferedImage image) {
        BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        if(image instanceof Image image1) {
            rgbImage.createGraphics().drawImage(image1.getBi(), 0, 0, null);
        } else {
            rgbImage.createGraphics().drawImage(image, 0, 0, null);
        }
        return new Image(rgbImage);
    }

    public BufferedImage getBi() {
        return convertToRGB(bi);
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

    private BufferedImage convertToARGB(BufferedImage bi) {
        return null;
    }


    @Override
    public boolean saveToFile(String s) throws IOException {
        Logger.getLogger(getClass().getCanonicalName()).info("Saving as png");
        return ImageIO.write(convertToRGB(bi), "png", new File(s));
    }


    @Override
    public void setImageToMatrix(int[][] ints) {

    }

    @Override
    public int[][] getMatrix() {
        return new int[0][];
    }

    public void setRgb(int x, int y, int rgb) {
        bi.setRGB(x, y, rgb&0x00FFFFFF);
    }

    public static void saveFile(BufferedImage image, String jpg,File out,
                                 boolean shouldOverwrite) {
        if(image instanceof Image image1) {
            image1.saveFile(out);
            return;
        } else {
            Image iimage = new Image(image);
            iimage.saveFile(out);
            return;
        }
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
            saveToFile(path.getAbsolutePath());
            System.out.println("Image saved to "+path.getAbsolutePath());
        }catch (IOException e) {
            System.out.println("Image not saved to "+path.getAbsolutePath());
        }
    }
    public static Image staticLoadFile(File path) {
        Image image = new Image(path);
        try {
            image.setBi(ImageIO.read(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    @Override
    public int getWidth() {
        return bi.getWidth();
    }

    @Override
    public int getHeight() {
        return bi.getHeight();
    }
}
