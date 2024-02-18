package com.geomatys.crop.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Dessin extends JPanel {

    BufferedImage monImage = null;

    public Dessin() {
        super();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(monImage != null)
            g.drawImage(monImage, 0, 0, null);
    }

    protected void reduireImage() {
        BufferedImage imageReduite = new BufferedImage((int)(monImage.getWidth()*0.5),(int)( monImage.getHeight()*0.5), monImage.getType());
        AffineTransform reduire = AffineTransform.getScaleInstance(0.5, 0.5);
        int interpolation = AffineTransformOp.TYPE_BICUBIC;
        AffineTransformOp retaillerImage = new AffineTransformOp(reduire, interpolation);
        retaillerImage.filter(monImage, imageReduite );
        monImage = imageReduite ;
        repaint();
    }

    protected void agrandirImage() {
        BufferedImage imageZoomer = new BufferedImage((int)(monImage.getWidth()*1.5),(int)( monImage.getHeight()*1.5), monImage.getType());
        AffineTransform agrandir = AffineTransform.getScaleInstance(1.5, 1.5);
        int interpolation = AffineTransformOp.TYPE_BICUBIC;
        AffineTransformOp retaillerImage = new AffineTransformOp(agrandir, interpolation);
        retaillerImage.filter(monImage, imageZoomer );
        monImage = imageZoomer ;
        repaint();
    }

    protected void imageConvolue() {
        BufferedImage imageFlou = new BufferedImage(monImage.getWidth(),monImage.getHeight(), monImage.getType());
        float[ ] masqueFlou = {
                        0.1f, 0.1f, 0.1f,
                        0.1f, 0.2f, 0.1f,
                        0.1f, 0.1f, 0.1f
        };

        Kernel masque = new Kernel(3, 3, masqueFlou);
        ConvolveOp operation = new ConvolveOp(masque);
        operation.filter(monImage, imageFlou);
        monImage = imageFlou;
        System.out.println("convolution effectuée");
        repaint();
    }

    protected void imageEclaircie() {

//            RescaleOp brillance = new RescaleOp(A, K, null);
//            1. A < 1, l’image devient plus sombre.
//            2. A > 1, l’image devient  plus brillante.
//            3. K est compris entre 0 et 256 et ajoute un éclairement.

        BufferedImage imgBrillant = new BufferedImage(monImage.getWidth(), monImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        RescaleOp brillance = new RescaleOp(1.2f, 0, null);
        brillance.filter(monImage, imgBrillant);
        monImage = imgBrillant;
        repaint();
    }

    protected void imageSombre() {

//            RescaleOp assombrir = new RescaleOp(A, K, null);
//            1.  A < 1, l’image devient plus sombre.
//            2.  A > 1, l’image devient  plus brillante.
//            3.  K est compris entre 0 et 256 et ajoute un éclairement.

        BufferedImage imgSombre = new BufferedImage(monImage.getWidth(), monImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        RescaleOp assombrir = new RescaleOp(0.7f, 10, null);
        assombrir.filter(monImage, imgSombre);
        monImage = imgSombre;
        System.out.println("assombrir effectuée");
        repaint();
    }

    protected void imageBinaire() {
        BufferedImage imgBinaire = new BufferedImage(monImage.getWidth(), monImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D surfaceImg = imgBinaire.createGraphics();
        surfaceImg.drawImage(monImage, null, null);
        monImage = imgBinaire;
        repaint();
    }

    protected void imageEnNiveauGris() {
        BufferedImage imageGris = new BufferedImage(monImage.getWidth(), monImage.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        Graphics2D surfaceImg = imageGris.createGraphics();
        surfaceImg.drawImage(monImage, null, null);
        monImage = imageGris;
        repaint();
    }

    protected void ajouterImage(File fichierImage) {    // dessiner une image à l'écran
        try {
            monImage = ImageIO.read(fichierImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

    protected BufferedImage getImagePanneau() {     // récupérer une image du panneau
        int width  = this.getWidth();
        int height = this.getHeight();
        BufferedImage image = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        this.paintAll(g);
        g.dispose();
        return image;
    }

    protected void enregistrerImage(File fichierImage) {
        String format ="JPG";
        BufferedImage image = getImagePanneau();
        try {
            ImageIO.write(image, format, fichierImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
