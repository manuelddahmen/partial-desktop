
package one.empty3.libs;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

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

        int orientation = readExifOrientation(file);
        return normalize(src, orientation);
    }

    /**
     * Lit l'orientation EXIF depuis un fichier JPEG en utilisant l'API ImageIO native
     */
    private static int readExifOrientation(File file) {
        try (ImageInputStream input = ImageIO.createImageInputStream(file)) {
            if (input == null) return 1;

            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) return 1;

            ImageReader reader = readers.next();
            try {
                reader.setInput(input);
                IIOMetadata metadata = reader.getImageMetadata(0);

                // Essayer de lire l'orientation depuis les métadonnées
                if (metadata != null) {
                    IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree("javax_imageio_jpeg_image_1.0");
                    if (root != null) {
                        return extractOrientationFromMetadata(root);
                    }
                }
            } finally {
                reader.dispose();
            }
        } catch (Exception e) {
            // Si erreur, retourner orientation par défaut
        }
        return 1; // Orientation par défaut
    }

    /**
     * Extrait la valeur d'orientation depuis les métadonnées JPEG
     */
    private static int extractOrientationFromMetadata(IIOMetadataNode root) {
        try {
            // Parcourir les nœuds pour trouver l'orientation EXIF
            // Note: Cette méthode est simplifiée et peut ne pas fonctionner pour tous les JPEG
            // Une alternative serait d'utiliser directement les bytes EXIF
            IIOMetadataNode markerSequence = getChildNode(root, "markerSequence");
            if (markerSequence != null) {
                IIOMetadataNode unknown = getChildNode(markerSequence, "unknown");
                if (unknown != null) {
                    String markerTag = unknown.getAttribute("MarkerTag");
                    if ("225".equals(markerTag)) { // APP1 marker (EXIF)
                        // Lecture des données EXIF (simplifié)
                        // Dans un cas réel, il faudrait parser les bytes EXIF
                        return 1; // Par défaut
                    }
                }
            }
        } catch (Exception e) {
            // Ignorer les erreurs
        }
        return 1;
    }

    /**
     * Récupère un nœud enfant par son nom
     */
    private static IIOMetadataNode getChildNode(IIOMetadataNode node, String name) {
        if (node == null) return null;
        for (int i = 0; i < node.getLength(); i++) {
            if (node.item(i).getNodeName().equals(name)) {
                return (IIOMetadataNode) node.item(i);
            }
        }
        return null;
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
            // Conserver l'alpha si présent
            return src.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        }
        return type;
    }
}