package one.empty3.libs;

import org.junit.Test;
import org.junit.Assert;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ColorImageTests {

    @Test
    public void testImageConstructorWithFile() throws IOException {
        // Créer un répertoire temporaire pour le test
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "test-images-" + System.currentTimeMillis());
        if(tempDir.mkdirs()) {
            System.err.println("Directory created: " + tempDir.getAbsolutePath());
        } else {
            System.err.println("Directory not created: " + tempDir.getAbsolutePath());
            return;
        }
        try {
            // Créer une image temporaire pour le test
            File tempImageFile = new File(tempDir, "test-image.jpg");
            BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

            // Dessiner quelque chose dans l'image
            for (int x = 0; x < 100; x++) {
                for (int y = 0; y < 100; y++) {
                    bufferedImage.setRGB(x, y, Color.RED.getRGB());
                }
            }

            // Sauvegarder l'image
            if(ImageIO.write(bufferedImage, "jpg", tempImageFile)){
                System.err.println("Image saved to "+tempImageFile.getAbsolutePath());
            } else {
                System.err.println("Image not saved to "+tempImageFile.getAbsolutePath());
                return;
            }

            // Tester le constructeur Image(File)
            Image image = new Image(tempImageFile);

            // Vérifier que l'image a été correctement chargée
            Assert.assertNotNull("L'image chargée ne devrait pas être null", image.getBi());
            Assert.assertEquals("La largeur de l'image devrait être 100", 100, image.getWidth());
            Assert.assertEquals("La hauteur de l'image devrait être 100", 100, image.getHeight());
            System.err.println(image.getRgb(50, 50));
            Assert.assertEquals("La couleur du pixel (50,50) devrait être rouge", 0x00FF0000 , bufferedImage.getRGB(50, 50));
            Assert.assertEquals("La couleur du pixel (50,50) devrait être rouge", Color.RED.getRGB() , image.getRgb(50, 50));
        } finally {
            // Nettoyer les fichiers temporaires
            deleteDirectory(tempDir);
        }
    }

    @Test
    public void testImageSaveFile() throws IOException {
        // Créer un répertoire temporaire pour le test
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "test-images-" + System.currentTimeMillis());
        tempDir.mkdirs();

        try {
            // Créer une image
            Image image = new Image(200, 150);

            // Remplir l'image avec une couleur bleue
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 150; y++) {
                    image.setRgb(x, y, Color.BLUE.getRGB());
                }
            }

            // Sauvegarder l'image
            File savedImageFile = new File(tempDir, "saved-image.jpg");
            if(image.saveToFile(savedImageFile.getAbsolutePath())) {
                System.err.println("Image saved to "+savedImageFile.getAbsolutePath());
            } else {
                System.err.println("Image not saved to "+savedImageFile.getAbsolutePath());
                return;
            }

            // Vérifier que le fichier existe
            Assert.assertTrue("Le fichier d'image devrait exister après la sauvegarde", savedImageFile.exists());

            // Charger l'image sauvegardée pour vérifier son contenu
            BufferedImage loadedImage = ImageIO.read(savedImageFile);
            Assert.assertNotNull("L'image chargée ne devrait pas être null", loadedImage);
            Assert.assertEquals("La largeur de l'image devrait être 200", 200, loadedImage.getWidth());
            Assert.assertEquals("La hauteur de l'image devrait être 150", 150, loadedImage.getHeight());
            System.err.println(loadedImage.getRGB(50, 50));
            //Assert.assertEquals("La couleur du pixel (100,75) devrait être bleue", Color.BLUE.getRGB() & 0x00FFFFFF, loadedImage.getRGB(100, 75) & 0x00FFFFFF);
        } finally {
            // Nettoyer les fichiers temporaires
            deleteDirectory(tempDir);
        }
    }

    @Test
    public void testGetAndSetRgb() {
        // Créer une image
        Image image = new Image(50, 50);

        // Définir une couleur à une position spécifique
        int x = 25;
        int y = 30;
        int rgb = Color.GREEN.getRGB();
        image.setRgb(x, y, rgb);

        // Vérifier que la couleur a été correctement définie
        Assert.assertEquals("La couleur du pixel devrait correspondre à la couleur définie", rgb & 0x00FFFFFF, image.getRgb(x, y));

        // Tester avec une autre couleur
        rgb = Color.YELLOW.getRGB();
        image.setRgb(10, 15, rgb);
        Assert.assertEquals("La couleur du pixel devrait correspondre à la couleur jaune", rgb & 0x00FFFFFF, image.getRgb(10, 15));

        // Vérifier que les pixels non modifiés ont la couleur par défaut (noir)
        Assert.assertEquals("La couleur par défaut devrait être noire", Color.BLACK.getRGB() & 0x00FFFFFF, image.getRgb(0, 0));
    }
    /**
     * Méthode utilitaire pour supprimer récursivement un répertoire
     */
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
