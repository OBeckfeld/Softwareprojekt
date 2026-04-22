package main;

import javax.swing.JFrame;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Programm beenden beim Schließen
        jframe.add(gamePanel); // Unser Spielfeld ins Fenster packen
        jframe.setResizable(false); // Größe festsetzen, damit nichts verzerrt
        jframe.pack(); // Fenstergröße an das GamePanel anpassen
        jframe.setLocationRelativeTo(null); // Fenster in der Mitte des Bildschirms öffnen
        jframe.setVisible(true); // Fenster anzeigen
    }
}

