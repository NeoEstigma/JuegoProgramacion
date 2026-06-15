package controller;

import javafx.scene.media.AudioClip;

/**
 * Controlador encargado de gestionar todos los sonidos y la música
 * de Terminal Clicker.
 *
 * Carga los distintos archivos de audio desde los recursos del proyecto
 * y proporciona métodos estáticos para reproducir o detener los efectos
 * de sonido asociados a las acciones del juego.
 *
 * Los sonidos disponibles son:
 * <ul>
 *   <li>click: sonido al hackear o pulsar botones.</li>
 *   <li>mejora: sonido al comprar una mejora.</li>
 *   <li>inicio: música del menú principal.</li>
 *   <li>final1: sonido de victoria al completar el juego.</li>
 *   <li>final2: sonido reproducido antes de volver al menú.</li>
 *   <li>error: sonido al intentar realizar una acción inválida.</li>
 * </ul>
 *
 * Todos los métodos son estáticos, por lo que no es necesario
 * instanciar esta clase.
 *
 * @author Diego, Mateo, Laura
 */
public class SoundController {

    /** Sonido al pulsar botones o hackear. */
    private static final AudioClip click =
            new AudioClip(
                    SoundController.class.getResource("/sounds/click.mp3")
                            .toExternalForm());

    /** Sonido al comprar una mejora. */
    private static final AudioClip mejora =
            new AudioClip(
                    SoundController.class.getResource("/sounds/mejora.mp3")
                            .toExternalForm());

    /** Música de fondo del menú principal. */
    private static final AudioClip inicio =
            new AudioClip(
                    SoundController.class.getResource("/sounds/inicio.mp3")
                            .toExternalForm());

    /** Sonido de victoria al completar el juego. */
    private static final AudioClip final1 =
            new AudioClip(
                    SoundController.class.getResource("/sounds/final1.mp3")
                            .toExternalForm());

    /** Sonido reproducido antes de volver al menú principal. */
    private static final AudioClip final2 =
            new AudioClip(
                    SoundController.class.getResource("/sounds/final2.mp3")
                            .toExternalForm());

    /** Sonido de error al realizar una acción inválida. */
    private static final AudioClip error =
            new AudioClip(
                    SoundController.class.getResource("/sounds/error.mp3")
                            .toExternalForm());

    /** Indica si la música del menú se encuentra reproduciéndose. */
    private static boolean inicioSonando = false;

    /**
     * Reproduce el sonido de error.
     * Se reinicia si ya estaba sonando.
     */
    public static void playError() {

        error.stop();

        error.play();
    }

    /**
     * Reproduce el sonido de clic.
     */
    public static void playClick() {

        click.play();
    }

    /**
     * Reproduce el sonido asociado a la compra de una mejora.
     * Si el sonido ya está reproduciéndose, se reinicia.
     */
    public static void playMejora() {

        mejora.stop();

        mejora.play();
    }

    /**
     * Reproduce el sonido final de victoria.
     */
    public static void playFinal1() {

        final1.stop();

        final1.play();
    }

    /**
     * Reproduce el sonido previo al regreso al menú principal.
     */
    public static void playFinal2() {

        final2.stop();

        final2.play();
    }

    /**
     * Reproduce la música del menú principal.
     * Solo se ejecuta si no se encuentra ya reproduciéndose,
     * evitando reinicios al volver desde otras vistas.
     */
    public static void playInicio() {

        if (inicio != null && !inicioSonando) {

            inicio.setVolume(0.5);

            inicio.play();

            inicioSonando = true;
        }
    }

    /**
     * Detiene la música del menú principal y actualiza
     * el estado interno indicando que ya no está sonando.
     */
    public static void stopInicio() {

        if (inicio != null) {

            inicio.stop();

            inicioSonando = false;
        }
    }
}