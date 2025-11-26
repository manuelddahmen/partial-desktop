package one.empty3.libs;

import one.empty3.libs.commons.IImageMp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Image extends BufferedImage implements IImageMp {
    private BufferedImage bi;

    public Image(BufferedImage image) {
        super(1, 1, image.getType());
        this.bi = image;
    }
    public Image(int la, int ha, int[] pixels) {
        super(1,1,BufferedImage.TYPE_INT_ARGB);
        BufferedImage bi = new BufferedImage(la, ha, BufferedImage.TYPE_INT_ARGB);
        bi.setRGB(0, 0, la, ha, pixels, 0, la);
        setBi(bi);
    }
    public Image(File image) {
        super(1, 1, BufferedImage.TYPE_INT_ARGB);
        try {
            this.bi = JpegOrientation.readNormalized(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
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

    public static BufferedImage convertToRGB(BufferedImage image) {
        return image;
        /*BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        if(image instanceof Image image1) {
            rgbImage.createGraphics().drawImage(image1.getBi(), 0, 0, new ImageObserver() {
                @Override
                public boolean imageUpdate(java.awt.Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
        } else {
            rgbImage.createGraphics().drawImage(image, 0, 0, new ImageObserver() {
                @Override
                public boolean imageUpdate(java.awt.Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
        }
        return new Image(rgbImage);

         */
    }

    public BufferedImage getBi() {
        return bi;
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

    @Override
    public boolean saveToFile(String s) throws IOException {
        if(s.endsWith("png")) {
            return ImageIO.write(getBi(), "png", new File(s));
        } else if(s.endsWith("jpg") || s.endsWith("jpeg")) {
            return ImageIO.write(getBi(), "jpg", new File(s));
        } else if(s.endsWith("bmp")) {
            return ImageIO.write(getBi(), "bmp", new File(s));
        } else {
            return ImageIO.write(getBi(), s.substring(s.lastIndexOf(".")+1), new File(s));
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
        bi.setRGB(x, y, rgb&0x00FFFFFF);
    }

    public static void saveFile(BufferedImage image, String png,File out,
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

    /***
     *
     * @param image BufferedImage to write to file
     * @param type            "jpg" or "png"
     * @param out             File to write to
     * @param shouldOverwrite test if file already exists overrides or not overrides
     */
    public static boolean saveFileAs(BufferedImage image, String type, File out,
                                boolean shouldOverwrite) {
        if(image instanceof Image image1) {
            try {
                if((out.exists()&&shouldOverwrite)||!out.exists()) {
                    ImageIO.write(image1.getBi(), type, new File(out.getAbsolutePath()));
                } else
                    return false;
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Logger.getLogger(Image.class.getCanonicalName()).log(Level.SEVERE, "Can't try write to file {0} No Image var");
        }
        return false;
    }
    public IImageMp loadFile(File path) {
        try {
            return new Image(ImageIO.read(path));
        } catch (IOException e) {
            Logger.getLogger(Image.class.getCanonicalName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    public boolean saveFile(File path) {
        try {
            return saveToFile(path.getAbsolutePath());
        }catch (IOException e) {
            Logger.getLogger(Image.class.getCanonicalName()).log(Level.SEVERE, null, e);
            return false;
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
