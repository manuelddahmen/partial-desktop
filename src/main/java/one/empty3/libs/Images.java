package one.empty3.libs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Images {
    private BufferedImage image;
    public Images(BufferedImage image) {
        this.image = image;
    }
    public Images(int x, int y) {
        this.image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
    }


    public static Images load(File path) {
        try {
            return new Images(ImageIO.read(path));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Image loadFile(File path) {
        try {
            BufferedImage read = ImageIO.read(path);
            return new Image(read);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean save(Images images, File path) {
        try {
            return ImageIO.write(images.image, "png", path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public int getRgb(int x, int y) {
        return image.getRGB(x, y);
    }
    public void setRgb(int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

}