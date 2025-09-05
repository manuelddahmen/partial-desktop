package one.empty3.libs;// Java
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public final class JpegOrientation {
    // Valeurs EXIF possibles pour le tag Orientation:
    // 1: Top-Left (aucune transfo)
    // 2: Top-Right (miroir horizontal)
    // 3: Bottom-Right (rotation 180°)
    // 4: Bottom-Left (miroir vertical)
    // 5: Left-Top (miroir horizontal + rot 90° CW)
    // 6: Right-Top (rot 90° CW)
    // 7: Right-Bottom (miroir horizontal + rot 90° CCW)
    // 8: Left-Bottom (rot 90° CCW)

    public static BufferedImage readNormalized(File file) throws Exception {
        BufferedImage src = ImageIO.read(file);
        if (src == null) return null;

        int orientation = 1; // défaut
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (ifd0 != null && ifd0.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = ifd0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception ignore) {
            // si pas d’EXIF ou lecture impossible, on garde orientation=1
        }

        return normalize(src, orientation);
    }

    public static BufferedImage normalize(BufferedImage src, int orientation) {
        int w = src.getWidth();
        int h = src.getHeight();

        AffineTransform tx = new AffineTransform();
        int newW = w;
        int newH = h;

        switch (orientation) {
            case 1: // Top-Left
                return src;

            case 2: // Top-Right -> Miroir horizontal
                tx.scale(-1, 1);
                tx.translate(-w, 0);
                break;

            case 3: // Bottom-Right -> 180°
                tx.translate(w, h);
                tx.rotate(Math.toRadians(180));
                break;

            case 4: // Bottom-Left -> Miroir vertical
                tx.scale(1, -1);
                tx.translate(0, -h);
                break;

            case 5: // Left-Top -> Miroir horizontal + 90° CW
                tx.rotate(Math.toRadians(90));
                tx.scale(-1, 1);
                tx.translate(-h, 0);
                newW = h; newH = w;
                break;

            case 6: // Right-Top -> 90° CW
                tx.translate(h, 0);
                tx.rotate(Math.toRadians(90));
                newW = h; newH = w;
                break;

            case 7: // Right-Bottom -> Miroir horizontal + 90° CCW
                tx.rotate(Math.toRadians(-90));
                tx.scale(-1, 1);
                tx.translate(-w, 0);
                newW = h; newH = w;
                break;

            case 8: // Left-Bottom -> 90° CCW
                tx.translate(0, w);
                tx.rotate(Math.toRadians(-90));
                newW = h; newH = w;
                break;

            default:
                return src;
        }

        BufferedImage dst = new BufferedImage(newW, newH, bestType(src));
        Graphics2D g = dst.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(src, new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC), 0, 0);
        } finally {
            g.dispose();
        }
        return dst;
    }

    private static int bestType(BufferedImage src) {
        int type = src.getType();
        if (type == BufferedImage.TYPE_CUSTOM) {
            // Conserver l’alpha si présent
            return src.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        }
        return type;
    }
}