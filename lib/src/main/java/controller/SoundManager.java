package controller;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private static final AudioClip click =
            new AudioClip(SoundManager.class.getResource("/sounds/click.mp3").toExternalForm());

    private static final AudioClip mejora =
            new AudioClip(SoundManager.class.getResource("/sounds/mejora.mp3").toExternalForm());

    private static final AudioClip inicio =
            new AudioClip(SoundManager.class.getResource("/sounds/inicio.mp3").toExternalForm());

    private static final AudioClip final1 =
            new AudioClip(SoundManager.class.getResource("/sounds/final1.mp3").toExternalForm());

    private static final AudioClip final2 =
            new AudioClip(SoundManager.class.getResource("/sounds/final2.mp3").toExternalForm());
    private static final AudioClip error =
            new AudioClip(
                SoundManager.class.getResource("/sounds/error.mp3")
                .toExternalForm());

    public static void playError() {

        error.stop();

        error.play();
    }
    
    public static void playClick() {
        click.play();
    }

    public static void playMejora() {
        mejora.stop(); // Reinicia el sonido si haces varias compras seguidas
        mejora.play();
    }
    public static void playFinal1() {

        final1.stop();

        final1.play();
    }

    public static void playFinal2() {

        final2.stop();

        final2.play();
    }

    public static void playInicio() {
        inicio.play();
    }

 
    public static void stopInicio() {
        inicio.stop();
    }
}