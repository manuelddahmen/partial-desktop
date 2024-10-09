package one.empty3.libs;

import java.awt.image.BufferedImage;
import java.io.File;

public class Image extends BufferedImage implements IImage {
    private Images image;

    public Image() {

    }
    public Images getImages() {
        return image;
    }

    public void setImages(Images images) {
        this.image = images;
    }
    public int getRgb(int x, int y) {
        return image.getRgb(x, y);
    }
    public void setRgb(int x, int y, int rgb) {
        image.setRgb(x, y, rgb);
    }
    public void loadFile(File path) {
        image = Images.load(path);
    }
    public void saveFile(Images images, File path) {
        Images.save(images, path);
    }
}
