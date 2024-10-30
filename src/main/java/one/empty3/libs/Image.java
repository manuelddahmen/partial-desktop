package one.empty3.libs;

import one.empty3.libs.commons.IImageMp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
