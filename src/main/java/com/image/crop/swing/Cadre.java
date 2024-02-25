package com.image.crop.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

/*
What is the difference between Swing and AWT ?
=> AWT is a thin layer of code on top of the OS, whereas Swing is much larger. Swing also has very much richer functionality.
Using AWT, you have to implement a lot of things yourself, while Swing has them built in. For GUI-intensive work,
AWT feels very primitive to work with, compared to Swing.

https://waytolearnx.com/2020/05/difference-entre-swing-et-awt-java.html
=> Alors que Swing est une collection de composants qui ont la capacité de développer des objets d’interface graphique (GUI)
indépendamment de la plate-forme, les composants AWT en dépendent et fonctionnent différemment sur différentes plates-formes.
*/

public class Cadre extends JFrame implements ActionListener {

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fichierMenu = new JMenu();
    private final JMenuItem ouvrirMenu = new JMenuItem();
    private final JMenu filtreMenu = new JMenu();
    private final Dessin panneau = new Dessin();
    private final JMenuItem enregistrerMenu = new JMenuItem();
    private final JMenuItem niveauGrisMenu = new JMenuItem();
    private final JMenuItem assombrirMenu = new JMenuItem();
    private final JMenuItem brillanceMenu = new JMenuItem();
    private final JMenuItem binarisationMenu = new JMenuItem();
    private final JMenuItem convolutionMenu = new JMenuItem();
    private final JMenu retaillerMenu = new JMenu();
    private final JMenuItem agrandirMenu = new JMenuItem();
    private final JMenuItem reduireMenu = new JMenuItem();

    public Cadre() {
        super();
        setBounds(100, 100, 500, 375);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        try {
            creerMenu();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void creerMenu() throws Exception {

        // construction du menu
        setJMenuBar(menuBar);
        menuBar.add(fichierMenu);
        fichierMenu.setText("Fichier");
        fichierMenu.add(ouvrirMenu);
        ouvrirMenu.addActionListener((ActionListener)this);
        ouvrirMenu.setText("ouvrir");
        fichierMenu.add(enregistrerMenu);
        enregistrerMenu.addActionListener((ActionListener)this);
        enregistrerMenu.setText("enregistrer");

        menuBar.add(filtreMenu);
        filtreMenu.setText("Filtre");
        filtreMenu.add(niveauGrisMenu);
        niveauGrisMenu.addActionListener((ActionListener)this);
        niveauGrisMenu.setText("niveau de gris");

        filtreMenu.add(binarisationMenu);
        binarisationMenu.addActionListener((ActionListener)this);
        binarisationMenu.setText("binarisation");

        filtreMenu.add(assombrirMenu);
        assombrirMenu.addActionListener((ActionListener)this);
        assombrirMenu.setText("assombrir");

        filtreMenu.add(brillanceMenu);
        brillanceMenu.addActionListener((ActionListener)this);
        brillanceMenu.setText("brillance");

        filtreMenu.add(convolutionMenu);
        convolutionMenu.addActionListener((ActionListener)this);
        convolutionMenu.setText("convolution");

        menuBar.add(retaillerMenu);
        retaillerMenu.setText("retailler");
        retaillerMenu.add(agrandirMenu);
        agrandirMenu.addActionListener((ActionListener)this);
        agrandirMenu.setText("agrandir");
        retaillerMenu.add(reduireMenu);
        reduireMenu.addActionListener((ActionListener)this);
        reduireMenu.setText("reduire");

        // ajout du panneau de dessin
        getContentPane().add(panneau);
    }

    public void actionPerformed(ActionEvent cliqueMenu) {
        if (cliqueMenu.getSource().equals(ouvrirMenu))
        {
            JFileChooser fileOuvrirImage = new JFileChooser();
            if (fileOuvrirImage.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                panneau.ajouterImage(new File(fileOuvrirImage.getSelectedFile()
                        .getAbsolutePath()));
            }
        } else if (cliqueMenu.getSource().equals(enregistrerMenu)) {
            JFileChooser fileEnregistrerImage = new JFileChooser();
            if (fileEnregistrerImage.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File fichierEnregistrement = new File(fileEnregistrerImage.getSelectedFile().getAbsolutePath()+ ".JPG");
                panneau.enregistrerImage(fichierEnregistrement);
            }
        } else if (cliqueMenu.getSource().equals(niveauGrisMenu)) {
            panneau.imageEnNiveauGris();
        } else if (cliqueMenu.getSource().equals(brillanceMenu)) {
            panneau.imageEclaircie();
        } else if (cliqueMenu.getSource().equals(binarisationMenu)) {
            panneau.imageBinaire();
        } else if (cliqueMenu.getSource().equals(convolutionMenu)) {
            panneau.imageConvolue();
            System.out.println("appel convolution");
        } else if (cliqueMenu.getSource().equals(agrandirMenu)) {
            panneau.agrandirImage();
        } else if (cliqueMenu.getSource().equals(reduireMenu)) {
            panneau.reduireImage();
        } else if (cliqueMenu.getSource().equals(assombrirMenu)){
            panneau.imageSombre();
        }
    }

    public static void main(String args[]) {
        try {
            Cadre frame = new Cadre();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
