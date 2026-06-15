package controller;

import javafx.scene.media.AudioClip;

public class SoundController {

    private static final AudioClip click =
            new AudioClip(SoundController.class.getResource("/sounds/click.mp3").toExternalForm());

    private static final AudioClip mejora =
            new AudioClip(SoundController.class.getResource("/sounds/mejora.mp3").toExternalForm());

    private static final AudioClip inicio =
            new AudioClip(SoundController.class.getResource("/sounds/inicio.mp3").toExternalForm());

    private static final AudioClip final1 =
            new AudioClip(SoundController.class.getResource("/sounds/final1.mp3").toExternalForm());

    private static final AudioClip final2 =
            new AudioClip(SoundController.class.getResource("/sounds/final2.mp3").toExternalForm());
    private static final AudioClip error =
            new AudioClip(
                SoundController.class.getResource("/sounds/error.mp3")
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

    private static boolean inicioSonando = false;

    public static void playInicio() {
        if (inicio != null && !inicioSonando) {
            inicio.setVolume(0.5);
            inicio.play();
            inicioSonando = true;
        }
    }

    public static void stopInicio() {
        if (inicio != null) {
            inicio.stop();
            inicioSonando = false;
        }
    }
}